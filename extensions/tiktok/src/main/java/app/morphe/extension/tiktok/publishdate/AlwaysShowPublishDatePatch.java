/*
 * Thanks to lyyako for the original implementation and help with this patch.
 *
 * TikTok 43.8.3 adaptation:
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.publishdate;

import app.morphe.extension.tiktok.settings.Settings;

public final class AlwaysShowPublishDatePatch {
    private AlwaysShowPublishDatePatch() {
    }

    public static boolean showPostTimeForMainFeeds(boolean original) {
        return Settings.ALWAYS_SHOW_PUBLISH_DATE.get() ? false : original;
    }
}
