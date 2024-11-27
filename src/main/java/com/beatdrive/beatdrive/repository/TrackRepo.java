package com.beatdrive.beatdrive.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beatdrive.beatdrive.entity.Track;

@Repository
public class TrackRepo {

    @Autowired
    private DataSource dataSource;

    // Récupérer tous les tracks
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
            logError("Erreur lors de la récupération des tracks", e);
            throw new RuntimeException("Erreur lors de la récupération des tracks", e);
        }
        return list;
    }

    // Récupérer les derniers tracks
    public List<Track> findLast(int limit) {
        List<Track> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT track.*, User.pseudo FROM track JOIN User ON track.id_user = User.id_user ORDER BY track.date DESC LIMIT ?;");
            stmt.setInt(1, limit);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                list.add(sqlToTracks(result, connection));
            }
        } catch (SQLException e) {
            logError("Erreur lors de la récupération des derniers tracks", e);
            throw new RuntimeException("Erreur lors de la récupération des derniers tracks", e);
        }
        return list;
    }

    // Méthode pour récupérer un track (morceau) à partir de son ID
    public Track findById(int id) {
        // Établir une connexion à la base de données en utilisant le DataSource
        try (Connection connection = dataSource.getConnection()) {
            // Préparer une requête SQL pour récupérer le track et le pseudo de
            // l'utilisateur associé
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT track.*, User.pseudo FROM track JOIN User ON track.id_user = User.id_user WHERE track.id_track = ?;");
            // Assigner l'ID du track à la requête SQL
            stmt.setInt(1, id);
            // Exécuter la requête et récupérer le résultat
            ResultSet result = stmt.executeQuery();
            // Vérifier si un résultat est trouvé
            if (result.next()) {
                // Convertir le résultat en un objet Track et le retourner
                return sqlToTracks(result, connection);
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQL et enregistrer un message d'erreur dans les logs
            logError("Erreur lors de la récupération du track avec l'ID : " + id, e);
        }
        // Retourner null si aucun track n'a été trouvé ou en cas d'erreur
        return null;
    }

    // Récupérer tous les tracks d'un utilisateur
    public List<Track> findByUserId(int idUser) {
        List<Track> tracks = new ArrayList<>();
        String query = "SELECT track.*, User.pseudo " +
                "FROM track " +
                "JOIN User ON track.id_user = User.id_user " +
                "WHERE User.id_user = ?;";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idUser);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                tracks.add(sqlToTracks(result, connection));
            }
        } catch (SQLException e) {
            logError("Erreur lors de la récupération des tracks pour l'utilisateur avec l'ID : " + idUser, e);
        }
        return tracks;
    }

    // Récupérer les tracks par genre
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
            logError("Erreur lors de la récupération des tracks par genre : " + genre, e);
            throw new RuntimeException("Erreur lors de la récupération des tracks par genre", e);
        }
        return list;
    }

    // Méthode pour supprimer un track (morceau) à partir de son ID
    public boolean delete(int id) {
        // Établir une connexion à la base de données en utilisant le DataSource
        try (Connection connection = dataSource.getConnection()) {
            // Préparer une requête SQL pour supprimer un track spécifique par son ID
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM track WHERE id_track = ?");
            // Assigner l'ID du track à la requête SQL
            stmt.setInt(1, id);
            // Exécuter la requête et récupérer le nombre de lignes affectées
            int result = stmt.executeUpdate();
            // Retourner true si une ou plusieurs lignes ont été affectées (le track a été
            // supprimé)
            return result > 0;
        } catch (SQLException e) {
            // Gérer les exceptions SQL et enregistrer un message d'erreur dans les logs
            logError("Erreur lors de la suppression du track avec l'ID : " + id, e);
        }

        // Retourner false si la suppression a échoué ou en cas d'erreur
        return false;
    }

    public boolean persist(Track track) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO track (titre, date, bpm, description, cle, genre, type, audio, statut, cover, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, track.getTitre());
            stmt.setObject(2, track.getDate() != null ? java.sql.Date.valueOf(track.getDate()) : null);
            stmt.setString(3, track.getBpm());
            stmt.setString(4, track.getDescription());
            stmt.setString(5, track.getCle());
            stmt.setString(6, track.getGenre());
            stmt.setString(7, track.getType());
            stmt.setString(8, track.getAudio());
            stmt.setString(9, track.getStatut());
            stmt.setString(10, track.getCover());
            stmt.setInt(11, track.getId_user());

            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    track.setId_track(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du track", e);
        }
        return false;
    }

    // Mettre à jour un track
    public boolean update(Track track) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE track SET titre = ?, date = ?, bpm = ?, description = ?, cle = ?, genre = ?, type = ?, audio = ?, statut = ?, cover = ?, id_user = ? WHERE id_track = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, track.getTitre());
            stmt.setDate(2, track.getDate() != null ? java.sql.Date.valueOf(track.getDate()) : null);
            stmt.setString(3, track.getBpm());
            stmt.setString(4, track.getDescription());
            stmt.setString(5, track.getCle());
            stmt.setString(6, track.getGenre());
            stmt.setString(7, track.getType());
            stmt.setString(8, track.getAudio());
            stmt.setString(9, track.getStatut());
            stmt.setString(10, track.getCover());
            stmt.setInt(11, track.getId_user());

            stmt.setInt(12, track.getId_track());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logError("Erreur lors de la mise à jour du track", e);
            throw new RuntimeException("Erreur lors de la mise à jour du track", e);
        }
    }

    // Mapper les résultats SQL vers un objet Track
    private Track sqlToTracks(ResultSet result, Connection connection) throws SQLException {
        int userId = result.getInt("id_user");
        try (PreparedStatement userStmt = connection.prepareStatement("SELECT * FROM user WHERE id_user = ?")) {
            userStmt.setInt(1, userId);
            ResultSet userResult = userStmt.executeQuery();
            if (userResult.next()) {
            }
        }

        return new Track(
                result.getInt("id_track"),
                result.getString("titre"),
                result.getDate("date").toLocalDate(),
                result.getString("bpm"),
                result.getString("description"),
                result.getString("cle"),
                result.getString("genre"),
                result.getString("type"),
                result.getString("audio"),
                result.getString("statut"),
                result.getString("cover"),
                result.getInt("id_user"));

    }

    private void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
