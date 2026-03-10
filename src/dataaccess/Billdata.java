package dataaccess;

import model.Bill;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Billdata {

    public static void populateBillDetailsByEmail(String email, JLabel billIdLabel, JTextField nameField, JTextField phoneField, JTextField emailField) {
        String query = "SELECT u.name, u.phone, u.email FROM \"user\" u WHERE u.email = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email);  // Set the email parameter in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    phoneField.setText(rs.getString("phone"));
                    emailField.setText(rs.getString("email"));

                    String nextBillId = Billdata.getId();
                    billIdLabel.setText(nextBillId);
                } else {
                    JOptionPane.showMessageDialog(null, "No user found with the provided email.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching details. Please try again.");
        }
    }


    public static String getId() {
        int id = 1;
        String query = "SELECT MAX(id) FROM bill";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                    id += 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching ID. Please try again.");
        }
        return String.valueOf(id);  // Return the ID as a String
    }

    public static void save(String userEmail, double grandTotal) {
        int userId = getUserIdByEmail(userEmail);
        if (userId == -1) {
            JOptionPane.showMessageDialog(null, "User not found.");
            return;
        }
        String currentDate = java.time.LocalDate.now().toString(); // YYYY-MM-DD format
        int billId = Integer.parseInt(getId());

        String query = "INSERT INTO bill (id, user_id, date, total) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, billId);
            stmt.setInt(2, userId);
            stmt.setString(3, currentDate);
            stmt.setString(4, String.format("%.2f", grandTotal));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Bill saved successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to save bill.");
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for debugging
            JOptionPane.showMessageDialog(null, "Error saving bill. Please try again.");
        }
    }

    private static int getUserIdByEmail(String email) {
        String query = "SELECT id FROM \"user\" WHERE email = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static ArrayList<Bill> getAllRecords(String email, String dateFilter) {
        ArrayList<Bill> bills = new ArrayList<>();
        String query;

        if (dateFilter == null || dateFilter.isEmpty()) {
            query = "SELECT b.id, b.date, b.total " + "FROM bill b " + "JOIN \"user\" u ON b.user_id = u.id " + "WHERE u.email = ?";
        } else {
            query = "SELECT b.id, b.date, b.total " + "FROM bill b " + "JOIN \"user\" u ON b.user_id = u.id " + "WHERE u.email = ? AND b.date LIKE ?";
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email);

            if (dateFilter != null && !dateFilter.isEmpty()) {
                stmt.setString(2, "%" + dateFilter + "%");  // Use LIKE for partial matching
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setId(rs.getInt("id"));
                    bill.setDate(rs.getString("date"));
                    bill.setTotal(rs.getString("total"));
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return bills;
    }

}
