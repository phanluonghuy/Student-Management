package DAO;

import Model.Accounts;
import Model.History;
import Model.Roles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginHistoryDAO {

    private DatabaseRepository databaseRepository;
    private static final String GET_ACC_BY_ID = "SELECT * FROM accounts where account_id = ?";
    private static final String GET_ROLE_BY_ID = "SELECT * FROM roles where role_id = ?";
    public LoginHistoryDAO(){
        this.databaseRepository = new DatabaseRepository();
    }
    private Connection getConnection(){
        return databaseRepository.getConnection();
    }

    private void closeConnection(Connection conn){
        databaseRepository.closeConnection(conn);
    }

    //    View login history
    public List<History> getLoginHistory(){
        List<History> historyList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM histories, accounts " +
                    "where histories.account_id = accounts.account_id";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String history_id = resultSet.getString("history_id");
                    String account_id = resultSet.getString("account_id");
                    Timestamp date = resultSet.getTimestamp("date_perform");
                    historyList.add(new History(history_id, account_id, date));
                }
            }
            closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting login history", e);
        }

        return historyList;
    }

    public Accounts getAccountsById(String id){
//        System.out.println(id);
        Accounts accounts = null;

        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ACC_BY_ID);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String user_id = resultSet.getString("user_id");
                String student_list_id = resultSet.getString("student_list_id");
                String user_name = resultSet.getString("user_name");
                String password = resultSet.getString("_password");
                String role_id = resultSet.getString("role_id");
                accounts = new Accounts(id, user_id, student_list_id, user_name, password, role_id);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting accounts", e);
        }

        return accounts;
    }

    public String getRoleByAccId(String id){
        Roles roles = null;

        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ROLE_BY_ID);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role_name = resultSet.getString("role_name");
                roles = new Roles(id, role_name);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting accounts", e);
        }

        return roles.getRole_name();
    }

    public void addLoginHistory(){
        try (Connection conn = getConnection()){
            String query = "INSERT INTO ";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
