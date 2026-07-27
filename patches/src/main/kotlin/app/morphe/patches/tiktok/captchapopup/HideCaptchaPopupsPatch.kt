/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.captchapopup

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patches.tiktok.misc.extension.sharedExtensionPatch
import app.morphe.patches.tiktok.misc.settings.SettingsStatusLoadFingerprint

private const val FEATURE_CONTROLS_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/featurecontrols/FeatureControls;"

private object CaptchaPopupFingerprint : Fingerprint(
    definingClass = "/sec/SecApiImpl;",
    name = "popCaptchaV2",
    returnType = "V",
    parameters = listOf(
        "Landroid/app/Activity;",
        "Ljava/lang/String;",
        "LX/10Dr;",
        "Landroidx/fragment/app/Fragment;",
    ),
    strings = listOf("popCaptchaV2 - riskInfo ="),
)

private object LegacyCaptchaPopupFingerprint : Fingerprint(
    definingClass = "/sec/SecApiImpl;",
    name = "popCaptcha",
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;", "I", "LX/10Dr;"),
    strings = listOf("popCaptcha - errorcode = "),
)

private object OecCaptchaPopupFingerprint : Fingerprint(
    definingClass = "Lcom/tts/oecverify/verify/RiskControlService;",
    name = "execute",
    returnType = "Z",
    parameters = listOf("LX/10FW;", "Lcom/tts/oecverify/BdTuringCallback;"),
)

private object LiveHostCaptchaPopupFingerprint : Fingerprint(
    definingClass = "/live/livehostimpl/LiveHostUser;",
    name = "popCaptchaV2",
    returnType = "V",
    parameters = listOf(
        "Landroid/app/Activity;",
        "Ljava/lang/String;",
        "LX/0zeW;",
        "Landroidx/fragment/app/Fragment;",
    ),
)

@Suppress("unused")
val hideCaptchaPopupsPatch = bytecodePatch(
    name = "Hide CAPTCHA popups",
    description = "Prevents client-side verification puzzle dialogs from opening, including those shown while browsing LIVE. This does not bypass server-side checks.",
    default = true,
) {
    dependsOn(sharedExtensionPatch)
    compatibleWith(*AppCompatibilities.tiktok4383())

    execute {
        SettingsStatusLoadFingerprint.method.addInstruction(
            0,
            "invoke-static {}, Lapp/morphe/extension/tiktok/settings/SettingsStatus;->enableCaptchaPopupSuppression()V",
        )

        CaptchaPopupFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $FEATURE_CONTROLS_CLASS_DESCRIPTOR->shouldHideCaptchaPopup()Z
                move-result v0
                if-eqz v0, :morphe_show_captcha_popup
                if-eqz p3, :morphe_hide_captcha_popup_return
                invoke-virtual {p3}, LX/10Dr;->LIZJ()V
                :morphe_hide_captcha_popup_return
                return-void
                :morphe_show_captcha_popup
                nop
            """,
        )

        LegacyCaptchaPopupFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $FEATURE_CONTROLS_CLASS_DESCRIPTOR->shouldHideCaptchaPopup()Z
                move-result v0
                if-eqz v0, :morphe_show_legacy_captcha_popup
                if-eqz p3, :morphe_hide_legacy_captcha_popup_return
                invoke-virtual {p3}, LX/10Dr;->LIZJ()V
                :morphe_hide_legacy_captcha_popup_return
                return-void
                :morphe_show_legacy_captcha_popup
                nop
            """,
        )

        OecCaptchaPopupFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $FEATURE_CONTROLS_CLASS_DESCRIPTOR->shouldHideCaptchaPopup()Z
                move-result v0
                if-eqz v0, :morphe_show_oec_captcha_popup
                const/4 v0, 0x3
                const/4 v1, 0x0
                move-object/from16 v2, p2
                invoke-interface {v2, v0, v1}, Lcom/tts/oecverify/BdTuringCallback;->onFail(ILorg/json/JSONObject;)V
                const/4 v0, 0x1
                return v0
                :morphe_show_oec_captcha_popup
                nop
            """,
        )

        LiveHostCaptchaPopupFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $FEATURE_CONTROLS_CLASS_DESCRIPTOR->shouldHideCaptchaPopup()Z
                move-result v0
                if-eqz v0, :morphe_show_live_captcha_popup
                if-eqz p3, :morphe_hide_live_captcha_popup_return
                invoke-interface {p3}, LX/0zeW;->LIZIZ()V
                :morphe_hide_live_captcha_popup_return
                return-void
                :morphe_show_live_captcha_popup
                nop
            """,
        )
    }
}
