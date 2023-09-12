package de.legoshi.graphsimulator;

import de.legoshi.graphsimulator.plot.ScatterPlotBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jfree.chart3d.data.xyz.XYZSeries;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    protected void onHelloButtonClick() {
        List<XYZSeries<String>> list = new ArrayList<>();
        list.add(createSeries("Element-1"));
        list.add(createSeries("Element-2"));

        ScatterPlotBuilder.create()
                .setTitle("ScatterPlot")
                .setSubTitle("for Tim")
                .addDataSet(list)
                .openWindow();
    }

    private XYZSeries<String> createSeries(String groupName) {
        XYZSeries<String> series = new XYZSeries<>(groupName);
        for (int i = 0; i < 50; i++) {
            series.add(Math.random()*5, Math.random()*5, Math.random()*5);
        }
        return series;
    }

}