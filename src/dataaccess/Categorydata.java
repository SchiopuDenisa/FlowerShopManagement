package dataaccess;

import model.Category;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Categorydata {
    public static void save(Category category) {
        String query = "INSERT INTO \"category\" (\"category_name\") VALUES (?)";  // Corrected query

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, category.getName());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Added Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error. Category may already exist. Please try again.");
        }
    }

    public static ArrayList<Category> getAllRecords(){
        ArrayList<Category> records = new ArrayList<>();
        ResultSet rs = null;
        Connection con = null;
        Statement st = null;

        try {
            con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM category";
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("category_name"));
                records.add(category);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while fetching categories: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error while closing resources: " + e.getMessage());
            }
        }
        return records;
    }
    public static void delete(int id) {
        String query = "DELETE FROM category WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Category deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Category not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting category: " + e.getMessage());
        }
    }

}
