"""Dependency-free checks for the public static website. Run: python check_site.py."""
from html.parser import HTMLParser
from pathlib import Path
from urllib.parse import unquote, urlsplit
import re

ROOT = Path(__file__).resolve().parent
SITE = "https://pratik00100.github.io/Portfolio/"
ROUTES = ("about", "skills", "projects", "experience", "education", "contact")


class Page(HTMLParser):
    def __init__(self, path):
        super().__init__(convert_charrefs=True)
        self.path = path
        self.ids, self.links, self.meta, self.headings = [], [], {}, []
        self.canonical = None
        self.feed(path.read_text(encoding="utf-8"))

    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)
        if "id" in attrs:
            self.ids.append(attrs["id"])
        if tag == "h1":
            self.headings.append(tag)
        if tag == "meta":
            name = attrs.get("name", attrs.get("property", attrs.get("http-equiv")))
            self.meta[name] = attrs.get("content", "")
        if tag == "link" and attrs.get("rel") == "canonical":
            self.canonical = attrs.get("href")
        for attribute in ("href", "src"):
            if attribute in attrs:
                self.links.append(attrs[attribute])
        if attrs.get("target") == "_blank":
            assert "noopener" in attrs.get("rel", ""), f"Unsafe external link in {self.path.name}"
        if tag == "img":
            assert "alt" in attrs, f"Image missing alt text in {self.path.name}"


def main():
    pages = {path.name: Page(path) for path in ROOT.glob("*.html")}
    assert set(pages) == {"index.html", *(name + ".html" for name in ROUTES)}, "Unexpected HTML routes"
    home = pages["index.html"]
    assert len(home.headings) == 1, "Homepage needs exactly one h1"
    assert len(home.meta.get("description", "")) >= 80, "Missing page description"
    assert home.meta.get("og:url") == SITE and home.canonical == SITE, "Wrong canonical URL"
    image = home.meta.get("og:image", "")
    assert image.startswith(SITE), "Social image must be a public site asset"
    assert (ROOT / image.removeprefix(SITE)).is_file(), "Missing social image"
    assert home.meta.get("twitter:card") == "summary_large_image", "Missing share metadata"
    assert {"main", *ROUTES} <= set(home.ids), "Missing homepage section"

    for name, page in pages.items():
        assert len(page.ids) == len(set(page.ids)), f"Duplicate ID in {name}"
        assert page.meta.get("viewport"), f"Missing viewport in {name}"
        for link in page.links:
            url = urlsplit(link)
            if url.scheme or url.netloc:
                assert url.scheme in ("https", "mailto"), f"Unexpected link scheme: {link}"
                continue
            relative = unquote(url.path)
            assert not relative.startswith("/"), f"Root-relative URL breaks Pages subpath: {link}"
            target = (ROOT / (relative or name)).resolve()
            assert target.is_relative_to(ROOT), f"Link escapes project: {link}"
            assert target.is_file(), f"Missing local target: {link} in {name}"
            if url.fragment:
                assert target.name in pages, f"Cannot validate fragment target: {link}"
                assert url.fragment in pages[target.name].ids, f"Missing section: {link}"
        if name != "index.html":
            section = name.removesuffix(".html")
            assert page.meta.get("refresh") == f"0; url=index.html#{section}", f"Broken redirect: {name}"
        text = page.path.read_text(encoding="utf-8")
        assert not re.search(r"(?:\+61[\s-]?4\d{2}|04\d{2})[\s-]?\d{3}[\s-]?\d{3}", text), f"Phone number in {name}"
        assert not re.search(r"(?i)(?:passport|date of birth|bank account|visa grant number)\s*[:=]", text), f"Private-data field in {name}"
    assert (ROOT / "robots.txt").is_file() and (ROOT / "sitemap.xml").is_file(), "Missing crawler files"
    print(f"PASS: {len(pages)} pages; internal links, redirects, metadata, assets and basic privacy checks")


if __name__ == "__main__":
    main()
