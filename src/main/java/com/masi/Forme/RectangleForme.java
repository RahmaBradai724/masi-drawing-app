package com.masi.Forme;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleForme implements Forme {
    private final Rectangle rectangle;

    public RectangleForme() {
        rectangle = new Rectangle(50, 50, 100, 60);
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
    }

    public void dessiner() {
        System.out.println("Je dessine un rectangle.");
    }
    @Override
    public Node afficher() {
        return rectangle;
    }
}