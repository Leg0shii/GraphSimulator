package de.legoshi.graphsimulator.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class DialogWindow extends Stage {

    public final GridPane gridPane;

    public DialogWindow() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add("styles.css");
        this.gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, new Insets(0, 0, 0, 0))));
        this.setScene(scene);
    }
    
    public abstract void registerInteract();

}