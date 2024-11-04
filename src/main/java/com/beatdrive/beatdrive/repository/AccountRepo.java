package com.beatdrive.beatdrive.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.sql.Statement;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import com.beatdrive.beatdrive.entity.account;

@Repository
public class AccountRepo {

    @Autowired
    private DataSource dataSource;

    public Optional<account> findByEmail(String email) {

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE email=?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                account account = sqlToUser(rs);
                return Optional.of(account);
            }

        } catch (SQLException e) {
            System.out.println("Repository Error");
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public Optional<account> findById(int id) {

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE id_account=?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                account account = sqlToUser(rs);
                return Optional.of(account);
            }

        } catch (SQLException e) {
            System.out.println("Repository Error");
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public boolean persist(account account) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO account (nom, prenom, email, password, pseudo, type, adresse_facturation, adresse_livraison, avatar, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, account.getNom());
            stmt.setString(2, account.getPrenom());
            stmt.setString(3, account.getEmail());
            stmt.setString(4, account.getPassword());
            stmt.setString(5, account.getPseudo());
            stmt.setString(6, account.getType());
            stmt.setString(7, account.getAdresse_facturation());
            stmt.setString(8, account.getAdresse_livraison());
            stmt.setString(9, account.getAvatar());
            stmt.setString(10, account.getTelephone());

            if (stmt.executeUpdate() == 1) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    account.setId_account(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Repository Error");
            throw new RuntimeException(e);
        }

        return false;
    }

    private account sqlToUser(ResultSet rs) throws SQLException {
        return new account(
                rs.getInt("id_account"),
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