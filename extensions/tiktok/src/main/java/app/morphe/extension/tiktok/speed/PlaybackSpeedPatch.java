package app.morphe.extension.tiktok.speed;

import app.morphe.extension.tiktok.settings.Settings;

/** Helper methods injected by the TikTok playback speed bytecode patch. */
public final class PlaybackSpeedPatch {
    private static final String EDGE_SPEEDUP_CLASS =
        "com.ss.android.ugc.aweme.feed.longvideo.edgespeedup.EdgeSpeedupAssem";
    private static final String SOURCE_LONG_PRESS = "long_press";
    private static final String SOURCE_SHARE_BUTTON = "click_share_button";
    private static final String SOURCE_IMMERSIVE_PICKER = "immersive_click";
    private static final String SOURCE_ON_SCREEN_BUTTON = "on_screen_button";
    private static final String SOURCE_SWIPE_LOCK = "swipe_up_lock_persist";

    private static volatile float rememberedSpeed = 1.0f;

    private PlaybackSpeedPatch() {}

    public static void rememberPlaybackSpeed(float speed, String source) {
        if (!isValidSpeed(speed) ||
            (!isExplicitSelectionSource(source) && !isEdgeSpeedupSelection(source))) {
            return;
        }

        rememberedSpeed = speed;
        try {
            Settings.REMEMBERED_SPEED.save(speed);
        } catch (Throwable ignored) {
            // Settings can be unavailable during early startup.
        }
    }

    public static float preserveTransitionSpeed(float requestedSpeed) {
        if (Float.compare(requestedSpeed, 1.0f) != 0) {
            return requestedSpeed;
        }
        return getPlaybackSpeed();
    }

    public static float getPlaybackSpeed() {
        try {
            float persisted = Settings.REMEMBERED_SPEED.get();
            return isValidSpeed(persisted) ? persisted : rememberedSpeed;
        } catch (Throwable ignored) {
            return rememberedSpeed;
        }
    }

    private static boolean isValidSpeed(float speed) {
        return !Float.isNaN(speed) && !Float.isInfinite(speed) && speed > 0.0f;
    }

    private static boolean isExplicitSelectionSource(String source) {
        return SOURCE_LONG_PRESS.equals(source)
            || SOURCE_SHARE_BUTTON.equals(source)
            || SOURCE_IMMERSIVE_PICKER.equals(source)
            || SOURCE_ON_SCREEN_BUTTON.equals(source)
            || SOURCE_SWIPE_LOCK.equals(source);
    }

    private static boolean isEdgeSpeedupSelection(String source) {
        if (source != null) {
            return false;
        }
        for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
            if (EDGE_SPEEDUP_CLASS.equals(frame.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
