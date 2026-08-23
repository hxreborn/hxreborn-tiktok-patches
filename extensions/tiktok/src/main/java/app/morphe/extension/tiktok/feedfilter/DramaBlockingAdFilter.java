/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public final class DramaBlockingAdFilter {
    private DramaBlockingAdFilter() {}

    public static boolean shouldBlock(boolean isBlockingAd) {
        if (isBlockingAd && Settings.REMOVE_ADS.get()) {
            Logger.printInfo(() -> "[Morphe TikTok FeedFilter] unblocked countdown-locked drama ad");
            return false;
        }
        return isBlockingAd;
    }
}
