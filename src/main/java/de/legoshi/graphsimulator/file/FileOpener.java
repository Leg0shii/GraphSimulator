package de.legoshi.graphsimulator.file;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;
import javafx.scene.control.ComboBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOpener {
    
    private final Path filePath;
    private final DrawHandler drawHandler;
    
    public FileOpener(String filePath, DrawHandler drawHandler) {
        this.filePath = Paths.get(filePath);
        this.drawHandler = drawHandler;
    }
    
    public void openFile() {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("NetworkSymbol")) {
                    drawHandler.addSymbol(NetworkSymbol.toSymbol(drawHandler, line).createNode());
                } else if (line.contains("ConnectionSymbol")) {
                    drawHandler.addSymbolBack(ConnectionSymbol.toSymbol(drawHandler, line).createNode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
