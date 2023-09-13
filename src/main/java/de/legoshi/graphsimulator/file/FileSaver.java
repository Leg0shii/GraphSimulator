package de.legoshi.graphsimulator.file;

import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.gui.draw.symbol.ConnectionSymbol;
import de.legoshi.graphsimulator.gui.draw.symbol.NetworkSymbol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileSaver {
    
    private final Path filePath;
    private final DrawHandler drawHandler;
    
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
