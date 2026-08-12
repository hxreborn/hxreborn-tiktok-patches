/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/interaction/speed/PlaybackSpeedPatch.kt
 */
package app.morphe.patches.tiktok.interaction.speed

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.tiktok.misc.extension.sharedExtensionPatch
import app.morphe.patches.tiktok.shared.OnRenderFirstFrameFingerprint
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val playbackSpeedPatch = bytecodePatch(
    name = "Playback speed",
    description = "Enables playback-speed controls for all videos and remembers the selected speed between videos.",
    default = true,
) {
    dependsOn(sharedExtensionPatch)

    compatibleWith(*AppCompatibilities.tiktok4623())

    execute {
        PlaybackSpeedMenuSelectionFingerprint.method.apply {
            val applySpeedIndex = indexOfFirstInstructionOrThrow {
                getReference<MethodReference>()?.let { reference ->
                    reference.returnType == "V" &&
                        reference.parameterTypes == listOf(
                            "F",
                            "Lcom/ss/android/ugc/aweme/feed/model/Aweme;",
                            "Ljava/lang/String;",
                            "Ljava/lang/String;",
                        )
                } == true
            }
            val speedRegister = getInstruction<FiveRegisterInstruction>(applySpeedIndex).registerC
            addInstruction(
                applySpeedIndex,
                "invoke-static {v$speedRegister}, " +
                    "Lapp/morphe/extension/tiktok/speed/PlaybackSpeedPatch;->rememberPlaybackSpeed(F)V",
            )
        }

        OnRenderFirstFrameFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Lapp/morphe/extension/tiktok/speed/PlaybackSpeedPatch;->getPlaybackSpeed()F
                move-result v0
                invoke-virtual {p0, v0}, Lcom/ss/android/ugc/aweme/feed/controller/PlayerController;->setSpeed(F)V
            """,
        )

        // Keep the extension speed entry point available to TikTok callers.
    }
}

