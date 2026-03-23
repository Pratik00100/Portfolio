#!/usr/bin/env python3
"""
YouTube Research Script
Uses yt-dlp to search YouTube and extract video metadata.
"""

import json
import sys
import argparse
import subprocess
import re

def search_youtube(query: str, max_results: int = 25) -> list[dict]:
    """Search YouTube and return video metadata using yt-dlp."""
    search_url = f"ytsearch{max_results}:{query}"

    cmd = [
        "yt-dlp",
        "--dump-json",
        "--no-download",
        "--flat-playlist",
        "--no-warnings",
        search_url,
    ]

    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        print(f"Error running yt-dlp: {result.stderr}", file=sys.stderr)
        sys.exit(1)

    videos = []
    for line in result.stdout.strip().split("\n"):
        if not line.strip():
            continue
        try:
            data = json.loads(line)
            video_id = data.get("id", "")
            url = data.get("url") or data.get("webpage_url") or (f"https://www.youtube.com/watch?v={video_id}" if video_id else "")

            # Clean duration
            duration_secs = data.get("duration")
            if duration_secs:
                mins, secs = divmod(int(duration_secs), 60)
                hours, mins = divmod(mins, 60)
                duration_str = f"{hours}:{mins:02d}:{secs:02d}" if hours else f"{mins}:{secs:02d}"
            else:
                duration_str = "N/A"

            views = data.get("view_count")
            views_str = f"{views:,}" if views else "N/A"

            videos.append({
                "rank": len(videos) + 1,
                "title": data.get("title", "Unknown"),
                "channel": data.get("uploader") or data.get("channel") or "Unknown",
                "duration": duration_str,
                "views": views_str,
                "url": url,
                "video_id": video_id,
                "description": (data.get("description") or "")[:200],
            })
        except (json.JSONDecodeError, KeyError):
            continue

    return videos


def get_full_metadata(video_url: str) -> dict:
    """Get full metadata for a specific video URL."""
    cmd = [
        "yt-dlp",
        "--dump-json",
        "--no-download",
        "--no-warnings",
        video_url,
    ]
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        return {}
    try:
        return json.loads(result.stdout.strip())
    except json.JSONDecodeError:
        return {}


def format_results(videos: list[dict], query: str) -> str:
    """Format video results as a readable markdown table."""
    if not videos:
        return "No results found."

    lines = [
        f"## YouTube Research Results",
        f"**Query:** `{query}`  |  **Results:** {len(videos)} videos\n",
        "| # | Title | Channel | Duration | Views | URL |",
        "|---|-------|---------|----------|-------|-----|",
    ]

    for v in videos:
        title = v["title"][:60] + "..." if len(v["title"]) > 60 else v["title"]
        lines.append(
            f"| {v['rank']} | {title} | {v['channel']} | {v['duration']} | {v['views']} | {v['url']} |"
        )

    lines.append("\n### Raw JSON (for pipeline use)")
    lines.append("```json")
    lines.append(json.dumps(videos, indent=2))
    lines.append("```")

    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(description="YouTube Research using yt-dlp")
    parser.add_argument("query", nargs="?", help="Search query")
    parser.add_argument("-n", "--count", type=int, default=25, help="Number of results (default: 25)")
    parser.add_argument("--json", action="store_true", help="Output raw JSON only")
    parser.add_argument("--urls-only", action="store_true", help="Output URLs only (one per line)")
    args = parser.parse_args()

    if not args.query:
        print("Error: Please provide a search query.", file=sys.stderr)
        sys.exit(1)

    videos = search_youtube(args.query, args.count)

    if args.json:
        print(json.dumps(videos, indent=2))
    elif args.urls_only:
        for v in videos:
            print(v["url"])
    else:
        print(format_results(videos, args.query))


if __name__ == "__main__":
    main()
