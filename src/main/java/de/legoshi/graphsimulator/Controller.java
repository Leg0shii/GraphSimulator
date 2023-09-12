package de.legoshi.graphsimulator;

import de.legoshi.graphsimulator.gui.MenuHandler;
import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {

    public AnchorPane drawPane;
    private DrawHandler drawHandler;
    private MenuHandler menuHandler;

    @FXML
    public void initialize() {
        drawHandler = new DrawHandler(drawPane);
        menuHandler = new MenuHandler();
    }

    public void clickSave(ActionEvent actionEvent) {
        menuHandler.onSaveClick(drawPane);
    }

    public void clickOpen(ActionEvent actionEvent) {
        menuHandler.onOpenClick(drawPane);
    }

    public void addNetworkSymbol() {
        drawHandler.addSymbol(new NetworkSymbol("Symbol 1").createNode());
    }

    public void addConnectionSymbol(ActionEvent actionEvent) {

    }

    public void openSimulatorWindow(ActionEvent actionEvent) {

    }

}