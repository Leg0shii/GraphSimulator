package de.legoshi.graphsimulator.gui.draw;

import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.Symbol;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DrawHandler {

    @Getter private final AnchorPane drawPane;

    public DrawHandler(AnchorPane drawPane) {
        this.drawPane = drawPane;
    }

    public void addSymbol(Symbol symbol) {
        this.drawPane.getChildren().add(symbol);
        symbol.applyPosition();
    }
    
    public void addSymbolBack(Symbol symbol) {
        this.drawPane.getChildren().add(0, symbol);
        symbol.applyPosition();
    }

    public void removeSymbol(Symbol symbol) {
        symbol.removeSelf(drawPane);
    }
    
    public NetworkSymbol getNetworkSymbolByID(UUID uuid) {
        for (Node node : drawPane.getChildren()) {
            if (node instanceof NetworkSymbol) {
                if (((NetworkSymbol) node).getUuid().equals(uuid)) {
                    return (NetworkSymbol) node;
                }
            }
        }
        return null;
    }
    
    public List<NetworkSymbol> getAllNetworkSymbols() {
        List<NetworkSymbol> symbols = new ArrayList<>();
        for (Node node : drawPane.getChildren()) {
            if (node instanceof NetworkSymbol) {
                symbols.add((NetworkSymbol) node);
            }
        }
        return symbols;
    }
    
    public List<ConnectionSymbol> getConnectionSymbols() {
        List<ConnectionSymbol> symbols = new ArrayList<>();
        for (Node node : drawPane.getChildren()) {
            if (node instanceof ConnectionSymbol) {
                symbols.add((ConnectionSymbol) node);
            }
        }
        return symbols;
    }
    
    public List<NetworkSymbol> getAllImportantNetworkSymbols() {
        List<NetworkSymbol> symbols = new ArrayList<>();
        for (Node node : drawPane.getChildren()) {
            if (node instanceof NetworkSymbol && ((NetworkSymbol) node).isImportant()) {
                symbols.add((NetworkSymbol) node);
            }
        }
        return symbols;
    }

}
