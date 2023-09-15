package de.legoshi.graphsimulator.gui.draw.edit;

import de.legoshi.graphsimulator.gui.DialogWindow;
import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class ConnectionCreateWindow extends DialogWindow {
    
    private final DrawHandler drawHandler;
    private final ComboBox<String> startBox;
    private final ComboBox<String> endBox;
    private final TextField redText;
    private final Button generateButton;
    
    public ConnectionCreateWindow(DrawHandler drawHandler) {
        super();
        this.drawHandler = drawHandler;
        this.startBox = new ComboBox<>();
        this.endBox = new ComboBox<>();
        
        this.redText = new TextField("1");
        this.generateButton = new Button("Generate");
        
        this.gridPane.add(new Label("Start: "), 0, 0);
        this.gridPane.add(startBox, 1, 0);
        
        this.gridPane.add(new Label("End: "), 0, 1);
        this.gridPane.add(endBox, 1, 1);
        
        this.gridPane.add(generateButton, 0, 2);
        
        fillBox();
        registerInteract();
    }
    
    @Override
    public void registerInteract() {
        generateButton.setOnAction(event -> {
            NetworkSymbol start = fetchSymbol(startBox.getValue());
            NetworkSymbol end = fetchSymbol(endBox.getValue());
            
            if (start == null || end == null) return;
            if (hasConnection(start, end)) {
                this.close();
                return;
            }
            
            drawHandler.addSymbolBack(new ConnectionSymbol(start, end, Integer.parseInt(redText.getText()), drawHandler).createNode());
            this.close();
        });
    }
    
    private boolean hasConnection(NetworkSymbol start, NetworkSymbol end) {
        for (ConnectionSymbol startCons : start.getConnectionSymbols()) {
            if (end.getConnectionSymbols().contains(startCons)) return true;
        }
    
        for (ConnectionSymbol endCons : end.getConnectionSymbols()) {
            if (start.getConnectionSymbols().contains(endCons)) return true;
        }
        
        return false;
    }
    
    private void fillBox() {
        this.startBox.getItems().clear();
        this.endBox.getItems().clear();
        
        List<String> symbols = drawHandler.getDrawPane().getChildren()
            .stream()
            .filter(node -> node instanceof NetworkSymbol)
            .map(node -> ((NetworkSymbol) node).getName())
            .collect(Collectors.toList());
        
        this.startBox.getItems().addAll(symbols);
        this.endBox.getItems().addAll(symbols);
    }
    
    private NetworkSymbol fetchSymbol(String name) {
        return drawHandler.getDrawPane().getChildren()
            .stream()
            .filter(node -> node instanceof NetworkSymbol && ((NetworkSymbol) node).getName().equals(name))
            .map(node -> (NetworkSymbol) node)
            .findFirst()
            .orElse(null);
    }
    
}
