package com.masi.Forme;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CercleForme implements Forme {
    private final Circle cercle;

    public CercleForme(double centerX, double centerY, double radius) {
        cercle = new Circle(centerX, centerY, radius);
        cercle.setFill(Color.ORANGE);
        cercle.setStroke(Color.BLACK);
    }

    @Override
    public void dessiner() {
        System.out.println("Je dessine un cercle.");
    }

    @Override
    public Node afficher() {
        return cercle;
    }

    // Method to update radius during dragging
    public void updateDimensions(double startX, double startY, double endX, double endY) {
        double dx = endX - startX;
        double dy = endY - startY;
        double radius = Math.sqrt(dx * dx + dy * dy) / 2;
        cercle.setRadius(radius);
        cercle.setCenterX(startX);
        cercle.setCenterY(startY);
    }
}