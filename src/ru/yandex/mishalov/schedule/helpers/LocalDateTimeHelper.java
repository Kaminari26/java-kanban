package ru.yandex.mishalov.schedule.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class  LocalDateTimeHelper {
    protected static final String dateTimeFormat = "DD.MM.yyyy HH:mm";

    public static LocalDateTime convertToLocalDateTime(String string) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalDateTime convertToLocalDateTime(String string, String dateTimeFormat) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
