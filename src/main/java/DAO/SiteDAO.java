package DAO;

import Model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SiteDAO {

    private DatabaseRepository databaseRepository;
    public SiteDAO(){
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection(){
        return databaseRepository.getConnection();
    }

    private void closeConnection(Connection conn){
        databaseRepository.closeConnection(conn);
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
            closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Users> getUser() {
        List<Users> userList = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String id = resultSet.getString("user_id");
                    String name = resultSet.getString("full_name");
                    int age = resultSet.getInt("age");
                    String phone = resultSet.getString("phone_number");
                    String img = resultSet.getString("image_profile");
                    String active = resultSet.getString("status_user");
                    userList.add(new Users(id, name, age, phone, img, active));
                }
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users from the database", e);
        }

        return userList;
    }
}
