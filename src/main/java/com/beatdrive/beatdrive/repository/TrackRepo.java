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

import com.beatdrive.beatdrive.entity.Track;
import com.beatdrive.beatdrive.entity.User;

@Repository
public class TrackRepo {

    @Autowired
    private DataSource dataSource;

    public List<Track> findAll() {
        List<Track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT track.*, User.pseudo FROM track JOIN User ON track.id_user = User.id_user;");
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToTracks(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des tracks", e);
        }
        return list;
    }

    public List<Track> findLast(int limit) {
        List<Track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT track.*, User.pseudo FROM track JOIN User ON track.id_user = User.id_user ORDER BY track.date DESC LIMIT ?; ");
            stmt.setInt(1, limit);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToTracks(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des derniers tracks", e);
        }
        return list;
    }

    public Track findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT track.*, User.pseudo FROM track JOIN User ON track.id_user = User.id_user WHERE track.id_track = ?;");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return sqlToTracks(result, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Track> findByGenre(String genre) {
        List<Track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT track.*, User.pseudo FROM track JOIN User ON track.id_user = User.id_user WHERE track.genre = ?;");
            stmt.setString(1, genre);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToTracks(result, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des tracks par genre", e);
        }
        return list;
    }

    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM track WHERE id_track = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean persist(Track track) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO track (titre, date, bpm, description, cle, genre, type, audio, status, cover, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, track.getTitre());
            stmt.setObject(2, track.getDate());
            stmt.setString(3, track.getBpm());
            stmt.setString(4, track.getDescription());
            stmt.setString(5, track.getCle());
            stmt.setString(6, track.getGenre());
            stmt.setString(7, track.getType());
            stmt.setString(8, track.getAudio());
            stmt.setString(9, track.getStatut());
            stmt.setString(11, track.getCover());
            stmt.setInt(12, track.getUser().getId_user());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    track.setId_track(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'insertion d'un track", e);
        }
        return false;
    }

    public boolean update(Track track) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE track SET titre = ?, date = ?, bpm = ?, description = ?, cle = ?, genre = ?, type = ?, audio = ?, statut = ?, cover = ?, id_user = ? WHERE id_track = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, track.getTitre());
            stmt.setObject(2, track.getDate());
            stmt.setString(3, track.getBpm());
            stmt.setString(4, track.getDescription());
            stmt.setString(5, track.getCle());
            stmt.setString(6, track.getGenre());
            stmt.setString(7, track.getType());
            stmt.setString(8, track.getAudio());
            stmt.setString(9, track.getStatut());
            stmt.setString(10, track.getCover());
            stmt.setInt(11, track.getUser().getId_user());
            stmt.setInt(12, track.getId_track());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du track", e);
        }
    }

    private Track sqlToTracks(ResultSet result, Connection connection) throws SQLException {
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

        return new Track(
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