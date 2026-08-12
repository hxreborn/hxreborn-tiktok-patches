package app.morphe.patches.tiktok.misc.settings

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.tiktok.misc.extension.sharedExtensionPatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private const val CRASH_CAPTURE_INITIALIZER =
    "Lapp/morphe/extension/tiktok/diagnostics/JavaCrashCapture;->initialize(Landroid/content/Context;)V"

@Suppress("unused")
val enableOpenDebugPatch = bytecodePatch(
    name = "Diagnostic tools",
    description = "Adds optional Morphe diagnostic logging, filtered reports, and local TikTok crash capture.",
    default = false,
) {
    dependsOn(sharedExtensionPatch, settingsPatch)

    compatibleWith(*AppCompatibilities.tiktok4623())

    execute {
        SettingsStatusLoadFingerprint.method.addInstruction(
            0,
            "invoke-static {}, Lapp/morphe/extension/tiktok/settings/SettingsStatus;->enableDiagnostics()V",
        )

        val npthExtent = NpthExtentTaskInitFingerprint.method
        val contextRegister =
            if (npthExtent.accessFlags and AccessFlags.STATIC.value != 0) "p0" else "p1"
        val returnIndices = npthExtent.implementation!!.instructions.withIndex()
            .filter { it.value.opcode == Opcode.RETURN_VOID }
            .map { it.index }

        returnIndices.asReversed().forEach { returnIndex ->
            npthExtent.addInstruction(
                returnIndex,
                "invoke-static {$contextRegister}, $CRASH_CAPTURE_INITIALIZER",
            )
        }
    }
}
