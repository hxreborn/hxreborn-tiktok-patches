package app.morphe.patches.tiktok.interaction.antirecording

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.util.findMutableMethodOf
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import org.w3c.dom.Element

private data class ScreenCaptureCallSite(
    val classDef: ClassDef,
    val method: Method,
    val instructionIndexes: List<Int>,
)

@Suppress("unused")
val antiRecordingPatch = resourcePatch(
    name = "Disable screen capture detection",
    description = "Prevents TikTok from reacting to screenshots and screen recordings.",
    default = true,
) {
    compatibleWith(*AppCompatibilities.tiktok4623())

    dependsOn(
        bytecodePatch {
            execute {
                listOf(
                    antiRecordingAddedFingerprint,
                    antiRecordingRemovedFingerprint,
                ).forEach { fingerprint ->
                    fingerprint.methodOrNull?.returnEarly()
                }

                val callSites = mutableListOf<ScreenCaptureCallSite>()
                classDefForEach { classDef ->
                    classDef.methods.forEach { method ->
                        val indexes = method.implementation?.instructions
                            ?.mapIndexedNotNull { index, instruction ->
                                val reference = (instruction as? ReferenceInstruction)
                                    ?.reference as? MethodReference
                                    ?: return@mapIndexedNotNull null
                                if (reference.definingClass != "Landroid/app/Activity;") {
                                    return@mapIndexedNotNull null
                                }
                                if (
                                    reference.name != "registerScreenCaptureCallback" &&
                                    reference.name != "unregisterScreenCaptureCallback"
                                ) {
                                    return@mapIndexedNotNull null
                                }
                                index
                            }
                            .orEmpty()
                        if (indexes.isNotEmpty()) {
                            callSites += ScreenCaptureCallSite(classDef, method, indexes)
                        }
                    }
                }

                callSites.forEach { callSite ->
                    val mutableMethod = mutableClassDefBy(callSite.classDef)
                        .findMutableMethodOf(callSite.method)
                    callSite.instructionIndexes.forEach { index ->
                        mutableMethod.replaceInstruction(index, "nop")
                    }
                }
            }
        },
    )

    finalize {
        document("AndroidManifest.xml").use { document ->
            document.documentElement.removeElementsByAndroidName("uses-permission", "android.permission.DETECT_SCREEN_CAPTURE")
            document.documentElement.removeElementsByAndroidName("permission", "android.permission.DETECT_SCREEN_CAPTURE")
        }
    }
}

private fun Element.removeElementsByAndroidName(tagName: String, value: String) {
    buildList {
        val nodes = getElementsByTagName(tagName)
        for (index in 0 until nodes.length) {
            (nodes.item(index) as? Element)
                ?.takeIf { it.getAttribute("android:name") == value }
                ?.let(::add)
        }
    }.forEach { element ->
        element.parentNode?.removeChild(element)
    }
}
