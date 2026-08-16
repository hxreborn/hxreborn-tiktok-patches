/*
 * Forked from:
 * https://github.com/ReVanced/revanced-patches/blob/377d4e15016296b45d809697f7f69bce74badd3a/extensions/tiktok/src/main/java/app/revanced/extension/tiktok/cleardisplay/RememberClearDisplayPatch.java
 */

package app.morphe.extension.tiktok.cleardisplay;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.shared.settings.BaseSettings;
import app.morphe.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public class RememberClearDisplayPatch {
    private static final int EVENT_SWITCH_PAGE = 3;
    private static final int EVENT_NOTIFY_EXIT = 9;
    private static volatile Boolean lastLoggedState;

    public static boolean getClearDisplayState() {
        boolean state = Settings.CLEAR_DISPLAY.get();
        if (BaseSettings.DEBUG.get() && (lastLoggedState == null || lastLoggedState != state)) {
            lastLoggedState = state;
            Logger.printInfo(() -> "[Morphe ClearDisplay] get state=" + state);
        }
        return state;
    }

    public static void rememberClearDisplayEvent(Object event) {
        if (event == null) return;
        try {
            Class<?> type = event.getClass();
            boolean isClean = type.getDeclaredField("LIZ").getBoolean(event);
            int eventType = type.getDeclaredField("LIZIZ").getInt(event);
            if (eventType == EVENT_SWITCH_PAGE || eventType == EVENT_NOTIFY_EXIT) {
                return;
            }
            if (BaseSettings.DEBUG.get()) {
                boolean oldState = Settings.CLEAR_DISPLAY.get();
                Logger.printInfo(() -> "[Morphe ClearDisplay] remember type=" + eventType +
                        " state " + oldState + " -> " + isClean);
            }
            Settings.CLEAR_DISPLAY.save(isClean);
        } catch (ReflectiveOperationException ex) {
            Logger.printException(() -> "[Morphe ClearDisplay] Could not read clear-display event", ex);
        }
    }
}

