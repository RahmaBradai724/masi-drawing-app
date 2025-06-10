package com.masi.Controller;

import com.masi.Factory.FormeFactory;
import com.masi.Forme.Forme;
import com.masi.logger.ConsoleLogger;
import com.masi.logger.DatabaseLogger;
import com.masi.logger.FileLogger;
import com.masi.logger.Logger;
import com.masi.observer.DrawingModel;
import com.masi.observer.Observer;
import com.masi.decorator.*;
import com.masi.Db.DrawingDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.List;

public class DrawingController implements Observer {
    @FXML
    private Pane drawingPane;
    @FXML
    private ChoiceBox<String> shapeChoice;
    @FXML
    private ChoiceBox<String> logChoice;
    @FXML
    private ChoiceBox<String> decoratorChoice;
    @FXML
    private Button drawButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button clearButton;

    private final DrawingModel model = new DrawingModel();
    private Logger logger;
    private final DrawingDAO drawingDAO = new DrawingDAO();
    private final List<DrawnShape> drawnShapes = new ArrayList<>();
    private Forme currentForme; // Temporary shape being drawn
    private Node currentShapeNode; // Temporary shape node
    private double startX, startY; // Starting point of drag

    // Classe interne pour stocker les formes dessinées avec leurs propriétés
    private static class DrawnShape {
        String type;
        double x, y;
        double width, height; // For rectangles and circles (radius as height)
        String decorator;

