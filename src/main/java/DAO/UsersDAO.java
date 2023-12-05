package DAO;

import Model.Users;

import java.sql.Connection;

public class UsersDAO {
    private DatabaseRepository databaseRepository;
    public UsersDAO(){
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection(){
        return databaseRepository.getConnection();
    }

    private void closeConnection(Connection conn){
        databaseRepository.closeConnection(conn);
    }

    public Users getUsers(){

        return new Users();
    }
}
