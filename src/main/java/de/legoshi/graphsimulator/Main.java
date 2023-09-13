package de.legoshi.graphsimulator;

import de.legoshi.graphsimulator.plot.Distribution;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Application.main(args);
        
        List<Distribution> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Distribution(i, 1));
        }
    }

}