"""Alert-KMP README banner (2560x1280, GitHub's 2:1 social-preview shape), from the demo app's screenshots.

python3 tools/make_banner.py <screenshots-dir> docs/banner.png
The screenshots dir holds full-size android-alert.png, ios-menu.png and web-top-custom.png from the sample app.
"""
import random
import sys
from pathlib import Path
from PIL import Image, ImageChops, ImageDraw, ImageFilter, ImageFont

SHOTS, OUT = Path(sys.argv[1]), Path(sys.argv[2])
W, H = 2560, 1280
HELVETICA = "/System/Library/Fonts/HelveticaNeue.ttc"
MENLO = "/System/Library/Fonts/Menlo.ttc"

def font(size, bold=False):
    # HelveticaNeue.ttc: 0 regular, 1 bold.
    return ImageFont.truetype(HELVETICA, size, index=1 if bold else 0)

def background():
    """Deep navy-violet with soft glows and a faint starfield, like the KMP Device Hub banner."""
    img = Image.new("RGB", (W, H), (14, 11, 38))
    glow = Image.new("RGB", (W, H), (0, 0, 0))
    d = ImageDraw.Draw(glow)
    for cx, cy, r, c in [(640, 420, 760, (52, 34, 120)), (1900, 640, 820, (24, 40, 110)), (1280, 1500, 1000, (6, 6, 16))]:
        d.ellipse((cx - r, cy - r, cx + r, cy + r), fill=c)
    img = ImageChops.add(img, glow.filter(ImageFilter.GaussianBlur(260)))
    stars = ImageDraw.Draw(img)
    rnd = random.Random(11)
    for _ in range(340):
        x, y = rnd.randrange(W), rnd.randrange(H)
        b = rnd.randrange(70, 190)
        s = rnd.choice([1, 1, 2, 2, 3])
        stars.ellipse((x, y, x + s, y + s), fill=(b, b, min(255, b + 30)))
    return img.convert("RGBA")

def gradient_text(canvas, xy, text, fnt, left=(198, 180, 255), right=(240, 236, 255)):
    mask = Image.new("L", canvas.size, 0)
    ImageDraw.Draw(mask).text(xy, text, font=fnt, fill=255)
    x0, _, x1, _ = mask.getbbox()
    grad = Image.new("RGBA", canvas.size)
    gd = ImageDraw.Draw(grad)
    for x in range(x0, x1 + 1):
        t = (x - x0) / max(1, x1 - x0)
        gd.line((x, 0, x, H), fill=tuple(int(left[i] + (right[i] - left[i]) * t) for i in range(3)) + (255,))
    canvas.paste(grad, (0, 0), mask)

def rounded(img, r):
    m = Image.new("L", img.size, 0)
    ImageDraw.Draw(m).rounded_rectangle((0, 0, img.size[0] - 1, img.size[1] - 1), r, fill=255)
    out = Image.new("RGBA", img.size)
    out.paste(img.convert("RGBA"), (0, 0), m)
    return out

def card(canvas, img, xy, r, shadow=48, glow=(0, 0, 0, 170)):
    c = rounded(img, r)
    sh = Image.new("RGBA", (c.size[0] + shadow * 2, c.size[1] + shadow * 2), (0, 0, 0, 0))
    ImageDraw.Draw(sh).rounded_rectangle((shadow, shadow, shadow + c.size[0], shadow + c.size[1]), r, fill=glow)
    canvas.alpha_composite(sh.filter(ImageFilter.GaussianBlur(shadow / 2)), (xy[0] - shadow, xy[1] - shadow + 16))
    canvas.alpha_composite(c, xy)

def fit(img, mw, mh):
    s = min(mw / img.size[0], mh / img.size[1])
    return img.resize((int(img.size[0] * s), int(img.size[1] * s)), Image.LANCZOS)

def phone(screen, height):
    """A screenshot in a simple black bezel."""
    s = fit(screen.convert("RGB"), 10_000, height)
    bezel = 18
    f = Image.new("RGBA", (s.size[0] + bezel * 2, s.size[1] + bezel * 2), (0, 0, 0, 0))
    ImageDraw.Draw(f).rounded_rectangle((0, 0, f.size[0] - 1, f.size[1] - 1), 76, fill=(12, 12, 16))
    f.alpha_composite(rounded(s, 60), (bezel, bezel))
    return f

