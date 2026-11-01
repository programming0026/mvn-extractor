package com.juniverse.extractor.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {
    }

    /**
     * Convierte el instante en fecha y hora local.
     *
     * @param instant instante
     * @return fecha y hora local
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(
                instant,
                ZoneId.systemDefault()
        );
    }

    /**
     * Da formato al valor.
     *
     * @param time hora
     * @return cadena
     */
    public static String format(long time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return fmt.format(Instant.ofEpochMilli(time));
    }

    public static int getYear() {
        return Year.now().getValue();
    }
}
