package de.legoshi.graphsimulator.gui.draw.symbol;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class ConnectionSymbol extends Symbol {

    private NetworkSymbol startSymbol;
    private NetworkSymbol endSymbol;

    private int redundancy;

    public ConnectionSymbol(NetworkSymbol startSymbol, NetworkSymbol endSymbol, int redundancy) {
        this.startSymbol = startSymbol;
        this.endSymbol = endSymbol;
        this.redundancy = redundancy;
    }

    @Override
    public Symbol createNode() {
        double startX = NetworkSymbol.SIZE/2.0 + startSymbol.getMinWidth();
        double startY = NetworkSymbol.SIZE/2.0 + startSymbol.getMinHeight();
        double endX = NetworkSymbol.SIZE/2.0 + endSymbol.getMinWidth();
        double endY = NetworkSymbol.SIZE/2.0 + endSymbol.getMinHeight();
        this.getChildren().add(new Line(startX, startY, endX, endY));
        this.getChildren().add(new Label("[" + redundancy + "]"));

        registerInteract();
        return this;
    }

    public void registerInteract() {
        this.setOnMouseClicked(event -> {
            if (event.isSecondaryButtonDown()) {
                // open edit window
            } else {
                System.out.println("CLICKED");
            }
        });
    }

}
