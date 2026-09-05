/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/feedfilter/FeedFilterPatch.kt
 */
package app.morphe.patches.tiktok.feedfilter

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.patches.tiktok.misc.extension.sharedExtensionPatch
import app.morphe.patches.tiktok.misc.settings.SettingsStatusLoadFingerprint
import app.morphe.patches.tiktok.misc.settings.SettingsStatusLoadFingerprint.method
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/FeedItemsFilter;"
private const val TAKO_AI_FILTER_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/TakoAiFilter;"
private const val PLAYLIST_BAR_FILTER_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/PlaylistBarFilter;"
private const val EVENT_BADGE_FILTER_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/EventBadgeFilter;"
private const val FRIEND_RECOMMENDATION_FILTER_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/FriendRecommendationFilter;"
private const val DRAMA_BLOCKING_AD_FILTER_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/DramaBlockingAdFilter;"
private const val CARD_INSERT_FILTER_CLASS_DESCRIPTOR = "Lapp/morphe/extension/tiktok/feedfilter/CardInsertFilter;"

@Suppress("unused")
val feedFilterPatch = bytecodePatch(
    name = "Feed filter",
    description = "Hides feed ads, TikTok Shop items, livestreams, stories, photo posts, the playlist bar, the floating event badge, AI-generated posts, paid partnership and promotional content, the account, bulletin-board and other inserted cards, posts from verified accounts, videos outside configured view or like ranges, and the countdown lock on short-drama ads.",
    default = true,
) {
    dependsOn(
        sharedExtensionPatch,
    )

    compatibleWith(*AppCompatibilities.tiktok4623())

    execute {
        // Enables the feed filter extension after settings were loaded.
        SettingsStatusLoadFingerprint.method.addInstruction(
            0,
            "invoke-static {}, Lapp/morphe/extension/tiktok/settings/SettingsStatus;->enableFeedFilter()V",
        )

        MainFeedResponseFingerprint.method.let { method ->
            val returnIndices =
                method.implementation!!.instructions.withIndex()
                    .filter { it.value.opcode == Opcode.RETURN_OBJECT }
                    .map { it.index }
                    .toList()

            returnIndices.asReversed().forEach { returnIndex ->
                val register = (method.implementation!!.instructions[returnIndex] as OneRegisterInstruction).registerA

                method.addInstructionsAtControlFlowLabel(
                    returnIndex,
                    "invoke-static/range { v$register .. v$register }, $EXTENSION_CLASS_DESCRIPTOR->filter(Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;)V",
                )
            }
        }

        FollowFeedFingerprint.method.let { method ->
            val returnIndices =
                method.implementation!!.instructions.withIndex()
                    .filter { it.value.opcode == Opcode.RETURN_OBJECT }
                    .map { it.index }
                    .toList()

            returnIndices.asReversed().forEach { returnIndex ->
                val register = (method.implementation!!.instructions[returnIndex] as OneRegisterInstruction).registerA

                method.addInstructionsAtControlFlowLabel(
                    returnIndex,
                    "invoke-static/range { v$register .. v$register }, $EXTENSION_CLASS_DESCRIPTOR->filter(Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;)V",
                )
            }
        }

        FollowFeedListGetItemsFingerprint.method.let { method ->
            val returnIndices = method.implementation!!.instructions.withIndex()
                .filter { it.value.opcode == Opcode.RETURN_OBJECT }
                .map { it.index }

            returnIndices.asReversed().forEach { returnIndex ->
                method.addInstructionsAtControlFlowLabel(
                    returnIndex,
                    "invoke-static/range {p0 .. p0}, $EXTENSION_CLASS_DESCRIPTOR->filterLate(Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;)V",
                )
            }
        }

        FollowFeedPresenterPostProcessFingerprint.method.let { method ->
            val returnIndices = method.implementation!!.instructions.withIndex()
                .filter { it.value.opcode == Opcode.RETURN_VOID }
                .map { it.index }

            returnIndices.asReversed().forEach { returnIndex ->
                method.addInstructionsAtControlFlowLabel(
                    returnIndex,
                    "invoke-static/range {p1 .. p1}, $EXTENSION_CLASS_DESCRIPTOR->filterLateFinal(Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;)V",
                )
            }
        }

        InsertedFeedItemsFingerprint.method.addInstructions(
            0,
            """
                invoke-static/range {p0 .. p3}, $EXTENSION_CLASS_DESCRIPTOR->filterInsertedFeedItems(Lcom/ss/android/ugc/aweme/feed/panel/BaseListFragmentPanel;ILjava/lang/String;Ljava/util/List;)Ljava/util/List;
                move-result-object p3
            """,
        )

        ColdStartCachedFeedFingerprint.method.let { method ->
            val cacheStoreIndices = method.implementation!!.instructions.withIndex()
                .filter {
                    it.value.opcode == Opcode.SPUT_OBJECT &&
                        (it.value as? com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction)
                            ?.reference
                            ?.let { reference ->
                                reference is FieldReference &&
                                    reference.type == "Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;"
                            } == true
                }
                .map { it.index }
                .toList()
            check(cacheStoreIndices.size == 4) {
                "Expected four cold-start cached FeedItemList stores, found ${cacheStoreIndices.size}"
            }

            cacheStoreIndices.asReversed().forEachIndexed { ordinal, storeIndex ->
                val listRegister =
                    (method.implementation!!.instructions[storeIndex] as OneRegisterInstruction).registerA
                method.addInstructionsWithLabels(
                    storeIndex,
                    """
                        invoke-static/range {v$listRegister .. v$listRegister}, $EXTENSION_CLASS_DESCRIPTOR->filterCachedFeedList(Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;)Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;
                        move-result-object v$listRegister
                        if-nez v$listRegister, :morphe_keep_cold_cache_$ordinal
                        const/4 v$listRegister, 0x0
                        return v$listRegister
                    """,
                    ExternalLabel(
                        "morphe_keep_cold_cache_$ordinal",
                        method.getInstruction(storeIndex),
                    ),
                )
            }
        }

        TakoAiFeedButtonSetVisibleFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $TAKO_AI_FILTER_CLASS_DESCRIPTOR->shouldHideFeedButton()Z
                move-result v0
                if-eqz v0, :morphe_keep_feed_tako_visible_state
                const/4 p1, 0x0
                :morphe_keep_feed_tako_visible_state
                nop
            """,
        )

        TakoAiFeedButtonBindFingerprint.method.addInstructions(
            2,
            "invoke-static {p1}, $TAKO_AI_FILTER_CLASS_DESCRIPTOR->hideBoundFeedButtonView(Landroid/view/View;)V",
        )

        PlaylistBottomBarAvailableFingerprint.methodOrNull?.addInstructions(
            0,
            """
                invoke-static {}, $PLAYLIST_BAR_FILTER_CLASS_DESCRIPTOR->shouldHide()Z
                move-result v0
                if-eqz v0, :morphe_show_playlist_bar
                const/4 v0, 0x0
                return v0
                :morphe_show_playlist_bar
                nop
            """,
        )

        // Null is the app's own "no recommended users to insert" result
        RecUserCardInsertFingerprint.methodOrNull?.addInstructions(
            0,
            """
                invoke-static {}, $FRIEND_RECOMMENDATION_FILTER_CLASS_DESCRIPTOR->shouldHide()Z
                move-result v0
                if-eqz v0, :morphe_insert_rec_user_card
                const/4 v0, 0x0
                return-object v0
                :morphe_insert_rec_user_card
                nop
            """,
        )

        // Card Lynx views preload before any list filter runs
        FeedLynxCardLoadFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, $CARD_INSERT_FILTER_CLASS_DESCRIPTOR->shouldHide()Z
                move-result v0
                if-eqz v0, :morphe_load_feed_card
                const/4 v0, 0x0
                return v0
                :morphe_load_feed_card
                nop
            """,
        )

        DramaBlockingAdFingerprint.methodOrNull?.apply {
            val returnIndex = indexOfFirstInstructionOrThrow {
                opcode == Opcode.RETURN
            }
            val register = (getInstruction(returnIndex) as OneRegisterInstruction).registerA

            addInstructions(
                returnIndex,
                """
                    invoke-static {v$register}, $DRAMA_BLOCKING_AD_FILTER_CLASS_DESCRIPTOR->shouldBlock(Z)Z
                    move-result v$register
                """,
            )
        }

        // Skip attaching the promotional event badge to the feed
        SpecActTouchpointAttachFingerprint.methodOrNull?.addInstructions(
            0,
            """
                invoke-static {}, $EVENT_BADGE_FILTER_CLASS_DESCRIPTOR->shouldHide()Z
                move-result v0
                if-eqz v0, :morphe_attach_event_badge
                return-void
                :morphe_attach_event_badge
                nop
            """,
        )
    }
}
