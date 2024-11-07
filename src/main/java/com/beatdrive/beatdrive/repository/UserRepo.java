package com.beatdrive.beatdrive.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beatdrive.beatdrive.entity.User;

@Repository
public class UserRepo {

    public class UserRepository {
        @Autowired
        private DataSource dataSource;

        public List<User> findAll() {
            List<User> users = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user");

                while (rs.next()) {
                    User user = sqlToUser(rs);
                    users.add(user);
                }
            } catch (SQLException e) {
                System.out.println("Repository Error");
                throw new RuntimeException(e);
            }
            return users;
        }

        public Optional<User> findByEmail(String email) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE email=?");
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    User user = sqlToUser(rs);
                    return Optional.of(user);
                }

            } catch (SQLException e) {
                System.out.println("Repository Error");
                throw new RuntimeException(e);
            }

            return Optional.empty();
        }

        public Optional<User> findById(int id) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE id_user=?");
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    User user = sqlToUser(rs);
                    return Optional.of(user);
                }

            } catch (SQLException e) {
                System.out.println("Repository Error");
                throw new RuntimeException(e);
            }

            return Optional.empty();
        }

        public boolean persist(User user) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO user (nom, prenom, email, password, pseudo, type, adresse_facturation, adresse_livraison, avatar, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, user.getNom());
                stmt.setString(2, user.getPrenom());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getPassword());
                stmt.setString(5, user.getPseudo());
                stmt.setString(6, user.getType());
                stmt.setString(7, user.getAdresse_facturation());
                stmt.setString(8, user.getAdresse_livraison());
                stmt.setString(9, user.getAvatar());
                stmt.setString(10, user.getTelephone());

                if (stmt.executeUpdate() == 1) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        user.setId_user(rs.getInt(1));
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Repository Error");
                throw new RuntimeException(e);
            }

            return false;
        }

        public boolean update(User user) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, password = ?, pseudo = ?, type = ?, adresse_facturation = ?, adresse_livraison = ?, avatar = ?, telephone = ? WHERE id_user = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, user.getNom());
                stmt.setString(2, user.getPrenom());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getPassword());
                stmt.setString(5, user.getPseudo());
                stmt.setString(6, user.getType());
                stmt.setString(7, user.getAdresse_facturation());
                stmt.setString(8, user.getAdresse_livraison());
                stmt.setString(9, user.getAvatar());
                stmt.setString(10, user.getTelephone());
                stmt.setInt(11, user.getId_user());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Repository Error");
                throw new RuntimeException(e);
            }
        }

        public boolean delete(int id) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM user WHERE id_user = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Repository Error");
                throw new RuntimeException(e);
            }
        }

        private User sqlToUser(ResultSet rs) throws SQLException {
            return new User(
                    rs.getInt("id_user"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("pseudo"),
                    rs.getString("type"),
                    rs.getString("adresse_facturation"),
                    rs.getString("adresse_livraison"),
                    rs.getString("avatar"),
                    rs.getString("telephone"));
        }
    }
}
