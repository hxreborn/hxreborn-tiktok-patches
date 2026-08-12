/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.tiktok.inbox

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patches.tiktok.misc.extension.sharedExtensionPatch
import app.morphe.patches.tiktok.misc.settings.SettingsStatusLoadFingerprint
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/inbox/InboxControls;"
private const val ATOMIC_BOOLEAN_DESCRIPTOR = "Ljava/util/concurrent/atomic/AtomicBoolean;"

@Suppress("unused")
val expandActivityListPatch = bytecodePatch(
    name = "Expand activity list",
    description = "Shows the full Activity and New followers lists instead of collapsing them behind a View all button.",
    default = true,
) {
    dependsOn(sharedExtensionPatch)

    compatibleWith(*AppCompatibilities.tiktok4623())

    execute {
        SettingsStatusLoadFingerprint.method.addInstruction(
            0,
            "invoke-static {}, Lapp/morphe/extension/tiktok/settings/SettingsStatus;->enableExpandActivityList()V",
        )

        val collapsedField = with(ExpandNotificationListFingerprint.method) {
            getInstruction<ReferenceInstruction>(indexOfFirstInstructionOrThrow(Opcode.IPUT_BOOLEAN))
                .reference as FieldReference
        }

        // onCreate rewrites what the constructor set
        listOf(
            NotificationWidgetContainerConstructorFingerprint,
            NotificationWidgetContainerOnCreateFingerprint,
        ).forEach { fingerprint ->
            fingerprint.method.apply {
                val index = indexOfFirstInstructionOrThrow {
                    opcode == Opcode.IPUT_BOOLEAN && getReference<FieldReference>() == collapsedField
                }

                addInstructions(
                    index,
                    """
                        invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->shouldCollapseActivityList()Z
                        move-result v${getInstruction<TwoRegisterInstruction>(index).registerA}
                    """,
                )
            }
        }

        FollowerWidgetContainerConstructorFingerprint.method.apply {
            val index = indexOfFirstInstructionOrThrow {
                opcode == Opcode.INVOKE_DIRECT &&
                    getReference<MethodReference>()?.definingClass == ATOMIC_BOOLEAN_DESCRIPTOR
            }

            addInstructions(
                index,
                """
                    invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->shouldCollapseActivityList()Z
                    move-result v${getInstruction<FiveRegisterInstruction>(index).registerD}
                """,
            )
        }
    }
}
