package com.github.freegeese.weixin.core.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 二维码工具类
 */
public abstract class QRCodeUtils {

    // 默认宽度
    private static final int DEFAULT_WIDTH = 200;
    // 默认高度
    private static final int DEFAULT_HEIGHT = 200;
    // 默认二维码图片格式(支持格式：jpg, tiff, bmp, pcx, gif, png, ppm, tif, pgm, wbmp, jpeg, pbm)
    private static final String DEFAULT_IMAGE_FORMAT = "jpg";

    // 二维码配置参数
    private static final Map<EncodeHintType, Object> HINTS = new LinkedHashMap<>();

    static {
        // 编码
        HINTS.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // 容错等级
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 边框
        HINTS.put(EncodeHintType.MARGIN, 0);
    }

    public static byte[] get(String contents) {
        return get(contents, DEFAULT_IMAGE_FORMAT);
    }

    public static byte[] get(String contents, String imageFormat) {
        return get(contents, imageFormat, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static byte[] get(String contents, String imageFormat, int width, int height) {
        return get(contents, imageFormat, width, height, HINTS);
    }

    public static byte[] get(String contents, String imageFormat, int width, int height, Map<EncodeHintType, Object> hints) {
        ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
        write(bytesOutput, contents, imageFormat, width, height, hints);
        return bytesOutput.toByteArray();
    }

    public static void write(OutputStream out, String contents) {
        write(out, contents, DEFAULT_IMAGE_FORMAT);
    }

    public static void write(OutputStream out, String contents, String imageFormat) {
        write(out, contents, imageFormat, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static void write(OutputStream out, String contents, String imageFormat, int width, int height) {
        write(out, contents, imageFormat, width, height, HINTS);
    }

    public static void write(OutputStream out, String contents, String imageFormat, int width, int height, Map<EncodeHintType, Object> hints) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(matrix, imageFormat.toLowerCase(), out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String read(File file) {
        return readFrom(file).getText();
    }

    public static String read(URL url) {
        return readFrom(url).getText();
    }

    public static String read(byte[] bytes) {
        ByteArrayInputStream bytesInput = new ByteArrayInputStream(bytes);
        return read(bytesInput);
    }

    public static String read(InputStream input) {
        return readFrom(input).getText();
    }

    private static Result readFrom(Object input) {
        try {
            BufferedImage image = null;
            Class<?> inputClass = input.getClass();
            if (File.class.isAssignableFrom(inputClass)) {
                image = ImageIO.read((File) input);
            } else if (InputStream.class.isAssignableFrom(inputClass)) {
                image = ImageIO.read((InputStream) input);
            } else if (URL.class.isAssignableFrom(inputClass)) {
                image = ImageIO.read((URL) input);
            }
            LuminanceSource luminanceSource = new BufferedImageLuminanceSource(image);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(luminanceSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            return new MultiFormatReader().decode(binaryBitmap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
