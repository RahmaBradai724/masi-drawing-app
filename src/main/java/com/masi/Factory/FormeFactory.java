package com.masi.Factory;

import com.masi.Forme.*;

public class FormeFactory {

    public static Forme creerForme(String type, double startX, double startY) {
        if (type == null) return null;

        switch (type.toLowerCase()) {
            case "rectangle":
                return new RectangleForme(startX, startY, 0, 0); // Initial dimensions
            case "cercle":
                return new CercleForme(startX, startY, 0); // Initial radius
            case "ligne":
                return new LigneForme(startX, startY, startX, startY); // Start = end initially
            default:
                throw new IllegalArgumentException("Type de forme inconnu : " + type);
        }
    }
}