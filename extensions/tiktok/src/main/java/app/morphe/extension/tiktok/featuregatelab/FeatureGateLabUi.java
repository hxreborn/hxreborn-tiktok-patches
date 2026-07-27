/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.featuregatelab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import app.morphe.extension.shared.Utils;
import app.morphe.extension.tiktok.settings.preference.SettingsUi;

final class FeatureGateLabUi {
    private FeatureGateLabUi() {
    }

    static void syncTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        Utils.setIsDarkModeEnabled(nightMode == Configuration.UI_MODE_NIGHT_YES);
    }

    static TextView text(Context context, String text, float size, int color, int style) {
        return SettingsUi.text(context, text, size, color, style);
    }

    static TextView heading(Context context, String text) {
        return text(context, text, 20, SettingsUi.textPrimary(), Typeface.BOLD);
    }

    static FrameLayout header(Context context, String title, Runnable onBack) {
        return header(context, title, onBack, null);
    }

    static FrameLayout header(Context context, String title, Runnable onBack, Runnable onMenu) {
        FrameLayout toolbar = new FrameLayout(context);
        toolbar.setMinimumHeight(dp(context, 52));
        toolbar.setBackgroundColor(SettingsUi.background());

        View back = iconButton(
                context,
                "icon_arrow_left_ltr",
                android.R.drawable.ic_media_previous,
                "Back",
                onBack
        );
        FrameLayout.LayoutParams backParams = new FrameLayout.LayoutParams(dp(context, 48), dp(context, 48));
        backParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        toolbar.addView(back, backParams);

        TextView heading = text(context, title, 17, SettingsUi.textPrimary(), Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);
        heading.setSingleLine(true);
        heading.setEllipsize(android.text.TextUtils.TruncateAt.END);
        FrameLayout.LayoutParams headingParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        headingParams.leftMargin = dp(context, 56);
        headingParams.rightMargin = dp(context, 56);
        toolbar.addView(heading, headingParams);

        if (onMenu != null) {
            View menu = iconButton(
                    context,
                    "icon_ellipsis_horizontal",
                    android.R.drawable.ic_menu_more,
                    "More options",
                    onMenu
            );
            FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(dp(context, 48), dp(context, 48));
            menuParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
            toolbar.addView(menu, menuParams);
            menu.setTag("feature_gate_menu");
        }
        return toolbar;
    }

    static View clearSearchButton(Context context, Runnable onClear) {
        return iconButton(
                context,
                "icon_x_mark_small",
                android.R.drawable.ic_menu_close_clear_cancel,
                "Clear search",
                onClear
        );
    }

    private static View iconButton(
            Context context,
            String rawIconName,
            int fallbackDrawable,
            String description,
            Runnable action
    ) {
        View icon = createTuxIcon(context, rawIconName);
        if (icon == null) {
            ImageButton fallback = new ImageButton(context);
            fallback.setImageResource(fallbackDrawable);
            fallback.setColorFilter(SettingsUi.textPrimary());
            fallback.setScaleType(ImageButton.ScaleType.CENTER);
            icon = fallback;
        }
        icon.setPadding(dp(context, 12), dp(context, 12), dp(context, 12), dp(context, 12));
        TypedValue ripple = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, ripple, true)) {
            icon.setBackgroundResource(ripple.resourceId);
        } else {
            icon.setBackgroundColor(0x00000000);
        }
        icon.setContentDescription(description);
        icon.setOnClickListener(view -> action.run());
        return icon;
    }

    private static View createTuxIcon(Context context, String rawIconName) {
        try {
            int iconId = context.getResources().getIdentifier(rawIconName, "raw", context.getPackageName());
            if (iconId == 0) {
                return null;
            }
            Class<?> iconClass = Class.forName("com.bytedance.tux.icon.TuxIconView");
            Constructor<?> constructor = iconClass.getConstructor(Context.class);
            Object icon = constructor.newInstance(context);
            iconClass.getMethod("setIconRes", int.class).invoke(icon, iconId);
            iconClass.getMethod("setIconWidth", int.class).invoke(icon, dp(context, 24));
            iconClass.getMethod("setIconHeight", int.class).invoke(icon, dp(context, 24));
            iconClass.getMethod("setTintColor", int.class).invoke(icon, SettingsUi.textPrimary());
            return (View) icon;
        } catch (Throwable ignored) {
            return null;
        }
    }

    static void hideKeyboard(View view) {
        if (view == null) return;
        InputMethodManager input = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (input != null) {
            input.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        view.clearFocus();
    }

    static TextView label(Context context, String text) {
        return FeatureGateLabUi.text(context, text, 13, SettingsUi.textSecondary(), Typeface.NORMAL);
    }

    static TextView body(Context context, String text) {
        return FeatureGateLabUi.text(context, text, 15, SettingsUi.textPrimary(), Typeface.NORMAL);
    }

    static LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    static LinearLayout.LayoutParams weight() {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
    }

    static int dp(Context context, int value) {
        return SettingsUi.dp(context, value);
    }

    static int warningColor(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES ? 0xffffa45b : 0xffb45309;
    }

    static void styleDialog(AlertDialog dialog) {
        SettingsUi.styleFramedDialog(dialog);
        if (dialog.getWindow() != null) {
            styleDialogTree(dialog.getWindow().getDecorView());
        }
    }

    private static void styleDialogTree(View view) {
        if (view instanceof RadioButton) {
            RadioButton radio = (RadioButton) view;
            radio.setTextColor(SettingsUi.textPrimary());
            if (Build.VERSION.SDK_INT >= 21) {
                radio.setButtonTintList(ColorStateList.valueOf(SettingsUi.ACCENT));
            }
        } else if (view instanceof Button) {
            ((Button) view).setTextColor(SettingsUi.ACCENT);
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(SettingsUi.textPrimary());
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                styleDialogTree(group.getChildAt(i));
            }
        }
    }

    static final class BackInterceptLayout extends LinearLayout {
        private final Runnable onBack;

        BackInterceptLayout(Context context, Runnable onBack) {
            super(context);
            this.onBack = onBack;
            setFocusableInTouchMode(true);
            setOnKeyListener((view, keyCode, event) -> consumeBack(event));
        }

        @Override
        public boolean dispatchKeyEventPreIme(KeyEvent event) {
            return consumeBack(event) || super.dispatchKeyEventPreIme(event);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            return consumeBack(event) || super.dispatchKeyEvent(event);
        }

        private boolean consumeBack(KeyEvent event) {
            if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) return false;
            if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) onBack.run();
            return true;
        }
    }

    static final class SystemBackHandler {
        private Object dispatcher;
        private Object callback;

        void register(Activity activity, Runnable action) {
            if (Build.VERSION.SDK_INT < 33 || activity == null || callback != null) return;
            try {
                Class<?> callbackClass = Class.forName("android.window.OnBackInvokedCallback");
                Class<?> dispatcherClass = Class.forName("android.window.OnBackInvokedDispatcher");
                Method getDispatcher = Activity.class.getMethod("getOnBackInvokedDispatcher");
                dispatcher = getDispatcher.invoke(activity);
                callback = Proxy.newProxyInstance(
                        callbackClass.getClassLoader(),
                        new Class<?>[]{callbackClass},
                        (proxy, method, args) -> {
                            String name = method.getName();
                            if ("onBackInvoked".equals(name)) {
                                action.run();
                                return null;
                            }
                            if ("hashCode".equals(name)) return System.identityHashCode(proxy);
                            if ("equals".equals(name)) return proxy == args[0];
                            if ("toString".equals(name)) return "FeatureGateLabBackCallback";
                            return null;
                        }
                );
                dispatcherClass.getMethod("registerOnBackInvokedCallback", int.class, callbackClass)
                        .invoke(dispatcher, 1_000_000, callback);
            } catch (Throwable ignored) {
                dispatcher = null;
                callback = null;
            }
        }

        void unregister() {
            if (dispatcher == null || callback == null) return;
            try {
                Class<?> callbackClass = Class.forName("android.window.OnBackInvokedCallback");
                Class<?> dispatcherClass = Class.forName("android.window.OnBackInvokedDispatcher");
                dispatcherClass.getMethod("unregisterOnBackInvokedCallback", callbackClass)
                        .invoke(dispatcher, callback);
            } catch (Throwable ignored) {
                // Activity teardown also removes callbacks.
            } finally {
                dispatcher = null;
                callback = null;
            }
        }
    }
}
