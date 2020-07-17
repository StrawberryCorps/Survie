package bzh.strawberry.survie.utils;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*
 * This file (TimeUtils) is part of a project Survie.
 * It was created on 17/07/2020 20:13 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class TimeUtils {

    private final static Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
            + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
            + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
            + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);

    public static String getDuration(final long futureTimestamp) {
        int seconds = (int) futureTimestamp;

        final List<String> item = new ArrayList<String>();

        int months = 0;
        while (seconds >= 2678400) {
            months++;
            seconds -= 2678400;
        }
        if (months > 0) {
            item.add(months + " mois");
        }

        int days = 0;
        while (seconds >= 86400) {
            days++;
            seconds -= 86400;
        }
        if (days > 0) {
            item.add(days + " jours");
        }

        int hours = 0;
        while (seconds >= 3600) {
            hours++;
            seconds -= 3600;
        }
        if (hours > 0) {
            item.add(hours + " heures");
        }

        int mins = 0;
        while (seconds >= 60) {
            mins++;
            seconds -= 60;
        }
        if (mins > 0) {
            item.add(mins + " minutes");
        }

        if (seconds > 0) {
            item.add(seconds + " secondes");
        }

        return Joiner.on(", ").join(item);
    }

    public static String getTime(long seconds) {
        long sec = seconds;
        List<String> item = new ArrayList<>();
        int hours = 0;
        while (seconds >= 3600) {
            hours++;
            seconds -= 3600;
        }
        if (hours > 0) {
            if (hours < 10) {
                item.add("0" + hours);
            } else {
                item.add(hours + "");
            }
        }

        int mins = 0;
        while (seconds >= 60) {
            mins++;
            seconds -= 60;
        }
        if (mins > 0) {
            if (mins < 10) {
                item.add("0" + mins);
            } else {
                item.add(mins + "");
            }
        }

        if (seconds > 0) {
            if (seconds < 10) {
                item.add("0" + seconds);
            } else {
                item.add(seconds + "");
            }
        }
        return Joiner.on(":").join(item);
    }

}
