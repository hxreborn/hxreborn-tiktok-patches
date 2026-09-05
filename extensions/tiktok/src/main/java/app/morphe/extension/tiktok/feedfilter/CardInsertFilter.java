/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class CardInsertFilter implements IFilter {
    static final int AWEME_TYPE_INSERT_CARD = 105;

    @Override
    public boolean getEnabled() {
        return shouldHide();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        return item.getAwemeType() == AWEME_TYPE_INSERT_CARD;
    }

    public static boolean shouldHide() {
        return Settings.HIDE_FRIEND_RECOMMENDATIONS.get();
    }

    // x-audio-tt chokepoint for a card's audio that outlives its view
    public static boolean blockLynxAudio(String src) {
        boolean block = shouldHide();
        Logger.printInfo(() -> "[Morphe TikTok FeedFilter] lynx audio src=" + src + " block=" + block);
        return block;
    }

    public static boolean blockLynxAudio() {
        return shouldHide();
    }
}
