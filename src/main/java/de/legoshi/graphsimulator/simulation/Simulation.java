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
            int randomAttackCount = 2; // rangeStart + ((int) (Math.random() * rangeEnd));
            long timeToNextFailure = lastAttack + failureDistribution.getRandomValue();
            lastAttack = timeToNextFailure;
            
            long timeToRepair = timeToNextFailure + repairTimeDistribution.getRandomValue();
            timePassed = timePassed + timeToRepair;
            
            for (ConnectionSymbol connectionSymbol : connections.keySet()) {
                connectionSymbol.resetRedundancy();
            }

            int listSize = drawHandler.getConnectionSymbols().size() - 1;
            for (int i = 0; i < randomAttackCount; i++) {
                ConnectionSymbol connectionSymbol = drawHandler.getConnectionSymbols().get((int) (Math.random() * listSize));
                ConnectionStat stat = connections.get(connectionSymbol);
                
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
            
            for (ConnectionSymbol connectionSymbol : connections.keySet()) {
                ConnectionStat stat = connections.get(connectionSymbol);
                connectionSymbol.setRedundancy(connectionSymbol.getRedundancy()-stat.getDestroyedCount(timeToNextFailure, timeToRepair));
            }
            
            Set<NetworkSymbol> connectedNetworks = getAllConnectedNodes();
            List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
            for (NetworkSymbol network : allNodes) {
                if (!connectedNetworks.contains(network)) {
                    network.setDestroyedTime(network.getDestroyedTime() + (timeToRepair - timeToNextFailure));
                }
            }
        }
    }
    
    private String compileResults() {
        String result = "";
        List<NetworkSymbol> allNodes = drawHandler.getAllNetworkSymbols();
        for (NetworkSymbol network : allNodes) {
            result = result + "" +network.getName() + ";" + failureDistribution.getMean() + ";"
                + failureDistribution.getStandardDeviation() + ";" + repairTimeDistribution.getMean() + ";"
                + repairTimeDistribution.getStandardDeviation() + ";"
                + (100.0-((double)network.getDestroyedTime()/(double)runTime)*100.0) + "%" + "\n";
        }
        return result;
    }
    
    private Set<NetworkSymbol> getAllConnectedNodes() {
        NetworkSymbol symbol = getCurrentlyActiveNetwork();
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
        for (int i = 0; i < symbols.size()-1; i++) {
            Set<NetworkSymbol> connectedNetworks = findAllConnectedNetworks(symbols.get(i));
            if (connectedNetworks.contains(symbols.get(i+1))) {
                return symbols.get(i);
            }
        }
        return symbols.get(symbols.size()-1);
    }
    
}
