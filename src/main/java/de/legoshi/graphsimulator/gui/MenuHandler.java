package de.legoshi.graphsimulator.gui;

import de.legoshi.graphsimulator.file.FileOpener;
import de.legoshi.graphsimulator.file.FileSaver;
import de.legoshi.graphsimulator.gui.draw.DrawHandler;

import java.io.File;

public class MenuHandler {

    public void onSaveClick(DrawHandler drawHandler, File file) {
        new FileSaver(drawHandler, file).saveFile();
    }

    public void onOpenClick(DrawHandler drawHandler, File file) {
        drawHandler.getDrawPane().getChildren().clear();
        new FileOpener(file.getPath(), drawHandler).openFile();
    }

}
