/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.interaction;

import app.morphe.extension.tiktok.settings.Settings;

public final class StopVideoLoopingPatch {
    private StopVideoLoopingPatch() {
    }

    public static boolean overrideLooping(boolean requested) {
        return Settings.STOP_VIDEO_LOOPING.get() ? false : requested;
    }
}
