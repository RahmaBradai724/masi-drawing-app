package com.masi.logger;

import com.masi.Db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLogger implements Logger {
    @Override
    public void log(String message) {
        Connection conn = DatabaseManager.getInstance().getConnexion();

        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible pour la journalisation");
            return;
        }

        String sql = "INSERT INTO logs (message) VALUES (?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, message);
            statement.executeUpdate();
            System.out.println("Database: " + message + " (enregistré dans la base)");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du log dans la base : " + e.getMessage());
        }
    }
}