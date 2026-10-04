# Portfolio agent instructions

This public repository contains Pratik Dhami's professional portfolio. Codex and
Claude Code use this same file. Read README.md and docs/content-sources.md before
changing career content. LinkedIn copy is in docs/linkedin-profile.md.

## Source of truth

- Career facts come from Pratik's local `career-ops/cv.md` in CLAUDE-MAIN,
  `config/profile.yml`, and direct, current user statements.
- The existing website, LinkedIn, memory, and other AI drafts are not independent
  evidence. If primary sources are unavailable, ask instead of inventing facts.
- Preserve exact role titles and dates. Customer-support ticketing is not paid IT
  support. Label labs and coursework. Security+ is in progress, not earned.
- Do not claim authorship of another repository or tool merely because Pratik
  uses it. Project claims need explicit primary-source attribution.
- Never commit phone numbers, street addresses, dates of birth, government or
  financial identifiers, private exports, credentials, or LinkedIn cookies.
  Public email, name, city and professional links are intentional.
- Do not edit CLAUDE-MAIN/life or CLAUDE-MAIN/visa from this project.

## Working agreement

- One agent owns each file at a time. Check git status, fetch, and use
  `git pull --ff-only` when the tree is clean. Preserve unrelated work.
- A lead agent decides scope, evidence and design. When delegation is requested,
  give smaller workers bounded tasks, exact file ownership and acceptance checks.
  Use a separate read-only review before publishing substantial changes.
- Use plain HTML, CSS and native browser features. No new dependencies, build
  framework, account tokens or background synchronisation for a static portfolio.
- Profile changes start as source-backed drafts. Do not send networking messages
  or edit a live LinkedIn profile without specific authorization.
- A local file request does not by itself authorize publishing. For an authorized
  live website update, test before pushing; main deploys via Pages.

## Checks and release

1. Run `python check_site.py`: internal links, metadata, legacy routes, asset
   availability and obvious accidental private-data exposure.
2. Serve with `python -m http.server 4173 --bind 127.0.0.1`. Inspect desktop and
   mobile sizes, keyboard focus, native project disclosures and overflow.
3. Check factual sentences against primary sources. Label illustrative diagrams;
   do not invent live telemetry, project outcomes or project artefacts.
4. Review `git diff --check` and the full diff. Commit only project files.
5. If publishing is authorized, push main, wait for successful Pages deployment,
   and verify the live URL. Report observed results and remaining limits.

Deploy an explicit allowlist of web assets. Keep repository docs and local
configuration out of the Pages artifact.
