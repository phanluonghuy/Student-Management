package DAO;

import Model.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SiteDAO {

    private DatabaseRepository databaseRepository;
    private static final String GET_USERS = "SELECT * FROM users";
    private static final String MAX_USER_ID = "SELECT MAX(user_id) from users";
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
            try (PreparedStatement statement = conn.prepareStatement(GET_USERS);
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
            throw new RuntimeException("Error getting users", e);
        }

        return userList;
    }
    public String AUTO_ACC_USER_ID(){
        String nextUserID = "";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(MAX_USER_ID);){
            ResultSet resultSet = statement.executeQuery();
            int currentID = 0;

            if(resultSet.next()){
                String maxID = resultSet.getString(1);
                if(maxID != null && maxID.startsWith("user_")){
                    currentID = Integer.parseInt(maxID.substring(5));
                }
            }

            int nextID = currentID + 1;
            nextUserID = "user_" + String.format("%05d", nextID);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return nextUserID;
    }

}
