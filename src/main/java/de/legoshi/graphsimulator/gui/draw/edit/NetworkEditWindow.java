package de.legoshi.graphsimulator.gui.draw.edit;

import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NetworkEditWindow extends DialogWindow {

    private final NetworkSymbol symbol;
    private final TextField textField;
    
    private final Button acceptButton;
    private final Button deleteButton;
    
    private final CheckBox importantSelector;

    public NetworkEditWindow(NetworkSymbol symbol) {
        super();
        this.symbol = symbol;
        this.textField = new TextField(symbol.getName());
        this.importantSelector = new CheckBox();
        this.acceptButton = new Button("OK");
        this.deleteButton = new Button("Delete");
        
        this.gridPane.add(new Label("Name: "), 0, 0);
        this.gridPane.add(textField, 1, 0);
        this.gridPane.add(new Label("Important: "), 0, 1);
        this.gridPane.add(importantSelector, 1, 1);
    
        this.gridPane.add(acceptButton, 0, 2);
        this.gridPane.add(deleteButton, 1, 2);
        
        registerInteract();
    }
    
    public void registerInteract() {
        this.acceptButton.setOnAction(event -> {
            symbol.setName(textField.getText());
            this.close();
        });
        this.deleteButton.setOnAction(event -> {
            symbol.getDrawHandler().removeSymbol(symbol);
            this.close();
        });
        this.importantSelector.setOnAction(event -> {
            symbol.setImportant(importantSelector.isSelected());
        });
    }

}
