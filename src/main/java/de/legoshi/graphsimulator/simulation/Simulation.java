package de.legoshi.graphsimulator.simulation;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import de.legoshi.graphsimulator.plot.Distribution;

import java.util.*;

public class Simulation {
    
    private final DrawHandler drawHandler;
    
    private final HashMap<ConnectionSymbol, ConnectionStat> connections;
    private final Distribution failureDistribution;
    private final Distribution repairTimeDistribution;
    
    private final long runTime;
    private final int rangeStart;
    private final int rangeEnd;
    
    public Simulation(DrawHandler drawHandler, Distribution offlineDistribution, Distribution onlineDistribution,
                      long runTime, int rangeStart, int rangeEnd) {
        this.drawHandler = drawHandler;
        this.failureDistribution = offlineDistribution;
        this.repairTimeDistribution = onlineDistribution;
        this.runTime = runTime;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        connections = new HashMap<>();
        loadConnections();
    }
    
    private void run() {
        resetNodes();
        applyDistributionToConnection();
        applyStatus();
        flattenDuration();
        calculateDestroyTime();
    }
    
    // name;meandes;stddes;meanrep;stdrep;available
    public String start() {
        run();
        return compileResults();
    }
    
    // name;destroyStart;destroyEnd
    public String startSingle() {
        run();
        return compileSingleResult();
    }
    
