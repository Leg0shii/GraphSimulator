package de.legoshi.graphsimulator.random;

import java.util.Random;

public class Distribution {

    private final Random random;

    private final double middleValue;
    private final double standardDerivation;

    public Distribution(long seed, double middleValue, double standardDerivation) {
        this.random = new Random(seed);
        this.middleValue = middleValue;
        this.standardDerivation = standardDerivation;
    }

    public Distribution(double middleValue, double standardDerivation) {
        this.random = new Random();
        this.middleValue = middleValue;
        this.standardDerivation = standardDerivation;
    }

    public double getRandomValue() {
        return middleValue + random.nextGaussian() * standardDerivation;
    }

}
