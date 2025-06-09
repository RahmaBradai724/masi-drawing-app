package com.masi.Controller;

import com.masi.decorator.BorderDecorator;
import com.masi.decorator.ShadowDecorator;
import com.masi.decorator.Shape;
import com.masi.decorator.Rectangle;
import com.masi.decorator.ShapeDecorator;
import com.masi.logger.ConsoleLogger;
import com.masi.logger.DatabaseLogger;
import com.masi.logger.FileLogger;
import com.masi.logger.Logger;
import com.masi.observer.DrawingModel;
import com.masi.observer.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

public class DrawingController implements Observer {
    @FXML
    private Pane drawingPane;
    @FXML
    private ChoiceBox<String> shapeChoice;
    @FXML
    private ChoiceBox<String> logChoice;
    @FXML
    private ChoiceBox<String> decoratorChoice;

    private DrawingModel model = new DrawingModel();
    private Logger logger;

    public void initialize() {
        // Initialize ChoiceBoxes
        shapeChoice.getItems().addAll("Rectangle", "Circle", "Line");
        logChoice.getItems().addAll("Console", "File", "Database");
        decoratorChoice.getItems().addAll("None", "Border", "Shadow");

        // Set default values
        shapeChoice.setValue("Rectangle");
        logChoice.setValue("Console");
        decoratorChoice.setValue("None");

        // Set logger based on selection
        logChoice.setOnAction(e -> setLogger(logChoice.getValue()));

        // Set default logger
        setLogger("Console");
        model.addObserver(this);
    }

    @FXML
    private void handleDraw() {
        String shapeType = shapeChoice.getValue();
        String decorator = decoratorChoice.getValue();

        // Create base shape based on selection
        Shape baseShape = new Rectangle();

        // Apply decorator based on selection
        if ("Border".equals(decorator)) {
            baseShape = new BorderDecorator(baseShape);
        } else if ("Shadow".equals(decorator)) {
            baseShape = new ShadowDecorator(baseShape);
        }

        baseShape.draw();
        logger.log("Drew " + shapeType + " with " + decorator);
        model.setSelectedShape(shapeType);
    }

    private void setLogger(String type) {
        switch (type) {
            case "Console":
                logger = new ConsoleLogger();
                break;
            case "File":
                logger = new FileLogger();
                break;
            case "Database":
                logger = new DatabaseLogger();
                break;
        }
    }

    @Override
    public void update() {
        // Update UI when model changes
        System.out.println("Model updated: " + model.getSelectedShape());
    }
}