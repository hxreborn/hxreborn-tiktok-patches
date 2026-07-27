/*
 * Thanks to lyyako for the original implementation and help with this patch.
 *
 * TikTok 43.8.3 adaptation:
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.patches.tiktok.interaction.publishdate

import app.morphe.patcher.Fingerprint

internal object VideoAuthorInfoStateFingerprint : Fingerprint(
    definingClass = "Lcom/ss/android/ugc/aweme/feed/assem/videoauthorinfo/VideoAuthorInfoVM;",
    custom = { method, _ ->
        method.name == "paramSync2StateAccept" &&
            "Lcom/ss/android/ugc/aweme/feed/model/VideoItemParams;" in method.parameterTypes
    },
)
