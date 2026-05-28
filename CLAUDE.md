# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Static HTML/CSS/JS personal portfolio for Pratik Dhami — a cybersecurity/IT professional. No build tools, no package manager, no framework. Open any `.html` file directly in a browser to preview.

## Architecture

The site is a collection of standalone HTML pages that each:
- Include the single shared `styles.css`
- Include the single shared `script.js`
- Copy-paste the same `<nav>` block (no templating system — navbar must be updated in every file manually)
- Contain a `<div class="matrix"></div>` element (currently unstyled; the matrix background comes from `body::before` in CSS using `matrix.gif`)

Pages: `index.html` (home), `about.html`, `skills.html`, `projects.html`, `experience.html`, `education.html`, `contact.html`

## Visual Theme

Matrix/hacker aesthetic: black background (`#0d0d0d`), neon green text (`#00ff00`), `Courier New` monospace font, green glowing box shadows. The `matrix.gif` is used at 20% opacity as a fixed full-page background via `body::before`.

## Key Conventions

- Navigation between pages uses plain `<a href="page.html">` links — `script.js` only handles smooth scroll for `href="#anchor"` links (currently unused).
- Adding a new page requires: creating the HTML file, adding a nav link in **every existing page**, and adding a link back in the new page's nav.
- All styling is in `styles.css` — there are no inline styles or page-specific CSS files.
