package com.juniverse.extractor.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static boolean hasText(final String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Da formato al valor.
     *
     * @param bytes bytes
     * @return cadena
     */
    public static String toFormat(long bytes) {
        if (bytes < 0) {
            return "-";
        }
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        return String.format("%.1f %cB", bytes / Math.pow(1024, exp), "KMGTPE".charAt(exp - 1));
    }

    public static String getName(String fileName) {
        if (!hasText(fileName)) {
            return fileName;
        }

        int index = fileName.lastIndexOf('.');

        if (index > 0) {
            return fileName.substring(0, index);
        }

        return fileName;
    }
}
