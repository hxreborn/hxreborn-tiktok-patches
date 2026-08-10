/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.diagnostics;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

import app.morphe.extension.shared.Utils;
import app.morphe.extension.shared.diagnostics.DiagnosticCategory;
import app.morphe.extension.shared.settings.preference.LogBufferManager;

public final class JavaCrashCapture {
    private static final String PREFS_NAME = "morphe_prefs";
    private static final String CAPTURE_KEY = "morphe_capture_java_crashes";
    private static final String DEBUG_KEY = "morphe_debug";
    private static final AtomicBoolean INSTALLED = new AtomicBoolean();
    private static final Object REPORT_LOCK = new Object();
    private static final int RECENT_EVENTS_MAX_CHARS = 12_000;

    private JavaCrashCapture() {
    }

    public static void initialize(Context context) {
        if (!isEnabled(context, CAPTURE_KEY)) return;
        if (!INSTALLED.compareAndSet(false, true)) return;

        Thread.UncaughtExceptionHandler original = Thread.getDefaultUncaughtExceptionHandler();
        if (original instanceof MorpheCrashHandler) return;

        Thread.setDefaultUncaughtExceptionHandler(
                new MorpheCrashHandler(context.getApplicationContext(), original)
        );
        LogBufferManager.appendEvent(
                DiagnosticCategory.SETTINGS,
                "JavaCrashCapture",
                "INFO",
                "Java crash capture installed after NpthExtent"
        );
        Log.i("morphe:JavaCrashCapture", "Java crash capture installed after NpthExtent");
    }

    private static final class MorpheCrashHandler implements Thread.UncaughtExceptionHandler {
        private final Context context;
        private final Thread.UncaughtExceptionHandler delegate;
        private final ThreadLocal<Boolean> handling = new ThreadLocal<>();

        MorpheCrashHandler(Context context, Thread.UncaughtExceptionHandler delegate) {
            this.context = context;
            this.delegate = delegate;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            if (Boolean.TRUE.equals(handling.get())) {
                if (delegate != null && delegate != this) {
                    delegate.uncaughtException(thread, throwable);
                }
                return;
            }

            handling.set(Boolean.TRUE);
            try {
                synchronized (REPORT_LOCK) {
                    LogBufferManager.persistCrashReport(
                            context,
                            buildReport(context, thread, throwable, delegate)
                    );
                }
            } catch (Throwable ignored) {
                // Crash-time code must never interfere with TikTok's handler.
            } finally {
                try {
                    if (delegate != null && delegate != this) {
                        delegate.uncaughtException(thread, throwable);
                    }
                } finally {
                    handling.remove();
                }
            }
        }
    }

    private static String buildReport(
            Context context,
            Thread thread,
            Throwable throwable,
            Thread.UncaughtExceptionHandler delegate
    ) {
        StringWriter stack = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stack));

        StringBuilder report = new StringBuilder();
        report.append("schema: 1\n")
                .append("complete: true\n")
                .append("timestamp_utc: ").append(utcNow()).append('\n')
                .append("package: ").append(context.getPackageName()).append('\n')
                .append("tiktok_version: ").append(Utils.getAppVersionName()).append('\n')
                .append("morphe_version: ").append(Utils.getPatchesReleaseVersion()).append('\n')
                .append("diagnostic_logging: ").append(isEnabled(context, DEBUG_KEY)).append('\n')
                .append("java_crash_capture: ").append(isEnabled(context, CAPTURE_KEY)).append('\n')
                .append("android_api: ").append(Build.VERSION.SDK_INT).append('\n')
                .append("abis: ").append(String.join(",", Build.SUPPORTED_ABIS)).append('\n')
                .append("process: ").append(processName(context)).append('\n')
                .append("pid: ").append(Process.myPid()).append('\n')
                .append("thread: ").append(thread.getName()).append('\n')
                .append("thread_id: ").append(thread.getId()).append('\n')
                .append("exception: ").append(throwable.getClass().getName()).append('\n')
                .append("message: ").append(safe(throwable.getMessage())).append('\n')
                .append("delegate: ").append(delegate == null ? "none" : delegate.getClass().getName())
                .append("\n\n[STACK TRACE]\n")
                .append(stack);

        String recent = LogBufferManager.snapshotForCrash(RECENT_EVENTS_MAX_CHARS);
        if (!recent.isEmpty()) {
            report.append("\n[RECENT MORPHE EVENTS]\n").append(recent);
        }
        return report.toString();
    }

    private static String processName(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        return context.getPackageName();
    }

    private static String safe(String value) {
        return value == null ? "" : value.replace('\n', ' ').replace('\r', ' ');
    }

    private static boolean isEnabled(Context context, String key) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

    private static String utcNow() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date());
    }
}
