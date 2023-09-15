package de.legoshi.graphsimulator.plot;

import lombok.Getter;

import java.util.Random;

public class Distribution {

    private final Random random;

    @Getter private final double middleValue;
    @Getter private final double standardDerivation;

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

    public int getRandomValue() {
        return (int) Math.round(middleValue + random.nextGaussian() * standardDerivation);
    }

}