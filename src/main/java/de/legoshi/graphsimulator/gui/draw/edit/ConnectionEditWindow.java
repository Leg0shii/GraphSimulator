package de.legoshi.graphsimulator.gui.draw.edit;

import de.legoshi.graphsimulator.gui.DialogWindow;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionEditWindow extends DialogWindow {
    
    private final ConnectionSymbol connectionSymbol;
    private final Button deleteButton;
    
    private final TextField textField;
    private final Button acceptButton;
    
    public ConnectionEditWindow(ConnectionSymbol connectionSymbol) {
        super();
        this.connectionSymbol = connectionSymbol;
        this.textField = new TextField(connectionSymbol.getRedundancy() + "");
        this.acceptButton = new Button("OK");
        this.deleteButton = new Button("Delete");
    
        this.gridPane.add(new Label("Redundancy: "), 0, 0);
        this.gridPane.add(textField, 1, 0);
        this.gridPane.add(acceptButton, 0, 1);
        this.gridPane.add(deleteButton, 1, 1);
        
        registerInteract();
    }
    
    @Override
    public void registerInteract() {
        this.acceptButton.setOnAction(event -> {
            connectionSymbol.setName(textField.getText());
            this.close();
        });
        this.deleteButton.setOnAction(event -> {
            connectionSymbol.getDrawHandler().removeSymbol(connectionSymbol);
            this.close();
        });
    }
    
}
