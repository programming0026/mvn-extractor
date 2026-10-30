package com.juniverse.extractor.util;

/**
 * The Class StringUtil.
 */
public final class StringUtil {

    /**
     * Instantiates a new string util.
     */
    private StringUtil() {
    }

    /**
     * Checks for text.
     *
     * @param s the s
     * @return true, if successful
     */
    public static boolean hasText(final String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * To format.
     *
     * @param bytes the bytes
     * @return the string
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

    /**
     * Gets the name.
     *
     * @param fileName the file name
     * @return the name
     */
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
