package com.masi.Forme;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LigneForme implements Forme {
    private final Line ligne;

    public LigneForme() {
        ligne = new Line(20, 20, 120, 80);
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
}
