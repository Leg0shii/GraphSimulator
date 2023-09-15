package de.legoshi.graphsimulator.simulation;

import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionStat {
    
    private ConnectionSymbol connectionSymbol;
    @Getter private final HashMap<Integer, List<Duration>> offlineMap;
    
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
        outer: for (int i = 0; i < offlineMap.size(); i++) {
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
    
    public static class Duration {
        
        public long start, end;
        
        public Duration(long start, long end) {
            this.start = start;
            this.end = end;
        }
        
    }
    
}