        DrawnShape(String type, double x, double y, double width, double height, String decorator) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.decorator = decorator;
        }
    }

    public void initialize() {
        // Initialize ChoiceBoxes
        shapeChoice.getItems().addAll("Rectangle", "Cercle", "Ligne");
        logChoice.getItems().addAll("Console", "File", "Database");
        decoratorChoice.getItems().addAll("None", "Border", "Shadow", "Border+Shadow");

        // Set default values
        shapeChoice.setValue("Rectangle");
        logChoice.setValue("Console");
        decoratorChoice.setValue("None");

        // Set logger based on selection
        logChoice.setOnAction(e -> setLogger(logChoice.getValue()));

        // Set default logger
        setLogger("Console");
        model.addObserver(this);

        // Mouse event handlers for drawing
        drawingPane.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();

            // Validate coordinates
            if (startX < 0 || startX > drawingPane.getPrefWidth() - 10 || startY < 0 || startY > drawingPane.getPrefHeight() - 10) {
                showAlert("Erreur", "Le point de départ doit être dans la zone de dessin.");
                logger.log("Erreur: Point de départ hors limites.");
                return;
            }

            // Create new shape at start point
            String shapeType = shapeChoice.getValue();
            currentForme = FormeFactory.creerForme(shapeType.toLowerCase(), startX, startY);
            if (currentForme != null) {
                currentShapeNode = currentForme.afficher();
                drawingPane.getChildren().add(currentShapeNode);
            }
        });

        drawingPane.setOnMouseDragged(event -> {
            if (currentForme != null) {
                double endX = event.getX();
                double endY = event.getY();

                // Validate end coordinates
                if (endX < 0 || endX > drawingPane.getPrefWidth() || endY < 0 || endY > drawingPane.getPrefHeight()) {
                    return; // Don't update if out of bounds
                }

                // Update shape dimensions
                currentForme.updateDimensions(startX, startY, endX, endY);
            }
        });

        drawingPane.setOnMouseReleased(event -> {
            if (currentForme != null) {
                String shapeType = shapeChoice.getValue();
                String decoratorType = decoratorChoice.getValue();
                double endX = event.getX();
                double endY = event.getY();

                // Validate final coordinates
                if (endX < 0 || endX > drawingPane.getPrefWidth() || endY < 0 || endY > drawingPane.getPrefHeight()) {
                    drawingPane.getChildren().remove(currentShapeNode);
                    showAlert("Erreur", "Les dimensions doivent être dans la zone de dessin.");
                    logger.log("Erreur: Dimensions hors limites.");
                    currentForme = null;
                    currentShapeNode = null;
                    return;
                }

                // Apply decorators
                applyDecorator(currentShapeNode, decoratorType);

                // Calculate dimensions for saving
                double width = 0, height = 0;
                if (shapeType.equalsIgnoreCase("Rectangle")) {
                    width = Math.abs(endX - startX);
                    height = Math.abs(endY - startY);
                } else if (shapeType.equalsIgnoreCase("Cercle")) {
                    double dx = endX - startX;
                    double dy = endY - startY;
                    width = Math.sqrt(dx * dx + dy * dy) / 2; // Radius
                    height = width; // Store radius as height for simplicity
                } else if (shapeType.equalsIgnoreCase("Ligne")) {
                    width = endX - startX; // Store delta for line
                    height = endY - startY;
                }

                // Log and save
                currentForme.dessiner();
                String message = String.format("Dessiné %s à (%.0f, %.0f) avec dimensions (%.0f, %.0f) et décorateur: %s",
                        shapeType, startX, startY, width, height, decoratorType);
                logger.log(message);

                // Save to drawnShapes
                drawnShapes.add(new DrawnShape(shapeType, startX, startY, width, height, decoratorType));

                // Save to database
                try {
                    drawingDAO.enregistrerDessin(shapeType, (int)startX, (int)startY, (int)width, ( int)height);
                    logger.log("Forme sauvegardée en base de données");
                } catch (Exception e) {
                    logger.log("Erreur sauvegarde DB: " + e.getMessage());
                }

                // Update model
                model.setSelectedShape(shapeType);

                // Reset temporary shape
                currentForme = null;
                currentShapeNode = null;
            }
        });

        logger.log("Application initialisée avec succès");
    }

    @FXML
    private void handleDraw() {
        showAlert("Info", "Cliquez et glissez sur la zone de dessin pour placer et dimensionner une forme.");
    }

    @FXML
    private void handleSave() {
        try {
            for (DrawnShape shape : drawnShapes) {
                drawingDAO.enregistrerDessin(shape.type, (int)shape.x, (int)shape.y, (int)shape.width, (int)shape.height);
            }
            logger.log("Tous les dessins sauvegardés en base de données (" + drawnShapes.size() + " formes)");
            showAlert("Succès", "Dessins sauvegardés avec succès!");
        } catch (Exception e) {
            logger.log("Erreur lors de la sauvegarde: " + e.getMessage());
            showAlert("Erreur", "Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoad() {
        try {
            // Clear current drawing area
            drawingPane.getChildren().clear();
            drawnShapes.clear();

            // Load from database
            List<String> dessins = drawingDAO.chargerDessins();
            for (String dessin : dessins) {
                // Parse: "ID: 1 | Type: Rectangle | Position: (100, 200) | Dimensions: (50, 30)"
                String[] parts = dessin.split("\\|");
                String type = parts[1].split(":")[1].trim();
                String[] coords = parts[2].split(":")[1].trim().replace("(", "").replace(")", "").split(",");
                double x = Double.parseDouble(coords[0].trim());
                double y = Double.parseDouble(coords[1].trim());
                String[] dims = parts[3].split(":")[1].trim().replace("(", "").replace(")", "").split(",");
                double width = Double.parseDouble(dims[0].trim());
                double height = Double.parseDouble(dims[1].trim());

                // Recreate shape with stored dimensions
                Forme forme = FormeFactory.creerForme(type.toLowerCase(), x, y);
                if (forme != null) {
                    if (type.equalsIgnoreCase("Cercle")) {
                        forme.updateDimensions(x, y, x + 2 * width, y); // Radius to diameter
                    } else if (type.equalsIgnoreCase("Ligne")) {
                        forme.updateDimensions(x, y, x + width, y + height);
                    } else {
                        forme.updateDimensions(x, y, x + width, y + height);
                    }
                    Node shapeNode = forme.afficher();
                    drawingPane.getChildren().add(shapeNode);
                    drawnShapes.add(new DrawnShape(type, x, y, width, height, "None"));
                }
            }
            logger.log("Dessins chargés depuis la base de données");
            showAlert("Succès", "Dessins chargés avec succès!");
        } catch (Exception e) {
            logger.log("Erreur lors du chargement: " + e.getMessage());
            showAlert("Erreur", "Erreur lors du chargement: " + e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        drawingPane.getChildren().clear();
        drawnShapes.clear();
        logger.log("Zone de dessin nettoyée");
    }

    private void applyDecorator(Node shapeNode, String decoratorType) {
        switch (decoratorType) {
            case "Border":
                applyBorderEffect(shapeNode);
                break;
            case "Shadow":
                applyShadowEffect(shapeNode);
                break;
            case "Border+Shadow":
                applyBorderEffect(shapeNode);
                applyShadowEffect(shapeNode);
                break;
            case "None":
            default:
                // No decoration
                break;
        }
    }

    private void applyBorderEffect(Node shapeNode) {
        if (shapeNode instanceof Shape) {
            Shape shape = (Shape) shapeNode;
            shape.setStrokeWidth(3.0);
            shape.setStroke(Color.DARKBLUE);
            logger.log("Effet bordure appliqué");
        }
    }

    private void applyShadowEffect(Node shapeNode) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setRadius(10);
        shapeNode.setEffect(shadow);
        logger.log("Effet ombre appliqué");
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
            default:
                logger = new ConsoleLogger();
        }
        logger.log("Logger changé vers: " + type);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        String selectedShape = model.getSelectedShape();
        System.out.println("Observer notifié - Forme sélectionnée: " + selectedShape);

        // Update UI if necessary
        if (!selectedShape.equals("None")) {
            drawButton.setText("Dessiner " + selectedShape);
        } else {
            drawButton.setText("Dessiner");
        }
    }
}