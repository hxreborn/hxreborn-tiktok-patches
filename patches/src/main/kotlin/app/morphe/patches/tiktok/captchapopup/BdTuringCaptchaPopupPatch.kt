/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.tiktok.captchapopup

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patches.tiktok.misc.extension.sharedExtensionPatch

private const val FEATURE_CONTROLS_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/featurecontrols/FeatureControls;"

private object BdTuringCaptchaPopupFingerprint : Fingerprint(
    definingClass = "Lcom/bytedance/bdturing/verify/RiskControlService;",
    name = "execute",
    returnType = "Z",
)

@Suppress("unused")
val bdTuringCaptchaPopupPatch = bytecodePatch(
    name = "Hide BdTuring CAPTCHA popups",
    description = "Hides the BdTuring risk-control CAPTCHA dialog, gated by the Hide CAPTCHA popups setting.",
    default = true,
) {
    dependsOn(sharedExtensionPatch)
    compatibleWith(*AppCompatibilities.tiktok4623())

    execute {
        BdTuringCaptchaPopupFingerprint.method.apply {
            val requestType = parameterTypes[0]
            val callbackType = parameterTypes[1]

            addInstructions(
                0,
                """
                    move-object/from16 v2, p1
                    invoke-virtual {v2}, $requestType->getActivity()Landroid/app/Activity;
                    move-result-object v0
                    invoke-virtual {v2}, $requestType->getServiceType()Ljava/lang/String;
                    move-result-object v1
                    invoke-static {v0, v1}, $FEATURE_CONTROLS_CLASS_DESCRIPTOR->shouldHideTuringCaptchaPopup(Landroid/app/Activity;Ljava/lang/String;)Z
                    move-result v0
                    if-eqz v0, :morphe_show_bdturing_captcha_popup
                    const/4 v0, 0x3
                    move-object/from16 v1, p2
                    invoke-interface {v1, v0}, $callbackType->onFail(I)V
                    const/4 v0, 0x1
                    return v0
                    :morphe_show_bdturing_captcha_popup
                    nop
                """,
            )
        }
    }
}
