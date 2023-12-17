package DAO;

import Model.Accounts;
import Model.Users;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UsersDAO {
    private DatabaseRepository databaseRepository;

    private static final String GET_USER_BY_ACC_ID = "SELECT * FROM `users`, `accounts` " +
                                                    "WHERE `account_id` = ? " +
                                                    "and `accounts`.`user_id` = `users`.`user_id`";
    private static final String GET_USERS_STATUS = "SELECT * FROM `users` WHERE `user_id` = ?";
    private static final String GET_ACC_SL_BY_USER_ID = "SELECT * FROM `users`, `accounts` " +
            "WHERE `accounts`.`user_id` = `users`.`user_id` " +
            "and `users`.`user_id` = ? ";
    private static final String ADD_USERS = "INSERT INTO `users` values (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_IMG_PROFILE_BY_ACC_ID = "UPDATE `users`, `accounts` set `users`.`image_profile` = ?" +
            " where `users`.`user_id` = `accounts`.`user_id` and `accounts`.`account_id` = ?";
    private static final String DELETE_USERS = "DELETE FROM `users` WHERE `user_id` = ?";
    private static final String UPDATE_USERS = "UPDATE `users` SET `full_name` = ?, `age` = ?, `phone_number` = ?, `image_profile` = ?, `status_user` = ? WHERE `user_id` = ?";

    private AccountsDAO accountsDAO = new AccountsDAO();
    public UsersDAO(){
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection(){
        return databaseRepository.getConnection();
    }

    private void closeConnection(Connection conn){
        databaseRepository.closeConnection(conn);
    }

    public List<Users> getUsers(){
        SiteDAO siteDAO = new SiteDAO();
        return siteDAO.getUser();
    }

    public Accounts getAccountByUserId(String id){
        return accountsDAO.getAccountByUserId(id);
    }

    public void deleteUsers(String id){
        accountsDAO.deleteAccount(id);
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USERS);
            statement.setString(1, id);
            int result = statement.executeUpdate();
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Delete User Successfully", "Delete Completed", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Delete User Failed", "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting accounts", e);
        }
    }

    public void addUsers(Users users, Accounts accounts){
        SiteDAO siteDAO = new SiteDAO();
        String max_id = siteDAO.AUTO_ACC_USER_ID();
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(ADD_USERS);
            statement.setString(1, max_id);
            statement.setString(2, users.getFullname());
            statement.setInt(3, users.getAge());
            statement.setString(4, users.getPhone());
            statement.setString(5, users.getImg_profile());
            statement.setString(6, users.getIsActive());
            int result = statement.executeUpdate();
            accountsDAO.addAccount(accounts);
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Add User Successfully", "Add Completed", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Add User Failed", "Add Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding users", e);
        }
    }

    public void updateUsers(String id, Users users, Accounts accounts){
        accountsDAO.updateAccounts(id, accounts);
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USERS);
            statement.setString(1, users.getFullname());
            statement.setInt(2, users.getAge());
            statement.setString(3, users.getPhone());
            statement.setString(4, users.getImg_profile());
            statement.setString(5, users.getIsActive());
            statement.setString(6, id);
            int result = statement.executeUpdate();
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Update User Successfully", "Update Completed", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Update User Failed", "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating users", e);
        }
    }

    public String getUsersStatus(String id){
        Users users = new Users();
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_USERS_STATUS);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String status = resultSet.getString("status_user");
                users.setIsActive(status);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting users status", e);
        }
        return users.getIsActive();
    }

    public Users getUsersById(String id){
        Users users = null;
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_USERS_STATUS);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String name = resultSet.getString("full_name");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone_number");
                String img = resultSet.getString("image_profile");
                String status = resultSet.getString("status_user");
                users = new Users(id, name, age, phone, img, status);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding users", e);
        }
        return users;
    }

    public void updateUserImgProfileByAccId(String id, String fileName){
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_IMG_PROFILE_BY_ACC_ID);
            statement.setString(1, fileName);
            statement.setString(2, id);

            int result = statement.executeUpdate();
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Update Profile Image Successfully", "Update Completed", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Update Profile Image Failed", "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile image", e);
        }
    }

    public String getStudentListByUserId(String id){
        Accounts accounts = new Accounts();
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ACC_SL_BY_USER_ID);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String student_list = resultSet.getString("student_list_id");
                accounts.setStudent_list_id(student_list);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting users status", e);
        }
        return accounts.getStudent_list_id();
    }

    public Users getUserByAccId(String id){
        Users users = null;
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_USER_BY_ACC_ID);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String user_id = resultSet.getString("user_id");
                String name = resultSet.getString("full_name");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone_number");
                String img = resultSet.getString("image_profile");
                String status = resultSet.getString("status_user");
                users = new Users(user_id, name, age, phone, img, status);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding users", e);
        }
        return users;
    }

}
