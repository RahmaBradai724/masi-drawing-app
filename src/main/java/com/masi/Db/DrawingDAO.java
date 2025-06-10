package com.masi.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DrawingDAO {

    public void enregistrerDessin(String type, int x, int y) {
        Connection conn = DatabaseManager.getInstance().getConnexion();

        String sql = "INSERT INTO dessins (type, x, y) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, type);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.executeUpdate();

            System.out.println("Dessin enregistré avec succès dans la base.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du dessin : " + e.getMessage());
        }
    }

    public void afficherDessins() {
        Connection conn = DatabaseManager.getInstance().getConnexion();

        String sql = "SELECT * FROM dessins";

        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Liste des dessins enregistrés :");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");

                System.out.println("ID: " + id + ", Type: " + type + ", x: " + x + ", y: " + y);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des dessins : " + e.getMessage());
        }
    }
}
