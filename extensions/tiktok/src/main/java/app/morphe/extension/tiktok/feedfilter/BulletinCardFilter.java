/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;
import com.ss.android.ugc.aweme.feed.model.cardinsert.CardInsertInfo;
import com.ss.android.ugc.aweme.feed.model.cardinsert.LynxTemplate;

public class BulletinCardFilter implements IFilter {
    @Override
    public boolean getEnabled() {
        return Settings.HIDE_FRIEND_RECOMMENDATIONS.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        CardInsertInfo cardInsert = item.getCardInsertInfo();
        if (cardInsert == null) {
            return false;
        }

        LynxTemplate template = cardInsert.getLynxTemplate();
        if (template == null) {
            return false;
        }

        return mentionsBulletin(template.getBizChannel())
            || mentionsBulletin(template.getMainEntranceChannel())
            || mentionsBulletin(template.getMainEntranceLynxUrl())
            || mentionsBulletin(template.getBizDynamicUrl());
    }

    private static boolean mentionsBulletin(String value) {
        return value != null && value.toLowerCase().contains("bulletin");
    }
}
