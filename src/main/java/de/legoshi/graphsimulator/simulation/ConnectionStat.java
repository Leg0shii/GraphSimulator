package de.legoshi.graphsimulator.simulation;

import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import lombok.Getter;

import java.util.*;

public class ConnectionStat {
    
    private ConnectionSymbol connectionSymbol;
    @Getter
    private final HashMap<Integer, List<Duration>> offlineMap;
    
    public ConnectionStat(ConnectionSymbol connectionSymbol) {
        this.connectionSymbol = connectionSymbol;
        this.offlineMap = new HashMap<>();
        for (int i = 0; i < connectionSymbol.getRedundancy(); i++) {
            offlineMap.put(i, new ArrayList<>());
        }
    }
    
    public void addOfflineTime(long startTime, long duration, int connectionID) {
        List<Duration> list = offlineMap.getOrDefault(connectionID, new ArrayList<>());
        list.add(new Duration(startTime, duration));
        offlineMap.put(connectionID, list);
    }
    
    public int getDestroyedCount(long start, long end) {
        int destroyed = 0;
        for (int i = 0; i < offlineMap.size(); i++) {
            for (Duration duration : offlineMap.get(i)) {
                if (duration.start <= start && duration.end >= end) {
                    destroyed++;
                }
            }
        }
        return destroyed;
    }
    
    public int getNotDestroyedID(long start, long end) {
        outer:
        for (int i = 0; i < offlineMap.size(); i++) {
            if (offlineMap.get(i).size() == 0) {
                return i;
            }
            for (Duration duration : offlineMap.get(i)) {
                if (duration.start <= start && duration.end >= end) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
    
    public long getOfflineStart(int id, int index) {
        return offlineMap.get(id).get(index).start;
    }
    
    public long getOfflineDuration(int id, int index) {
        return offlineMap.get(id).get(index).end - getOfflineStart(id, index);
    }
    
    public static class Duration implements Comparable<Duration> {
    
        public long start, end;
    
        public Duration(long start, long end) {
            this.start = start;
            this.end = end;
        }
    
        @Override
        public int compareTo(Duration other) {
            return Long.compare(this.start, other.start);
        }
    
        public static List<Duration> getOverlappingDurations(List<Duration> durations) {
            if (durations == null || durations.isEmpty()) {
                return new ArrayList<>();
            }
        
            // Sort the durations based on their start time
            durations.sort(Comparator.comparingLong(a -> a.start));
        
            List<Duration> mergedDurations = new ArrayList<>();
            Duration currentDuration = durations.get(0);
        
            for (int i = 1; i < durations.size(); i++) {
                // If the current duration overlaps with the next one
                if (currentDuration.end >= durations.get(i).start) {
                    currentDuration.end = Math.max(currentDuration.end, durations.get(i).end);
                } else {
                    mergedDurations.add(currentDuration);
                    currentDuration = durations.get(i);
                }
            }
        
            mergedDurations.add(currentDuration);
            return mergedDurations;
        }
    
        public static Triple<Duration, String, ConnectionSymbol> getNextDuration(TreeMap<Duration, ConnectionSymbol> durMap, long value) {
            long closestValue = Long.MAX_VALUE;
            Duration closestDuration = null;
            String type = null;
            ConnectionSymbol symbol = null;
        
            for (Map.Entry<Duration, ConnectionSymbol> entry : durMap.entrySet()) {
                Duration duration = entry.getKey();
                if (duration.start > value && duration.start < closestValue) {
                    closestValue = duration.start;
                    closestDuration = duration;
                    type = "start";
                    symbol = entry.getValue();
                }
                if (duration.end > value && duration.end < closestValue) {
                    closestValue = duration.end;
                    closestDuration = duration;
                    type = "ende";
                    symbol = entry.getValue();
                }
            }
        
            if (type != null && type.equals("ende")) {
                durMap.remove(closestDuration);  // Entferne die Duration aus der TreeMap
            }
        
            return closestDuration != null ? new Triple<>(closestDuration, type, symbol) : null;
        }
    
        public static class Triple<K, V, W> {
            K key;
            V value;
            W third;
        
            public Triple(K key, V value, W third) {
                this.key = key;
                this.value = value;
                this.third = third;
            }
        
            @Override
            public String toString() {
                return "(" + key + ", " + value + ", " + third + ")";
            }
        }
    
    }
}
