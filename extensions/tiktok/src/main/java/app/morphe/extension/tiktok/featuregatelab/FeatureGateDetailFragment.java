/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.featuregatelab;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.morphe.extension.shared.Utils;
import app.morphe.extension.tiktok.settings.preference.SettingsUi;

@SuppressWarnings({"deprecation", "SetTextI18n"})
public final class FeatureGateDetailFragment extends Fragment {
    private static final String ARG_MANAGER = "manager";
    private static final String ARG_KEY = "key";
    private static final String ARG_TYPE = "type";

    private final FeatureGateLabUi.SystemBackHandler systemBack = new FeatureGateLabUi.SystemBackHandler();

    private FeatureGateCatalog.Entry entry;
    private FeatureGateLabStore.Rule rule;
    private TextView status;
    private TextView effectiveValue;
    private Spinner values;
    private Switch force;
    private Switch booleanValue;
    private TextView reset;
    private LinearLayout technicalDetails;
    private TextView technicalToggle;
    private List<ValueOption> options;
    private boolean suppress;
    private int lastConcreteSelection;

    public static FeatureGateDetailFragment forEntry(String manager, String key, String type) {
        FeatureGateDetailFragment fragment = new FeatureGateDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_MANAGER, manager);
        arguments.putString(ARG_KEY, key);
        arguments.putString(ARG_TYPE, type);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle state) {
        Context context = getActivity();
        FeatureGateLabUi.syncTheme(context);
        FeatureGateCatalog.Snapshot snapshot = FeatureGateCatalog.cachedSnapshot();
        String manager = getArguments().getString(ARG_MANAGER, "");
        String key = getArguments().getString(ARG_KEY, "");
        entry = snapshot == null ? null : snapshot.byIdentity.get(manager + "\n" + key);

        FeatureGateLabUi.BackInterceptLayout screen =
                new FeatureGateLabUi.BackInterceptLayout(context, this::leaveDetail);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setBackgroundColor(SettingsUi.background());
        screen.requestFocus();
        screen.addView(
                FeatureGateLabUi.header(context, "Gate details", this::leaveDetail),
                FeatureGateLabUi.matchWrap()
        );

        if (entry == null) {
            TextView error = FeatureGateLabUi.body(context, "This catalog entry is no longer available. Refresh the Lab and try again.");
            error.setGravity(Gravity.CENTER);
            error.setPadding(
                    FeatureGateLabUi.dp(context, 24),
                    FeatureGateLabUi.dp(context, 24),
                    FeatureGateLabUi.dp(context, 24),
                    FeatureGateLabUi.dp(context, 24)
            );
            screen.addView(error, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
            return screen;
        }
        rule = FeatureGateLabStore.rule(entry.manager, entry.key, entry.type);

        ScrollView scroll = new ScrollView(context);
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                FeatureGateLabUi.dp(context, 16),
                FeatureGateLabUi.dp(context, 8),
                FeatureGateLabUi.dp(context, 16),
                FeatureGateLabUi.dp(context, 24)
        );
        scroll.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        screen.addView(scroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        TextView title = FeatureGateLabUi.heading(context, entry.title);
        title.setMaxLines(3);
        title.setEllipsize(TextUtils.TruncateAt.END);
        content.addView(title, FeatureGateLabUi.matchWrap());

        TextView rawKey = FeatureGateLabUi.label(context, entry.key);
        rawKey.setTextIsSelectable(true);
        LinearLayout.LayoutParams keyParams = FeatureGateLabUi.matchWrap();
        keyParams.setMargins(0, FeatureGateLabUi.dp(context, 3), 0, 0);
        content.addView(rawKey, keyParams);

        if (!entry.description.isEmpty()) {
            TextView description = FeatureGateLabUi.body(context, entry.description);
            LinearLayout.LayoutParams params = FeatureGateLabUi.matchWrap();
            params.setMargins(0, FeatureGateLabUi.dp(context, 12), 0, 0);
            content.addView(description, params);
        }

        addSectionTitle(content, "Current state");
        addInfo(content, "Loaded for this account", entry.loaded ? "Yes" : "No");
        addInfo(content, "TikTok cached value", entry.loaded
                ? entry.currentValue + " (" + entry.currentType + ")"
                : "Not present in the current cache");
        effectiveValue = addInfo(content, "Effective getter result", effectiveValueText());
        TextView cacheNote = FeatureGateLabUi.label(
                context,
                "An override changes the value returned by the getter. It does not rewrite TikTok's cached value or prove the named feature changed."
        );
        LinearLayout.LayoutParams cacheNoteParams = FeatureGateLabUi.matchWrap();
        cacheNoteParams.setMargins(0, FeatureGateLabUi.dp(context, 8), 0, 0);
        content.addView(cacheNote, cacheNoteParams);

        if (isSensitiveKey(entry.key)) {
            TextView sensitive = FeatureGateLabUi.label(
                    context,
                    "Account-sensitive name: forcing this key may affect security, login, compliance, region, payment, or account safety behavior."
            );
            sensitive.setTextColor(FeatureGateLabUi.warningColor(context));
            LinearLayout.LayoutParams params = FeatureGateLabUi.matchWrap();
            params.setMargins(0, FeatureGateLabUi.dp(context, 12), 0, 0);
            content.addView(sensitive, params);
        }

        addSectionTitle(content, "Override");
        status = FeatureGateLabUi.text(context, "", 13, SettingsUi.textSecondary(), Typeface.BOLD);
        content.addView(status, FeatureGateLabUi.matchWrap());

        boolean editable = FeatureGateLabStore.masterEnabled();
        boolean booleanEntry = "BOOLEAN".equals(entry.type);
        if (booleanEntry) {
            LinearLayout valueRow = settingRow(
                    context,
                    "Forced result",
                    "Off forces false; on forces true. Reset returns control to TikTok"
            );
            booleanValue = new Switch(context);
            booleanValue.setChecked(Boolean.parseBoolean(rule == null ? bestInitialValue(entry) : rule.value));
            booleanValue.setEnabled(editable);
            valueRow.addView(booleanValue, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    FeatureGateLabUi.dp(context, 48)
            ));
            content.addView(valueRow, FeatureGateLabUi.matchWrap());
        } else {
            LinearLayout forceRow = settingRow(
                    context,
                    "Override this gate",
                    "When TikTok requests this key, return the selected value below"
            );
            force = new Switch(context);
            forceRow.addView(force, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    FeatureGateLabUi.dp(context, 48)
            ));
            content.addView(forceRow, FeatureGateLabUi.matchWrap());

            TextView valueLabel = FeatureGateLabUi.label(context, "Value to return");
            content.addView(valueLabel, FeatureGateLabUi.matchWrap());
            options = buildOptions(entry, rule);
            values = new Spinner(context);
            applyOptionsAdapter();
            values.setEnabled(editable);
            values.setBackground(SettingsUi.borderedSurface(context, 6, false));
            values.setMinimumHeight(FeatureGateLabUi.dp(context, 48));
            LinearLayout.LayoutParams valueParams = FeatureGateLabUi.matchWrap();
            valueParams.setMargins(0, FeatureGateLabUi.dp(context, 4), 0, FeatureGateLabUi.dp(context, 4));
            content.addView(values, valueParams);
        }

        reset = FeatureGateLabUi.text(context, "Reset override", 14, SettingsUi.ACCENT, Typeface.BOLD);
        reset.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        reset.setPadding(0, FeatureGateLabUi.dp(context, 10), 0, FeatureGateLabUi.dp(context, 10));
        reset.setVisibility(rule == null ? View.GONE : View.VISIBLE);
        content.addView(reset, FeatureGateLabUi.matchWrap());

        if (force != null) force.setEnabled(editable);
        if (!editable) {
            TextView disabled = FeatureGateLabUi.label(context, "Overrides are disabled in the Feature Gate Lab.");
            content.addView(disabled, FeatureGateLabUi.matchWrap());
        }

        suppress = true;
        if (!booleanEntry) {
            int selected = selectedIndex(options, rule == null ? bestInitialValue(entry) : rule.value);
            values.setSelection(selected);
            lastConcreteSelection = selected;
        }
        if (force != null) force.setChecked(rule != null && rule.enabled);
        suppress = false;
        updateStatus();

        if (values != null) {
            values.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (suppress || !FeatureGateLabStore.masterEnabled()) return;
                    ValueOption option = options.get(position);
                    if (option.custom) {
                        showCustomValue();
                        return;
                    }
                    lastConcreteSelection = position;
                    String storedValue = rule == null ? null : rule.value;
                    if ((storedValue == null && option.value.equals(bestInitialValue(entry)))
                            || option.value.equals(storedValue)) return;
                    persist(option.value, force.isChecked());
                }

                @Override public void onNothingSelected(AdapterView<?> parent) { }
            });
        }

        if (booleanValue != null) {
            booleanValue.setOnCheckedChangeListener((button, checked) -> {
                if (suppress || !FeatureGateLabStore.masterEnabled()) return;
                persist(String.valueOf(checked), true);
            });
        }

        if (force != null) {
            force.setOnCheckedChangeListener((button, enabled) -> {
                if (suppress || !FeatureGateLabStore.masterEnabled()) return;
                ValueOption selectedOption = options.get(values.getSelectedItemPosition());
                if (selectedOption.custom) {
                    suppress = true;
                    force.setChecked(false);
                    suppress = false;
                    showCustomValue();
                    return;
                }
                persist(selectedOption.value, enabled);
            });
        }

        reset.setOnClickListener(view -> resetRule());
        addTechnicalDetails(content);
        return screen;
    }

    @Override
    public void onResume() {
        super.onResume();
        systemBack.register(getActivity(), this::leaveDetail);
        updateStatus();
    }

    @Override
    public void onPause() {
        systemBack.unregister();
        super.onPause();
    }

    private void leaveDetail() {
        if (getFragmentManager() != null) getFragmentManager().popBackStack();
    }

    private LinearLayout settingRow(Context context, String title, String subtitle) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout labels = new LinearLayout(context);
        labels.setOrientation(LinearLayout.VERTICAL);
        labels.addView(FeatureGateLabUi.body(context, title), FeatureGateLabUi.matchWrap());
        labels.addView(FeatureGateLabUi.label(context, subtitle), FeatureGateLabUi.matchWrap());
        row.addView(labels, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        return row;
    }

    private void persist(String value, boolean enabled) {
        String error = FeatureGateLabStore.validateValue(entry.type, value);
        if (error != null) {
            Utils.showToastLong(error);
            return;
        }
        FeatureGateLabStore.saveRule(entry.manager, entry.key, entry.type, value, enabled);
        rule = FeatureGateLabStore.rule(entry.manager, entry.key, entry.type);
        reset.setVisibility(View.VISIBLE);
        updateStatus();
    }

    private void resetRule() {
        FeatureGateLabStore.deleteRule(entry.manager, entry.key, entry.type);
        rule = null;
        suppress = true;
        if (force != null) force.setChecked(false);
        if (booleanValue != null) {
            booleanValue.setChecked(Boolean.parseBoolean(bestInitialValue(entry)));
        } else {
            values.setSelection(selectedIndex(options, bestInitialValue(entry)));
        }
        suppress = false;
        reset.setVisibility(View.GONE);
        updateStatus();
        Utils.showToastShort("Feature gate override reset");
    }

    private void showCustomValue() {
        EditText input = new EditText(getActivity());
        input.setSingleLine(!"STRING".equals(entry.type));
        input.setHint("Custom " + entry.type.toLowerCase(Locale.ROOT) + " value (unverified)");
        input.setText(rule == null ? "" : rule.value);
        SettingsUi.styleEditText(input);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Custom value (unverified)")
                .setView(input)
                .setPositiveButton("Use value", null)
                .setNegativeButton("Cancel", (ignored, which) -> restoreSelection())
                .setOnCancelListener(ignored -> restoreSelection())
                .create();
        dialog.setOnShowListener(ignored -> {
            FeatureGateLabUi.styleDialog(dialog);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String value = input.getText().toString();
                String error = FeatureGateLabStore.validateValue(entry.type, value);
                if (error != null) {
                    input.setError(error);
                    return;
                }
                addOrSelectCustomValue(value);
                persist(value, force.isChecked());
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void addOrSelectCustomValue(String value) {
        int index = selectedIndex(options, value);
        if (index >= options.size() || options.get(index).custom || !value.equals(options.get(index).value)) {
            int customIndex = options.size() - 1;
            options.add(customIndex, new ValueOption(value, value + " (Custom, unverified)", false));
            applyOptionsAdapter();
            index = customIndex;
        }
        suppress = true;
        values.setSelection(index);
        lastConcreteSelection = index;
        suppress = false;
    }

    private void applyOptionsAdapter() {
        List<String> labels = new ArrayList<>();
        for (ValueOption option : options) labels.add(option.label);
        ArrayAdapter<String> adapter = new ValueAdapter(getActivity(), labels);
        values.setAdapter(adapter);
    }

    private static final class ValueAdapter extends ArrayAdapter<String> {
        ValueAdapter(Context context, List<String> labels) {
            super(context, android.R.layout.simple_spinner_item, labels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return valueView(position, false);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return valueView(position, true);
        }

        private TextView valueView(int position, boolean dropdown) {
            Context context = getContext();
            TextView view = FeatureGateLabUi.body(context, getItem(position));
            view.setGravity(Gravity.CENTER_VERTICAL);
            view.setSingleLine(true);
            view.setEllipsize(TextUtils.TruncateAt.END);
            view.setPadding(
                    FeatureGateLabUi.dp(context, 12),
                    dropdown ? FeatureGateLabUi.dp(context, 10) : 0,
                    FeatureGateLabUi.dp(context, 12),
                    dropdown ? FeatureGateLabUi.dp(context, 10) : 0
            );
            view.setBackgroundColor(SettingsUi.background());
            return view;
        }
    }

    private void restoreSelection() {
        suppress = true;
        values.setSelection(lastConcreteSelection);
        suppress = false;
    }

    private void updateStatus() {
        if (status == null || entry == null) return;
        rule = FeatureGateLabStore.rule(entry.manager, entry.key, entry.type);
        if (rule == null) {
            status.setText("Using TikTok's value");
            status.setTextColor(SettingsUi.textSecondary());
            if (effectiveValue != null) effectiveValue.setText(effectiveValueText());
            return;
        }
        if (!rule.enabled) {
            status.setText("Override saved but off");
            status.setTextColor(SettingsUi.textSecondary());
            if (effectiveValue != null) effectiveValue.setText(effectiveValueText());
            return;
        }
        boolean triggered = FeatureGateLabRuntime.isTriggered(entry.manager, entry.key, entry.type);
        status.setText(triggered ? "Getter requested" : "Getter not requested yet");
        status.setTextColor(triggered ? SettingsUi.ACCENT : FeatureGateLabUi.warningColor(getActivity()));
        if (effectiveValue != null) effectiveValue.setText(effectiveValueText());
    }

    private String effectiveValueText() {
        if (entry == null) return "Unavailable";
        FeatureGateLabStore.Rule currentRule = FeatureGateLabStore.rule(entry.manager, entry.key, entry.type);
        if (FeatureGateLabStore.masterEnabled() && currentRule != null && currentRule.enabled) {
            boolean triggered = FeatureGateLabRuntime.isTriggered(entry.manager, entry.key, entry.type);
            return currentRule.value + (triggered
                    ? " (override returned)"
                    : " (will be returned when requested)");
        }
        return entry.loaded
                ? entry.currentValue + " (TikTok value)"
                : "No current value; no active override";
    }

    private void addTechnicalDetails(LinearLayout root) {
        Context context = root.getContext();
        LinearLayout heading = new LinearLayout(context);
        heading.setOrientation(LinearLayout.HORIZONTAL);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = FeatureGateLabUi.text(context, "Technical details", 15, SettingsUi.textPrimary(), Typeface.BOLD);
        heading.addView(title, new LinearLayout.LayoutParams(0, FeatureGateLabUi.dp(context, 48), 1f));
        technicalToggle = FeatureGateLabUi.text(context, "Show", 14, SettingsUi.ACCENT, Typeface.BOLD);
        technicalToggle.setGravity(Gravity.CENTER);
        technicalToggle.setPadding(
                FeatureGateLabUi.dp(context, 12),
                0,
                FeatureGateLabUi.dp(context, 12),
                0
        );
        heading.addView(technicalToggle, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                FeatureGateLabUi.dp(context, 40)
        ));
        LinearLayout.LayoutParams headingParams = FeatureGateLabUi.matchWrap();
        headingParams.setMargins(0, FeatureGateLabUi.dp(context, 12), 0, 0);
        root.addView(heading, headingParams);

        technicalDetails = new LinearLayout(context);
        technicalDetails.setOrientation(LinearLayout.VERTICAL);
        technicalDetails.setVisibility(View.GONE);
        addInfo(technicalDetails, "Manager", entry.manager);
        addInfo(technicalDetails, "Type", entry.type);
        addInfo(technicalDetails, "Source", entry.sourceLabel());
        addInfo(technicalDetails, "Generated defaults", join(entry.defaults));
        addInfo(technicalDetails, "Historical values", join(entry.historical));
        addInfo(technicalDetails, "Researched values", join(entry.researched));
        addInfo(technicalDetails, "Proof", entry.proof);
        String original = FeatureGateLabRuntime.originalValue(entry.manager, entry.key, entry.type);
        if (original != null) addInfo(technicalDetails, "Last original value", original);
        String caller = FeatureGateLabRuntime.firstCaller(entry.manager, entry.key, entry.type);
        if (caller != null) addInfo(technicalDetails, "First caller", caller);
        root.addView(technicalDetails, FeatureGateLabUi.matchWrap());

        View.OnClickListener toggle = ignored -> {
            boolean show = technicalDetails.getVisibility() != View.VISIBLE;
            technicalDetails.setVisibility(show ? View.VISIBLE : View.GONE);
            technicalToggle.setText(show ? "Hide" : "Show");
        };
        heading.setOnClickListener(toggle);
        technicalToggle.setOnClickListener(toggle);
    }

    private void addSectionTitle(LinearLayout root, String text) {
        TextView title = FeatureGateLabUi.text(root.getContext(), text, 13, SettingsUi.ACCENT, Typeface.BOLD);
        LinearLayout.LayoutParams params = FeatureGateLabUi.matchWrap();
        params.setMargins(0, FeatureGateLabUi.dp(root.getContext(), 18), 0, FeatureGateLabUi.dp(root.getContext(), 4));
        root.addView(title, params);
    }

    private TextView addInfo(LinearLayout root, String label, String value) {
        Context context = root.getContext();
        TextView labelView = FeatureGateLabUi.label(context, label);
        LinearLayout.LayoutParams labelParams = FeatureGateLabUi.matchWrap();
        labelParams.setMargins(0, FeatureGateLabUi.dp(context, 8), 0, 0);
        root.addView(labelView, labelParams);
        TextView valueView = FeatureGateLabUi.body(context, value == null || value.isEmpty() ? "None recorded" : value);
        valueView.setTextIsSelectable(true);
        root.addView(valueView, FeatureGateLabUi.matchWrap());
        return valueView;
    }

    private static List<ValueOption> buildOptions(FeatureGateCatalog.Entry entry, FeatureGateLabStore.Rule rule) {
        LinkedHashMap<String, List<String>> values = new LinkedHashMap<>();
        if (entry.loaded) addValue(values, entry.currentValue, "Current");
        for (String value : entry.defaults) addValue(values, value, "Default");
        for (String value : entry.researched) addValue(values, value, "Researched");
        for (String value : entry.historical) addValue(values, value, "Historical");
        if (rule != null) addValue(values, rule.value, "Selected");
        List<ValueOption> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> item : values.entrySet()) {
            result.add(new ValueOption(item.getKey(), item.getKey() + " (" + join(item.getValue()) + ")", false));
        }
        result.add(new ValueOption(null, "Custom value...", true));
        return result;
    }

    private static void addValue(Map<String, List<String>> values, String value, String source) {
        if (value == null || "null".equals(value)) return;
        List<String> sources = values.get(value);
        if (sources == null) {
            sources = new ArrayList<>();
            values.put(value, sources);
        }
        if (!sources.contains(source)) sources.add(source);
    }

    private static int selectedIndex(List<ValueOption> options, String value) {
        for (int i = 0; i < options.size(); i++) {
            if (!options.get(i).custom && options.get(i).value.equals(value)) return i;
        }
        return 0;
    }

    private static String bestInitialValue(FeatureGateCatalog.Entry entry) {
        if (entry.loaded && entry.currentValue != null) return entry.currentValue;
        if (!entry.defaults.isEmpty()) return entry.defaults.get(0);
        if (!entry.researched.isEmpty()) return entry.researched.get(0);
        if (!entry.historical.isEmpty()) return entry.historical.get(0);
        if ("BOOLEAN".equals(entry.type)) return "false";
        return "";
    }

    private static boolean isSensitiveKey(String key) {
        String lower = key.toLowerCase(Locale.ROOT);
        String[] terms = {"auth", "login", "security", "risk", "compliance", "region", "age", "minor", "payment", "purchase", "wallet", "account", "ban"};
        for (String term : terms) if (lower.contains(term)) return true;
        return false;
    }

    private static String join(List<String> values) {
        if (values == null || values.isEmpty()) return "None recorded";
        StringBuilder result = new StringBuilder();
        for (String value : values) {
            if (result.length() > 0) result.append(", ");
            result.append(value);
        }
        return result.toString();
    }

    private static final class ValueOption {
        final String value;
        final String label;
        final boolean custom;

        ValueOption(String value, String label, boolean custom) {
            this.value = value;
            this.label = label;
            this.custom = custom;
        }
    }
}
