/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.misc.absettings

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

internal const val APP_AB_DESCRIPTOR = "LX/0BYX;"
internal const val APP_AB_INT_METHOD = "LJFF"
internal const val APP_AB_INT_KEY_REGISTER = "p3"
internal val APP_AB_INT_PARAMETERS = listOf("I", "I", "Ljava/lang/String;", "Z")

internal object AppAbIntBoundaryFingerprint : Fingerprint(
    returnType = "I",
    parameters = APP_AB_INT_PARAMETERS,
    custom = { method, classDef ->
        classDef.type == APP_AB_DESCRIPTOR && method.name == APP_AB_INT_METHOD
    },
)

internal fun BytecodePatchContext.hookAppAbIntBoundary(
    extensionDescriptor: String,
    extensionMethod: String,
) {
    AppAbIntBoundaryFingerprint.method.apply {
        implementation!!.instructions.withIndex()
            .filter { it.value.opcode == Opcode.RETURN }
            .map { it.index }
            .asReversed()
            .forEach { returnIndex ->
                val register = getInstruction<OneRegisterInstruction>(returnIndex).registerA
                addInstructions(
                    returnIndex,
                    """
                        invoke-static {$APP_AB_INT_KEY_REGISTER, v$register}, $extensionDescriptor->$extensionMethod(Ljava/lang/String;I)I
                        move-result v$register
                    """,
                )
            }
    }
}
