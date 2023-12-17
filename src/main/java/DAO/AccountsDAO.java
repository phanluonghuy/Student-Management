package DAO;

import Model.Accounts;
import Model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsDAO {
    private DatabaseRepository databaseRepository;
    private static final String CHECK_UNIQUE_USERNAME = "SELECT * FROM `accounts` WHERE `user_name` = ?";
    private static final String GET_ACC_BY_USER_ID = "SELECT * FROM `accounts` WHERE `user_id` = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM `accounts` WHERE `account_id` = ?";
    private static final String ADD_ACCOUNT = "INSERT INTO `accounts` values (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ACCOUNTS = "UPDATE `accounts` SET `user_id` = ?, `student_list_id` = ?, `user_name` = ?, `_password` = ?, `role_id` = ? WHERE `account_id` = ?";

    public AccountsDAO() {
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection() {
        return databaseRepository.getConnection();
    }

    private void closeConnection(Connection conn) {
        databaseRepository.closeConnection(conn);
    }

    public Accounts getAccountByUserId(String id){
        Accounts accounts = null;
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ACC_BY_USER_ID);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String acc_id = resultSet.getString("account_id");
                String student_list = resultSet.getString("student_list_id");
                String username = resultSet.getString("user_name");
                String password = resultSet.getString("_password");
                String role_id = resultSet.getString("role_id");
                accounts = new Accounts(acc_id, id, student_list, username, password, role_id);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting account", e);
        }
        return accounts;
    }

    public void deleteAccount(String id){
        Accounts accounts = getAccountByUserId(id);
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_ACCOUNT);
            statement.setString(1, accounts.getAccount_id());
            int result = statement.executeUpdate();
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting accounts", e);
        }
    }

    public void addAccount(Accounts accounts){
        SiteDAO siteDAO = new SiteDAO();
        String max_id = siteDAO.AUTO_ACC_ID();
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(ADD_ACCOUNT);
            statement.setString(1, max_id);
            statement.setString(2, accounts.getUser_id());
            statement.setString(3, accounts.getStudent_list_id());
            statement.setString(4, accounts.getUser_name());
            statement.setString(5, accounts.getPassword());
            statement.setString(6, accounts.getRole_id());
            int result = statement.executeUpdate();
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding account", e);
        }
    }

    public void updateAccounts(String id, Accounts accounts){
        Accounts account = getAccountByUserId(id);
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_ACCOUNTS);
            statement.setString(1, accounts.getUser_id());
            statement.setString(2, accounts.getStudent_list_id());
            statement.setString(3, accounts.getUser_name());
            statement.setString(4, accounts.getPassword());
            statement.setString(5, accounts.getRole_id());
            statement.setString(6, account.getAccount_id());
            int result = statement.executeUpdate();
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating accounts", e);
        }
    }

    public boolean checkUniqueUsername(String username){
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CHECK_UNIQUE_USERNAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return false;
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error checking username", e);
        }
        return true;
    }

    public boolean checkUniqueUsernameWhileUpdate(String username){
        int count = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CHECK_UNIQUE_USERNAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                count++;
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error checking username", e);
        }
//        System.out.println(count < 2);
        return count >= 2;
    }
}
