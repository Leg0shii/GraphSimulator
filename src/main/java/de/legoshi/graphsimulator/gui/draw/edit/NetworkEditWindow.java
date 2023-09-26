package de.legoshi.graphsimulator.gui.draw.edit;

import de.legoshi.graphsimulator.gui.DialogWindow;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NetworkEditWindow extends DialogWindow {

    private final NetworkSymbol symbol;
    private final TextField textField;
    private final TextField prioText;
    
    private final Button acceptButton;
    private final Button deleteButton;
    
    private final CheckBox importantSelector;

    public NetworkEditWindow(NetworkSymbol symbol) {
        super();
        this.symbol = symbol;
        this.textField = new TextField(symbol.getName());
        this.prioText = new TextField(symbol.getPriority() + "");
        
        this.importantSelector = new CheckBox();
        this.importantSelector.setSelected(symbol.isImportant());
        this.acceptButton = new Button("OK");
        this.deleteButton = new Button("Delete");
        
        this.gridPane.add(new Label("Name: "), 0, 0);
        this.gridPane.add(textField, 1, 0);
        this.gridPane.add(new Label("Important: "), 0, 1);
        this.gridPane.add(importantSelector, 1, 1);
        this.gridPane.add(new Label("Priority: "), 0, 2);
        this.gridPane.add(prioText, 1, 2);
    
        this.gridPane.add(acceptButton, 0, 3);
        this.gridPane.add(deleteButton, 1, 3);
        
        registerInteract();
    }
    
    public void registerInteract() {
        this.acceptButton.setOnAction(event -> {
            symbol.setName(textField.getText());
            symbol.setPriority(Integer.parseInt(prioText.getText()));
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
