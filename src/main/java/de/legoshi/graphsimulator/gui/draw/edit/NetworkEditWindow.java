package de.legoshi.graphsimulator.gui.draw.edit;

import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NetworkEditWindow extends DialogWindow {

    private final NetworkSymbol symbol;
    private final TextField textField;

    public NetworkEditWindow(NetworkSymbol symbol) {
        super();
        this.symbol = symbol;
        this.textField = new TextField();
        this.gridPane.add(new Label("Name: "), 0, 0);
        this.gridPane.add(textField, 0, 1);
    }

}
