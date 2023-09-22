package de.legoshi.graphsimulator.gui.draw.symbol;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.edit.NetworkEditWindow;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkSymbol extends Symbol {
    
    protected static final int SIZE = 75;
    private static final Color FILL_COLOR = Color.GRAY;
    
    @Getter private final DrawHandler drawHandler;
    @Getter private final UUID uuid;
    
    private final Label nameLabel;
    @Getter private String name;
    @Getter private final List<ConnectionSymbol> connectionSymbols = new ArrayList<>();
    @Getter private boolean important = false;
    @Getter @Setter private int priority = 99;
    @Getter @Setter private long destroyedTime = 0;
    
    public double lastX;
    public double lastY;
    
    private double posX = 0;
    private double posY = 0;
    
    public NetworkSymbol(DrawHandler drawHandler) {
        this.uuid = UUID.randomUUID();
        
        this.drawHandler = drawHandler;
        this.name = uuid.toString().replace("-", "").substring(0, 5);
        this.nameLabel = new Label(name);
        
        this.lastX = Double.MAX_VALUE;
        this.lastY = Double.MAX_VALUE;
    }
    
    protected NetworkSymbol(DrawHandler drawHandler, String uuid, String name, boolean important, int prio, double posX, double posY) {
        this.uuid = UUID.fromString(uuid);
        this.name = name;
        this.priority = prio;
        this.important = important;
        setImportant(important);
        
        this.drawHandler = drawHandler;
        this.nameLabel = new Label(name);
        this.posX = posX;
        this.posY = posY;
    
        this.lastX = Double.MAX_VALUE;
        this.lastY = Double.MAX_VALUE;
    }
    
    @Override
    public Symbol createNode() {
        this.getChildren().add(new Rectangle(SIZE, SIZE, FILL_COLOR));
        this.getChildren().add(nameLabel);
        registerInteract();
        return this;
    }
    
    @Override
    public void registerInteract() {
        this.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                this.lastX = event.getSceneX();
                this.lastY = event.getSceneY();
            } else {
                new NetworkEditWindow(this).show();
            }
        });
        
        this.setOnMouseDragged(event -> {
            if (event.isSecondaryButtonDown()) return;
            
            if (this.lastX == Double.MAX_VALUE || this.lastY == Double.MAX_VALUE) {
                this.lastX = event.getSceneX();
                this.lastY = event.getSceneY();
            }
            
            this.setTranslateX(this.getTranslateX() + (event.getSceneX() - lastX));
            this.setTranslateY(this.getTranslateY() + (event.getSceneY() - lastY));
            
            this.lastX = event.getSceneX();
            this.lastY = event.getSceneY();
    
            connectionSymbols.forEach(ConnectionSymbol::recalculateBindings);
            event.consume();
        });
    }
    
    @Override
    public void removeSelf(AnchorPane pane) {
        for (ConnectionSymbol symbol : new ArrayList<>(connectionSymbols)) {
            symbol.removeSelf(pane);
        }
        pane.getChildren().remove(this);
    }
    
    public void setName(String name) {
        this.name = name;
        nameLabel.setText(name);
    }
    
    public void setImportant(boolean selected) {
        this.important = selected;
        if (selected) {
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(4))));
        } else {
            this.setBorder(null);
        }
    }
    
    // uuid;name;important;posX;posY
    @Override
    public String toString() {
        return getClass().getSimpleName() + ";" + uuid + ";" + name + ";" + important + ";" + this.getTranslateX() + ";" + this.getTranslateY() + ";" + priority;
    }
    
    public static NetworkSymbol toSymbol(DrawHandler drawHandler, String value) {
        String[] args = value.split(";");
        String uuid = args[1];
        String name = args[2];
        boolean important = Boolean.parseBoolean(args[3]);
        double posX = Double.parseDouble(args[4]);
        double posY = Double.parseDouble(args[5]);
        int prio = 99;
        try {
            prio = Integer.parseInt(args[6]);
        } catch (Exception ignored) {}
    
        return new NetworkSymbol(drawHandler, uuid, name, important, prio, posX, posY);
    }
    
    @Override
    public void applyPosition() {
        this.setTranslateX(posX);
        this.setTranslateY(posY);
    }
    
}
