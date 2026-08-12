/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/extensions/tiktok/src/main/java/app/revanced/extension/tiktok/spoof/sim/SpoofSimPatch.java
 */

package app.morphe.extension.tiktok.spoof.sim;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.shared.Utils;
import app.morphe.extension.tiktok.settings.Settings;

@SuppressWarnings("unused")
public class SpoofSimPatch {
    private static boolean isContextNotSet(String fieldSpoofed) {
        if (Utils.getContext() != null) {
            return false;
        }

        Logger.printException(() -> "Context is not yet set, cannot spoof: " + fieldSpoofed, null);
        return true;
    }

    public static String getCountryIso(String value) {
        if (isContextNotSet("countryIso")) return value;

        if (Settings.SIM_SPOOF.get()) {
            String iso = Settings.SIM_SPOOF_ISO.get();
            Logger.printDebug(() -> "Spoofing countryIso from: " + value + " to: " + iso);
            return iso;
        }

        return value;
    }

    public static String getOperator(String value) {
        if (isContextNotSet("MCC-MNC")) return value;

        if (Settings.SIM_SPOOF.get()) {
            String mccMnc = Settings.SIMSPOOF_MCCMNC.get();
            Logger.printDebug(() -> "Spoofing sim MCC-MNC from: " + value + " to: " + mccMnc);
            return mccMnc;
        }

        return value;
    }

    public static String getOperatorName(String value) {
        if (isContextNotSet("operatorName")) return value;

        if (Settings.SIM_SPOOF.get()) {
            String operator = Settings.SIMSPOOF_OP_NAME.get();
            Logger.printDebug(() -> "Spoofing sim operatorName from: " + value + " to: " + operator);
            return operator;
        }

        return value;
    }

    public static String getMcc(String value) {
        return getMccMncPart(value, true);
    }

    public static String getMnc(String value) {
        return getMccMncPart(value, false);
    }

    private static String getMccMncPart(String value, boolean mcc) {
        if (isContextNotSet(mcc ? "cellMcc" : "cellMnc")) return value;
        if (!Settings.SIM_SPOOF.get()) return value;

        String combined = Settings.SIMSPOOF_MCCMNC.get();
        if (combined == null || combined.length() < 5) return value;

        String replacement = mcc ? combined.substring(0, 3) : combined.substring(3);
        Logger.printDebug(() -> "Spoofing " + (mcc ? "cell MCC" : "cell MNC")
                + " from: " + value + " to: " + replacement);
        return replacement;
    }
}
