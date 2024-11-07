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
import com.beatdrive.beatdrive.entity.Track;
import com.beatdrive.beatdrive.entity.User;

@Repository
public class Licence_trackRepo {

    @Autowired
    private DataSource dataSource;

    public List<Licence_track> findAll() {
        List<Licence_track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT licence_track.*, track.titre FROM licence_track JOIN track ON licence_track.id_track = track.id_track;");
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
                    .prepareStatement(
                            "SELECT licence_track.*, track.titre FROM licence_track JOIN track ON licence_track.id_track = track.id_track WHERE licence_track.id_licence_track = ?;");
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
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT licence_track.*, track.titre FROM licence_track JOIN track ON licence_track.id_track = track.id_track WHERE licence_track.type = ?;");
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
                    .prepareStatement("DELETE FROM licence_track WHERE id_licence_track = ?");
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
                    "INSERT INTO licence_track (type, prix, id_track) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, licenceTrack.getType());
            stmt.setString(2, licenceTrack.getPrix());
            stmt.setInt(3, licenceTrack.getTrack().getId_track());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    licenceTrack.setId_licence_track(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'insertion d'une licence", e);
        }
        return false;
    }

    public boolean update(Licence_track licenceTrack) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE licence_track SET type = ?, prix = ?, id_track = ? WHERE id_licence_track = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, licenceTrack.getType());
            stmt.setString(2, licenceTrack.getPrix());
            stmt.setInt(3, licenceTrack.getTrack().getId_track());
            stmt.setInt(4, licenceTrack.getId_licence_track());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la licence", e);
        }
    }

    private Licence_track sqlToLicenceTrack(ResultSet result, Connection connection) throws SQLException {
        int trackId = result.getInt("id_track");

        Track track = null;
        try (PreparedStatement trackStmt = connection.prepareStatement("SELECT * FROM track WHERE id_track = ?")) {
            trackStmt.setInt(1, trackId);
            ResultSet trackResult = trackStmt.executeQuery();
            if (trackResult.next()) {

                User user = null;
                int userId = trackResult.getInt("id_user");
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

                track = new Track(
                        result.getInt("id_track"),
                        result.getString("titre"),
                        result.getObject("date", LocalDateTime.class),
                        result.getString("bpm"),
                        result.getString("description"),
                        result.getString("cle"),
                        result.getString("genre"),
                        result.getString("type"),
                        result.getString("audio"),
                        result.getString("statut"),
                        result.getString("cover"),
                        user);
            }

        }

        return new Licence_track(
                result.getInt("id_licence_track"),
                result.getString("type"),
                result.getString("prix"),
                track);
    }
}
