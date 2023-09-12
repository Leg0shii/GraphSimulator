package de.legoshi.graphsimulator.gui.draw.symbol;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NetworkSymbol extends Symbol {

    protected static final int SIZE = 50;
    private static final Color FILL_COLOR = Color.YELLOW;

    private final ImageView imageView = new ImageView();
    private final String name;

    public NetworkSymbol(String name) {
        this.name = name;
    }

    @Override
    public Symbol createNode() {
        this.getChildren().add(new Rectangle(SIZE, SIZE, FILL_COLOR));
        this.getChildren().add(new Label(name));
        return this;
    }

    @Override
    public void registerInteract() {
        final Delta dragDelta = new Delta();

        this.setOnMousePressed(event -> {
            // Record the current mouse position when the mouse is pressed
            dragDelta.x = event.getX();
            dragDelta.y = event.getY();
        });

        this.setOnMouseDragged(event -> {
            // Calculate the new position based on the difference between the current mouse position and the recorded position
            double newX = this.getLayoutX() + event.getX() - dragDelta.x;
            double newY = this.getLayoutY() + event.getY() - dragDelta.y;

            // Update the position of the StackPane
            this.setLayoutX(newX);
            this.setLayoutY(newY);
        });
    }

}
