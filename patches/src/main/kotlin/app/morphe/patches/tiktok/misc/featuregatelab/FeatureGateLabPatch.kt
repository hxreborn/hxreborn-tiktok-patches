/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.misc.featuregatelab

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patches.tiktok.misc.settings.enableOpenDebugPatch
import app.morphe.patches.tiktok.misc.settings.settingsPatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

private const val ABMOCK_DESCRIPTOR = "LX/0BPv;"
private const val ABMOCK_RAW_DESCRIPTOR = "LX/0BPb;"
private const val ABMOCK_LEGACY_DESCRIPTOR = "LX/0BP8;"
private const val ABMOCK_SETTINGS_MANAGER_DESCRIPTOR = "Lcom/bytedance/ies/abmock/SettingsManager;"
private const val LIVE_SETTINGS_DESCRIPTOR = "Lcom/bytedance/android/live_settings/SettingsManager;"
private const val ACTIVITY_CENTER_DESCRIPTOR = "Lcom/ss/android/ugc/aweme/compliance/business/activitycenter/EnterActivityCenterAction;"
private const val VE_CONFIG_DESCRIPTOR = "Lcom/ss/android/vesdk/VEConfigCenter;"
private const val RUNTIME_DESCRIPTOR = "Lapp/morphe/extension/tiktok/featuregatelab/FeatureGateLabRuntime;"

private data class TypedBoundary(
    val targetDescriptor: String,
    val methodName: String,
    val returnType: String,
    val parameters: List<String>,
    val keyParameter: String,
    val returnOpcode: Opcode,
    val runtimeMethod: String,
    val runtimeDescriptor: String,
    val wide: Boolean = false,
)

