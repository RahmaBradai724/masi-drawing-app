package com.masi.Forme;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CercleForme implements Forme {
    private final Circle cercle;

    public CercleForme() {
        cercle = new Circle(75, 75, 40);
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
}
