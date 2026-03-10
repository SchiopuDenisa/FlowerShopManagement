package dataaccess;

import model.User;
import javax.swing.JOptionPane;

import java.sql.*;
import java.util.ArrayList;

public class Userdata {
    public static void save(User user) {
        String query = "INSERT INTO \"user\" (\"name\", \"email\", \"phone\", \"address\", \"password\", \"status\") "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, "false");

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Registered Successfully! Wait for admin approval!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error registering user. Please try again.");
        }
    }

    public static User login(String email, String password) {
        User user = null;
        String query = "SELECT * FROM \"user\" WHERE \"email\" = ? AND \"password\" = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setStatus(rs.getString("status"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
        return user;
    }

    public static ArrayList<User> getAllRecords(String email) {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM \"user\" WHERE \"email\" LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, "%" + email + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setPassword(rs.getString("password"));
                    user.setStatus(rs.getString("status"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return users;
    }

    public static void changeStatus(String email, String status) {
        String query = "UPDATE \"user\" SET \"status\" = ? WHERE \"email\" = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Status updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No user found with the given email.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean changePassword(String email, String oldPassword, String newPassword) {
        String selectQuery = "SELECT * FROM \"user\" WHERE \"email\" = ? AND \"password\" = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {

            selectStmt.setString(1, email);
            selectStmt.setString(2, oldPassword);

            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String updateQuery = "UPDATE \"user\" SET \"password\" = ? WHERE \"email\" = ?";

                try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setString(2, email);

                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect old password.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}

