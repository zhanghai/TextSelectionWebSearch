#!/bin/bash
set -e

for dpi in {m,h,xh,xxh,xxxh}dpi; do
    cp "launcher_icon-${dpi}.png" "../app/src/main/res/mipmap-${dpi}/launcher_icon.png"
    cp "launcher_icon_foreground-${dpi}.png" "../app/src/main/res/mipmap-${dpi}/launcher_icon_foreground.png"
done

cp launcher_icon-play.png ../fastlane/metadata/android/en-US/images/icon.png
