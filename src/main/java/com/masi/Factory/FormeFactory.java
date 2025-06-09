package com.masi.Factory;

import com.masi.Forme.*;

public class FormeFactory {

    public static Forme creerForme(String type) {
        if (type == null) return null;

        switch (type.toLowerCase()) {
            case "rectangle":
                return new RectangleForme();
            case "cercle":
                return new CercleForme();
            case "ligne":
                return new LigneForme();
            default:
                throw new IllegalArgumentException("Type de forme inconnu : " + type);
        }
    }
}