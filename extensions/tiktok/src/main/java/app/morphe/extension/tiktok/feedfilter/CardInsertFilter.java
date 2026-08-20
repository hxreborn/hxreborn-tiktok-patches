/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class CardInsertFilter implements IFilter {
    private static final int AWEME_TYPE_INSERT_CARD = 105;

    @Override
    public boolean getEnabled() {
        return Settings.HIDE_FRIEND_RECOMMENDATIONS.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        return item.getAwemeType() == AWEME_TYPE_INSERT_CARD;
    }

    public static boolean shouldBlockMusic() {
        boolean block = Settings.HIDE_FRIEND_RECOMMENDATIONS.get();
        Logger.printInfo(() -> "[Morphe TikTok FeedFilter] bulletin music requested: block=" + block);
        return block;
    }
}
