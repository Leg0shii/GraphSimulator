package de.legoshi.graphsimulator.gui.draw.symbol;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.edit.ConnectionEditWindow;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import lombok.Getter;

import java.util.UUID;

// uuid1;uuid2;redundancy
public class ConnectionSymbol extends Symbol {
    
    @Getter
    private final DrawHandler drawHandler;
    private Line line;
    
    @Getter private final NetworkSymbol startSymbol;
    @Getter private final NetworkSymbol endSymbol;
    
    private Label redundancyLabel;
    private final int initialRedundancy;
    @Getter
    private int redundancy;
    
    public ConnectionSymbol(NetworkSymbol startSymbol, NetworkSymbol endSymbol, int redundancy, DrawHandler drawHandler) {
        this.drawHandler = drawHandler;
        this.startSymbol = startSymbol;
        this.endSymbol = endSymbol;
        this.initialRedundancy = redundancy;
        this.redundancy = redundancy;
        
        startSymbol.getConnectionSymbols().add(this);
        endSymbol.getConnectionSymbols().add(this);
    }
    
    @Override
    public Symbol createNode() {
        this.line = new Line();
        this.redundancyLabel = new Label("[" + redundancy + "]");
        
        this.getChildren().add(line);
        this.getChildren().add(redundancyLabel);
        
        recalculateBindings();
        registerInteract();
        return this;
    }
    
    public void registerInteract() {
        this.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                new ConnectionEditWindow(this).show();
            }
        });
    }
    
    @Override
    public void removeSelf(AnchorPane pane) {
        startSymbol.getConnectionSymbols().remove(this);
        endSymbol.getConnectionSymbols().remove(this);
        pane.getChildren().remove(this);
    }
    
    @Override
    public void setName(String name) {
        setRedundancy(Integer.parseInt(name));
    }
    
    // uuid1;uuid2;redundancy
    @Override
    public String toString() {
        return getClass().getSimpleName() + ";" + startSymbol.getUuid().toString() + ";" + endSymbol.getUuid().toString() + ";" + redundancy;
    }
    
    @Override
    public void applyPosition() {
    
    }
    
    public NetworkSymbol getOtherEnd(NetworkSymbol network) {
        return (network == startSymbol) ? endSymbol : startSymbol;
    }
    
    public static ConnectionSymbol toSymbol(DrawHandler drawHandler, String value) {
        String[] split = value.split(";");
        NetworkSymbol start = drawHandler.getNetworkSymbolByID(UUID.fromString(split[1]));
        NetworkSymbol end = drawHandler.getNetworkSymbolByID(UUID.fromString(split[2]));
        int redundancy = Integer.parseInt(split[3]);
        return new ConnectionSymbol(start, end, redundancy, drawHandler);
    }
    
    public void setRedundancy(int redundancy) {
        this.redundancy = redundancy;
        this.redundancyLabel.setText("[" + redundancy + "]");
    }
    
    public void recalculateBindings() {
        removeBindings();
        
        DoubleBinding startX = startSymbol.translateXProperty().add(NetworkSymbol.SIZE / 2.0);
        DoubleBinding startY = startSymbol.translateYProperty().add(NetworkSymbol.SIZE / 2.0);
        
        DoubleBinding endX = endSymbol.translateXProperty().add(NetworkSymbol.SIZE / 2.0);
        DoubleBinding endY = endSymbol.translateYProperty().add(NetworkSymbol.SIZE / 2.0);
        
        DoubleBinding midX = (startX.get() < endX.get()) ? endX.subtract(startX).divide(2.0) : startX.subtract(endX).divide(2.0);
        DoubleBinding midY = (startY.get() < endY.get()) ? endY.subtract(startY).divide(2.0) : startY.subtract(endY).divide(2.0);
        
        DoubleBinding relX = (startX.get() < endX.get()) ? startX.add(midX) : endX.add(midX);
        DoubleBinding relY = (startY.get() < endY.get()) ? startY.add(midY) : endY.add(midY);
        
        line.startXProperty().bind(startX);
        line.endXProperty().bind(endX);
        
        line.startYProperty().bind(startY);
        line.endYProperty().bind(endY);
        
        translateXProperty().bind(relX.subtract(midX));
        translateYProperty().bind(relY.subtract(midY));
    }
    
    private void removeBindings() {
        line.startXProperty().unbind();
        line.startYProperty().unbind();
        line.endXProperty().unbind();
        line.endYProperty().unbind();
        translateXProperty().unbind();
        translateYProperty().unbind();
    }
    
    public void decrementRedundancy() {
        if (redundancy >= 0) {
            redundancy = redundancy - 1;
        }
    }
    
    public void resetRedundancy() {
        redundancy = initialRedundancy;
    }
}
