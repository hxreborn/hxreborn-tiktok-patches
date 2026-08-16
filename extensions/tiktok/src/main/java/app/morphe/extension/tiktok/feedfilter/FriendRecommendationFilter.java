/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public final class FriendRecommendationFilter {
    private FriendRecommendationFilter() {}

    public static boolean shouldHide() {
        boolean hide = Settings.HIDE_FRIEND_RECOMMENDATIONS.get();
        Logger.printDebug(() -> "[Morphe TikTok FeedFilter] rec user card insert: hide=" + hide);
        return hide;
    }
}
