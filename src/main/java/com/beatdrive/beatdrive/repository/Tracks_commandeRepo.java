package com.beatdrive.beatdrive.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beatdrive.beatdrive.entity.Details_commande;
import com.beatdrive.beatdrive.entity.Licence_track;
import com.beatdrive.beatdrive.entity.Tracks;
import com.beatdrive.beatdrive.entity.Tracks_commande;
import com.beatdrive.beatdrive.entity.User;

@Repository
public class Tracks_commandeRepo {

    @Autowired
    private DataSource dataSource;

    // CREATE
    public boolean persist(Tracks_commande tracksCommande) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO tracks_commande (licence_tracks_id, details_commande_id) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, tracksCommande.getLicence_tracks().getId_licence_tracks());
            stmt.setInt(2, tracksCommande.getDetails_commande().getId_detail_commande());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    tracksCommande.setId_tracks_commande(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'insertion d'une commande de track", e);
        }
        return false;
    }

    // READ ALL
    public List<Tracks_commande> findAll() {
        List<Tracks_commande> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tracks_commande");
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToTracksCommande(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des commandes de tracks", e);
        }
        return list;
    }

    // READ BY ID
    public Tracks_commande findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection
                    .prepareStatement("SELECT * FROM tracks_commande WHERE id_tracks_commande = ?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return sqlToTracksCommande(result, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean update(Tracks_commande tracksCommande) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE tracks_commande SET licence_tracks_id = ?, details_commande_id = ? WHERE id_tracks_commande = ?");
            stmt.setInt(1, tracksCommande.getLicence_tracks().getId_licence_tracks());
            stmt.setInt(2, tracksCommande.getDetails_commande().getId_detail_commande());
            stmt.setInt(3, tracksCommande.getId_tracks_commande());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour d'une commande de track", e);
        }
    }

    // DELETE
    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection
                    .prepareStatement("DELETE FROM tracks_commande WHERE id_tracks_commande = ?");
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode utilitaire pour transformer un ResultSet en un objet Tracks_commande
    private Tracks_commande sqlToTracksCommande(ResultSet result, Connection connection) throws SQLException {
        int licenceTrackId = result.getInt("licence_tracks_id");
        int detailsCommandeId = result.getInt("details_commande_id");

        // Récupérer l'objet Licence_track associé
        Licence_track licenceTrack = null;
        try (PreparedStatement licenceStmt = connection
                .prepareStatement("SELECT * FROM licence_tracks WHERE id_licence_tracks = ?")) {
            licenceStmt.setInt(1, licenceTrackId);
            ResultSet licenceResult = licenceStmt.executeQuery();
            if (licenceResult.next()) {
                // Récupérer l'objet Track et User pour Licence_track
                Tracks track = null;
                int trackId = licenceResult.getInt("tracks_id");
                try (PreparedStatement trackStmt = connection
                        .prepareStatement("SELECT * FROM tracks WHERE id_tracks = ?")) {
                    trackStmt.setInt(1, trackId);
                    ResultSet trackResult = trackStmt.executeQuery();
                    if (trackResult.next()) {
                        // Récupérer l'utilisateur associé au Track
                        User user = null;
                        int userId = trackResult.getInt("users_id");
                        try (PreparedStatement userStmt = connection
                                .prepareStatement("SELECT * FROM user WHERE id_user = ?")) {
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
                        track = new Tracks(
                                trackResult.getInt("id_tracks"),
                                trackResult.getString("titre"),
                                trackResult.getObject("date", LocalDateTime.class),
                                trackResult.getString("bpm"),
                                trackResult.getString("description"),
                                trackResult.getString("cle"),
                                trackResult.getString("genre"),
                                trackResult.getString("type"),
                                trackResult.getString("audio"),
                                trackResult.getString("status"),
                                trackResult.getString("like"),
                                trackResult.getString("cover"),
                                user);
                    }
                }
                licenceTrack = new Licence_track(
                        licenceResult.getInt("id_licence_tracks"),
                        licenceResult.getString("type"),
                        licenceResult.getString("prix"),
                        track);
            }
        }

        // Récupérer l'objet Details_commande associé
        Details_commande detailsCommande = null;
        try (PreparedStatement detailsStmt = connection
                .prepareStatement("SELECT * FROM details_commande WHERE id_details_commande = ?")) {
            detailsStmt.setInt(1, detailsCommandeId);
            ResultSet detailsResult = detailsStmt.executeQuery();
            if (detailsResult.next()) {
                detailsCommande = new Details_commande(
                        detailsResult.getInt("id_details_commande"),
                        detailsResult.getString("commande"),
                        null, detailsResult.getDate("date_commande").toLocalDate(), null);
            }
        }

        return new Tracks_commande(
                result.getInt("id_tracks_commande"),
                licenceTrack,
                detailsCommande);
    }
}
