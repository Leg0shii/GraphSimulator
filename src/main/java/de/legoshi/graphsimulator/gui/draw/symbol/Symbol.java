package de.legoshi.graphsimulator.gui.draw.symbol;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public abstract class Symbol extends StackPane {

    Color COLOR = Color.BLACK;

    public abstract Symbol createNode();

    public abstract void registerInteract();

}
