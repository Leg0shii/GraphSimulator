package de.legoshi.graphsimulator.file;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileSaver {
    
    private Path filePath;
    private DrawHandler drawHandler;
    
    public FileSaver(DrawHandler drawHandler, String content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
    
        File file = fileChooser.showSaveDialog(drawHandler.getDrawPane().getScene().getWindow());
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getPath()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public FileSaver(DrawHandler drawHandler, File file) {
        this.filePath = Paths.get(file.getPath());
        this.drawHandler = drawHandler;
    }
    
    public void saveFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (NetworkSymbol symbol : drawHandler.getAllNetworkSymbols()) {
                writer.write(symbol.toString());
                writer.newLine();
            }
            for (ConnectionSymbol symbol : drawHandler.getConnectionSymbols()) {
                writer.write(symbol.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
