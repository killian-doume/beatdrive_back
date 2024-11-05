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

import com.beatdrive.beatdrive.entity.Licence_track;
import com.beatdrive.beatdrive.entity.Tracks;
import com.beatdrive.beatdrive.entity.User;

@Repository
public class Licence_trackRepo {

    @Autowired
    private DataSource dataSource;

    public List<Licence_track> findAll() {
        List<Licence_track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM licence_tracks");
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToLicenceTrack(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des licences", e);
        }
        return list;
    }

    public Licence_track findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection
                    .prepareStatement("SELECT * FROM licence_tracks WHERE id_licence_tracks = ?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return sqlToLicenceTrack(result, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Licence_track> findByType(String type) {
        List<Licence_track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM licence_tracks WHERE type = ?");
            stmt.setString(1, type);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToLicenceTrack(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des licences par type", e);
        }
        return list;
    }

    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection
                    .prepareStatement("DELETE FROM licence_tracks WHERE id_licence_tracks = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean persist(Licence_track licenceTrack) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO licence_tracks (type, prix, tracks_id) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, licenceTrack.getType());
            stmt.setString(2, licenceTrack.getPrix());
            stmt.setInt(3, licenceTrack.getTracks().getId_tracks());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    licenceTrack.setId_licence_tracks(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'insertion d'une licence", e);
        }
        return false;
    }

    private Licence_track sqlToLicenceTrack(ResultSet result, Connection connection) throws SQLException {
        int trackId = result.getInt("tracks_id");

        Tracks track = null;
        try (PreparedStatement trackStmt = connection.prepareStatement("SELECT * FROM tracks WHERE id_tracks = ?")) {
            trackStmt.setInt(1, trackId);
            ResultSet trackResult = trackStmt.executeQuery();
            if (trackResult.next()) {

                User user = null;
                int userId = trackResult.getInt("users_id");
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

        return new Licence_track(
                result.getInt("id_licence_tracks"),
                result.getString("type"),
                result.getString("prix"),
                track);
    }
}
