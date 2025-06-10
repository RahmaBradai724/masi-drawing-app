package com.masi.Forme;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LigneForme implements Forme {
    private final Line ligne;

    public LigneForme(double startX, double startY, double endX, double endY) {
        ligne = new Line(startX, startY, endX, endY);
        ligne.setStroke(Color.RED);
        ligne.setStrokeWidth(2);
    }

    @Override
    public void dessiner() {
        System.out.println("Je dessine une ligne.");
    }

    @Override
    public Node afficher() {
        return ligne;
    }

    // Method to update end points during dragging
    public void updateDimensions(double startX, double startY, double endX, double endY) {
        ligne.setStartX(startX);
        ligne.setStartY(startY);
        ligne.setEndX(endX);
        ligne.setEndY(endY);
    }
}