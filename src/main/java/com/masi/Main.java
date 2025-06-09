package com.masi;

import com.masi.Forme.Forme;
import com.masi.Factory.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Application de dessin MASI");

        Pane root = new Pane();

        Forme forme = FormeFactory.creerForme("rectangle");
        if (forme != null) {
            forme.dessiner();
            root.getChildren().add(forme.afficher());
        }

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
