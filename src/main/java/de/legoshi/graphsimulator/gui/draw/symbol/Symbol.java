package de.legoshi.graphsimulator.gui.draw.symbol;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public abstract class Symbol extends StackPane {

    Color COLOR = Color.BLACK;

    public abstract Symbol createNode();

    public abstract void registerInteract();
    
    public abstract void removeSelf(AnchorPane pane);
    
    public abstract void setName(String name);
    
    public abstract String toString();
    
    public abstract void applyPosition();

}
