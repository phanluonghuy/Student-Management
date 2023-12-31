package DAO;

import Model.Users;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SiteDAO {

    private DatabaseRepository databaseRepository;
    private static final String GET_USERS = "SELECT * FROM users";
    private static final String MAX_USER_ID = "SELECT MAX(user_id) FROM users";
    private static final String MAX_ACC_ID = "SELECT MAX(account_id) FROM accounts";
    private static final String MAX_STUDENT_ID = "SELECT MAX(student_id) FROM students";
    private static final String MAX_HISTORY_ID = "SELECT MAX(history_id) FROM histories";

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
                return stringHashMap;
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
    public String AUTO_ACC_ID(){
        String nextAccID = "";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(MAX_ACC_ID)){
            ResultSet resultSet = statement.executeQuery();
            int currentID = 0;

            if(resultSet.next()){
                String maxID = resultSet.getString(1);
                if(maxID != null && maxID.startsWith("ACC")){
                    currentID = Integer.parseInt(maxID.substring(3));
                }
            }

            int nextID = currentID + 1;
            nextAccID = "ACC" + String.format("%06d", nextID);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return nextAccID;
    }

    public String AUTO_ACC_USER_ID(){
        String nextUserID = "";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(MAX_USER_ID)){
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

    public String AUTO_STUDENT_ID() {
        String nextStudentID = "";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(MAX_STUDENT_ID)){
            ResultSet resultSet = statement.executeQuery();
            int currentID = 0;

            if(resultSet.next()){
                String maxID = resultSet.getString(1);
                if(maxID != null && maxID.startsWith("STU")){
                    currentID = Integer.parseInt(maxID.substring(3));
                }
            }

            int nextID = currentID + 1;
            nextStudentID = "STU" + String.format("%07d", nextID);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return nextStudentID;
    }
    public String AUTO_HISTORY_ID() {
        String nextHistoryID = "";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(MAX_HISTORY_ID)){
            ResultSet resultSet = statement.executeQuery();
            int currentID = 0;

            if(resultSet.next()){
                String maxID = resultSet.getString(1);
                if(maxID != null && maxID.startsWith("HIST")){
                    currentID = Integer.parseInt(maxID.substring(4));
                }
            }

            int nextID = currentID + 1;
            nextHistoryID = "HIST" + String.format("%06d", nextID);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return nextHistoryID;
    }


}
