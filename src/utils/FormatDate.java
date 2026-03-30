package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDate {
    public static String formatDateTime(LocalDateTime date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return format.format(date);
    }
}
