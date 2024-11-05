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

import com.beatdrive.beatdrive.entity.Tracks;
import com.beatdrive.beatdrive.entity.User;

@Repository
public class TracksRepo {

    @Autowired
    private DataSource dataSource;

    public List<Tracks> findAll() {
        List<Tracks> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tracks");
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

    public List<Tracks> findLast(int limit) {
        List<Tracks> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tracks ORDER BY date DESC LIMIT ?");
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

    public Tracks findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tracks WHERE id_tracks = ?");
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

    public List<Tracks> findByGenre(String genre) {
        List<Tracks> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tracks WHERE genre = ?");
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
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM tracks WHERE id_tracks = ?");
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean persist(Tracks track) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO tracks (titre, date, bpm, description, cle, genre, type, audio, status, `like`, cover, users_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, track.getTitre());
            stmt.setObject(2, track.getDate());
            stmt.setString(3, track.getBpm());
            stmt.setString(4, track.getDescription());
            stmt.setString(5, track.getCle());
            stmt.setString(6, track.getGenre());
            stmt.setString(7, track.getType());
            stmt.setString(8, track.getAudio());
            stmt.setString(9, track.getStatus());
            stmt.setString(10, track.getLike());
            stmt.setString(11, track.getCover());
            stmt.setInt(12, track.getUsers().getId_user());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    track.setId_tracks(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'insertion d'un track", e);
        }
        return false;
    }

    private Tracks sqlToTracks(ResultSet result, Connection connection) throws SQLException {
        int userId = result.getInt("users_id");
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

        return new Tracks(
                result.getInt("id_tracks"),
                result.getString("titre"),
                result.getObject("date", LocalDateTime.class),
                result.getString("bpm"),
                result.getString("description"),
                result.getString("cle"),
                result.getString("genre"),
                result.getString("type"),
                result.getString("audio"),
                result.getString("status"),
                result.getString("like"),
                result.getString("cover"),
                user);
    }
}
