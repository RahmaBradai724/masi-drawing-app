package com.masi;

import com.masi.Db.DrawingDAO;
import com.masi.Forme.Forme;
import com.masi.Factory.FormeFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Application de dessin MASI");

        Pane root = new Pane();
        DrawingDAO drawingDAO = new DrawingDAO(); // DAO pour la base de données

        // Liste des formes à créer
        String[] types = {"rectangle", "cercle", "ligne"};
        int yOffset = 0;

        for (String type : types) {
            Forme forme = FormeFactory.creerForme(type);
            if (forme != null) {
                forme.dessiner();

                // Décalage vertical des formes
                forme.afficher().setLayoutY(yOffset);
                root.getChildren().add(forme.afficher());

                // Enregistrement dans la base de données
                drawingDAO.enregistrerDessin(type, 50, yOffset);

                yOffset += 100;
            }
        }

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Affiche les dessins enregistrés dans la base (console)
        drawingDAO.afficherDessins();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
