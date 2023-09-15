package de.legoshi.graphsimulator;

import de.legoshi.graphsimulator.gui.MenuHandler;
import de.legoshi.graphsimulator.gui.MultipleSimulatorWindow;
import de.legoshi.graphsimulator.gui.SimulationWindow;
import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.edit.ConnectionCreateWindow;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    public AnchorPane drawPane;
    
    private DrawHandler drawHandler;
    private MenuHandler menuHandler;

    @FXML
    public void initialize() {
        drawHandler = new DrawHandler(drawPane);
        menuHandler = new MenuHandler();
    }

    public void clickSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
    
        File file = fileChooser.showSaveDialog(drawPane.getScene().getWindow());
        if (file != null) {
            menuHandler.onSaveClick(drawHandler, file);
        }
    }

    public void clickOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
    
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
    
        File file = fileChooser.showOpenDialog(drawPane.getScene().getWindow());
        if (file != null) {
            menuHandler.onOpenClick(drawHandler, file);
        }
    }

    public void addNetworkSymbol() {
        drawHandler.addSymbol(new NetworkSymbol(drawHandler).createNode());
    }

    public void addConnectionSymbol() {
        new ConnectionCreateWindow(drawHandler).show();
    }

    public void openSimulatorWindow() {
        new SimulationWindow(drawHandler).show();
    }
    
    public void openMultipleSimulatorWindow() {
        new MultipleSimulatorWindow(drawHandler).show();
    }
}