package DAO;

import java.sql.*;

public class DatabaseRepository {
    private static String DB_URL = "jdbc:mysql://localhost:3306/studentmanagement";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";

    public DatabaseRepository() {
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
//            System.out.println("connect successfully!");
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }

    public String login(String username,String password) {
        try {
            Connection connection = getConnection();

            String query = "SELECT * FROM accounts WHERE user_name = ? AND _password = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return "admin";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
