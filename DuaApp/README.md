# Dua App

A minimal Android app for "The Comprehensive Supplication" — Arabic text in
embedded Scheherazade New, English translation, and a toggle switch to
show/hide the translation for each entry (top-right of the screen).

## Why you're getting source, not a compiled APK

This project was generated in a sandboxed environment with no access to
Google's Maven/Android SDK repositories, so it can't run Gradle here to
produce a signed .apk. Building it locally (Android Studio, or `./gradlew`
from a machine with normal internet access) takes about 2 minutes.

## To build

1. Open the `DuaApp/` folder in Android Studio (File → Open).
2. Let it sync Gradle (needs internet the first time, to fetch AGP/Kotlin/
   AndroidX — after that it's cached).
3. Run on a device/emulator, or Build → Generate Signed Bundle / APK for a
   real .apk you can sideload onto the Kompakt.

Or from the command line on a machine with internet access:

```
cd DuaApp
./gradlew assembleDebug
```

The APK will land in `app/build/outputs/apk/debug/app-debug.apk`.

## Structure

- `app/src/main/assets/dua.json` — all 20 dua entries (Arabic + translation +
  divider flag). Edit this file to add/change entries; no code changes needed.
- `app/src/main/res/font/scheherazade_new.ttf` — embedded font (SIL Open
  Font License, see `licenses/ScheherazadeNew-OFL.txt`), so Arabic renders
  correctly regardless of what's on the device.
- `app/src/main/java/com/stephane/dua/MainActivity.kt` — loads the JSON,
  inflates one `item_dua.xml` per entry, wires the translation toggle switch.
- `app/src/main/res/layout/item_dua.xml` — centered Arabic on top, gray
  translation text below (toggle-able), optional divider line.
- Theme is flat black-on-white with no elevation/shadows — matches your
  MMD e-ink design approach on the other Kompakt apps.

## Notes

- `minSdk = 24`, `targetSdk = 34` — adjust if the Kompakt's Android version
  needs something different.
- The launcher icon (`res/drawable/ic_launcher.xml`) is a placeholder vector
  crescent — regenerate via Android Studio's Image Asset tool if you want
  something nicer.
- Switch defaults to "on" (translations visible) on launch.
