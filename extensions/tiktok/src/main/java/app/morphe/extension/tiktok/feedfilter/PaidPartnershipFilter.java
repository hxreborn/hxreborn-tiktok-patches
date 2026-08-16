/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.commerce.AwemeCommerceStruct;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class PaidPartnershipFilter implements IFilter {
    @Override
    public boolean getEnabled() {
        return Settings.HIDE_PAID_PARTNERSHIP.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        final AwemeCommerceStruct commerceInfo = item.getCommerceVideoAuthInfo();
        if (commerceInfo == null) {
            return false;
        }

        return commerceInfo.isBrandedContent() || commerceInfo.isBrandOrganicContent();
    }
}