def browser(page, width):
    bar = 64
    p = fit(page.convert("RGB"), width, 10_000)
    f = Image.new("RGB", (p.size[0], p.size[1] + bar), (236, 237, 242))
    d = ImageDraw.Draw(f)
    for i, c in enumerate([(255, 95, 87), (254, 188, 46), (40, 200, 64)]):
        d.ellipse((26 + i * 34, 24, 44 + i * 34, 42), fill=c)
    d.rounded_rectangle((150, 16, p.size[0] - 40, 48), 16, fill=(255, 255, 255))
    d.text((176, 32), "localhost:8080  ·  Wasm", font=font(24), fill=(110, 112, 125), anchor="lm")
    f.paste(p, (0, bar))
    return f

def bell_icon(size):
    """App-icon style rounded square with a bell, drawn so no font glyph is needed."""
    s = size * 4
    icon = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    grad = Image.new("RGBA", (s, s))
    gd = ImageDraw.Draw(grad)
    a, b = (127, 82, 255), (30, 136, 229)
    for y in range(s):
        t = y / s
        gd.line((0, y, s, y), fill=tuple(int(a[i] + (b[i] - a[i]) * t) for i in range(3)) + (255,))
    mask = Image.new("L", (s, s), 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, s - 1, s - 1), int(s * 0.23), fill=255)
    icon.paste(grad, (0, 0), mask)
    d = ImageDraw.Draw(icon)
    w = (255, 255, 255, 255)
    d.ellipse((s * 0.30, s * 0.20, s * 0.70, s * 0.62), fill=w)          # dome
    d.rectangle((s * 0.30, s * 0.41, s * 0.70, s * 0.66), fill=w)        # body
    d.rounded_rectangle((s * 0.22, s * 0.63, s * 0.78, s * 0.71), s * 0.04, fill=w)  # rim
    d.ellipse((s * 0.44, s * 0.71, s * 0.56, s * 0.82), fill=w)          # clapper
    d.ellipse((s * 0.46, s * 0.13, s * 0.54, s * 0.21), fill=w)          # top knob
    d.ellipse((s * 0.64, s * 0.16, s * 0.84, s * 0.36), fill=(255, 92, 92, 255))  # badge
    return icon.resize((size, size), Image.LANCZOS)

canvas = background()
d = ImageDraw.Draw(canvas)

# Left: icon, name, tagline, platforms, one line of code.
canvas.alpha_composite(bell_icon(150), (130, 150))
gradient_text(canvas, (124, 330), "Alert-KMP", font(170, True))
d.multiline_text((132, 560), "Native alerts, toasts, notifications\nand menus for Kotlin & Compose\nMultiplatform. One API, every platform.",
                 font=font(50), fill=(205, 200, 235), spacing=18)

# Translucent shapes go on an overlay: drawing them straight onto the canvas would replace, not blend.
overlay = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
o = ImageDraw.Draw(overlay)
chips, x = [], 132
for label, color in [("Android", (61, 220, 132)), ("iOS", (120, 170, 255)), ("Desktop", (255, 140, 120)), ("Web", (255, 205, 90))]:
    tw = d.textlength(label, font=font(36, True))
    o.rounded_rectangle((x, 800, x + tw + 56, 862), 31, fill=color + (40,), outline=color + (255,), width=3)
    chips.append((x + 28, label, color))
    x += tw + 56 + 22
o.rounded_rectangle((132, 930, 1180, 1050), 24, fill=(6, 6, 20, 190), outline=(90, 78, 160, 255), width=2)
canvas.alpha_composite(overlay)
d = ImageDraw.Draw(canvas)
for cx, label, color in chips:
    d.text((cx, 831), label, font=font(36, True), fill=color, anchor="lm")

code = ImageFont.truetype(MENLO, 32)
d.text((168, 966), "createNotification(NotificationType.ALERT)", font=code, fill=(198, 180, 255))
d.text((168, 1008), '    .show("Order placed", title = "Shop")', font=code, fill=(150, 220, 170))

# Right: the web demo behind, an iPhone and an Android phone in front.
web = browser(Image.open(SHOTS / "web-top-custom.png"), 980)
card(canvas, web, (1440, 130), r=22, glow=(30, 136, 229, 110))
iphone = phone(Image.open(SHOTS / "ios-menu.png"), 860)
card(canvas, iphone, (1390, 330), r=76, glow=(127, 82, 255, 150))
android = phone(Image.open(SHOTS / "android-alert.png"), 820)
card(canvas, android, (1880, 380), r=76, glow=(30, 136, 229, 150))

OUT.parent.mkdir(parents=True, exist_ok=True)
canvas.convert("RGB").save(OUT, optimize=True)
print("wrote", OUT)
