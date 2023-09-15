package de.legoshi.graphsimulator.simulation;

import org.apache.commons.math3.special.Erf;

public class StdCalc {
    
    public static double calc(double meanDistance, double percentage) {
        return meanDistance / (2 * Math.sqrt(2) * Erf.erfInv((1 - (percentage / 2))));
    }
    
}
