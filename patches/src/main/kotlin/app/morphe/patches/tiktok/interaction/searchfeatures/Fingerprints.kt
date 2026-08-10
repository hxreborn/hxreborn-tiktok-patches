/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.interaction.searchfeatures

import app.morphe.patcher.Fingerprint

internal object NonPersonalizedSearchGateFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("enable_non_personalized_search"),
)

internal object NonPersonalizedSearchStateFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("non_personalized_search_state_"),
)

internal object LiveSearchGateFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    custom = { method, classDef ->
        classDef.type ==
            "Lcom/bytedance/android/livesdk/livesetting/feed/LiveDrawerSearchEnableSetting;" &&
            method.name == "getValue"
    },
)
