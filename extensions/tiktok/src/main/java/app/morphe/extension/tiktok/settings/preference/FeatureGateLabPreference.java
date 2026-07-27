/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.settings.preference;

import android.app.Activity;
import android.preference.Preference;
import android.view.View;

import app.morphe.extension.tiktok.featuregatelab.FeatureGateLabFragment;

@SuppressWarnings("deprecation")
public final class FeatureGateLabPreference extends Preference {
    public FeatureGateLabPreference(Activity activity) {
        super(activity);
        setTitle("Feature Gate Lab");
        setSummary("Search local TikTok gate metadata and configure isolated typed overrides.");
        setOnPreferenceClickListener(preference -> {
            FeatureGateLabFragment.open(activity);
            return true;
        });
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        app.morphe.extension.tiktok.Utils.setTitleAndSummaryColor(view);
    }
}
