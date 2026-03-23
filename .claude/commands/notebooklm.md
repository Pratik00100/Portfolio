# NotebookLM Skill

Create a NotebookLM notebook, upload YouTube or web URLs as sources, and generate analysis deliverables (infographics, slide decks, flashcards, etc.) using the notebooklm-py library.

## Usage

`/notebooklm [notebook-name] --urls <url1> <url2> ... [--deliverables <type>...] [--infographic-style <style>]`

## Authentication Prerequisite

**Before first use**, the user must authenticate in a separate terminal:
```bash
notebooklm login
```
This opens a browser for Google account sign-in and stores credentials locally.

## Available Deliverables

| Type | Description |
|------|-------------|
| `infographic` | Visual infographic (PNG) |
| `slide-deck` | Presentation slides (PDF) |
| `flashcards` | Study flashcards (JSON) |
| `quiz` | Quiz questions |
| `audio-overview` | Podcast-style audio summary |
| `briefing-doc` | Written briefing document |
| `study-guide` | Study guide |
| `mind-map` | Mind map visualization |
| `timeline` | Timeline visualization |
| `faq` | Frequently asked questions |

## Infographic Styles

`handwritten`, `chalkboard`, `minimalist`, `colorful`, `corporate`, `retro`

## Instructions

1. **Check authentication**: Remind the user to run `notebooklm login` in a separate terminal if this is their first time.

2. **Determine inputs** from context:
   - If URLs come from a previous `/yt-research` run, extract them from the results
   - If a notebook name isn't specified, generate one based on the topic
   - If deliverables aren't specified, default to `infographic`

3. **Save URLs to a temp file** when passing many URLs:
   ```bash
   # Write URLs to a temp file
   python3 -c "
   urls = [
     'URL1',
     'URL2',
     # ...
   ]
   with open('/tmp/yt_urls.txt', 'w') as f:
       f.write('\n'.join(urls))
   "
   ```

4. **Run the pipeline script:**
   ```bash
   python3 scripts/notebooklm_pipeline.py create "<NOTEBOOK_NAME>" \
     --urls-file /tmp/yt_urls.txt \
     --deliverables <DELIVERABLE_TYPES> \
     --infographic-style <STYLE> \
     --output-dir ./notebooklm_output
   ```

5. **Report results** including:
   - Notebook ID and name
   - Number of sources successfully added
   - Paths to generated deliverable files
   - Any errors encountered

6. **Present findings**: After generation, read and summarize the key analysis from the deliverable content when possible.

## Full Pipeline Example

When the user says "research [TOPIC] and send to NotebookLM with an infographic in chalkboard style":

```bash
# Step 1: Get YouTube URLs
python3 scripts/yt_research.py "TOPIC" --count 25 --urls-only > /tmp/yt_urls.txt

# Step 2: Create notebook and generate infographic
python3 scripts/notebooklm_pipeline.py create "TOPIC Research" \
  --urls-file /tmp/yt_urls.txt \
  --deliverables infographic \
  --infographic-style chalkboard \
  --output-dir ./notebooklm_output
```

Output files will be saved to `./notebooklm_output/`.

## Notes
- Requires: `pip install 'notebooklm-py[browser]' && playwright install chromium`
- Authentication via `notebooklm login` must be done once before use
- NotebookLM processes sources asynchronously; large batches may take several minutes
