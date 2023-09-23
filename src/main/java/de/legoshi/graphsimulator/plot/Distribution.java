package de.legoshi.graphsimulator.plot;

import lombok.Getter;

import java.util.Random;

public class Distribution {

    private final Random random;

    @Getter private final double mean;
    @Getter private final double standardDeviation;

    public Distribution(long seed, double mean, double standardDeviation) {
        this.random = new Random(seed);
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public Distribution(double mean, double standardDeviation) {
        this.random = new Random();
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public int getRandomValue() {
        int result = (int) Math.round(mean + random.nextGaussian() * standardDeviation);
        return result <= 0 ? result * (-1) : result;
    }

}