private val boundaries = listOf(
    TypedBoundary(ABMOCK_DESCRIPTOR, "LIZJ", "Z", listOf("I", "Ljava/lang/String;", "Z", "Z"), "p2", Opcode.RETURN, "overrideBoolean", "(Ljava/lang/String;Z)Z"),
    TypedBoundary(ABMOCK_DESCRIPTOR, "LJFF", "D", listOf("D", "I", "Ljava/lang/String;", "Z"), "p4", Opcode.RETURN_WIDE, "overrideDouble", "(Ljava/lang/String;D)D", true),
    TypedBoundary(ABMOCK_DESCRIPTOR, "LJIIIIZZ", "F", listOf("I", "Ljava/lang/String;", "Z", "F"), "p2", Opcode.RETURN, "overrideFloat", "(Ljava/lang/String;F)F"),
    TypedBoundary(ABMOCK_DESCRIPTOR, "LJIIJJI", "I", listOf("I", "I", "Ljava/lang/String;", "Z"), "p3", Opcode.RETURN, "overrideInt", "(Ljava/lang/String;I)I"),
    TypedBoundary(ABMOCK_DESCRIPTOR, "LJIILJJIL", "J", listOf("I", "J", "Ljava/lang/String;", "Z"), "p4", Opcode.RETURN_WIDE, "overrideLong", "(Ljava/lang/String;J)J", true),
    TypedBoundary(ABMOCK_DESCRIPTOR, "LJIILLIIL", "Ljava/lang/String;", listOf("I", "Ljava/lang/String;", "Ljava/lang/String;", "Z"), "p2", Opcode.RETURN_OBJECT, "overrideString", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"),
    TypedBoundary(ABMOCK_LEGACY_DESCRIPTOR, "LJFF", "Z", listOf("Ljava/lang/String;", "Z"), "p1", Opcode.RETURN, "overrideBoolean", "(Ljava/lang/String;Z)Z"),
    TypedBoundary(ABMOCK_LEGACY_DESCRIPTOR, "LIZLLL", "D", listOf("Ljava/lang/String;", "D"), "p1", Opcode.RETURN_WIDE, "overrideDouble", "(Ljava/lang/String;D)D", true),
    TypedBoundary(ABMOCK_LEGACY_DESCRIPTOR, "LJII", "F", listOf("Ljava/lang/String;", "F"), "p1", Opcode.RETURN, "overrideFloat", "(Ljava/lang/String;F)F"),
    TypedBoundary(ABMOCK_LEGACY_DESCRIPTOR, "LIZ", "I", listOf("Ljava/lang/String;", "I"), "p1", Opcode.RETURN, "overrideInt", "(Ljava/lang/String;I)I"),
    TypedBoundary(ABMOCK_LEGACY_DESCRIPTOR, "LIZIZ", "J", listOf("Ljava/lang/String;", "J"), "p1", Opcode.RETURN_WIDE, "overrideLong", "(Ljava/lang/String;J)J", true),
    TypedBoundary(ABMOCK_LEGACY_DESCRIPTOR, "LJ", "Ljava/lang/String;", listOf("Ljava/lang/String;", "Ljava/lang/String;"), "p1", Opcode.RETURN_OBJECT, "overrideString", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"),
    TypedBoundary(ABMOCK_SETTINGS_MANAGER_DESCRIPTOR, "LIZ", "Z", listOf("Ljava/lang/String;", "Z"), "p0", Opcode.RETURN, "overrideBoolean", "(Ljava/lang/String;Z)Z"),
    TypedBoundary(ABMOCK_SETTINGS_MANAGER_DESCRIPTOR, "LIZIZ", "D", listOf("Ljava/lang/String;", "D"), "p0", Opcode.RETURN_WIDE, "overrideDouble", "(Ljava/lang/String;D)D", true),
    TypedBoundary(ABMOCK_SETTINGS_MANAGER_DESCRIPTOR, "LIZJ", "F", listOf("Ljava/lang/String;", "F"), "p0", Opcode.RETURN, "overrideFloat", "(Ljava/lang/String;F)F"),
    TypedBoundary(ABMOCK_SETTINGS_MANAGER_DESCRIPTOR, "LJ", "I", listOf("Ljava/lang/String;", "I"), "p0", Opcode.RETURN, "overrideInt", "(Ljava/lang/String;I)I"),
    TypedBoundary(ABMOCK_SETTINGS_MANAGER_DESCRIPTOR, "LJFF", "J", listOf("Ljava/lang/String;", "J"), "p0", Opcode.RETURN_WIDE, "overrideLong", "(Ljava/lang/String;J)J", true),
    TypedBoundary(ABMOCK_SETTINGS_MANAGER_DESCRIPTOR, "LJI", "Ljava/lang/String;", listOf("Ljava/lang/String;", "Ljava/lang/String;"), "p0", Opcode.RETURN_OBJECT, "overrideString", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"),
    TypedBoundary(LIVE_SETTINGS_DESCRIPTOR, "getBooleanValue", "Z", listOf("Ljava/lang/String;", "Z"), "p1", Opcode.RETURN, "overrideLiveBoolean", "(Ljava/lang/String;Z)Z"),
    TypedBoundary(LIVE_SETTINGS_DESCRIPTOR, "getDoubleValue", "D", listOf("Ljava/lang/String;", "D"), "p1", Opcode.RETURN_WIDE, "overrideLiveDouble", "(Ljava/lang/String;D)D", true),
    TypedBoundary(LIVE_SETTINGS_DESCRIPTOR, "getFloatValue", "F", listOf("Ljava/lang/String;", "F"), "p1", Opcode.RETURN, "overrideLiveFloat", "(Ljava/lang/String;F)F"),
    TypedBoundary(LIVE_SETTINGS_DESCRIPTOR, "getIntValue", "I", listOf("Ljava/lang/String;", "I"), "p1", Opcode.RETURN, "overrideLiveInt", "(Ljava/lang/String;I)I"),
    TypedBoundary(LIVE_SETTINGS_DESCRIPTOR, "getLongValue", "J", listOf("Ljava/lang/String;", "J"), "p1", Opcode.RETURN_WIDE, "overrideLiveLong", "(Ljava/lang/String;J)J", true),
    TypedBoundary(LIVE_SETTINGS_DESCRIPTOR, "getStringValue", "Ljava/lang/String;", listOf("Ljava/lang/String;", "Ljava/lang/String;"), "p1", Opcode.RETURN_OBJECT, "overrideLiveString", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"),
    TypedBoundary(VE_CONFIG_DESCRIPTOR, "getValue", "Ljava/lang/Boolean;", listOf("Ljava/lang/String;", "Z"), "p1", Opcode.RETURN_OBJECT, "overrideVeBoolean", "(Ljava/lang/String;Ljava/lang/Boolean;)Ljava/lang/Boolean;"),
    TypedBoundary(VE_CONFIG_DESCRIPTOR, "getValue", "F", listOf("Ljava/lang/String;", "F"), "p1", Opcode.RETURN, "overrideVeFloat", "(Ljava/lang/String;F)F"),
    TypedBoundary(VE_CONFIG_DESCRIPTOR, "getValue", "I", listOf("Ljava/lang/String;", "I"), "p1", Opcode.RETURN, "overrideVeInt", "(Ljava/lang/String;I)I"),
    TypedBoundary(VE_CONFIG_DESCRIPTOR, "getValue", "Ljava/lang/String;", listOf("Ljava/lang/String;", "Ljava/lang/String;"), "p1", Opcode.RETURN_OBJECT, "overrideVeString", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"),
)

@Suppress("unused")
val featureGateLabPatch = bytecodePatch(
    name = "Feature Gate Lab",
    description = "Adds a menu for viewing and overriding supported TikTok feature flags and configuration values.",
    default = true,
) {
    dependsOn(settingsPatch, enableOpenDebugPatch)
    compatibleWith(*AppCompatibilities.tiktok4383())

    execute {
        boundaries.forEach { boundary ->
            val target = mutableClassDefBy(boundary.targetDescriptor)
            val method = target.methods.singleOrNull {
                it.name == boundary.methodName &&
                    it.returnType == boundary.returnType &&
                    it.parameterTypes == boundary.parameters
            } ?: throw PatchException("Feature Gate Lab boundary not found: ${boundary.targetDescriptor}->${boundary.methodName}${boundary.parameters}")
            method.patchBoundary(boundary)
        }

        val rawAbmock = mutableClassDefBy(ABMOCK_RAW_DESCRIPTOR)
        val rawGetter = rawAbmock.methods.singleOrNull {
            it.name == "LJIIL" &&
                it.returnType == "Ljava/lang/Object;" &&
                it.parameterTypes == listOf("Ljava/lang/String;", "Z")
        } ?: throw PatchException("Feature Gate Lab raw App AB boundary not found")
        rawGetter.patchRawAbBoundary()

        val activityCenter = mutableClassDefBy(ACTIVITY_CENTER_DESCRIPTOR)
        val getSchema = activityCenter.methods.singleOrNull {
            it.name == "getSchema" &&
                it.returnType == "Ljava/lang/String;" &&
                it.parameterTypes == listOf(
                    "Ljava/lang/String;",
                    "Ljava/lang/String;",
                    "Ljava/lang/String;",
                )
        } ?: throw PatchException("Feature Gate Lab Activity Center schema boundary not found")
        getSchema.patchActivityCenterSchema()

        val runtime = mutableClassDefBy(RUNTIME_DESCRIPTOR)
        val installed = runtime.methods.singleOrNull {
            it.name == "isInstalled" && it.returnType == "Z" && it.parameterTypes.isEmpty()
        } ?: throw PatchException("Feature Gate Lab runtime marker not found")
        installed.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
    }
}

private fun MutableMethod.patchRawAbBoundary() {
    val implementation = implementation
        ?: throw PatchException("Feature Gate Lab raw App AB boundary has no implementation")
    implementation.instructions
        .withIndex()
        .filter { (_, instruction) -> instruction.opcode == Opcode.RETURN_OBJECT }
        .map { (index, instruction) ->
            index to ((instruction as? OneRegisterInstruction)?.registerA
                ?: throw PatchException("Feature Gate Lab raw App AB return without result register"))
        }
        .asReversed()
        .forEach { (index, register) ->
            addInstructions(
                index,
                """
                    invoke-static {p1, v$register, p2}, $RUNTIME_DESCRIPTOR->overrideRawAbValue(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;
                    move-result-object v$register
                """,
            )
        }
}

private fun MutableMethod.patchActivityCenterSchema() {
    val implementation = implementation
        ?: throw PatchException("Feature Gate Lab Activity Center boundary has no implementation")
    implementation.instructions
        .withIndex()
        .filter { (_, instruction) -> instruction.opcode == Opcode.RETURN_OBJECT }
        .map { (index, instruction) ->
            index to ((instruction as? OneRegisterInstruction)?.registerA
                ?: throw PatchException("Feature Gate Lab Activity Center return without result register"))
        }
        .asReversed()
        .forEach { (index, register) ->
            addInstructions(
                index,
                """
                    invoke-static {v$register}, $RUNTIME_DESCRIPTOR->transformActivityCenterSchema(Ljava/lang/String;)Ljava/lang/String;
                    move-result-object v$register
                """,
            )
        }
}

private fun MutableMethod.patchBoundary(boundary: TypedBoundary) {
    val implementation = implementation ?: throw PatchException("Feature Gate Lab boundary has no implementation: $this")
    implementation.instructions
        .withIndex()
        .filter { (_, instruction) -> instruction.opcode == boundary.returnOpcode }
        .map { (index, instruction) ->
            index to ((instruction as? OneRegisterInstruction)?.registerA
                ?: throw PatchException("Feature Gate Lab return without result register: $this"))
        }
        .asReversed()
        .forEach { (index, register) ->
            val resultRegisters = if (boundary.wide) "v$register, v${register + 1}" else "v$register"
            val moveResult = when (boundary.returnOpcode) {
                Opcode.RETURN_WIDE -> "move-result-wide v$register"
                Opcode.RETURN_OBJECT -> "move-result-object v$register"
                else -> "move-result v$register"
            }
            addInstructions(
                index,
                """
                    invoke-static {${boundary.keyParameter}, $resultRegisters}, $RUNTIME_DESCRIPTOR->${boundary.runtimeMethod}${boundary.runtimeDescriptor}
                    $moveResult
                """,
            )
        }
}
