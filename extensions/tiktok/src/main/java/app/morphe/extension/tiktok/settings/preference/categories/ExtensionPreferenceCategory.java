/*
 * Forked from:
 * https://github.com/ReVanced/revanced-patches/blob/377d4e15016296b45d809697f7f69bce74badd3a/extensions/tiktok/src/main/java/app/revanced/extension/tiktok/settings/preference/categories/ExtensionPreferenceCategory.java
 */

package app.morphe.extension.tiktok.settings.preference.categories;

import android.content.Context;
import android.preference.PreferenceScreen;

import app.morphe.extension.shared.settings.BaseSettings;
import app.morphe.extension.tiktok.settings.Settings;
import app.morphe.extension.tiktok.settings.SettingsStatus;
import app.morphe.extension.tiktok.settings.preference.TogglePreference;

@SuppressWarnings("deprecation")
public class ExtensionPreferenceCategory extends ConditionalPreferenceCategory {
    public ExtensionPreferenceCategory(Context context, PreferenceScreen screen) {
        super(context, screen);
        setTitle("App behavior");
    }

    @Override
    public boolean getSettingsStatus() {
        return true;
    }

    @Override
    public void addPreferences(Context context) {
        addPreference(new TogglePreference(
                context,
                "Sanitize sharing links",
                "Remove tracking parameters from shared links.",
                BaseSettings.SANITIZE_SHARING_LINKS
        ));
        if (SettingsStatus.externalBrowserEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Open external links directly",
                    "Open profile and story website links in your system browser instead of TikTok's in-app browser.",
                    Settings.OPEN_EXTERNAL_LINKS
            ));
        }

        addPreference(new TogglePreference(
                context,
                "Show seekbar",
                "Show the native seekbar on videos where TikTok would normally hide it.",
                Settings.SHOW_SEEKBAR
        ));
        if (SettingsStatus.longPressSpeedLockEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Enable hold-and-slide 2x lock",
                    "Use TikTok's native hold, slide down, and release gesture to lock 2x speed.",
                    Settings.ENABLE_LONG_PRESS_SPEED_LOCK
            ));
        }

        if (SettingsStatus.disableTelemetryEnabled) {
            addPreference(new TogglePreference(
                    context,
                    "Disable analytics",
                    "Stops ByteDance, AppsFlyer and Firebase analytics, background location uploads and crash reporting.",
                    Settings.DISABLE_ANALYTICS
            ));
        }
    }
}

