package app.morphe.extension.tiktok.offline;

import java.util.ArrayList;
import java.util.List;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public final class CustomOfflineVideosLimitPatch {
    public static final int MIN_LIMIT = 1;
    public static final int MAX_LIMIT = 1000;
    private static final int STORAGE_MB_PER_VIDEO = 2;
    private static final double WATCH_MINUTES_PER_VIDEO = 0.6;

    private CustomOfflineVideosLimitPatch() {
    }

    public static List<Integer> getOfflineVideoOptions(List<Integer> originalOptions) {
        if (originalOptions == null) {
            return null;
        }

        if (!Settings.CUSTOM_OFFLINE_VIDEOS.get()) {
            return originalOptions;
        }

        int customLimit = getCustomOfflineVideoLimit();
        try {
            if (originalOptions.contains(customLimit)) {
                return originalOptions;
            }

            ArrayList<Integer> options = new ArrayList<>(originalOptions.size() + 1);
            options.addAll(originalOptions);
            options.add(customLimit);
            return options;
        } catch (Exception ex) {
            Logger.printException(() -> "[Morphe Offline Videos] option append failure", ex);
            return originalOptions;
        }
    }

    public static int getCustomOfflineVideoLimit() {
        try {
            return clamp(Settings.CUSTOM_OFFLINE_VIDEO_LIMIT.get());
        } catch (Exception ex) {
            Logger.printException(() -> "[Morphe Offline Videos] limit read failure", ex);
            return MAX_LIMIT;
        }
    }

    public static int getCustomOfflineVideoLimitOrOriginal(int originalLimit) {
        if (!Settings.CUSTOM_OFFLINE_VIDEOS.get()) {
            return originalLimit;
        }

        return getCustomOfflineVideoLimit();
    }

    public static int getCustomOfflineVideoMinutesOrOriginal(int originalMinutes) {
        if (!Settings.CUSTOM_OFFLINE_VIDEOS.get()) {
            return originalMinutes;
        }

        return Math.max(1, (int) Math.ceil(getCustomOfflineVideoLimit() * WATCH_MINUTES_PER_VIDEO));
    }

    public static int getCustomOfflineVideoSizeMbOrOriginal(int originalSizeMb) {
        if (!Settings.CUSTOM_OFFLINE_VIDEOS.get()) {
            return originalSizeMb;
        }

        return getCustomOfflineVideoLimit() * STORAGE_MB_PER_VIDEO;
    }

    private static int clamp(int value) {
        return Math.max(MIN_LIMIT, Math.min(MAX_LIMIT, value));
    }
}
