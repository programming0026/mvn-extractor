package com.juniverse.extractor.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * The Class DateUtil.
 */
public final class DateUtil {

    /**
     * Instantiates a new date util.
     */
    private DateUtil() {
    }

    /**
     * To local date time.
     *
     * @param instant the instant
     * @return the local date time
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(
                instant,
                ZoneId.systemDefault()
        );
    }

    /**
     * Format.
     *
     * @param time the time
     * @return the string
     */
    public static String format(long time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return fmt.format(Instant.ofEpochMilli(time));
    }

    /**
     * Gets the year.
     *
     * @return the year
     */
    public static int getYear() {
        return Year.now().getValue();
    }
}
