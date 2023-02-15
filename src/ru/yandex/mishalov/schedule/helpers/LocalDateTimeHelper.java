package ru.yandex.mishalov.schedule.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class  LocalDateTimeHelper {
    protected static final String dateTimeFormat = "dd.MM.yyyy HH:mm";

    public static LocalDateTime convertToLocalDateTime(String string) {
        if(string == null || string.isBlank())
        {
            return null;
        }
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static String fromLocalDateTimeToString(LocalDateTime dateTime) {
        if(dateTime == null)
        {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalDateTime convertToLocalDateTime(String string, String dateTimeFormat) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
