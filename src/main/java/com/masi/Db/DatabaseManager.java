package com.masi.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connexion;

    // Informations de connexion à MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/masi_dessin";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";
    // Constructeur privé pour le Singleton
    private DatabaseManager() {
        try {
            // Chargement du driver JDBC MySQL (optionnel depuis Java 6+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connexion à la base de données MySQL
            connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
            System.out.println("Connexion à la base de données MySQL réussie !");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à MySQL : " + e.getMessage());
        }
    }

    // Méthode pour récupérer l'instance unique
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Méthode pour récupérer la connexion
    public Connection getConnexion() {
        return connexion;
    }
}
