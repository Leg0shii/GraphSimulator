package de.legoshi.graphsimulator.gui;

import de.legoshi.graphsimulator.file.FileSaver;
import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.plot.Distribution;
import de.legoshi.graphsimulator.simulation.Simulation;
import de.legoshi.graphsimulator.simulation.TimeParser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SimulationWindow extends DialogWindow {
    
    private final DrawHandler drawHandler;
    
    private TextField duration;
    private TextField meanConnectionsField;
    private TextField stdDevConnectionsField;
    private TextField meanRepairField;
    private TextField stdDevRepairField;
    private TextField rangeStart;
    private TextField rangeEnd;
    private Button startGenerateButton;
    
    public SimulationWindow(DrawHandler drawHandler) {
        super();
        this.drawHandler = drawHandler;
        initComponents();
        placeComponents();
        registerInteract();
    }
    
    private void initComponents() {
        duration = new TextField("1y");
        meanConnectionsField = new TextField("2m");
        stdDevConnectionsField = new TextField("1m");
        meanRepairField = new TextField("2w");
        stdDevRepairField = new TextField("1d");
        startGenerateButton = new Button("Start Generate");
        rangeStart = new TextField("1");
        rangeEnd = new TextField("1");
    }
    
    private void placeComponents() {
        gridPane.add(new Label("Zeitdauer:"), 0, 0);
        gridPane.add(duration, 1, 0);
        
        gridPane.add(new Label("Ø Verbindungsausfall:"), 0, 1);
        gridPane.add(meanConnectionsField, 1, 1);
        
        gridPane.add(new Label("σ Verbindungsausfall:"), 0, 2);
        gridPane.add(stdDevConnectionsField, 1, 2);
        
        gridPane.add(new Label("Ø Reperatur:"), 0, 3);
        gridPane.add(meanRepairField, 1, 3);
        
        gridPane.add(new Label("σ Reperatur:"), 0, 4);
        gridPane.add(stdDevRepairField, 1, 4);
        
        gridPane.add(new Label("Attack Count:"), 0, 5);
        gridPane.add(rangeStart, 1, 5);
        gridPane.add(rangeEnd, 2, 5);
        
        gridPane.add(startGenerateButton, 1, 7);
    }
    
    // header;duration;meandes;stddes;meanrep;stdrep;rangestart;rangeend
    @Override
    public void registerInteract() {
        startGenerateButton.setOnAction(e -> {
            String resultString = "configuration;" + duration.getText() + ";";
            resultString += meanConnectionsField.getText() + ";" + stdDevConnectionsField.getText() + ";" + meanRepairField.getText() + ";" + stdDevRepairField.getText();
            resultString += ";" + rangeStart.getText() + ";" + rangeEnd.getText()+ "\n";
            resultString += "name;desstart;desend\n";
            
            Simulation simulation = new Simulation(
                drawHandler,
                new Distribution(TimeParser.parseToSeconds(meanConnectionsField.getText()), TimeParser.parseToSeconds(stdDevConnectionsField.getText())),
                new Distribution(TimeParser.parseToSeconds(meanRepairField.getText()), TimeParser.parseToSeconds(stdDevRepairField.getText())),
                TimeParser.parseToSeconds(duration.getText()),
                Integer.parseInt(rangeStart.getText()),
                Integer.parseInt(rangeEnd.getText())
            );
            
            resultString = resultString + simulation.startSingle();
            new FileSaver(drawHandler, resultString);
        });
    }
}
