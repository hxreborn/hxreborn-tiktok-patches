<p align="center">
  <a href="https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tiktok-46-2-3-release/tiktok-46-2-3-android-apk-download/"><img alt="TikTok 46.2.3" src="https://img.shields.io/badge/TikTok-46.2.3-ff0050?style=flat-square" /></a>
  <a href="https://github.com/hxreborn/hxreborn-tiktok-patches/releases/latest"><img alt="release" src="https://img.shields.io/github/v/release/hxreborn/hxreborn-tiktok-patches?style=flat-square&color=ff0050&label=release" /></a>
  <a href="https://github.com/hxreborn/hxreborn-tiktok-patches/commits/main"><img alt="commits since release" src="https://img.shields.io/github/commits-since/hxreborn/hxreborn-tiktok-patches/latest?style=flat-square&color=00b894&label=since%20release" /></a>
  <a href="LICENSE"><img alt="license" src="https://img.shields.io/badge/license-GPLv3-blue?style=flat-square" /></a>
  <a href="https://morphe.software/add-source?github=hxreborn/hxreborn-tiktok-patches"><img alt="add source" src="https://img.shields.io/badge/Morphe-Add%20Source-00a8ff?style=flat-square" /></a>
</p>

# hxreborn TikTok Patches

<br>

Personal TikTok patches for [Morphe](https://github.com/MorpheApp/morphe-cli), based on [icysymmetra](https://github.com/icysymmetra)'s work. Targets `com.zhiliaoapp.musically` on [TikTok `46.2.3`](https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tiktok-46-2-3-release/tiktok-46-2-3-android-apk-download/).

<br>

## Install

On the device where Morphe is installed: [add to Morphe](https://morphe.software/add-source?github=hxreborn/hxreborn-tiktok-patches), or paste the repo URL as a source: `https://github.com/hxreborn/hxreborn-tiktok-patches`.

Or try a prebuilt APK (at your own risk): [hxreborn/Morphe-AutoBuilds](https://github.com/hxreborn/Morphe-AutoBuilds/releases/latest).

<br>

## Patches

Everything from [icysymmetra's upstream](https://github.com/icysymmetra/tiktok-patches-for-morphe), plus:

- Hide AI-generated posts
- Hide paid partnership and promotional content posts
- Hide friend-recommendation posts
- Hide posts from verified accounts\*
- Disable telemetry, ByteDance/AppsFlyer/Firebase
- Hide the in-feed playlist bar
- Hide the floating promotional event badge, e.g. FIFA World Cup
- Hide suggested accounts on the Activity, New followers, and Inbox pages
- Hide the Inbox stories row
- Expand the Activity and New followers lists instead of hiding them behind View all
- Separate image and video download folders

\*TikTok decides which accounts count as institutions. Major news outlets usually carry that tag and
stay in your feed, but some do not and get hidden like any other verified account.

Last synced with upstream at [`e06bb70`](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/e06bb70). Release numbers are this fork's own and do not track upstream's.

<br>

## Building

```bash
./gradlew :patches:buildAndroid :patches:generatePatchesList
```

Outputs `patches/build/libs/patches-<version>.mpp`.

<br>

## Credits

Built on [icysymmetra/tiktok-patches-for-morphe](https://github.com/icysymmetra/tiktok-patches-for-morphe) and everyone upstream of it. See [NOTICE](NOTICE) for full attribution. Not affiliated with TikTok, ByteDance, or Morphe.

<br>

## License

GPLv3. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
