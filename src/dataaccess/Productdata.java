package dataaccess;

import model.Product;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Productdata {
    public static void save(Product product) {
        String query = "INSERT INTO \"product\" (\"name\", \"category_id\", \"price\") " + "VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategory());
            stmt.setString(3, product.getPrice());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Added Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error. Product may already exist. Please try again.");
        }
    }

    public static void update(Product product) {
        String query = "UPDATE product SET name = ?, category_id = ?, price = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategory());
            stmt.setString(3, product.getPrice());
            stmt.setInt(4, product.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Product updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating product: " + e.getMessage());
        }
    }


    public static void delete(int productId) {
        String query = "DELETE FROM product WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, productId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Product deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting product: " + e.getMessage());
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCategory(rs.getInt("category_id"));
                product.setPrice(rs.getString("price"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    public static int getCategoryIdByName(String categoryName) {
        String query = "SELECT id FROM category WHERE category_name = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if category is not found
    }

    public static String getCategoryById(int categoryId) {
        String query = "SELECT category_name FROM category WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("category_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT category_name FROM category";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static Product getProductByName(String name) {
        Product product = null;
        String query = "SELECT * FROM product WHERE name = ?";  // SQL query to fetch product by name

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);  // Set the product name in the query

            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if we have a result
            if (resultSet.next()) {
                // Map the result set to a Product object
                product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setCategory(resultSet.getInt("category_id"));  // Assuming category is a foreign key
                product.setPrice(resultSet.getString("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
}
