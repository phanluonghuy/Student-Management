package DAO;

import java.sql.*;
import java.util.HashMap;

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

    public HashMap<String,String> login(String username, String password) {
        try {
            Connection connection = getConnection();
            HashMap<String,String> stringHashMap = new HashMap<>();

            String query = "SELECT a.user_name, r.role_name\n" +
                    "FROM accounts a\n" +
                    "JOIN roles r ON a.role_id = r.role_id\n" +
                    "WHERE a.user_name = ? AND a._password = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
//                return new HashMap<resultSet.getString(1), resultSet.getString(2)>;
                stringHashMap.put("user_name", resultSet.getString(1));
                stringHashMap.put("role", resultSet.getString(2));
                return  stringHashMap;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
