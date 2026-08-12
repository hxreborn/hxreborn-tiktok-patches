/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/interaction/speed/Fingerprints.kt
 */
package app.morphe.patches.tiktok.interaction.speed

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object PlaybackSpeedMenuSelectionFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf(
        "Landroid/view/View;",
        "Lcom/ss/android/ugc/aweme/share/base/model/BaseSharePackage;",
    ),
    strings = listOf(
        "enter_from",
        "panel_source",
        "click_share_button",
    ),
)

internal object SetSpeedFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/Object;",
    strings = listOf("playback_speed"),
    custom = { method, _ ->
        method.name == "invoke" && method.parameterTypes.isEmpty()
    },
)

internal object LongPressSpeedUpEnableFingerprint : Fingerprint(
    name = "<clinit>",
    returnType = "V",
    parameters = emptyList(),
    strings = listOf("long_press_speed_up_enable"),
)

internal object LongPressSpeedUpLockFingerprint : Fingerprint(
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = emptyList(),
    strings = listOf("long_press_speed_up_lock"),
)

