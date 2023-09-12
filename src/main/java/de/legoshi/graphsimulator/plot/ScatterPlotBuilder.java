package de.legoshi.graphsimulator.plot;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jfree.chart3d.Chart3D;
import org.jfree.chart3d.Chart3DFactory;
import org.jfree.chart3d.Colors;
import org.jfree.chart3d.data.xyz.XYZDataset;
import org.jfree.chart3d.data.xyz.XYZSeries;
import org.jfree.chart3d.data.xyz.XYZSeriesCollection;
import org.jfree.chart3d.fx.Chart3DViewer;
import org.jfree.chart3d.graphics3d.Dimension3D;
import org.jfree.chart3d.graphics3d.ViewPoint3D;
import org.jfree.chart3d.label.StandardXYZLabelGenerator;
import org.jfree.chart3d.plot.XYZPlot;
import org.jfree.chart3d.renderer.xyz.ScatterXYZRenderer;

import java.util.List;

public class ScatterPlotBuilder {

    private String title;
    private String subTitle;
    private XYZDataset<String> dataset;

    private ScatterPlotBuilder() {

    }

    public static ScatterPlotBuilder create() {
        return new ScatterPlotBuilder();
    }

    public ScatterPlotBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ScatterPlotBuilder setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public ScatterPlotBuilder addDataSet(List<XYZSeries<String>> dataSetList) {
        XYZSeriesCollection<String> dataset = new XYZSeriesCollection<>();
        dataSetList.forEach(dataset::add);
        this.dataset = dataset;
        return this;
    }

    public void openWindow() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            StackPane sp = new StackPane();
            sp.getChildren().add(createDemoNode());
            Scene scene = new Scene(sp, 768, 512);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        });
    }

    private Node createDemoNode() {
        Chart3D chart = createChart(dataset);
        return new Chart3DViewer(chart);
    }

    private Chart3D createChart(XYZDataset<String> dataset) {
        Chart3D chart = Chart3DFactory.createScatterChart(title, subTitle, dataset, "X", "Y", "Z");
        XYZPlot plot = (XYZPlot) chart.getPlot();
        plot.setDimensions(new Dimension3D(10.0, 4.0, 4.0));
        plot.setLegendLabelGenerator(new StandardXYZLabelGenerator(StandardXYZLabelGenerator.COUNT_TEMPLATE));

        ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
        renderer.setSize(0.15);
        renderer.setColors(Colors.createIntenseColors());
        chart.setViewPoint(ViewPoint3D.createAboveLeftViewPoint(40));
        return chart;
    }

}
