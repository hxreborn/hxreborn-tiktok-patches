/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.featuregatelab;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.morphe.extension.shared.Utils;
import app.morphe.extension.tiktok.settings.preference.SettingsUi;

public final class FeatureGateLabSession {
    private static volatile boolean restartNeeded;
    private static volatile boolean exitPromptPending;

    private FeatureGateLabSession() {
    }

    public static void begin() {
        exitPromptPending = false;
    }

    public static void markRestartNeeded() {
        restartNeeded = true;
    }

    public static boolean restartNeeded() {
        return restartNeeded;
    }

    public static void showRestartDialog(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FeatureGateLabUi.syncTheme(activity);
        Dialog dialog = new Dialog(activity);
        LinearLayout panel = new LinearLayout(activity);
        panel.setOrientation(LinearLayout.VERTICAL);
        int horizontalPadding = SettingsUi.dp(activity, 24);
        panel.setPadding(
                horizontalPadding,
                SettingsUi.dp(activity, 20),
                horizontalPadding,
                SettingsUi.dp(activity, 12)
        );
        panel.setBackground(SettingsUi.borderedSurface(activity, 6, true));

        TextView title = FeatureGateLabUi.text(
                activity,
                "Restart required",
                20,
                SettingsUi.textPrimary(),
                Typeface.BOLD
        );
        panel.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        TextView message = FeatureGateLabUi.text(
                activity,
                "Restart TikTok to apply the Feature Gate Lab changes.",
                16,
                SettingsUi.textSecondary(),
                Typeface.NORMAL
        );
        message.setSingleLine(false);
        message.setHorizontallyScrolling(false);
        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        messageParams.topMargin = SettingsUi.dp(activity, 12);
        panel.addView(message, messageParams);

        LinearLayout actions = new LinearLayout(activity);
        actions.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        actions.setOrientation(LinearLayout.HORIZONTAL);

        Button later = new Button(activity);
        later.setText("Later");
        later.setAllCaps(false);
        later.setBackgroundColor(Color.TRANSPARENT);
        SettingsUi.styleActionButton(later, false);
        later.setOnClickListener(view -> dialog.dismiss());
        actions.addView(later, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                SettingsUi.dp(activity, 48)
        ));

        Button restart = new Button(activity);
        restart.setText("Restart now");
        restart.setAllCaps(false);
        restart.setBackgroundColor(Color.TRANSPARENT);
        SettingsUi.styleActionButton(restart, true);
        restart.setOnClickListener(view -> Utils.restartApp(activity));
        actions.addView(restart, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                SettingsUi.dp(activity, 48)
        ));

        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        actionParams.topMargin = SettingsUi.dp(activity, 8);
        panel.addView(actions, actionParams);

        dialog.setContentView(panel);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            int width = activity.getResources().getDisplayMetrics().widthPixels
                    - SettingsUi.dp(activity, 32);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public static void showAfterHostExit(Activity host) {
        if (!restartNeeded || exitPromptPending) {
            return;
        }
        exitPromptPending = true;
        Application application = host.getApplication();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private boolean handled;

            @Override
            public void onActivityResumed(Activity activity) {
                if (handled || activity == host || activity.isFinishing()) {
                    return;
                }
                handled = true;
                exitPromptPending = false;
                application.unregisterActivityLifecycleCallbacks(this);
                activity.getWindow().getDecorView().post(
                        () -> showRestartDialog(activity)
                );
            }

            @Override public void onActivityCreated(Activity activity, Bundle state) { }
            @Override public void onActivityStarted(Activity activity) { }
            @Override public void onActivityPaused(Activity activity) { }
            @Override public void onActivityStopped(Activity activity) { }
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle state) { }
            @Override public void onActivityDestroyed(Activity activity) { }
        });
    }
}
