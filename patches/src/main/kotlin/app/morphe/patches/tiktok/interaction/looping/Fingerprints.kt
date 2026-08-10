/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.interaction.looping

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

private const val VIDEO_ENGINE_CLASS_DESCRIPTOR =
    "Lcom/ss/ttvideoengine/TTVideoEngine;"

internal object VideoEngineSetLoopingFingerprint : Fingerprint(
    definingClass = VIDEO_ENGINE_CLASS_DESCRIPTOR,
    name = "setLooping",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Z"),
)
