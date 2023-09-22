package de.legoshi.graphsimulator.simulation;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import de.legoshi.graphsimulator.plot.Distribution;

import java.util.*;

public class Simulation {
    
    private final DrawHandler drawHandler;
    
    private final HashMap<ConnectionSymbol, ConnectionStat> connections;
    private final Distribution offlineDistribution;
    private final Distribution onlineDistribution;
    
    private final long runTime;
    private final int rangeStart;
    private final int rangeEnd;
    
    public Simulation(DrawHandler drawHandler, Distribution offlineDistribution, Distribution onlineDistribution,
                      long runTime, int rangeStart, int rangeEnd) {
        this.drawHandler = drawHandler;
        this.offlineDistribution = offlineDistribution;
        this.onlineDistribution = onlineDistribution;
        this.runTime = runTime;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        connections = new HashMap<>();
        loadConnections();
    }
    
    // name;meandes;stddes;meanrep;stdrep;available
    public String start() {
        resetNodes();
        applyDistributionToConnection();
        return compileResults();
    }
    
    private void resetNodes() {
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            network.setDestroyedTime(0);
        }
    }
    
    private void applyDistributionToConnection() {
        long timePassed = 0;
        long lastAttack = 0;
        while (timePassed < runTime) {
            int randomAttackCount = 1; // rangeStart + ((int) (Math.random() * rangeEnd));
            long attackStart = lastAttack + offlineDistribution.getRandomValue();
            lastAttack = attackStart;
            
            long repairTime = attackStart + onlineDistribution.getRandomValue();
            timePassed = timePassed + repairTime;
            
            for (ConnectionSymbol connectionSymbol : connections.keySet()) {
                connectionSymbol.resetRedundancy();
            }
            
            int listSize = drawHandler.getConnectionSymbols().size()-1;
            for (int i = 0; i < randomAttackCount; i++) {
                ConnectionSymbol connectionSymbol = drawHandler.getConnectionSymbols().get((int) (Math.random() * listSize));
                ConnectionStat stat = connections.get(connectionSymbol);
                
                if (stat != null) {
                    int currentlyNotDestroyed = stat.getNotDestroyedID(attackStart, repairTime);
                    if (currentlyNotDestroyed == -1) {
                        continue;
                    }
                }
                
                if (stat == null) {
                    stat = new ConnectionStat(connectionSymbol);
                    connections.put(connectionSymbol, stat);
                }
    
                int currentlyNotDestroyed = stat.getNotDestroyedID(attackStart, repairTime);
                stat.addOfflineTime(attackStart, repairTime, currentlyNotDestroyed);
            }
            
            for (ConnectionSymbol connectionSymbol : connections.keySet()) {
                ConnectionStat stat = connections.get(connectionSymbol);
                connectionSymbol.setRedundancy(connectionSymbol.getRedundancy()-stat.getDestroyedCount(attackStart, repairTime));
            }
            
            Set<NetworkSymbol> connectedNetworks = getAllConnectedNodes();
            List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
            for (NetworkSymbol network : allNodes) {
                if (!connectedNetworks.contains(network)) {
                    network.setDestroyedTime(network.getDestroyedTime() + (repairTime - attackStart));
                }
            }
        }
    }
    
    private String compileResults() {
        String result = "";
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            if (network.isImportant()) continue;
            result = result + "" +network.getName() + ";" + offlineDistribution.getMiddleValue() + ";"
                + offlineDistribution.getStandardDerivation() + ";" + onlineDistribution.getMiddleValue() + ";"
                + onlineDistribution.getStandardDerivation() + ";"
                + (100.0-((double)network.getDestroyedTime()/(double)runTime)*100.0) + "%" + "\n";
        }
        return result;
    }
    
    private Set<NetworkSymbol> getAllConnectedNodes() {
        List<NetworkSymbol> symbols = drawHandler.getAllImportantNetworkSymbols();
        Set<NetworkSymbol> connectedNetworks = new HashSet<>();
        for (NetworkSymbol importantSymbol : symbols) {
            connectedNetworks.addAll(findAllConnectedNetworks(importantSymbol));
        }
        return connectedNetworks;
    }
    
    public static Set<NetworkSymbol> findAllConnectedNetworks(NetworkSymbol startingNetwork) {
        Set<NetworkSymbol> visitedNetworks = new HashSet<>();
        Stack<NetworkSymbol> stack = new Stack<>();
        
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
        
        visitedNetworks.remove(startingNetwork);
        return visitedNetworks;
    }
    
    private void loadConnections() {
        drawHandler.getConnectionSymbols().forEach(cs -> connections.put(cs, new ConnectionStat(cs)));
    }
    
    public String secondsToTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
}
