package org.trhelper.service.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeFormatterUtils {

    public static String getCurrentTime(String timeFormat){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        return now.format(formatter);
    }

    //duration as "2 days 1 hour 2 mins"
    public static Duration parseDurationString(String durationStr) {
        Pattern pattern = Pattern.compile("(\\d+)\\s+(day|hour|min)");
        Matcher matcher = pattern.matcher(durationStr);

        long days = 0, hours = 0, minutes = 0;

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "day":
                    days += value;
                    break;
                case "hour":
                    hours += value;
                    break;
                case "min":
                    minutes += value;
                    break;
            }
        }

        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
    }

    public static LocalDateTime addDurationStringToLocalDateTime(String timestamp, String durationStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        Duration duration = parseDurationString(durationStr);

        return dateTime.plus(duration);
    }

    public static String formatResult(LocalDateTime result) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return result.format(formatter);
    }



}