    private void resetNodes() {
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            network.setDestroyedTime(0);
            network.setDestroyedTimes(new ArrayList<>());
        }
        for (ConnectionSymbol connection : connections.keySet()) {
            connection.resetRedundancy();
        }
    }
    
    private void applyDistributionToConnection() {
        long timeToNextFailure = 0;
        long lastAttack = 0;
        while (timeToNextFailure < runTime) {
            int randomAttackCount = 1; // rangeStart + ((int) (Math.random() * rangeEnd));
            timeToNextFailure = lastAttack + failureDistribution.getRandomValue();
            lastAttack = timeToNextFailure;
            
            long timeToRepair = timeToNextFailure + repairTimeDistribution.getRandomValue();
            
            if (timeToRepair > runTime) {
                timeToRepair = runTime;
            }
            
            if (timeToNextFailure > runTime) {
                timeToNextFailure = runTime;
            }
            
            int listSize = drawHandler.getConnectionSymbols().size() - 1;
            for (int i = 0; i < randomAttackCount; i++) {
                ConnectionSymbol connectionSymbol = drawHandler.getConnectionSymbols().get((int) (Math.random() * listSize));
                ConnectionStat stat = connections.get(connectionSymbol);
                System.out.println("ATTACKED: " + connectionSymbol.getStartSymbol().getName() + " -> " + connectionSymbol.getEndSymbol().getName() + "[" + timeToNextFailure + "-" + timeToRepair + "]");
                
                if (stat != null) {
                    int currentlyNotDestroyed = stat.getNotDestroyedID(timeToNextFailure, timeToRepair);
                    if (currentlyNotDestroyed == -1) {
                        continue;
                    }
                }
                
                if (stat == null) {
                    stat = new ConnectionStat(connectionSymbol);
                    connections.put(connectionSymbol, stat);
                }
                
                int currentlyNotDestroyed = stat.getNotDestroyedID(timeToNextFailure, timeToRepair);
                stat.addOfflineTime(timeToNextFailure, timeToRepair, currentlyNotDestroyed);
            }
        }
    }
    
    private void applyStatus() {
        HashMap<ConnectionStat.Duration, ConnectionSymbol> durMap = new HashMap<>();
        for (ConnectionSymbol connectionSymbol : connections.keySet()) {
            ConnectionStat stat = connections.get(connectionSymbol);
            HashMap<Integer, List<ConnectionStat.Duration>> map = stat.getOfflineMap();
            if (map == null) continue;
            
            List<ConnectionStat.Duration> list = map.get(0);
            if (list == null) continue;
            
            for (ConnectionStat.Duration duration : list) {
                durMap.put(duration, connectionSymbol);
            }
        }
        
        TreeMap<ConnectionStat.Duration, ConnectionSymbol> treeMap = new TreeMap<>(durMap);
        String type = "start";
        String prevType;
        ConnectionSymbol connectionSymbol = null;
        ConnectionSymbol prevSymbol;
        
        ConnectionStat.Duration.Triple<ConnectionStat.Duration, String, ConnectionSymbol> triple = ConnectionStat.Duration.getNextDuration(treeMap, 0);
        long periodEnd;
        long periodStart = 0;
        do {
            if (triple != null) {
                prevSymbol = connectionSymbol;
                connectionSymbol = triple.third;
                prevType = type;
                type = triple.value;
                
                if (prevType.equals("start") && prevSymbol != null) {
                    prevSymbol.setRedundancy(0);
                    periodEnd = triple.key.start;
                } else if (prevType.equals("end") && prevSymbol != null) {
                    prevSymbol.setRedundancy(1);
                    periodEnd = triple.key.end;
                } else if (prevType.equals("start")) {
                    periodEnd = triple.key.start;
                } else {
                    periodEnd = triple.key.end;
                }
                
                if (periodStart >= periodEnd) continue;
                System.out.println("STEP: " + type + " " + periodStart + " " + periodEnd);
                
                Set<NetworkSymbol> connectedNetworks = getAllConnectedNodes();
                boolean debug = connectedNetworks.size() != 7;
                if (debug) System.out.println("Connected Symbol count: " + connectedNetworks.size());
                
                List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
                for (NetworkSymbol network : allNodes) {
                    if (!connectedNetworks.contains(network)) {
                        if (debug)
                            System.out.println("network " + network.getName() + " destroyed: " + periodStart + " " + periodEnd);
                        // network.getDestroyedTimes().add(new ConnectionStat.Duration(periodStart, periodEnd));
                        network.setDestroyedTime(Math.abs(periodEnd - periodStart));
                    }
                }
                
                periodStart = periodEnd;
                triple = ConnectionStat.Duration.getNextDuration(treeMap, periodEnd);
            }
        } while (triple != null);
    }
    
    private void flattenDuration() {
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            List<ConnectionStat.Duration> resDurations = ConnectionStat.Duration.getOverlappingDurations(network.getDestroyedTimes());
            network.setDestroyedTimes(resDurations);
        }
    }
    
    private void calculateDestroyTime() {
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            for (ConnectionStat.Duration duration : network.getDestroyedTimes()) {
                network.setDestroyedTime(network.getDestroyedTime() + duration.end - duration.start);
            }
            System.out.println("NETWORK: " + network.getName() + " " + network.getDestroyedTime());
        }
    }
    
    private String compileResults() {
        String result = "";
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            result = result + ""
                + network.getName() + ";"
                + failureDistribution.getMean() + ";"
                + failureDistribution.getStandardDeviation() + ";"
                + repairTimeDistribution.getMean() + ";"
                + repairTimeDistribution.getStandardDeviation() + ";"
                + (100.0-((double)network.getDestroyedTime()/(double)runTime)*100.0) + "%" + "\n";
        }
        return result;
    }
    
    private String compileSingleResult() {
        String result = "";
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            for (ConnectionStat.Duration duration : network.getDestroyedTimes()) {
                result = result + "" + network.getName() + ";" + duration.start + ";" + duration.end + "\n";
            }
        }
        return result;
    }
    
    private Set<NetworkSymbol> getAllConnectedNodes() {
        NetworkSymbol symbol = getCurrentlyActiveNetwork();
        // System.out.println("ACTIVE: " + symbol.getName() +"\n");
        return new HashSet<>(findAllConnectedNetworks(symbol));
    }
    
    public static Set<NetworkSymbol> findAllConnectedNetworks(NetworkSymbol startingNetwork) {
        Set<NetworkSymbol> visitedNetworks = new HashSet<>();
        Stack<NetworkSymbol> stack = new Stack<>();
        
        if (startingNetwork == null) {
            return visitedNetworks;
        }
        
        stack.push(startingNetwork);
        
        while (!stack.isEmpty()) {
            NetworkSymbol currentNetwork = stack.pop();
            if (!visitedNetworks.contains(currentNetwork)) {
                visitedNetworks.add(currentNetwork);
                
                for (ConnectionSymbol connection : currentNetwork.getConnectionSymbols()) {
                    if (connection.getRedundancy() > 0) {
                        NetworkSymbol adjacentNetwork = connection.getOtherEnd(currentNetwork);
                        if (!visitedNetworks.contains(adjacentNetwork)) {
                            stack.push(adjacentNetwork);
                        }
                    }
                }
            }
        }
        
        return visitedNetworks;
    }
    
    private void loadConnections() {
        drawHandler.getConnectionSymbols().forEach(cs -> connections.put(cs, new ConnectionStat(cs)));
    }
    
    private NetworkSymbol getCurrentlyActiveNetwork() {
        List<NetworkSymbol> symbols = drawHandler.getAllImportantNetworkSymbols();
        symbols.sort(Comparator.comparingInt(NetworkSymbol::getPriority));
        for (int i = 0; i < symbols.size() - 1; i++) {
            Set<NetworkSymbol> connectedNetworks = findAllConnectedNetworks(symbols.get(i));
            if (connectedNetworks.contains(symbols.get(i + 1))) {
                return symbols.get(i);
            }
        }
        return symbols.get(symbols.size() - 1);
    }
    
}
