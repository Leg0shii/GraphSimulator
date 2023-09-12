package de.legoshi.graphsimulator.gui.draw;

import de.legoshi.graphsimulator.gui.draw.symbol.Symbol;
import javafx.scene.layout.AnchorPane;

public class DrawHandler {

    private final AnchorPane drawPane;

    public DrawHandler(AnchorPane drawPane) {
        this.drawPane = drawPane;
    }

    public void addSymbol(Symbol symbol) {
        this.drawPane.getChildren().add(symbol);
    }

    public void removeSymbol(Symbol symbol) {
        this.drawPane.getChildren().remove(symbol);
    }

}
