package com.beatdrive.beatdrive.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beatdrive.beatdrive.entity.Detail_commande;
import com.beatdrive.beatdrive.entity.User;

@Repository
public class Detail_commandeRepo {

    @Autowired
    private DataSource dataSource;

    public List<Detail_commande> findAll() {
        List<Detail_commande> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT detail_commande.*, user.pseudo FROM detail_commande JOIN user ON detail_commande.id_user = user.id_user;");
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToDetailCommande(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des commandes", e);
        }
        return list;
    }

    public Detail_commande findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT detail_commande.*, user.pseudo FROM detail_commande JOIN user ON detail_commande.id_user = user.id_user WHERE detail_commande.id_detail_commande = ?;");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return sqlToDetailCommande(result, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean persist(Detail_commande detailCommande) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO detail_commande (prix_total, nombre_total, date, id_user) VALUES (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, detailCommande.getPrix_total());
            stmt.setString(2, detailCommande.getNombre_total());
            stmt.setObject(3, detailCommande.getDate());
            stmt.setInt(4, detailCommande.getUsers().getId_user());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    detailCommande.setId_detail_commande(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'insertion d'une commande", e);
        }
        return false;
    }

    public boolean update(Detail_commande detailCommande) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE detail_commande SET prix_total = ?, nombre_total = ?, date = ?, id_user = ? WHERE id_detail_commande = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, detailCommande.getPrix_total());
            stmt.setString(2, detailCommande.getNombre_total());
            stmt.setObject(3, detailCommande.getDate());
            stmt.setInt(4, detailCommande.getUsers().getId_user());
            stmt.setInt(5, detailCommande.getId_detail_commande());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la commande", e);
        }
    }

    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection
                    .prepareStatement("DELETE FROM detail_commande WHERE id_detail_commande = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Detail_commande sqlToDetailCommande(ResultSet result, Connection connection) throws SQLException {
        int userId = result.getInt("id_user");
        User user = null;

        try (PreparedStatement userStmt = connection.prepareStatement("SELECT * FROM user WHERE id_user = ?")) {
            userStmt.setInt(1, userId);
            ResultSet userResult = userStmt.executeQuery();
            if (userResult.next()) {
                user = new User(
                        userResult.getInt("id_user"),
                        userResult.getString("nom"),
                        userResult.getString("prenom"),
                        userResult.getString("email"),
                        userResult.getString("password"),
                        userResult.getString("pseudo"),
                        userResult.getString("type"),
                        userResult.getString("adresse_facturation"),
                        userResult.getString("adresse_livraison"),
                        userResult.getString("avatar"),
                        userResult.getString("telephone"));
            }
        }

        return new Detail_commande(
                result.getInt("id_detail_commande"),
                result.getString("prix_total"),
                result.getString("nombre_total"),
                result.getObject("date", LocalDate.class),
                user);
    }
}
