/*
 * Copyright (c) 2026 Metra TikTok Patches
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 */
package app.morphe.extension.tiktok.download;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/** Converts TikTok's animated WebP sticker frames to a motion-preserving APNG. */
final class AnimatedWebpApngConverter {
    private static final byte[] PNG_SIGNATURE = new byte[]{
            (byte) 0x89, 'P', 'N', 'G', 0x0d, 0x0a, 0x1a, 0x0a
    };
    private static final int MAX_CHUNK_DATA = 64 * 1024;

    private AnimatedWebpApngConverter() {
    }

    static void convert(byte[] webpData, OutputStream outputStream) throws Exception {
        Object image = null;
        try {
            Class<?> imageClass = Class.forName("com.facebook.animated.webp.WebPImage");
            Method create = imageClass.getDeclaredMethod("create", byte[].class);
            create.setAccessible(true);
            image = create.invoke(null, (Object) webpData);
            if (image == null) throw new IllegalStateException("WebP decoder returned null");

            int width = invokeInt(image, "getWidth");
            int height = invokeInt(image, "getHeight");
            int frameCount = invokeInt(image, "getFrameCount");
            int[] durations = (int[]) invoke(image, "getFrameDurations");
            if (width <= 0 || height <= 0 || frameCount <= 0) {
                throw new IllegalStateException("Invalid animated WebP dimensions or frame count");
            }

            DataOutputStream output = new DataOutputStream(outputStream);
            output.write(PNG_SIGNATURE);
            writeIhdr(output, width, height);
            writeActl(output, frameCount, 0);

            int sequence = 0;
            for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
                Object frame = null;
                Bitmap bitmap = null;
                Bitmap canvasBitmap = null;
                try {
                    frame = invokeFrame(image, frameIndex);
                    int frameWidth = invokeInt(frame, "getWidth");
                    int frameHeight = invokeInt(frame, "getHeight");
                    int xOffset = invokeInt(frame, "getXOffset");
                    int yOffset = invokeInt(frame, "getYOffset");
                    validateFrame(width, height, frameWidth, frameHeight, xOffset, yOffset);

                    int durationMs = durations != null && frameIndex < durations.length
                            ? durations[frameIndex]
                            : 100;
                    boolean blend = invokeBoolean(frame, "LIZ");
                    boolean disposeToBackground = invokeBoolean(frame, "LIZIZ");

                    bitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
                    invoke(frame, "renderFrame", new Class<?>[]{
                            int.class, int.class, Bitmap.class
                    }, frameWidth, frameHeight, bitmap);

                    Bitmap encodedBitmap = bitmap;
                    int encodedWidth = frameWidth;
                    int encodedHeight = frameHeight;
                    int encodedXOffset = xOffset;
                    int encodedYOffset = yOffset;
                    boolean encodedBlend = blend;

                    // PNG's first IDAT uses the IHDR canvas dimensions. Expand a WebP
                    // subframe to that canvas so the generated APNG remains valid.
                    if (frameIndex == 0 && (frameWidth != width || frameHeight != height
                            || xOffset != 0 || yOffset != 0)) {
                        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        new Canvas(canvasBitmap).drawBitmap(bitmap, xOffset, yOffset, null);
                        encodedBitmap = canvasBitmap;
                        encodedWidth = width;
                        encodedHeight = height;
                        encodedXOffset = 0;
                        encodedYOffset = 0;
                        encodedBlend = false;
                    }

                    writeFctl(
                            output,
                            sequence++,
                            encodedWidth,
                            encodedHeight,
                            encodedXOffset,
                            encodedYOffset,
                            durationMs,
                            disposeToBackground,
                            encodedBlend
                    );
                    byte[] compressed = compressBitmap(encodedBitmap);

                    if (frameIndex == 0) {
                        writeChunkedIdat(output, compressed);
                    } else {
                        sequence = writeChunkedFdat(output, compressed, sequence);
                    }
                } finally {
                    if (canvasBitmap != null) canvasBitmap.recycle();
                    if (bitmap != null) bitmap.recycle();
                    dispose(frame);
                }
            }

            writeChunk(output, "IEND", new byte[0]);
            output.flush();
        } finally {
            dispose(image);
        }
    }

    private static Object invokeFrame(Object image, int index) throws Exception {
        for (Method method : image.getClass().getMethods()) {
            if (!"getFrame".equals(method.getName()) || method.getParameterTypes().length != 1) continue;
            if (!method.getReturnType().getName().endsWith("WebPFrame")) continue;
            method.setAccessible(true);
            return method.invoke(image, index);
        }
        throw new NoSuchMethodException("WebPImage.getFrame(int)");
    }

    private static Object invoke(Object target, String methodName) throws Exception {
        return invoke(target, methodName, new Class<?>[0]);
    }

    private static Object invoke(
            Object target,
            String methodName,
            Class<?>[] parameterTypes,
            Object... arguments
    ) throws Exception {
        Method method = target.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(target, arguments);
    }

    private static int invokeInt(Object target, String methodName) throws Exception {
        return ((Number) invoke(target, methodName)).intValue();
    }

    private static boolean invokeBoolean(Object target, String methodName) throws Exception {
        return (Boolean) invoke(target, methodName);
    }

    private static void dispose(Object target) {
        if (target == null) return;
        try {
            invoke(target, "dispose");
        } catch (Throwable ignored) {
            // Native resources are also finalized by Fresco; explicit disposal is best effort.
        }
    }

    private static void validateFrame(
            int canvasWidth,
            int canvasHeight,
            int frameWidth,
            int frameHeight,
            int xOffset,
            int yOffset
    ) {
        if (frameWidth <= 0 || frameHeight <= 0 || xOffset < 0 || yOffset < 0
                || xOffset + frameWidth > canvasWidth || yOffset + frameHeight > canvasHeight) {
            throw new IllegalStateException("Animated WebP frame is outside its canvas");
        }
    }

    private static byte[] compressBitmap(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
        try (DeflaterOutputStream deflaterOutput = new DeflaterOutputStream(compressed, deflater, 16 * 1024)) {
            int width = bitmap.getWidth();
            int[] pixels = new int[width];
            byte[] row = new byte[1 + width * 4];
            for (int y = 0; y < bitmap.getHeight(); y++) {
                bitmap.getPixels(pixels, 0, width, 0, y, width, 1);
                row[0] = 0;
                int offset = 1;
                for (int pixel : pixels) {
                    row[offset++] = (byte) ((pixel >>> 16) & 0xff);
                    row[offset++] = (byte) ((pixel >>> 8) & 0xff);
                    row[offset++] = (byte) (pixel & 0xff);
                    row[offset++] = (byte) ((pixel >>> 24) & 0xff);
                }
                deflaterOutput.write(row);
            }
        } finally {
            deflater.end();
        }
        return compressed.toByteArray();
    }

    private static void writeIhdr(DataOutputStream output, int width, int height) throws Exception {
        ByteArrayOutputStream data = new ByteArrayOutputStream(13);
        DataOutputStream chunk = new DataOutputStream(data);
        chunk.writeInt(width);
        chunk.writeInt(height);
        chunk.writeByte(8);
        chunk.writeByte(6);
        chunk.writeByte(0);
        chunk.writeByte(0);
        chunk.writeByte(0);
        writeChunk(output, "IHDR", data.toByteArray());
    }

    private static void writeActl(DataOutputStream output, int frameCount, int playCount) throws Exception {
        ByteArrayOutputStream data = new ByteArrayOutputStream(8);
        DataOutputStream chunk = new DataOutputStream(data);
        chunk.writeInt(frameCount);
        chunk.writeInt(playCount);
        writeChunk(output, "acTL", data.toByteArray());
    }

    private static void writeFctl(
            DataOutputStream output,
            int sequence,
            int width,
            int height,
            int xOffset,
            int yOffset,
            int durationMs,
            boolean disposeToBackground,
            boolean blend
    ) throws Exception {
        int safeDuration = Math.max(1, durationMs);
        int delayNumerator = safeDuration;
        int delayDenominator = 1000;
        while (delayNumerator > 0xffff) {
            delayNumerator = (delayNumerator + 1) / 2;
            delayDenominator = Math.max(1, delayDenominator / 2);
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream(26);
        DataOutputStream chunk = new DataOutputStream(data);
        chunk.writeInt(sequence);
        chunk.writeInt(width);
        chunk.writeInt(height);
        chunk.writeInt(xOffset);
        chunk.writeInt(yOffset);
        chunk.writeShort(delayNumerator);
        chunk.writeShort(delayDenominator);
        chunk.writeByte(disposeToBackground ? 1 : 0);
        chunk.writeByte(blend ? 1 : 0);
        writeChunk(output, "fcTL", data.toByteArray());
    }

    private static void writeChunkedIdat(DataOutputStream output, byte[] compressed) throws Exception {
        for (int offset = 0; offset < compressed.length; offset += MAX_CHUNK_DATA) {
            int length = Math.min(MAX_CHUNK_DATA, compressed.length - offset);
            byte[] data = new byte[length];
            System.arraycopy(compressed, offset, data, 0, length);
            writeChunk(output, "IDAT", data);
        }
    }

    private static int writeChunkedFdat(
            DataOutputStream output,
            byte[] compressed,
            int sequence
    ) throws Exception {
        for (int offset = 0; offset < compressed.length; offset += MAX_CHUNK_DATA) {
            int length = Math.min(MAX_CHUNK_DATA, compressed.length - offset);
            ByteArrayOutputStream data = new ByteArrayOutputStream(length + 4);
            DataOutputStream chunk = new DataOutputStream(data);
            chunk.writeInt(sequence++);
            chunk.write(compressed, offset, length);
            writeChunk(output, "fdAT", data.toByteArray());
        }
        return sequence;
    }

    private static void writeChunk(DataOutputStream output, String type, byte[] data) throws Exception {
        byte[] typeBytes = type.getBytes(StandardCharsets.US_ASCII);
        CRC32 crc = new CRC32();
        crc.update(typeBytes);
        crc.update(data);

        output.writeInt(data.length);
        output.write(typeBytes);
        output.write(data);
        output.writeInt((int) crc.getValue());
    }
}
