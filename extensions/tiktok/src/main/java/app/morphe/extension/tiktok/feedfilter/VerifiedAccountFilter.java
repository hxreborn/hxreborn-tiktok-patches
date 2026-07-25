/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;
import com.ss.android.ugc.aweme.profile.model.User;

public class VerifiedAccountFilter implements IFilter {
    private static final String INSTITUTION_BADGE = "institution account";

    @Override
    public boolean getEnabled() {
        return Settings.HIDE_VERIFIED_ACCOUNTS.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        final User author = item.getAuthor();
        if (author == null) {
            return false;
        }

        final String reason = author.getEnterpriseVerifyReason();
        if (INSTITUTION_BADGE.equals(reason)) {
            return false;
        }

        return isSet(author.getCustomVerify()) || isSet(reason);
    }

    private static boolean isSet(String value) {
        return value != null && !value.isEmpty();
    }
}
