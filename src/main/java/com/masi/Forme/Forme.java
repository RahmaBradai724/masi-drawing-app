package com.masi.Forme;

import javafx.scene.Node;

public interface Forme {
    void dessiner();
    Node afficher();
    void updateDimensions(double startX, double startY, double endX, double endY);
}