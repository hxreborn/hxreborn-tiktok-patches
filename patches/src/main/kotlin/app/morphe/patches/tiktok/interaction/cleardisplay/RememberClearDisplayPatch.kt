/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/interaction/cleardisplay/RememberClearDisplayPatch.kt
 */
package app.morphe.patches.tiktok.interaction.cleardisplay

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.patches.tiktok.shared.OnRenderFirstFrameFingerprint
import app.morphe.util.findInstructionIndicesReversedOrThrow
import app.morphe.util.indexOfFirstInstructionOrThrow
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val rememberClearDisplayPatch = bytecodePatch(
    name = "Remember clear display",
    description = "Remembers TikTok's clear-display state between videos.",
    default = true,
) {
    compatibleWith(*AppCompatibilities.tiktok4623())

    execute {
        // Prevent excessive logging in high-frequency feed paths.
        ClearModeLogCoreFingerprint.methodOrNull?.returnEarly()
        ClearModeLogStateFingerprint.methodOrNull?.returnEarly()
        ClearModeLogPlaytimeFingerprint.methodOrNull?.returnEarly()

        OnClearDisplayEventFingerprint.method.let { method ->
            val isEnabledIndex = method.indexOfFirstInstructionOrThrow(Opcode.IGET_BOOLEAN) + 1
            val isEnabledRegister = method.getInstruction<TwoRegisterInstruction>(isEnabledIndex - 1).registerA

            method.addInstructions(
                isEnabledIndex,
                "invoke-static {v$isEnabledRegister}, " +
                    "Lapp/morphe/extension/tiktok/cleardisplay/RememberClearDisplayPatch;->rememberClearDisplayState(Z)V",
            )

            val clearDisplayEventClass = method.parameters[0].type
            OnRenderFirstFrameFingerprint.method.apply {
                val returnIndex = findInstructionIndicesReversedOrThrow {
                    opcode == Opcode.RETURN_VOID
                }.first()
                addInstructionsWithLabels(
                    returnIndex,
                    """
                        invoke-static {}, Lapp/morphe/extension/tiktok/cleardisplay/RememberClearDisplayPatch;->getClearDisplayState()Z
                        move-result v0

                        if-eqz v0, :morphe_clear_display_return

                        const/4 v1, 0x0
                        const-string v2, ""
                        const-string p1, "long_press"

                        new-instance p0, $clearDisplayEventClass
                        invoke-direct {p0, v0, v1, v2, p1}, $clearDisplayEventClass-><init>(ZILjava/lang/String;Ljava/lang/String;)V
                        invoke-virtual {p0}, $clearDisplayEventClass->post()Lcom/ss/android/ugc/governance/eventbus/IEvent;
                    """,
                    ExternalLabel("morphe_clear_display_return", getInstruction(returnIndex)),
                )
            }
        }
    }
}

