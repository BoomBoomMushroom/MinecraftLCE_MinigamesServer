package me.dillionweaver.tumblegamemode.data;

public class TimeStuff {
    public static long getTimeMS(){
        return System.currentTimeMillis();
    }

    public static long timeSinceMS(long start){
        return getTimeMS() - start;
    }

    public static String msToFormat(long milliseconds){
        // 1 second = 1000 milliseconds
        long seconds = milliseconds / 1000;

        // remaining milliseconds after converting to seconds
        long remainingMillis = milliseconds % 1000;

        // minutes = remaining seconds divided by 60 (discarding remainder)
        long minutes = seconds / 60;

        // seconds = remaining seconds after converting to minutes
        seconds = seconds % 60;

        // format time string
        return String.format("%01d:%02d:%03d", minutes, seconds, remainingMillis);
    }
}
