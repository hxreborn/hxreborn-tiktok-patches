/*
 * Copyright 2026 icysymmetra/tiktok-patches-for-morphe contributors
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.shared.diagnostics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DiagnosticEvent {
    public final DiagnosticCategory category;
    public final long timestamp;
    public final String thread;
    public final String source;
    public final String level;
    public final String message;

    public DiagnosticEvent(
            DiagnosticCategory category,
            long timestamp,
            String thread,
            String source,
            String level,
            String message
    ) {
        this.category = category;
        this.timestamp = timestamp;
        this.thread = thread;
        this.source = source;
        this.level = level;
        this.message = message;
    }

    public String format() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return category.value + " | " + format.format(new Date(timestamp))
                + " | " + thread + " | " + source + " | " + level + " | " + message;
    }
}
