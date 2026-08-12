/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.settings.preference.categories;

import android.content.Context;
import android.preference.PreferenceScreen;

import app.morphe.extension.tiktok.settings.Settings;
import app.morphe.extension.tiktok.settings.SettingsStatus;
import app.morphe.extension.tiktok.settings.preference.TogglePreference;

@SuppressWarnings("deprecation")
public final class InterfacePreferenceCategory extends ConditionalPreferenceCategory {
    public InterfacePreferenceCategory(Context context, PreferenceScreen screen) {
        super(context, screen);
        setTitle("Interface");
    }

    @Override
    public boolean getSettingsStatus() {
        return SettingsStatus.captchaPopupSuppressionEnabled
                || SettingsStatus.hideSuggestedAccountsEnabled
                || SettingsStatus.expandActivityListEnabled
                || SettingsStatus.hideInboxStoriesEnabled
                || SettingsStatus.alwaysShowPublishDateEnabled;
    }

    @Override
    public void addPreferences(Context context) {
        if (SettingsStatus.captchaPopupSuppressionEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Hide CAPTCHA popups",
                    "Hide browsing and LIVE puzzle dialogs. Login and account verification remain available.",
                    Settings.HIDE_CAPTCHA_POPUPS
            ));
        }
        if (SettingsStatus.hideSuggestedAccountsEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Hide suggested accounts",
                    "Hide the Suggested accounts list on the Activity, New followers and Inbox pages. Requires restart.",
                    Settings.HIDE_SUGGESTED_ACCOUNTS
            ));
        }
        if (SettingsStatus.expandActivityListEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Expand activity list",
                    "Show the full Activity and New followers lists instead of collapsing them behind View all. Requires restart.",
                    Settings.EXPAND_ACTIVITY_LIST
            ));
        }
        if (SettingsStatus.hideInboxStoriesEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Hide inbox stories",
                    "Hide the Stories row at the top of the Inbox page. Requires restart.",
                    Settings.HIDE_INBOX_STORIES
            ));
        }
        if (SettingsStatus.alwaysShowPublishDateEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Always show publish date",
                    "Always show the publish date in video author information. Requires restart.",
                    Settings.ALWAYS_SHOW_PUBLISH_DATE
            ));
        }
    }
}
