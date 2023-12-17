package DAO;

import Model.Roles;
import Model.Users;

import javax.management.relation.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private DatabaseRepository databaseRepository;

    private static final String GET_ROLE = "SELECT * FROM `roles`";
    private static final String GET_ROLE_ID_BY_ROLE_NAME = "SELECT * FROM `roles` WHERE `role_name` = ?";
    private static final String GET_ROLE_BY_USER_ID = "SELECT * FROM `roles`, `accounts`, `users`" +
            " WHERE `accounts`.`role_id` = `roles`.`role_id` and `accounts`.`user_id` = `users`.`user_id`" +
            " AND `users`.`user_id` = ?";
    public RoleDAO(){
        this.databaseRepository = new DatabaseRepository();
    }
    private Connection getConnection(){
        return databaseRepository.getConnection();
    }

    private void closeConnection(Connection conn){
        databaseRepository.closeConnection(conn);
    }

    public List<Roles> getRole(){
        List<Roles> rolesList = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ROLE);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String role_id = resultSet.getString("role_id");
                String role_name = resultSet.getString("role_name");
                rolesList.add(new Roles(role_id, role_name));
            }

            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting roles", e);
        }

        return rolesList;
    }

    public Roles getRoleByUserId(String id){
        Roles roles = null;

        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ROLE_BY_USER_ID);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String role_id = resultSet.getString("role_id");
                String role_name = resultSet.getString("role_name");
                roles = new Roles(role_id, role_name);
            }

            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting roles", e);
        }

        return roles;
    }

    public String getRoleIdByRoleName(String name){
        Roles roles = null;
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_ROLE_ID_BY_ROLE_NAME);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String id = resultSet.getString("role_id");

                roles = new Roles(id, name);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting role", e);
        }
        return roles.getRole_id();
    }

}
