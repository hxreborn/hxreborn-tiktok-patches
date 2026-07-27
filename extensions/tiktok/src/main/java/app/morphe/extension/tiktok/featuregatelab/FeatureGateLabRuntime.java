/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.featuregatelab;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class FeatureGateLabRuntime {
    private static final String TAG = "MorpheFeatureGateLab";
    private static volatile Snapshot snapshot;
    private static final ThreadLocal<Boolean> buildingSnapshot = new ThreadLocal<>();
    private static final Set<String> triggered = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Map<String, String> firstCallers = new ConcurrentHashMap<>();
    private static final Map<String, String> originalValues = new ConcurrentHashMap<>();

    private FeatureGateLabRuntime() {
    }

    public static boolean isInstalled() {
        return false;
    }

    public static void reloadRules() {
        snapshot = null;
    }

    public static void resetTriggered(String ruleId) {
        triggered.remove(ruleId);
        firstCallers.remove(ruleId);
        originalValues.remove(ruleId);
    }

    public static void clearTriggered() {
        triggered.clear();
        firstCallers.clear();
        originalValues.clear();
    }

    public static boolean isTriggered(String manager, String key, String type) {
        return triggered.contains(FeatureGateLabStore.idFor(manager, key, type));
    }

    public static String firstCaller(String manager, String key, String type) {
        return firstCallers.get(FeatureGateLabStore.idFor(manager, key, type));
    }

    public static String originalValue(String manager, String key, String type) {
        return originalValues.get(FeatureGateLabStore.idFor(manager, key, type));
    }

    public static String runtimeRuleState(String manager, String key, String type) {
        if (!FeatureGateLabStore.runtimeStorageAvailable()) {
            return "Waiting for TikTok context";
        }
        Snapshot current = currentSnapshot();
        if (current == null) {
            return "Runtime snapshot unavailable";
        }
        if (!current.masterEnabled) {
            return "Master override is off";
        }
        if (!FeatureGateLabStore.supportsOverride(manager, type)) {
            return "No supported override boundary";
        }
        return current.rules.containsKey(identity(manager, key, type))
                ? "Exact typed rule loaded"
                : "Exact typed rule not loaded";
    }

    public static boolean overrideBoolean(String key, boolean original) {
        return overrideBooleanFor(FeatureGateLabStore.MANAGER_ABMOCK, key, original);
    }

    public static boolean overrideLiveBoolean(String key, boolean original) {
        return overrideBooleanFor(FeatureGateLabStore.MANAGER_LIVE, key, original);
    }

    public static Boolean overrideVeBoolean(String key, Boolean original) {
        if (original == null) {
            return null;
        }
        return Boolean.valueOf(overrideBooleanFor(
                FeatureGateLabStore.MANAGER_VE_CONFIG,
                key,
                original.booleanValue()
        ));
    }

    private static boolean overrideBooleanFor(String manager, String key, boolean original) {
        FeatureGateLabStore.Rule rule = activeRule(manager, key, "BOOLEAN");
        if (rule == null) {
            return original;
        }
        boolean forced = Boolean.parseBoolean(rule.value);
        markTriggered(rule, String.valueOf(original), String.valueOf(forced));
        return forced;
    }

    public static int overrideInt(String key, int original) {
        return overrideIntFor(FeatureGateLabStore.MANAGER_ABMOCK, key, original);
    }

    public static int overrideLiveInt(String key, int original) {
        return overrideIntFor(FeatureGateLabStore.MANAGER_LIVE, key, original);
    }

    public static int overrideVeInt(String key, int original) {
        return overrideIntFor(FeatureGateLabStore.MANAGER_VE_CONFIG, key, original);
    }

    private static int overrideIntFor(String manager, String key, int original) {
        FeatureGateLabStore.Rule rule = activeRule(manager, key, "INT");
        if (rule == null) {
            return original;
        }
        try {
            int forced = Integer.parseInt(rule.value);
            markTriggered(rule, String.valueOf(original), String.valueOf(forced));
            return forced;
        } catch (NumberFormatException ignored) {
            return original;
        }
    }

    public static long overrideLong(String key, long original) {
        return overrideLongFor(FeatureGateLabStore.MANAGER_ABMOCK, key, original);
    }

    public static long overrideLiveLong(String key, long original) {
        return overrideLongFor(FeatureGateLabStore.MANAGER_LIVE, key, original);
    }

    private static long overrideLongFor(String manager, String key, long original) {
        FeatureGateLabStore.Rule rule = activeRule(manager, key, "LONG");
        if (rule == null) {
            return original;
        }
        try {
            long forced = Long.parseLong(rule.value);
            markTriggered(rule, String.valueOf(original), String.valueOf(forced));
            return forced;
        } catch (NumberFormatException ignored) {
            return original;
        }
    }

    public static float overrideFloat(String key, float original) {
        return overrideFloatFor(FeatureGateLabStore.MANAGER_ABMOCK, key, original);
    }

    public static float overrideLiveFloat(String key, float original) {
        return overrideFloatFor(FeatureGateLabStore.MANAGER_LIVE, key, original);
    }

    public static float overrideVeFloat(String key, float original) {
        return overrideFloatFor(FeatureGateLabStore.MANAGER_VE_CONFIG, key, original);
    }

    private static float overrideFloatFor(String manager, String key, float original) {
        FeatureGateLabStore.Rule rule = activeRule(manager, key, "FLOAT");
        if (rule == null) {
            return original;
        }
        try {
            float forced = Float.parseFloat(rule.value);
            if (!Float.isFinite(forced)) {
                return original;
            }
            markTriggered(rule, String.valueOf(original), String.valueOf(forced));
            return forced;
        } catch (NumberFormatException ignored) {
            return original;
        }
    }

    public static double overrideDouble(String key, double original) {
        return overrideDoubleFor(FeatureGateLabStore.MANAGER_ABMOCK, key, original);
    }

    public static double overrideLiveDouble(String key, double original) {
        return overrideDoubleFor(FeatureGateLabStore.MANAGER_LIVE, key, original);
    }

    private static double overrideDoubleFor(String manager, String key, double original) {
        FeatureGateLabStore.Rule rule = activeRule(manager, key, "DOUBLE");
        if (rule == null) {
            return original;
        }
        try {
            double forced = Double.parseDouble(rule.value);
            if (!Double.isFinite(forced)) {
                return original;
            }
            markTriggered(rule, String.valueOf(original), String.valueOf(forced));
            return forced;
        } catch (NumberFormatException ignored) {
            return original;
        }
    }

    public static String overrideString(String key, String original) {
        return overrideStringFor(FeatureGateLabStore.MANAGER_ABMOCK, key, original);
    }

    public static String overrideLiveString(String key, String original) {
        return overrideStringFor(FeatureGateLabStore.MANAGER_LIVE, key, original);
    }

    public static String overrideVeString(String key, String original) {
        return overrideStringFor(FeatureGateLabStore.MANAGER_VE_CONFIG, key, original);
    }

    public static Object overrideRawAbValue(String key, Object original, boolean returnStringForObject) {
        if (returnStringForObject) {
            return original;
        }
        String type = scalarTypeOf(original);
        FeatureGateLabStore.Rule rule;
        if (type == null) {
            if (original != null) {
                return original;
            }
            rule = uniqueActiveAbRule(key);
        } else {
            rule = activeRule(FeatureGateLabStore.MANAGER_ABMOCK, key, type);
        }
        if (rule == null) {
            return original;
        }
        Object forced = parseBoxedScalar(rule);
        if (forced == null) {
            return original;
        }
        markTriggered(rule, original == null ? "null" : String.valueOf(original), String.valueOf(forced));
        return forced;
    }

    public static String transformActivityCenterSchema(String schema) {
        if (schema == null || !schema.contains("activity_center")) {
            return schema;
        }
        Snapshot current = currentSnapshot();
        if (current == null || !current.masterEnabled) {
            return schema;
        }
        try {
            Uri outer = Uri.parse(schema);
            String innerText = outer.getQueryParameter("url");
            if (innerText == null || !innerText.contains("activity_center")) {
                return schema;
            }
            Uri inner = Uri.parse(innerText);
            String existingText = inner.getQueryParameter("dev_fg_json");
            JSONObject values = existingText == null || existingText.isEmpty()
                    ? new JSONObject()
                    : new JSONObject(existingText);
            boolean changed = false;
            for (FeatureGateLabStore.Rule rule : current.rules.values()) {
                if (!FeatureGateLabStore.MANAGER_PIA_ACTIVITY_CENTER.equals(rule.manager)) {
                    continue;
                }
                Object original = values.has(rule.key) ? values.opt(rule.key) : null;
                Object forced = parseJsonScalar(rule);
                if (forced == null) {
                    continue;
                }
                values.put(rule.key, forced);
                markTriggered(rule, original == null ? "absent" : String.valueOf(original), String.valueOf(forced));
                changed = true;
            }
            if (!changed) {
                return schema;
            }
            Uri updatedInner = replaceQueryParameter(inner, "dev_fg_json", values.toString());
            return replaceQueryParameter(outer, "url", updatedInner.toString()).toString();
        } catch (Throwable throwable) {
            Log.w(TAG, "Activity Center override skipped: " + throwable.getClass().getSimpleName());
            return schema;
        }
    }

    private static String overrideStringFor(String manager, String key, String original) {
        FeatureGateLabStore.Rule rule = activeRule(manager, key, "STRING");
        if (rule == null) {
            return original;
        }
        markTriggered(rule, original, rule.value);
        return rule.value;
    }

    private static Object parseJsonScalar(FeatureGateLabStore.Rule rule) {
        try {
            switch (rule.type) {
                case "BOOLEAN":
                    return Boolean.parseBoolean(rule.value);
                case "INT":
                    return Integer.parseInt(rule.value);
                case "LONG":
                    return Long.parseLong(rule.value);
                case "FLOAT":
                    return Float.parseFloat(rule.value);
                case "DOUBLE":
                    return Double.parseDouble(rule.value);
                case "STRING":
                    return rule.value;
                default:
                    return null;
            }
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Object parseBoxedScalar(FeatureGateLabStore.Rule rule) {
        try {
            switch (rule.type) {
                case "BOOLEAN":
                    return Boolean.valueOf(rule.value);
                case "INT":
                    return Integer.valueOf(rule.value);
                case "LONG":
                    return Long.valueOf(rule.value);
                case "FLOAT": {
                    float value = Float.parseFloat(rule.value);
                    return Float.isFinite(value) ? value : null;
                }
                case "DOUBLE": {
                    double value = Double.parseDouble(rule.value);
                    return Double.isFinite(value) ? value : null;
                }
                case "STRING":
                    return rule.value;
                default:
                    return null;
            }
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static String scalarTypeOf(Object value) {
        if (value instanceof Boolean) return "BOOLEAN";
        if (value instanceof Integer) return "INT";
        if (value instanceof Long) return "LONG";
        if (value instanceof Float) return "FLOAT";
        if (value instanceof Double) return "DOUBLE";
        if (value instanceof String) return "STRING";
        return null;
    }

    private static FeatureGateLabStore.Rule uniqueActiveAbRule(String key) {
        Snapshot current = currentSnapshot();
        if (current == null || !current.masterEnabled || key == null) {
            return null;
        }
        FeatureGateLabStore.Rule match = null;
        for (FeatureGateLabStore.Rule candidate : current.rules.values()) {
            if (!FeatureGateLabStore.MANAGER_ABMOCK.equals(candidate.manager)
                    || !key.equals(candidate.key)) {
                continue;
            }
            if (match != null) {
                return null;
            }
            match = candidate;
        }
        return match;
    }

    private static Uri replaceQueryParameter(Uri uri, String targetName, String targetValue) {
        Uri.Builder builder = uri.buildUpon().clearQuery();
        for (String name : uri.getQueryParameterNames()) {
            if (targetName.equals(name)) {
                continue;
            }
            for (String value : uri.getQueryParameters(name)) {
                builder.appendQueryParameter(name, value);
            }
        }
        builder.appendQueryParameter(targetName, targetValue);
        return builder.build();
    }

    private static FeatureGateLabStore.Rule activeRule(String manager, String key, String type) {
        Snapshot current = currentSnapshot();
        if (current == null || !current.masterEnabled || key == null) {
            return null;
        }
        return current.rules.get(identity(manager, key, type));
    }

    private static Snapshot currentSnapshot() {
        Snapshot current = snapshot;
        if (current != null) {
            return current;
        }
        if (Boolean.TRUE.equals(buildingSnapshot.get())) {
            return null;
        }
        synchronized (FeatureGateLabRuntime.class) {
            current = snapshot;
            if (current == null) {
                buildingSnapshot.set(Boolean.TRUE);
                try {
                    current = buildSnapshot();
                    if (current != null) {
                        snapshot = current;
                    }
                } finally {
                    buildingSnapshot.remove();
                }
            }
        }
        return current;
    }

    private static Snapshot buildSnapshot() {
        if (!FeatureGateLabStore.runtimeStorageAvailable()) {
            return null;
        }
        Map<String, FeatureGateLabStore.Rule> active = new HashMap<>();
        for (FeatureGateLabStore.Rule rule : FeatureGateLabStore.rules()) {
            if (rule.enabled && FeatureGateLabStore.supportsOverride(rule.manager, rule.type)) {
                active.put(identity(rule.manager, rule.key, rule.type), rule);
            }
        }
        boolean masterEnabled = FeatureGateLabStore.masterEnabled();
        Log.i(TAG, "snapshot master=" + masterEnabled
                + " active_rules=" + active.size()
                + " identities=" + summarizeRules(active));
        return new Snapshot(masterEnabled, Collections.unmodifiableMap(active));
    }

    private static String summarizeRules(Map<String, FeatureGateLabStore.Rule> rules) {
        if (rules.isEmpty()) {
            return "none";
        }
        StringBuilder result = new StringBuilder();
        for (FeatureGateLabStore.Rule rule : rules.values()) {
            if (result.length() > 0) {
                result.append(',');
            }
            result.append(rule.manager).append('/').append(safeLog(rule.key)).append(':').append(rule.type);
        }
        return result.toString();
    }

    private static String identity(String manager, String key, String type) {
        return manager + "\n" + key + "\n" + FeatureGateLabStore.normalizeType(type);
    }

    private static void markTriggered(FeatureGateLabStore.Rule rule, String original, String forced) {
        originalValues.put(rule.id, original == null ? "null" : original);
        if (!triggered.add(rule.id)) {
            return;
        }
        String caller = findCaller();
        firstCallers.put(rule.id, caller);
        Log.i(TAG, "manager=" + rule.manager
                + " key=" + rule.key
                + " type=" + rule.type
                + " original=" + safeLog(original)
                + " forced=" + safeLog(forced)
                + " caller=" + caller);
    }

    private static String findCaller() {
        for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
            String className = frame.getClassName();
            if (className.startsWith("app.morphe.extension.tiktok.featuregatelab")
                    || className.startsWith("java.lang.Thread")
                    || className.startsWith("dalvik.system.VMStack")
                    || className.equals("X.0BPv")
                    || className.equals("X.0BPb")
                    || className.equals("X.0BP8")
                    || className.equals("X.090T")
                    || className.equals("X.0b13")
                    || className.equals("com.bytedance.ies.abmock.SettingsManager")
                    || className.equals("com.bytedance.android.live_settings.SettingsManager")
                    || className.equals("com.ss.android.vesdk.VEConfigCenter")) {
                continue;
            }
            return className + "#" + frame.getMethodName();
        }
        return "unknown";
    }

    private static String safeLog(String value) {
        if (value == null) {
            return "null";
        }
        String singleLine = value.replace('\n', ' ').replace('\r', ' ');
        return singleLine.length() <= 160 ? singleLine : singleLine.substring(0, 157) + "...";
    }

    private static final class Snapshot {
        final boolean masterEnabled;
        final Map<String, FeatureGateLabStore.Rule> rules;

        Snapshot(boolean masterEnabled, Map<String, FeatureGateLabStore.Rule> rules) {
            this.masterEnabled = masterEnabled;
            this.rules = rules;
        }
    }
}
