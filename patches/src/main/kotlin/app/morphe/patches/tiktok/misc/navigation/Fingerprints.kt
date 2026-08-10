package app.morphe.patches.tiktok.misc.navigation

import app.morphe.patcher.Fingerprint

internal object TopTabModelListFingerprint : Fingerprint(
    definingClass = "/TabAbilityAssem;",
    name = "W52",
    returnType = "Ljava/util/List;",
    parameters = listOf(),
)

internal object BottomTabModelListFingerprint : Fingerprint(
    definingClass = "/TabAbilityAssem;",
    name = "OA",
    returnType = "Ljava/util/List;",
    parameters = listOf(),
)
