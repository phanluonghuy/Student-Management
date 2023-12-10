package DAO;

import Model.Students;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentsDAO {
    private static final String ADD_STUDENTS = "INSERT INTO students VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATES_TOTAL = "UPDATE studentlist SET total= ? WHERE student_list_id= ?";
    private static final String DELETE_STUDENTS = "DELETE FROM students WHERE student_id = ?";
    private static final String UPDATE_STUDENTS = "UPDATE students SET full_name= ?, birthday= ?, gender = ?, home_address= ?, phone_number = ?, gpa= ? WHERE student_id = ?";
    private DatabaseRepository databaseRepository;
    private static final String GET_STUDENTS = "SELECT * FROM students";

    private static final String GET_STUDENTS_BY_ID = "SELECT * FROM students WHERE student_id = ?";

    public StudentsDAO() {
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection() {
        return databaseRepository.getConnection();
    }

    private void CloseConnection(Connection conn) {
        databaseRepository.closeConnection(conn);
    }

    public List<Students> getStudentList() {
        return getStudentsFromDb();
    }

    public void closeConnection(Connection connection) {databaseRepository.closeConnection(connection);}

    //Methods getStudentList
    public List<Students> getStudentsFromDb() {
        List<Students> students = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENTS);
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                    while (resultSet.next()) {
                        String student_id = resultSet.getString("student_id");
                        String student_list_id = resultSet.getString("student_list_id");
                        String fullName = resultSet.getString("full_name");
                        Date birthDay = resultSet.getDate("birthDay");
                        String gender = resultSet.getString("gender");
                        String home_address = resultSet.getString("home_address");
                        String phone = resultSet.getString("phone_number");
                        float gpa = resultSet.getFloat("gpa");
                        students.add(new Students(student_id, student_list_id, fullName, birthDay, gender,home_address,phone,gpa));
                    }
            }
            CloseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    public void addStudent(Students student) {
        SiteDAO siteDAO = new SiteDAO();
        String new_id = siteDAO.AUTO_STUDENT_ID();
        try(Connection conn = getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(ADD_STUDENTS);
            preparedStatement.setString(1, new_id);
            preparedStatement.setString(2, student.getStudent_list_id());
            preparedStatement.setString(3, student.getFull_name());
            Date utilDate = student.getBirthday();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, student.getGender());
            preparedStatement.setString(6, student.getHome_address());
            preparedStatement.setString(7, student.getPhone());
            preparedStatement.setFloat(8, student.getGPA());

            int result = preparedStatement.executeUpdate();
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Add Student Successfully", "Add Completed", JOptionPane.INFORMATION_MESSAGE);
                updateTotalList();
            }else{
                JOptionPane.showMessageDialog(null, "Add Student Failed", "Add Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTotalList() {
        int size = getStudentsFromDb().size();
        try(Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATES_TOTAL);
            preparedStatement.setInt(1, size);
            preparedStatement.setString(2,"SL001");
            int result = preparedStatement.executeUpdate();
            closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudent(String id) {
        System.out.println(id);
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_STUDENTS);
            statement.setString(1, id);
            int result = statement.executeUpdate();
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Delete Student Successfully", "Delete Completed", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Delete Student Failed", "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error Deleting student", e);
        }
    }

    public void updateStudent(Students student) {
        try (Connection conn = getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_STUDENTS);
            preparedStatement.setString(1, student.getFull_name());
            Date utilDate = student.getBirthday();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setString(3, student.getGender());
            preparedStatement.setString(4, student.getHome_address());
            preparedStatement.setString(5, student.getPhone());
            preparedStatement.setFloat(6, student.getGPA());
            preparedStatement.setString(7, student.getStudent_id());
            int result = preparedStatement.executeUpdate();
            if (result > 0){
                JOptionPane.showMessageDialog(null, "Update Student Successfully", "Update Completed", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Update Student Failed", "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Student", e);
        }
    }

    public Students getStudentById(String id){
        Students students = null;
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(GET_STUDENTS_BY_ID);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String student_list_id = resultSet.getString("student_list_id");
                String fullName = resultSet.getString("full_name");
                Date birthDay = resultSet.getDate("birthDay");
                String gender = resultSet.getString("gender");
                String home_address = resultSet.getString("home_address");
                String phone = resultSet.getString("phone_number");
                float gpa = resultSet.getFloat("gpa");
                students = new Students(id, student_list_id, fullName, birthDay, gender, home_address, phone, gpa);
            }
            closeConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding users", e);
        }
        return students;
    }
}
