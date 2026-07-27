<p align="center">
  <img src="assets/readme-header.png" alt="TikTok Patches for Morphe" width="290"/>
</p>

<p align="center">
  <a href="https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tiktok-43-8-3-release/tiktok-43-8-3-2-android-apk-download/"><img alt="TikTok 43.8.3" src="https://img.shields.io/badge/TikTok-43.8.3-ff0050?style=flat-square" /></a>
  <a href="https://github.com/hxreborn/tiktok-patches-for-morphe/releases/latest"><img alt="release" src="https://img.shields.io/github/v/release/hxreborn/tiktok-patches-for-morphe?style=flat-square&color=ff0050&label=release" /></a>
  <a href="https://github.com/hxreborn/tiktok-patches-for-morphe/commits/main"><img alt="commits since release" src="https://img.shields.io/github/commits-since/hxreborn/tiktok-patches-for-morphe/latest?style=flat-square&color=00b894&label=since%20release" /></a>
  <a href="LICENSE"><img alt="license" src="https://img.shields.io/badge/license-GPLv3-blue?style=flat-square" /></a>
</p>

# TikTok Patches for Morphe

<br>

Personal TikTok patches for [Morphe](https://github.com/MorpheApp/morphe-cli), based on [icysymmetra](https://github.com/icysymmetra)'s work. Targets `com.zhiliaoapp.musically` on [TikTok `43.8.3`](https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tiktok-43-8-3-release/tiktok-43-8-3-2-android-apk-download/).

<br>

## Install

On the device where Morphe is installed: [add to Morphe](https://morphe.software/add-source?github=hxreborn/tiktok-patches-for-morphe), or paste the repo URL as a source: `https://github.com/hxreborn/tiktok-patches-for-morphe`.

Or try a prebuilt APK (at your own risk): [hxreborn/Morphe-AutoBuilds](https://github.com/hxreborn/Morphe-AutoBuilds/releases/latest).

<br>

## Patches

| Patch | Description |
|---|---|
| `Always show publish date` | Keeps the video's publish date visible in its author information. |
| `Copy comments without username` | Copies only the comment text without including the creator's username. |
| `Custom offline videos limit` | Adds a custom entry to TikTok's offline videos menu with a configurable limit of up to 500 videos. |
| `Disable login requirement` | Removes TikTok's mandatory login gate from supported flows. |
| `Disable screen capture detection` | Prevents TikTok from detecting screenshots and screen recordings. |
| `Downloads` | Adds watermark-free video and photo downloads, comment sticker saving, and configurable download-folder controls. |
| `Enable Open Debug` | Repurposes TikTok's hidden Open Debug row as the entry point for Morphe settings. Required for the settings menu to appear. |
| `Feature Gate Lab` | Adds a searchable menu for viewing and overriding supported TikTok feature flags and configuration values. Client-side overrides cannot bypass server enforcement. |
| `Feed filter` | Hides feed ads, TikTok Shop items, livestreams, stories, photo posts, the playlist bar, the floating event badge, AI-generated posts, paid partnership and promotional content, friend recommendations, posts from verified accounts\*, and videos outside configured view or like ranges. |
| `Feed tab navigation` | Controls which loaded top and bottom navigation tabs remain visible, blocks newly added tabs when requested, and can hide the Tako AI bubble. |
| `Fix Google login` | Restores Google account sign-in after patching. |
| `Hide CAPTCHA popups` | Prevents client-side verification puzzle dialogs from opening, including those shown while browsing LIVE. It does not bypass server-side checks. |
| `Hold-and-slide 2x lock` | Enables TikTok's native hold, slide down, and release gesture for locking playback at 2x speed. |
| `Open external links directly` | Opens profile and story website links in the system browser instead of TikTok's in-app browser. |
| `Playback speed` | Enables playback-speed controls for all videos and remembers the selected speed between videos. |
| `Remember clear display` | Remembers TikTok's clear-display state between videos. |
| `SIM spoof` | Replaces SIM country and operator values reported to TikTok and provides country presets. TikTok may still use IP address, account history, language, and other region signals. |
| `Sanitize sharing links` | Removes tracking parameters from TikTok links before they are shared. |
| `Settings` | Adds the Morphe settings screen inside TikTok. |
| `Show seekbar` | Shows TikTok's native video seekbar where it would normally be hidden. |
| `Translate comments` | Adds comment translation controls using TikTok's translation system, with selectable language exclusions. |

This fork adds, on top of [icysymmetra's upstream](https://github.com/icysymmetra/tiktok-patches-for-morphe):

- Hide AI-generated posts
- Hide paid partnership and promotional content posts
- Hide friend-recommendation posts
- Hide posts from verified accounts\*
- Disable telemetry, ByteDance/AppsFlyer/Firebase
- Hide the in-feed playlist bar
- Hide the floating promotional event badge, e.g. FIFA World Cup
- Separate image and video download folders

\*TikTok decides which accounts count as institutions. Major news outlets usually carry that tag and
stay in your feed, but some do not and get hidden like any other verified account.

<br>

## Building

```bash
./gradlew :patches:buildAndroid :patches:generatePatchesList
```

Outputs `patches/build/libs/patches-<version>.mpp`.

<br>

## Credits

Built on [icysymmetra/tiktok-patches-for-morphe](https://github.com/icysymmetra/tiktok-patches-for-morphe) and everyone upstream of it. [@lyyako](https://github.com/lyyako) authored the original sanitize sharing links, show seekbar, anti-recording, `Open external links directly`, and `Always show publish date` work. See [NOTICE](NOTICE) for full attribution. Not affiliated with TikTok, ByteDance, or Morphe.

<br>

## License

GPLv3. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
