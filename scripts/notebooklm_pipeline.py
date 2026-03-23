#!/usr/bin/env python3
"""
NotebookLM Pipeline Script
Uses notebooklm-py to create notebooks, add sources, and generate deliverables.
"""

import asyncio
import json
import sys
import argparse
import os
from pathlib import Path

try:
    from notebooklm import NotebookLMClient
except ImportError:
    print("Error: notebooklm-py is not installed.", file=sys.stderr)
    print("Run: pip install 'notebooklm-py[browser]' && playwright install chromium", file=sys.stderr)
    sys.exit(1)


VALID_DELIVERABLES = [
    "infographic",
    "slide-deck",
    "flashcards",
    "quiz",
    "audio-overview",
    "briefing-doc",
    "study-guide",
    "mind-map",
    "timeline",
    "faq",
]

INFOGRAPHIC_STYLES = [
    "handwritten",
    "chalkboard",
    "minimalist",
    "colorful",
    "corporate",
    "retro",
]


async def create_notebook_with_sources(
    name: str,
    urls: list[str],
    deliverables: list[str] | None = None,
    infographic_style: str | None = None,
    output_dir: str = "./notebooklm_output",
) -> dict:
    """Create a NotebookLM notebook, add YouTube URLs, and optionally generate deliverables."""

    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    results = {"notebook_name": name, "sources_added": [], "deliverables": {}}

    async with await NotebookLMClient.from_storage() as client:
        # Create notebook
        print(f"Creating notebook: {name}")
        nb = await client.notebooks.create(name)
        results["notebook_id"] = nb.id
        print(f"Notebook created: {nb.id}")

        # Add sources
        print(f"\nAdding {len(urls)} sources...")
        for i, url in enumerate(urls, 1):
            try:
                print(f"  [{i}/{len(urls)}] Adding: {url[:80]}")
                await client.sources.add_url(nb.id, url, wait=True)
                results["sources_added"].append(url)
                print(f"  OK")
            except Exception as e:
                print(f"  WARN: Failed to add {url}: {e}", file=sys.stderr)

        print(f"\nSuccessfully added {len(results['sources_added'])} sources.")

        # Generate deliverables
        if deliverables:
            print(f"\nGenerating deliverables: {', '.join(deliverables)}")
            for deliverable in deliverables:
                try:
                    print(f"\n  Generating {deliverable}...")
                    dl_result = await _generate_deliverable(
                        client, nb.id, deliverable, infographic_style, output_path
                    )
                    results["deliverables"][deliverable] = dl_result
                    print(f"  Saved to: {dl_result.get('file', 'N/A')}")
                except Exception as e:
                    print(f"  ERROR generating {deliverable}: {e}", file=sys.stderr)
                    results["deliverables"][deliverable] = {"error": str(e)}

    return results


async def _generate_deliverable(
    client,
    notebook_id: str,
    deliverable: str,
    infographic_style: str | None,
    output_path: Path,
) -> dict:
    """Generate a single deliverable and save it."""
    artifacts = client.artifacts

    if deliverable == "infographic":
        kwargs = {}
        if infographic_style:
            kwargs["style"] = infographic_style
        status = await artifacts.generate_infographic(notebook_id, orientation="portrait", **kwargs)
        await artifacts.wait_for_completion(notebook_id, status.task_id)
        out_file = output_path / "infographic.png"
        await artifacts.download_infographic(notebook_id, str(out_file))
        return {"file": str(out_file), "style": infographic_style}

    elif deliverable == "slide-deck":
        status = await artifacts.generate_slide_deck(notebook_id)
        await artifacts.wait_for_completion(notebook_id, status.task_id)
        out_file = output_path / "slides.pdf"
        await artifacts.download_slide_deck(notebook_id, str(out_file))
        return {"file": str(out_file)}

    elif deliverable == "flashcards":
        status = await artifacts.generate_flashcards(notebook_id)
        await artifacts.wait_for_completion(notebook_id, status.task_id)
        out_file = output_path / "flashcards.json"
        await artifacts.download_flashcards(notebook_id, str(out_file), output_format="json")
        return {"file": str(out_file)}

    elif deliverable == "audio-overview":
        status = await artifacts.generate_audio_overview(notebook_id)
        await artifacts.wait_for_completion(notebook_id, status.task_id)
        out_file = output_path / "audio_overview.wav"
        await artifacts.download_audio_overview(notebook_id, str(out_file))
        return {"file": str(out_file)}

    elif deliverable == "study-guide":
        status = await artifacts.generate_study_guide(notebook_id)
        await artifacts.wait_for_completion(notebook_id, status.task_id)
        out_file = output_path / "study_guide.pdf"
        await artifacts.download_study_guide(notebook_id, str(out_file))
        return {"file": str(out_file)}

    else:
        # Generic fallback using CLI subprocess
        import subprocess
        result = subprocess.run(
            ["notebooklm", "generate", deliverable, "--notebook", notebook_id],
            capture_output=True, text=True
        )
        if result.returncode != 0:
            raise RuntimeError(result.stderr)
        return {"output": result.stdout.strip()}


async def list_notebooks() -> list[dict]:
    """List all existing notebooks."""
    async with await NotebookLMClient.from_storage() as client:
        notebooks = await client.notebooks.list()
        return [{"id": nb.id, "name": nb.title} for nb in notebooks]


def main():
    parser = argparse.ArgumentParser(description="NotebookLM Pipeline")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # create command
    create_parser = subparsers.add_parser("create", help="Create notebook and add sources")
    create_parser.add_argument("name", help="Notebook name")
    create_parser.add_argument("--urls", nargs="+", help="YouTube/web URLs to add as sources")
    create_parser.add_argument("--urls-file", help="File containing URLs (one per line)")
    create_parser.add_argument(
        "--deliverables", nargs="+", choices=VALID_DELIVERABLES,
        help="Deliverables to generate"
    )
    create_parser.add_argument(
        "--infographic-style", choices=INFOGRAPHIC_STYLES,
        default=None, help="Style for infographic generation"
    )
    create_parser.add_argument("--output-dir", default="./notebooklm_output", help="Output directory")
    create_parser.add_argument("--json", action="store_true", help="Output results as JSON")

    # list command
    subparsers.add_parser("list", help="List all notebooks")

    args = parser.parse_args()

    if args.command == "list":
        notebooks = asyncio.run(list_notebooks())
        for nb in notebooks:
            print(f"{nb['id']}  {nb['name']}")
        return

    # Build URL list
    urls = list(args.urls or [])
    if args.urls_file:
        with open(args.urls_file) as f:
            urls.extend(line.strip() for line in f if line.strip())

    if not urls:
        print("Error: No URLs provided. Use --urls or --urls-file.", file=sys.stderr)
        sys.exit(1)

    results = asyncio.run(
        create_notebook_with_sources(
            name=args.name,
            urls=urls,
            deliverables=args.deliverables,
            infographic_style=args.infographic_style,
            output_dir=args.output_dir,
        )
    )

    if args.json:
        print(json.dumps(results, indent=2))
    else:
        print("\n=== Pipeline Complete ===")
        print(f"Notebook: {results['notebook_name']} (ID: {results.get('notebook_id', 'N/A')})")
        print(f"Sources added: {len(results['sources_added'])}")
        if results["deliverables"]:
            print("Deliverables:")
            for name, info in results["deliverables"].items():
                if "error" in info:
                    print(f"  {name}: ERROR - {info['error']}")
                else:
                    print(f"  {name}: {info.get('file', info)}")


if __name__ == "__main__":
    main()
