/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/feedfilter/Fingerprints.kt
 */
package app.morphe.patches.tiktok.feedfilter

import app.morphe.patcher.Fingerprint
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

internal object FeedItemListGetItemsFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "Ljava/util/List;",
    custom = { method, classDef ->
        classDef.endsWith("/FeedItemList;") &&
            method.name == "getItems" &&
            method.parameterTypes.isEmpty()
    },
)

internal object FollowFeedFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;",
    custom = { method, _ ->
        method.parameterTypes.size == 2
    },
)

internal object FollowFeedListGetItemsFingerprint : Fingerprint(
    definingClass = "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;",
    name = "getItems",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "Ljava/util/List;",
    parameters = emptyList(),
)

internal object PlaylistBottomBarAvailableFingerprint : Fingerprint(
    definingClass = "/InteractPlayListBottomBarAssem;",
    returnType = "Z",
    parameters = listOf("Lcom/ss/android/ugc/aweme/feed/model/VideoItemParams;"),
)

internal object SpecActTouchpointAttachFingerprint : Fingerprint(
    definingClass = "/specact/SpecActServiceImpl;",
    returnType = "V",
    parameters = listOf("Landroid/view/ViewGroup;", "Landroidx/fragment/app/Fragment;"),
)

internal object TakoAiFeedButtonSetVisibleFingerprint : Fingerprint(
    definingClass = "/feed/assem/tikbot/TakoAssem;",
    name = "bq",
    returnType = "V",
    parameters = listOf("Z"),
)

internal object FollowFeedPresenterPostProcessFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;"),
    custom = { method, _ ->
        val references = method.implementation?.instructions
            ?.mapNotNull { it.getReference<MethodReference>() }
            ?: emptyList()
        references.any {
            it.definingClass == "Lcom/ss/android/ugc/aweme/feed/model/Aweme;" &&
                it.name == "isAd"
        } && references.any {
            it.definingClass == "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;" &&
                it.name == "setItems"
        } && references.any {
            it.definingClass == "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;" &&
                it.name == "setInsertedResults"
        }
    },
)

internal object TakoAiFeedButtonBindFingerprint : Fingerprint(
    definingClass = "/feed/assem/tikbot/TakoAssem;",
    name = "onViewCreated",
    returnType = "V",
    parameters = listOf("Landroid/view/View;"),
)

internal object RecUserCardInsertFingerprint : Fingerprint(
    returnType = "Lkotlin/Pair;",
    parameters = listOf("I", "Ljava/util/List;", "Ljava/lang/String;", "Lkotlin/jvm/functions/Function0;"),
    strings = listOf("friend_recommend_card"),
)
