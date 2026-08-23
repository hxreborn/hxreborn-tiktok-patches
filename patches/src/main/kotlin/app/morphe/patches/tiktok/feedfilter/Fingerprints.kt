/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/feedfilter/Fingerprints.kt
 */
package app.morphe.patches.tiktok.feedfilter

import app.morphe.patcher.Fingerprint
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

internal object MainFeedResponseFingerprint : Fingerprint(
    definingClass = "Lcom/ss/android/ugc/aweme/feed/FeedApiService;",
    name = "fetchFeedList",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;",
    custom = { method, classDef ->
        classDef.type == "Lcom/ss/android/ugc/aweme/feed/FeedApiService;" &&
            method.parameterTypes.size == 1
    },
)

internal object FollowFeedFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;",
    strings = listOf("feed"),
    custom = { method, _ ->
        method.parameterTypes.size == 2 && method.implementation?.instructions?.any {
            it.getReference<MethodReference>()?.let { reference ->
                reference.definingClass == "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;" &&
                    reference.name == "getItems"
            } == true
        } == true
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

internal object DramaBlockingAdFingerprint : Fingerprint(
    definingClass = "Lcom/ss/android/ugc/aweme/impl/DramaBlockingAdServiceImpl;",
    returnType = "Z",
    parameters = listOf("Lcom/ss/android/ugc/aweme/feed/model/Aweme;"),
)

internal object SpecActTouchpointAttachFingerprint : Fingerprint(
    definingClass = "/specact/SpecActServiceImpl;",
    returnType = "V",
    parameters = listOf("Landroid/view/ViewGroup;", "Landroidx/fragment/app/Fragment;"),
)

internal object InsertedFeedItemsFingerprint : Fingerprint(
    definingClass = "Lcom/ss/android/ugc/aweme/feed/panel/BaseListFragmentPanel;",
    name = "yM2",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf(
        "I",
        "Ljava/lang/String;",
        "Ljava/util/List;",
    ),
    custom = { method, _ ->
        method.implementation?.instructions?.any {
            it.getReference<MethodReference>()?.let { reference ->
                reference.definingClass == "LX/0SN6;" &&
                    reference.name == "LIZ" &&
                    reference.returnType == "LX/0SN6;"
            } == true
        } == true
    },
)

internal object ColdStartCachedFeedFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf(
        "processGoldenVideoHitCache hitCache , time cost ",
        "processOfflineVideoHitCache error",
    ),
    custom = { method, _ ->
        method.implementation?.instructions?.count {
            it.opcode == com.android.tools.smali.dexlib2.Opcode.SPUT_OBJECT &&
                it.getReference<FieldReference>()?.type ==
                "Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;"
        } == 4
    },
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
