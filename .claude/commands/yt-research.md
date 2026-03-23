# YouTube Research Skill

Search YouTube for videos on a given topic and return structured metadata (titles, views, channel, duration, URLs) using yt-dlp.

## Usage

`/yt-research [topic] [--count N]`

- **topic** – The subject to research (required). If not provided, ask the user before proceeding.
- **--count N** – Number of videos to retrieve (default: 25, max: 50)

## Instructions

1. **If no topic is provided**, ask the user: "What topic would you like me to research on YouTube?"

2. **Run the research script:**
   ```bash
   python3 scripts/yt_research.py "<TOPIC>" --count <N>
   ```
   Replace `<TOPIC>` with the user's topic and `<N>` with the requested count (default 25).

3. **Present the results** in a clean markdown table showing:
   - Rank, Title, Channel, Duration, Views, URL

4. **Store the results** for pipeline use. If the user wants to send results to NotebookLM immediately, extract the URL list and pass it to the `/notebooklm` skill.

5. **Extract just URLs** when needed for the pipeline:
   ```bash
   python3 scripts/yt_research.py "<TOPIC>" --count <N> --urls-only
   ```

## Notes
- yt-dlp must be installed: `pip install yt-dlp`
- Results include YouTube watch URLs suitable for NotebookLM source ingestion
- The script outputs a markdown table plus a JSON block for downstream use
