## [0.11.4](https://github.com/hxreborn/hxreborn-tiktok-patches/compare/v0.11.1...v0.11.4) (2026-08-19)


### Bug Fixes

* **build:** complete feed model stubs ([053ce6c](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/053ce6cd064c16c1a06e87e10b76896da7c00a83))
* **captcha:** stop suppressing risk-control puzzles by default ([bb48b71](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/bb48b71b8aa09d6447528bb7cba26d8685e6e286))
* **feedfilter:** filter cached feed insertions ([d30a6dd](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/d30a6dd37f9eaa1c1faecdf2b30b047707170860))
* **feedfilter:** hide every inserted feed card instead of only bulletin-board ones ([85d3a55](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/85d3a5579a8036ccf4c9bf514787ce97349860c9))

## [0.11.1](https://github.com/hxreborn/hxreborn-tiktok-patches/compare/v0.11.0...v0.11.1) (2026-08-16)


### Bug Fixes

* **feedfilter:** stop hiding posts that carry no paid-partnership label ([39d15ab](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/39d15ab20dd0385e0c16e4c219dd19d9083a0989))

# [0.11.0](https://github.com/hxreborn/hxreborn-tiktok-patches/compare/v0.10.0...v0.11.0) (2026-08-16)


### Bug Fixes

* **captcha:** also suppress BdTuring risk-control captcha popups ([d5f6a01](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/d5f6a01df90710f6c01f8d76feb972a4f93a47ce))
* **clear-display:** preserve state across feed transitions ([a433fe0](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/a433fe03b98ecee8814a83f94aaef760222e538f))
* **downloads:** keep stickers out of the video folder when migrating a custom path ([3f21cce](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/3f21cce9c8e474c9e7935a4c7bc70756ade4ba87))
* **playback:** persist explicit speed selections ([0f785fc](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/0f785fc8685c30793bf7ffea028d1223613f52c9))
* **settings:** make custom dialogs fit device screens ([7592339](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/75923397520ff45fde1b5f618c1de2010de422e0))
* **settings:** wrap menu summaries to three lines ([d383ea8](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/d383ea8c6ed69c56a77ede21cc2e2eb267ebc950))
* **tiktok:** cover direct Turing CAPTCHA dialogs ([27b2639](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/27b263920ffadaf2e27777c703c225f2e4f3ce40))
* **tiktok:** expand startup and runtime hook coverage ([bac0ba8](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/bac0ba8d2697a60d2c66d723fba75e211ca48a49))
* **tiktok:** prevent settings crash and expand crash reports ([08186e7](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/08186e77ba89d92debfcd319fe8fcc745e2a363b))


### Features

