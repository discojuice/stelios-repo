#!/usr/bin/env python3
"""Generate small preview images for the blog grid.

Writes src/assets/blog/thumbs/<name>.jpg for every photo and video in
src/assets/blog. Run it again after adding new media:

    pip install pillow imageio-ffmpeg
    python3 scripts/make-thumbs.py
"""
import io
import subprocess
from pathlib import Path

import imageio_ffmpeg
from PIL import Image, ImageOps

BLOG_DIR = Path(__file__).resolve().parent.parent / 'src' / 'assets' / 'blog'
THUMB_DIR = BLOG_DIR / 'thumbs'
SHORT_SIDE = 600  # grid tiles are at most ~420 CSS px, so this covers 2x screens
IMAGE_EXT = {'.jpg', '.jpeg', '.png', '.webp'}
VIDEO_EXT = {'.mp4', '.mov', '.webm'}


def save_thumb(img: Image.Image, out: Path) -> None:
    img = ImageOps.exif_transpose(img).convert('RGB')
    scale = SHORT_SIDE / min(img.size)
    if scale < 1:
        img = img.resize((round(img.width * scale), round(img.height * scale)), Image.LANCZOS)
    img.save(out, 'JPEG', quality=72, optimize=True, progressive=True)


def video_frame(path: Path) -> Image.Image:
    ffmpeg = imageio_ffmpeg.get_ffmpeg_exe()
    data = subprocess.run(
        [ffmpeg, '-v', 'error', '-ss', '1', '-i', str(path), '-frames:v', '1', '-f', 'image2', '-c:v', 'png', '-'],
        check=True, capture_output=True).stdout
    return Image.open(io.BytesIO(data))


def main() -> None:
    THUMB_DIR.mkdir(exist_ok=True)
    for src in sorted(BLOG_DIR.iterdir()):
        ext = src.suffix.lower()
        if ext not in IMAGE_EXT | VIDEO_EXT:
            continue
        out = THUMB_DIR / (src.stem + '.jpg')
        if out.exists() and out.stat().st_mtime >= src.stat().st_mtime:
            continue
        save_thumb(video_frame(src) if ext in VIDEO_EXT else Image.open(src), out)
        print(f'{src.name} -> thumbs/{out.name} ({out.stat().st_size // 1024} KB)')


if __name__ == '__main__':
    main()
