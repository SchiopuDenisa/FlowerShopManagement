package dataaccess;

import java.sql.*;

public class DatabaseConnection {
    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/flower_shop";
            String user = "postgres";
            String password = "D3n1s404";

            Connection con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection con = getConnection();
        if (con != null) {
            System.out.println("Connection successful!");
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection failed.");
        }
    }
}