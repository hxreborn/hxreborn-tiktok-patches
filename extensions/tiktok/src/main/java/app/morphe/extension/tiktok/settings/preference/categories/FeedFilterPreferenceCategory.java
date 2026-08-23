/*
 * Forked from:
 * https://github.com/ReVanced/revanced-patches/blob/377d4e15016296b45d809697f7f69bce74badd3a/extensions/tiktok/src/main/java/app/revanced/extension/tiktok/settings/preference/categories/FeedFilterPreferenceCategory.java
 */

package app.morphe.extension.tiktok.settings.preference.categories;

import android.content.Context;
import android.preference.PreferenceScreen;

import app.morphe.extension.tiktok.settings.Settings;
import app.morphe.extension.tiktok.settings.SettingsStatus;
import app.morphe.extension.tiktok.settings.preference.RangeValuePreference;
import app.morphe.extension.tiktok.settings.preference.TogglePreference;

@SuppressWarnings("deprecation")
public class FeedFilterPreferenceCategory extends ConditionalPreferenceCategory {
    public FeedFilterPreferenceCategory(Context context, PreferenceScreen screen) {
        super(context, screen);
        setTitle("Feed filter");
    }

    @Override
    public boolean getSettingsStatus() {
        return SettingsStatus.feedFilterEnabled;
    }

    @Override
    public void addPreferences(Context context) {
        addPreference(new TogglePreference(
                context,
                "Remove feed ads", "Remove ads from feed.",
                Settings.REMOVE_ADS
        ));
        addPreference(new TogglePreference(
                context,
                "Hide TikTok Shop", "Hide TikTok shop from feed.",
                Settings.HIDE_SHOP
        ));
        addPreference(new TogglePreference(
                context,
                "Hide livestreams", "Hide livestreams from feed.",
                Settings.HIDE_LIVE
        ));
        addPreference(new TogglePreference(
                context,
                "Hide story", "Hide story from feed.",
                Settings.HIDE_STORY
        ));
        addPreference(new TogglePreference(
                context,
                "Hide image video", "Hide image video from feed.",
                Settings.HIDE_IMAGE
        ));
        addPreference(new TogglePreference(
                context,
                "Hide playlist bar", "Hide the playlist bar shown below videos in the feed.",
                Settings.HIDE_PLAYLIST_BAR
        ));
        addPreference(new TogglePreference(
                context,
                "Hide event badge", "Hide the floating promotional event badge shown over the feed.",
                Settings.HIDE_EVENT_BADGE
        ));
        addPreference(new TogglePreference(
                context,
                "Hide AI-generated content", "Hide posts labeled as AI-generated from feed.",
                Settings.HIDE_AI_GENERATED
        ));
        addPreference(new TogglePreference(
                context,
                "Hide paid partnerships", "Hide posts labeled as a paid partnership or promotional content from feed.",
                Settings.HIDE_PAID_PARTNERSHIP
        ));
        addPreference(new TogglePreference(
                context,
                "Hide friend recommendations", "Hide account, bulletin-board and other cards inserted between videos in the feed.",
                Settings.HIDE_FRIEND_RECOMMENDATIONS
        ));
        addPreference(new TogglePreference(
                context,
                "Hide verified accounts", "Hide posts from accounts with a verified badge, except news and other institutions.",
                Settings.HIDE_VERIFIED_ACCOUNTS
        ));
        addPreference(new RangeValuePreference(
                context,
                "Min/Max views", "The minimum or maximum views of a video to show.",
                Settings.MIN_MAX_VIEWS
        ));
        addPreference(new RangeValuePreference(
                context,
                "Min/Max likes", "The minimum or maximum likes of a video to show.",
                Settings.MIN_MAX_LIKES
        ));
        addPreference(new TogglePreference(
                context,
                "Filter cached/offline videos",
                "Also apply these filters to TikTok's cached and offline fallback videos.",
                Settings.FILTER_CACHED_OFFLINE_VIDEOS
        ));
    }
}

