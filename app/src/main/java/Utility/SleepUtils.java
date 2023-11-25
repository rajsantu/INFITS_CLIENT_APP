package Utility;

import java.util.Locale;

public class SleepUtils {

    public static String formatSleepDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String formattedDuration;
        if (hours > 0) {
            if (minutes > 0) {
                formattedDuration = String.format(Locale.ENGLISH, "%d hr %d mins", hours, minutes);
            } else {
                formattedDuration = String.format(Locale.ENGLISH, "%d hr", hours);
            }
        } else {
            formattedDuration = String.format(Locale.ENGLISH, "%d mins", minutes);
        }

        return formattedDuration;
    }
}
