package com.masi.Forme;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleForme implements Forme {
    private final Rectangle rectangle;

    public RectangleForme(double startX, double startY, double width, double height) {
        rectangle = new Rectangle(startX, startY, Math.abs(width), Math.abs(height));
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
    }

    @Override
    public void dessiner() {
        System.out.println("Je dessine un rectangle.");
    }

    @Override
    public Node afficher() {
        return rectangle;
    }

    // Method to update dimensions during dragging
    public void updateDimensions(double startX, double startY, double endX, double endY) {
        rectangle.setX(Math.min(startX, endX));
        rectangle.setY(Math.min(startY, endY));
        rectangle.setWidth(Math.abs(endX - startX));
        rectangle.setHeight(Math.abs(endY - startY));
    }
}