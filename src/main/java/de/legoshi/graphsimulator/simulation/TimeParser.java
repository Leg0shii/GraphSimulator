package de.legoshi.graphsimulator.simulation;

public class TimeParser {
    
    public static long parseToSeconds(String timeString) {
        long totalSeconds = 0;
        
        // Year to seconds
        if (timeString.contains("y")) {
            String year = timeString.split("y")[0];
            totalSeconds += Long.parseLong(year) * 365.25 * 24 * 60 * 60;
            timeString = timeString.substring(timeString.indexOf("y") + 1);
        }
        
        // Month to seconds
        if (timeString.contains("m") && !timeString.contains("h") && !timeString.contains("s")) {
            String month = timeString.split("m")[0];
            totalSeconds += Long.parseLong(month) * 30.44 * 24 * 60 * 60;
            timeString = timeString.substring(timeString.indexOf("m") + 1);
        }
        
        // Week to seconds
        if (timeString.contains("w")) {
            String week = timeString.split("w")[0];
            totalSeconds += Long.parseLong(week) * 7 * 24 * 60 * 60;
            timeString = timeString.substring(timeString.indexOf("w") + 1);
        }
        
        // Day to seconds
        if (timeString.contains("d")) {
            String day = timeString.split("d")[0];
            totalSeconds += Long.parseLong(day) * 24 * 60 * 60;
            timeString = timeString.substring(timeString.indexOf("d") + 1);
        }
        
        // Hour to seconds
        if (timeString.contains("h")) {
            String hour = timeString.split("h")[0];
            totalSeconds += Long.parseLong(hour) * 60 * 60;
            timeString = timeString.substring(timeString.indexOf("h") + 1);
        }
        
        // Minute to seconds
        if (timeString.contains("m")) {
            String minute = timeString.split("m")[0];
            totalSeconds += Long.parseLong(minute) * 60;
            timeString = timeString.substring(timeString.indexOf("m") + 1);
        }
        
        // Seconds
        if (timeString.contains("s")) {
            String second = timeString.split("s")[0];
            totalSeconds += Long.parseLong(second);
        }
        
        return totalSeconds;
    }
}