package de.legoshi.graphsimulator.test;

import de.legoshi.graphsimulator.plot.ScatterPlotBuilder;
import de.legoshi.graphsimulator.plot.Distribution;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jfree.chart3d.data.xyz.XYZSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMain {

    public static class TestApplication extends Application {

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) {
            // TEST: Here

            printDistribution();
            // showExamplePlot();
        }
    }

    public static void main(String[] args) {
        TestApplication.main(args);
    }

    public static void printDistribution() {
        HashMap<Integer, Integer> map = new HashMap<>();
        Distribution distribution = new Distribution(10, 1);
        for (int i = 0; i < 100000; i++) {
            int randValue = (int) Math.round(distribution.getRandomValue());
            map.put(randValue, map.getOrDefault(randValue, 0)+1);
        }

        for (int key : map.keySet()) {
            System.out.println(key + ": " + map.get(key) + "x");
        }
    }

    private static void showExamplePlot() {
        List<XYZSeries<String>> list = new ArrayList<>();
        list.add(createSeries("Element-1"));
        list.add(createSeries("Element-2"));

        ScatterPlotBuilder.create()
                .setTitle("ScatterPlot")
                .setSubTitle("for Tim")
                .addDataSet(list)
                .openWindow();
    }

    private static XYZSeries<String> createSeries(String groupName) {
        XYZSeries<String> series = new XYZSeries<>(groupName);
        for (int i = 0; i < 50; i++) {
            series.add(Math.random() * 5, Math.random() * 5, Math.random() * 5);
        }
        return series;
    }

}
