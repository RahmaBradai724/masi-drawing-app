package com.masi.persistence;

import com.masi.Controller.DrawingController.DrawnShape;
import com.masi.Db.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabasePersistenceStrategy implements DrawingPersistenceStrategy {
    @Override
    public void saveDrawing(String drawingName, List<DrawnShape> shapes) {
        Connection conn = DatabaseManager.getInstance().getConnexion();
        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible");
            return;
        }

        try {
            conn.setAutoCommit(false); // Start transaction

            // Insert complete drawing
            String sqlDessinComplet = "INSERT INTO dessins_complets (nom) VALUES (?)";
            int dessinCompletId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlDessinComplet, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, drawingName);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    dessinCompletId = rs.getInt(1);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID du dessin complet");
                }
            }

            // Save each shape and link to the complete drawing
            for (DrawnShape shape : shapes) {
                // Save shape to dessins table
                String sqlForme = "INSERT INTO dessins (type, x, y, width, height) VALUES (?, ?, ?, ?, ?)";
                int formeId;
                try (PreparedStatement stmt = conn.prepareStatement(sqlForme, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, shape.getType());
                    stmt.setInt(2, (int) shape.getX());
                    stmt.setInt(3, (int) shape.getY());
                    stmt.setInt(4, (int) shape.getWidth());
                    stmt.setInt(5, (int) shape.getHeight());
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        formeId = rs.getInt(1);
                    } else {
                        throw new SQLException("Échec de la récupération de l'ID de la forme");
                    }
                }

                // Link shape to complete drawing
                String sqlLien = "INSERT INTO dessins_formes (dessin_complet_id, forme_id, decorator) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlLien)) {
                    stmt.setInt(1, dessinCompletId);
                    stmt.setInt(2, formeId);
                    stmt.setString(3, shape.getDecorator());
                    stmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Dessin complet '" + drawingName + "' sauvegardé avec succès");
        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            System.err.println("Erreur lors de la sauvegarde du dessin complet : " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la réactivation de l'auto-commit : " + e.getMessage());
            }
        }
    }

    @Override
    public List<DrawnShape> loadDrawing(int drawingId) {
        List<DrawnShape> shapes = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnexion();
        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible");
            return shapes;
        }

        try {
            String sql = "SELECT d.type, d.x, d.y, d.width, d.height, df.decorator " +
                    "FROM dessins d JOIN dessins_formes df ON d.id = df.forme_id " +
                    "WHERE df.dessin_complet_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, drawingId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("type");
                    double x = rs.getInt("x");
                    double y = rs.getInt("y");
                    double width = rs.getInt("width");
                    double height = rs.getInt("height");
                    String decorator = rs.getString("decorator");
                    shapes.add(new DrawnShape(type, x, y, width, height, decorator));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement du dessin complet : " + e.getMessage());
        }
        return shapes;
    }

    @Override
    public List<String> listDrawings() {
        List<String> dessins = new ArrayList<>();
        Connection conn = DatabaseManager.getInstance().getConnexion();
        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible");
            return dessins;
        }

        String sql = "SELECT id, nom, date_creation FROM dessins_complets ORDER BY date_creation DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Timestamp date = rs.getTimestamp("date_creation");
                dessins.add(String.format("ID: %d | Nom: %s | Créé le: %s", id, nom, date));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la liste des dessins complets : " + e.getMessage());
        }
        return dessins;
    }

    @Override
    public int getDrawingIdByName(String drawingName) {
        Connection conn = DatabaseManager.getInstance().getConnexion();
        if (conn == null) {
            System.err.println("Connexion à la base de données non disponible");
            return -1;
        }

        String sql = "SELECT id FROM dessins_complets WHERE nom = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, drawingName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du dessin : " + e.getMessage());
        }
        return -1; // Return -1 if no drawing is found
    }
}