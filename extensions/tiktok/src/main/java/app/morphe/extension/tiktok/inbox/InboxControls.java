/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.inbox;

import app.morphe.extension.tiktok.settings.Settings;

public final class InboxControls {
    private InboxControls() {
    }

    public static boolean shouldShowSuggestedAccounts() {
        return !Settings.HIDE_SUGGESTED_ACCOUNTS.get();
    }

    public static boolean shouldCollapseActivityList() {
        return !Settings.EXPAND_ACTIVITY_LIST.get();
    }

    public static boolean shouldShowInboxStories() {
        return !Settings.HIDE_INBOX_STORIES.get();
    }
}
