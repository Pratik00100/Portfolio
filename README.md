# Pratik Dhami's portfolio

Static professional portfolio for entry-level IT support and cyber security roles.

| Destination | URL |
| --- | --- |
| Portfolio | https://pratik00100.github.io/Portfolio/ |
| LinkedIn | https://www.linkedin.com/in/pratik-dhami-0a41a12ab/ |

Keep the links reciprocal by adding the portfolio under LinkedIn's Contact info and Featured sections. The portfolio links back to LinkedIn. This is a manual update: GitHub Pages does not sync LinkedIn profile changes.

The paste-ready copy is in [docs/linkedin-profile.md](docs/linkedin-profile.md); it is a draft, not a live profile update.

## Local workflow

The site uses plain HTML, CSS and native browser features, with no build or runtime dependencies.

```powershell
python -m http.server 4173 --bind 127.0.0.1
```

Open <http://127.0.0.1:4173/>. Check the site with:

```powershell
python check_site.py
```

## Agent workflow

Read [AGENTS.md](AGENTS.md) and [the content evidence map](docs/content-sources.md) before changing career claims. `CLAUDE.md` adapts the same repository rules for Claude Code.

The lead sets scope, approved facts and acceptance checks. A bounded worker may use a smaller model when the task and file ownership are clear; assign one worker per file. Have a separate read-only reviewer check factual claims. Run `codex` from this repository to start Codex; Claude Code is available in Pratik's app, but this project does not assume a `claude` command is on `PATH`. The official Codex plugin is installed and enabled in Claude Code on Pratik's workstation; it supports task delegation, not automatic conversation-history sharing. This repository does not add another wrapper or install.

Only an explicitly authorized push publishes changes. The GitHub Pages workflow validates the site and stages an explicit list of public web files; repository instructions and drafts stay out of the Pages artifact. LinkedIn profile content is never fetched or synchronized automatically.
