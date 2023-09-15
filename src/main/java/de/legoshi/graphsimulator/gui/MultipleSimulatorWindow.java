package de.legoshi.graphsimulator.gui;

import de.legoshi.graphsimulator.file.FileSaver;
import de.legoshi.graphsimulator.gui.draw.DrawHandler;
import de.legoshi.graphsimulator.plot.Distribution;
import de.legoshi.graphsimulator.simulation.Simulation;
import de.legoshi.graphsimulator.simulation.StdCalc;
import de.legoshi.graphsimulator.simulation.TimeParser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MultipleSimulatorWindow extends DialogWindow {
    
    private final DrawHandler drawHandler;
    
    private TextField duration;
    
    private TextField meanDestructionStart;
    private TextField meanDestructionEnd;
    private TextField meanDestructionStepCount;
    private TextField meanDestructionPercentOverlap;
    
    private TextField meanRepairStart;
    private TextField meanRepairEnd;
    private TextField meanRepairStepCount;
    private TextField meanRepairPercentOverlap;
    
    private Button startGenerateButton;
    
    public MultipleSimulatorWindow(DrawHandler drawHandler) {
        super();
        this.drawHandler = drawHandler;
        initComponents();
        placeComponents();
        registerInteract();
    }
    
    private void initComponents() {
        duration = new TextField("1y");
        
        meanDestructionStart = new TextField("1d");
        meanDestructionEnd = new TextField("10d");
        meanDestructionStepCount = new TextField("10");
        meanDestructionPercentOverlap = new TextField("0.1");
        
        meanRepairStart = new TextField("1h");
        meanRepairEnd = new TextField("15h");
        meanRepairStepCount = new TextField("10");
        meanRepairPercentOverlap = new TextField("0.1");
        
        startGenerateButton = new Button("Start Generate");
    }
    
    private void placeComponents() {
        this.gridPane.add(new Label("Zeitdauer:"), 0, 0);
        this.gridPane.add(duration, 1, 0);
        
        this.gridPane.add(new Label("Ø Verbindungsausfall:"), 0, 1);
        this.gridPane.add(meanDestructionStart, 1, 1);
        this.gridPane.add(meanDestructionEnd, 2, 1);
        this.gridPane.add(meanDestructionStepCount, 3, 1);
        this.gridPane.add(meanDestructionPercentOverlap, 4, 1);
        
        this.gridPane.add(new Label("Ø Reperatur:"), 0, 2);
        this.gridPane.add(meanRepairStart, 1, 2);
        this.gridPane.add(meanRepairEnd, 2, 2);
        this.gridPane.add(meanRepairStepCount, 3, 2);
        this.gridPane.add(meanRepairPercentOverlap, 4, 2);
        
        this.gridPane.add(startGenerateButton, 0, 5, 1, 4);
    }
    
    // header;duration;meandesstart;meandesend;meandessteps;meandespercent;meanrepstart;meanrepend;meanrepsteps;meanreppercent
    @Override
    public void registerInteract() {
        this.startGenerateButton.setOnAction(e -> {
            String resultString = "configuration;" + duration.getText() + ";";
            resultString += meanDestructionStart.getText() + ";" + meanDestructionEnd.getText() + ";" + meanDestructionStepCount.getText() + ";" + meanDestructionPercentOverlap.getText();
            resultString += ";" + meanRepairStart.getText() + ";" + meanRepairEnd.getText() + ";" + meanRepairStepCount.getText() + ";" + meanRepairPercentOverlap.getText() + "\n";
            resultString += "name;meandes;stddes;meanrep;stdrep;available\n";
            
            long durationTime = TimeParser.parseToSeconds(duration.getText());
            
            double mDesStart = TimeParser.parseToSeconds(meanDestructionStart.getText());
            double mDesEnd = TimeParser.parseToSeconds(meanDestructionEnd.getText());
            int mDesStep = Integer.parseInt(meanDestructionStepCount.getText());
            double mDesOverlap = Double.parseDouble(meanDestructionPercentOverlap.getText());
            double desStepDistance = (mDesEnd-(mDesStart-1)) / mDesStep;
            double desStd = StdCalc.calc(desStepDistance, mDesOverlap);
            
            double mRepStart = TimeParser.parseToSeconds(meanRepairStart.getText());
            double mRepEnd = TimeParser.parseToSeconds(meanRepairEnd.getText());
            int mRepStep = Integer.parseInt(meanRepairStepCount.getText());
            double mRepOverlap = Double.parseDouble(meanRepairPercentOverlap.getText());
            double repStepDistance = (mRepEnd-(mRepStart-1)) / mRepStep;
            double repStd = StdCalc.calc(repStepDistance, mRepOverlap);
            
            for (double meanDesTime = mDesStart; meanDesTime <= mDesEnd; meanDesTime += desStepDistance) {
                for (double meanRepTime = mRepStart; meanRepTime <= mRepEnd; meanRepTime += repStepDistance) {
                    Distribution destruction = new Distribution(meanDesTime, desStd);
                    Distribution repair = new Distribution(meanRepTime, repStd);
                    Simulation simulation = new Simulation(drawHandler, destruction, repair, durationTime, 1, 1);
                    resultString = resultString + simulation.start();
                }
            }
            
            new FileSaver(drawHandler, resultString);
        });
    }
    
}