* **downloads:** support separate media destinations ([f4580c9](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/f4580c94b3b2c0d2d79c2bb0bfa6a544abeaedb3))
* **feedfilter:** hide account and bulletin-board suggestion cards in the feed ([3e11a12](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/3e11a12cc8e01351b9733ae1aed86789238009d2))
* **inbox:** hide suggested accounts and stories and expand activity lists ([bc53dbb](https://github.com/hxreborn/hxreborn-tiktok-patches/commit/bc53dbb7b09ce67b0d1c76f16dd2709d28c15692))

# [0.10.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.9.1...v0.10.0) (2026-08-10)


### Bug Fixes

* **build:** select current bundle for patch catalog ([69bd1e9](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/69bd1e93194825b7af567e8456de4f29831fd9a8))
* **morphe:** support patching on older Android versions ([01463a0](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/01463a04e2a64a2a1400391778576779cfb2b211)), closes [#85](https://github.com/hxreborn/tiktok-patches-for-morphe/issues/85)
* **tiktok:** filter following ads at cached feed reads ([13d74f0](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/13d74f00b1d076aa22b7a4ab8bbe92bfaf2c4b6b))
* **tiktok:** filter late following feed ads ([a232cba](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/a232cbaeb3725f0c1361d20a99e9a6fd31a0be53))
* **tiktok:** hook native video looping boundary ([2721f01](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2721f010b670842f9776b2afb34486de418f29d9))
* **tiktok:** preserve account verification CAPTCHA flows ([dd35d89](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/dd35d8948b002f0d098ed1f83a3f64cddc5f7904))
* **tiktok:** preserve native download addresses ([61da3c6](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/61da3c6413ed75cdb04f5eb1cb4b7292dfd06089))
* **tiktok:** repair automatic comment translation ([636ef64](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/636ef648c18be2610c6ee48beed7270e37ffce41))
* **tiktok:** reset completed video resume state ([100cab5](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/100cab58fc178f360c2e727308e161e3e3e2f9ad))
* **tiktok:** skip same-language automatic translations ([3415710](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/3415710facfc60db19943599112b9bc47cf82647))
* **tiktok:** use native video loop control ([f508adb](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f508adbdbf6229e7a993a42616ed00cc0feadcd2))


### Features

* **feedfilter:** adapt hiding AI-generated posts to TikTok 46.2.3 ([4d31a23](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/4d31a230b44b0790c9292ad76f50bfa1f3fae68b))
* **patches:** retarget the bundle to TikTok 46.2.3 ([6495c15](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/6495c15e8aad0c7124b9ac1434cb147dbe12de70))
* **settings:** load settings safely when TikTok reads them before init ([f731841](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f731841cb628ddbcc24a7cd25f40faa178b61018))
* **settings:** rename the in-app settings entry to Morphe patches ([f267269](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f267269e80733ca155d2a586e0eceda3ed50a59a))
* **telemetry:** adapt disabling analytics to TikTok 46.2.3 ([56a335c](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/56a335ce780de6da2d03d2b6dd8b00b98f22e26c))
* **tiktok:** add native search and seekbar controls ([c36bfaf](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/c36bfaf51d6e8d30fbef6e5c6ba99bfdeae283e2))
* **tiktok:** add playback completion controls ([9a51c2f](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/9a51c2f614145250177600144abd61eb30cf0841))
* **tiktok:** add quick-action controls ([fe29a24](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/fe29a249e42620d53dc6224c347c74aadc291c08))
* **tiktok:** port core patches to TikTok 46.2.3 ([2c52b6a](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2c52b6aa1362a3a9ccbcdd8dad2e16ec4b9a06cc))
* **tiktok:** port Feature Gate Lab to TikTok 46.2.3 ([4bdbdee](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/4bdbdeed1649bdc6177ea6a2c62bd2d9300ac8d6))
* **tiktok:** redesign patch settings navigation ([a479bdb](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/a479bdb09a4e01584f9d683cdfda1c65d3ab6dff))
* **tiktok:** restore download templates and sticker formats ([91d158c](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/91d158ca3da1126a2d2ca9f4c2330e81d3ea96f9))
* **tiktok:** separate optional diagnostics from settings entry ([aad18c3](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/aad18c3e4b301366685174b0faa5a23a8a2850de))

# [0.10.0-dev.1](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.9.1...v0.10.0-dev.1) (2026-08-10)


### Bug Fixes

* **build:** select current bundle for patch catalog ([69bd1e9](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/69bd1e93194825b7af567e8456de4f29831fd9a8))
* **morphe:** support patching on older Android versions ([01463a0](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/01463a04e2a64a2a1400391778576779cfb2b211)), closes [#85](https://github.com/hxreborn/tiktok-patches-for-morphe/issues/85)
* **tiktok:** filter following ads at cached feed reads ([13d74f0](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/13d74f00b1d076aa22b7a4ab8bbe92bfaf2c4b6b))
* **tiktok:** filter late following feed ads ([a232cba](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/a232cbaeb3725f0c1361d20a99e9a6fd31a0be53))
* **tiktok:** hook native video looping boundary ([2721f01](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2721f010b670842f9776b2afb34486de418f29d9))
* **tiktok:** preserve account verification CAPTCHA flows ([dd35d89](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/dd35d8948b002f0d098ed1f83a3f64cddc5f7904))
* **tiktok:** preserve native download addresses ([61da3c6](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/61da3c6413ed75cdb04f5eb1cb4b7292dfd06089))
* **tiktok:** repair automatic comment translation ([636ef64](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/636ef648c18be2610c6ee48beed7270e37ffce41))
* **tiktok:** reset completed video resume state ([100cab5](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/100cab58fc178f360c2e727308e161e3e3e2f9ad))
* **tiktok:** skip same-language automatic translations ([3415710](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/3415710facfc60db19943599112b9bc47cf82647))
* **tiktok:** use native video loop control ([f508adb](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f508adbdbf6229e7a993a42616ed00cc0feadcd2))


### Features

* **feedfilter:** adapt hiding AI-generated posts to TikTok 46.2.3 ([4d31a23](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/4d31a230b44b0790c9292ad76f50bfa1f3fae68b))
* **patches:** retarget the bundle to TikTok 46.2.3 ([6495c15](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/6495c15e8aad0c7124b9ac1434cb147dbe12de70))
* **settings:** load settings safely when TikTok reads them before init ([f731841](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f731841cb628ddbcc24a7cd25f40faa178b61018))
* **settings:** rename the in-app settings entry to Morphe patches ([f267269](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f267269e80733ca155d2a586e0eceda3ed50a59a))
* **telemetry:** adapt disabling analytics to TikTok 46.2.3 ([56a335c](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/56a335ce780de6da2d03d2b6dd8b00b98f22e26c))
* **tiktok:** add native search and seekbar controls ([c36bfaf](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/c36bfaf51d6e8d30fbef6e5c6ba99bfdeae283e2))
* **tiktok:** add playback completion controls ([9a51c2f](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/9a51c2f614145250177600144abd61eb30cf0841))
* **tiktok:** add quick-action controls ([fe29a24](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/fe29a249e42620d53dc6224c347c74aadc291c08))
* **tiktok:** port core patches to TikTok 46.2.3 ([2c52b6a](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2c52b6aa1362a3a9ccbcdd8dad2e16ec4b9a06cc))
* **tiktok:** port Feature Gate Lab to TikTok 46.2.3 ([4bdbdee](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/4bdbdeed1649bdc6177ea6a2c62bd2d9300ac8d6))
* **tiktok:** redesign patch settings navigation ([a479bdb](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/a479bdb09a4e01584f9d683cdfda1c65d3ab6dff))
* **tiktok:** restore download templates and sticker formats ([91d158c](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/91d158ca3da1126a2d2ca9f4c2330e81d3ea96f9))
* **tiktok:** separate optional diagnostics from settings entry ([aad18c3](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/aad18c3e4b301366685174b0faa5a23a8a2850de))

## [0.9.1](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.9.0...v0.9.1) (2026-07-30)


### Bug Fixes

* **tiktok:** prevent seekbar recursion on stories ([9d2c0bc](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/9d2c0bc823ce661b385fff12945ac5c2ace0223f))

# [0.9.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.8.0...v0.9.0) (2026-07-27)


### Features

* **tiktok:** add feature gate lab and UI suppressors ([72bf32a](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/72bf32aa6991257ace0bab91d3a8767617cbd802))
* **tiktok:** add translation language exclusions ([8f60185](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/8f60185434a3cbb13653438a6561e2d7fc45f4dd))
* **tiktok:** enable Feature Gate Lab by default ([a164b70](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/a164b70cf046a738d3b0a817a57b1fedf515ef2e))
* **tiktok:** enable hold-and-slide 2x lock ([a2fd626](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/a2fd626ecb1fb116461b760445128bfdd8980f29))
* **tiktok:** improve feature gate lab search ([ffc19de](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/ffc19dece888a6ef814d20401d0bc4b9ba93858d))
* **tiktok:** port external browser patch from lyyako ([7a517c2](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/7a517c218a35508ac4f284450ff6d6c4d3ea05d8))
* **tiktok:** port publish date patch from lyyako ([1b6c386](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/1b6c386611239c39242bc813fca9614c8de3d3d1))
* **tiktok:** redesign support row ([cc39469](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/cc39469c98eabc0ccdea40fc423e94639c34703f))


### Reverts

* **feedfilter:** restore event badge hook over upstream promo patch ([5e0de5c](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/5e0de5c6bf6161c1d0d72b9985fb4830de260342))

# [0.8.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.7.0...v0.8.0) (2026-07-25)


### Bug Fixes

* **downloads:** default video path to Movies/TikTok ([10227c1](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/10227c1a9793180cf95252efea3991ff4850c62d))
* **downloads:** keep working across TikTok builds ([b4d5804](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/b4d5804f30faff05c9e02a20ff30bbf65d225011))
* **navigation:** keep tab controls across TikTok builds ([206912a](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/206912a264e11df9d50c09d01717be423c4af9f4))


### Features

* **feedfilter:** hide friend-recommendation posts ([2d99209](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2d9920965a1255980e379eff08bb4c8c6c2475f7))
* **feedfilter:** hide paid partnership posts ([d446d8a](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/d446d8a05f364a5bac5d34fce69df2c0a21b8a25))
* **feedfilter:** hide posts from verified accounts ([f8b831c](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f8b831cc24759bd0321891201993a07e3c6b0e66))
* **feedfilter:** hide promotional content posts ([f9e9015](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/f9e90150fa96678a6467995d1e7911ceb29a0061))

## [0.7.1-dev.2](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.7.1-dev.1...v0.7.1-dev.2) (2026-07-20)


### Bug Fixes

* **downloads:** default video path to Movies/TikTok ([10227c1](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/10227c1a9793180cf95252efea3991ff4850c62d))

## [0.7.1-dev.1](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.7.0...v0.7.1-dev.1) (2026-07-20)


### Bug Fixes

* **downloads:** keep working across TikTok builds ([b4d5804](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/b4d5804f30faff05c9e02a20ff30bbf65d225011))
* **navigation:** keep tab controls across TikTok builds ([206912a](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/206912a264e11df9d50c09d01717be423c4af9f4))

# [0.7.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.6.0...v0.7.0) (2026-07-20)


### Features

* **downloads:** change default download video path to Videos/TikTok ([bfdbda6](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/bfdbda645c794663c374186a10ef1adc5e766859))
* **telemetry:** make disabling analytics optional ([2afa561](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2afa5615c1512e52d9f209f17814e4b2f7ad3bfa))

# [0.7.0-dev.1](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.6.0...v0.7.0-dev.1) (2026-07-19)


### Features

* **downloads:** change default download video path to Videos/TikTok ([bfdbda6](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/bfdbda645c794663c374186a10ef1adc5e766859))
* **telemetry:** make disabling analytics optional ([2afa561](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2afa5615c1512e52d9f209f17814e4b2f7ad3bfa))

# [0.6.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.5.0...v0.6.0) (2026-07-19)


### Features

* **feedfilter:** hide floating event badge ([2141c1d](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2141c1d99e9b9cccede63e526d82cebe46a79091))

# [0.6.0-dev.1](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.5.0...v0.6.0-dev.1) (2026-07-19)


### Features

* **feedfilter:** hide floating event badge ([2141c1d](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/2141c1d99e9b9cccede63e526d82cebe46a79091))

# [0.5.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.4.0...v0.5.0) (2026-07-19)


### Features

* **feedfilter:** hide in-feed playlist bar ([ae2be23](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/ae2be2398359048df76ddad002b143a0c7cba1a1))
* **telemetry:** disable ByteDance/AppsFlyer/Firebase analytics ([fb2425e](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/fb2425e3d3b8da84c85da2123234704f981f53a4))
* **tiktok:** add separate image download path ([5215368](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/52153681fca942491ba45e2c8572e46d4385f080))
* **tiktok:** filter ai generated posts from feed ([58671de](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/58671de5e4c0a2baebb42d643c66d27f9ff273c2))

# [0.4.0](https://github.com/hxreborn/tiktok-patches-for-morphe/compare/v0.3.1...v0.4.0) (2026-07-17)


### Features

* **tiktok:** filter ai generated posts from feed ([1143f77](https://github.com/hxreborn/tiktok-patches-for-morphe/commit/1143f7773e747d6ab66a986534a88035a20447ae))

## [0.3.1](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.3.0...v0.3.1) (2026-06-17)


### Bug Fixes

* avoid concurrent feed list iteration crash ([275e8d5](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/275e8d5f3ad273b24a5bd592b0da86bfe7dc3ee4))

# [0.3.0](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.2.0...v0.3.0) (2026-06-17)


### Bug Fixes

* improve settings menu dark mode layout ([2aa837b](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/2aa837b165e16c3bf435b385f15160c0ad6c41d1))
* prefer largest clean download source ([4f63b8a](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/4f63b8aab765373041cbce77135efd55f34623c0))
* reduce repeated feed filter scans ([d161ef2](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/d161ef20c8d9648fb4e48d84655f062c9be99813))
* restore downloads fallback for restricted videos ([32cfafe](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/32cfafeaf10ad1bcf87d72a38927977f76000ec5))


### Features

* add navigation translation and settings controls ([72ad5de](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/72ad5de04b8f7c670f25ca718d219d06124fe553))
* add SIM spoof country presets ([bb73b3d](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/bb73b3dfebdcbbf9f612a7716e408a8928b56568))
* copy comments without username ([ac01d5f](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/ac01d5f8f2e673d6b72a38a6abf5d5da27330208))
* filter bottom navigation tabs ([e1f01b2](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/e1f01b25e35bb6d284691c273ae4b0b4778a57dc))
* save comment media downloads ([77b4dab](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/77b4dabcd6f327a7b9f06e5646193a0d7e54a52f))
* **tiktok:** add custom offline videos limit ([bfcb717](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/bfcb717d9e6fdcfb69ec7d0b30016352ed6d6c90))
* **tiktok:** add focused debug diagnostics ([878d0f4](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/878d0f44b4b1bc1846ecb888ca3f9536fcf4db11))


### Performance Improvements

* reduce feed filter scanning overhead ([ac13277](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/ac13277743e2c5f4022a4e1bafea1caf067520fe))

# [0.2.0](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.1.5...v0.2.0) (2026-06-06)


### Bug Fixes

* improve settings menu dark mode layout ([4da9d87](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/4da9d8785f0e6ad97f8c9ad7e4e3cf5655f2e0f4))
* reduce repeated feed filter scans ([370b806](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/370b806cc1c64574d767b397d612e79a58ed6896))
* restore downloads fallback for restricted videos ([277f400](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/277f4003643b35045b62f9f95f63ed2d8bb889bf))


### Features

* add SIM spoof country presets ([7f8fb32](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/7f8fb32dd7bc1f0b34078482216c087914b8ff4c))


### Performance Improvements

* reduce feed filter scanning overhead ([007d1be](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/007d1be0a93393a7ca1935cea8740cb167b5e6f9))

## [0.1.5](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.1.4...v0.1.5) (2026-06-01)


### Bug Fixes

* limit TikTok compatibility to global package ([49806ad](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/49806ad5a1c75bc2d7f052abf227b7d055879935))
* sanitize TikTok sharing links before shortening ([53a3558](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/53a3558f7e1921ccd2636fe2d322a4b7c1ca3d04))

## [0.1.4](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.1.3...v0.1.4) (2026-05-30)


### Bug Fixes

* improve settings readability and debug logging ([f4f4bc4](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/f4f4bc4dc9104182c1a419878ddf837057555340))

## [0.1.3](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.1.2...v0.1.3) (2026-05-29)


### Bug Fixes

* support global settings top row insertion ([c38f1b6](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/c38f1b63d32b3d7218de1f5c46243f728c6c5868))

## [0.1.2](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.1.1...v0.1.2) (2026-05-29)


### Bug Fixes

* fallback open debug settings insertion ([e201d53](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/e201d533d19504f4e32001d6985cad7ee9804b15))

## [0.1.1](https://github.com/icysymmetra/tiktok-patches-for-morphe/compare/v0.1.0...v0.1.1) (2026-05-29)


### Bug Fixes

* add release changelog script ([74fa14e](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/74fa14ebe975509816a9344031dd176a76595407))
* distinguish global and jp tiktok packages ([7e1f7df](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/7e1f7dfd23d79346529715854a9b1840af210d47))
* soften support preference copy ([5cbfd26](https://github.com/icysymmetra/tiktok-patches-for-morphe/commit/5cbfd26030b35d5b2996ead5073b2ffd7df166a5))
