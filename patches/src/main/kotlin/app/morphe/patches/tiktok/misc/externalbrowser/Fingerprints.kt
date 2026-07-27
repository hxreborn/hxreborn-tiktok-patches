/*
 * Thanks to lyyako for the original implementation and help with this patch.
 *
 * TikTok 43.8.3 adaptation:
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.misc.externalbrowser

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object SparkThirdRouterOpenFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf(
        "Landroid/content/Context;",
        "Lcom/bytedance/hybrid/spark/third/router/SparkThirdContext;",
    ),
    strings = listOf("ContainerId", "Context_startActivity_1"),
)

internal object StoryLinkSheetFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Lcom/ss/android/ugc/aweme/sticker/data/InteractStickerStruct;"),
    strings = listOf("external_website_security_pop_up_window_show"),
)

internal object SparkActivityOnCreateFingerprint : Fingerprint(
    definingClass = "Lcom/bytedance/hybrid/spark/page/SparkActivity;",
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
    strings = listOf("SparkContextContainerId"),
)
