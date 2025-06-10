package com.masi.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrawingDAO {

    public void enregistrerDessin(String type, int x, int y, int width, int height) {
        Connection conn = DatabaseManager.getInstance().getConnexion();

        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible");
            return;
        }

        String sql = "INSERT INTO dessins (type, x, y, width, height) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, type);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, width);
            statement.setInt(5, height);
            statement.executeUpdate();

            System.out.println("✅ Dessin enregistré avec succès dans la base.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'enregistrement du dessin : " + e.getMessage());
            System.out.println("🔄 SIMULATION DB: " + type + " sauvegardé à (" + x + ", " + y + ") avec dimensions (" + width + ", " + height + ")");
        }
    }

    public List<String> chargerDessins() {
        List<String> dessins = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnexion();

        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible");
            return dessins;
        }

        String sql = "SELECT * FROM dessins ORDER BY id DESC";

        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                int width = resultSet.getInt("width");
                int height = resultSet.getInt("height");

                String dessin = String.format("ID: %d | Type: %s | Position: (%d, %d) | Dimensions: (%d, %d)",
                        id, type, x, y, width, height);
                dessins.add(dessin);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la lecture des dessins : " + e.getMessage());
        }

        return dessins;
    }

    public void afficherDessins() {
        List<String> dessins = chargerDessins();

        if (dessins.isEmpty()) {
            System.out.println("📋 Aucun dessin trouvé dans la base de données");
        } else {
            System.out.println("📊 === Liste des dessins enregistrés ===");
            for (String dessin : dessins) {
                System.out.println("  " + dessin);
            }
            System.out.println("=====================================");
        }
    }
}