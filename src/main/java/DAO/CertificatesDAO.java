package DAO;

import Model.Certificates;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificatesDAO {
    private DatabaseRepository databaseRepository;
    private static final String GET_CERTIFICATES = "SELECT c.*, clf.classification_name\n" +
            "FROM certificates c\n" +
            "JOIN classifications clf ON c.classification_id = clf.classification_id\n" +
            "WHERE c.student_id = ?;";
    private static final String DELETE_CERTIFICATE = "DELETE FROM certificates WHERE certificate_id = ?";
    public CertificatesDAO() {
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection() {
        return databaseRepository.getConnection();
    }

    private void CloseConnection(Connection conn) {
        databaseRepository.closeConnection(conn);
    }

    public List<Certificates> getCertificates(String studentID) throws SQLException {
        List<Certificates> certificatesList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CERTIFICATES);
            preparedStatement.setString(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String certificate_id = resultSet.getString("certificate_id");
                String classification_id = resultSet.getString("classification_id");
                String student_id = resultSet.getString("student_id");
                String account_id = resultSet.getString("account_id");
                Date date_created = resultSet.getDate("date_create");
                String certificate_name = resultSet.getString("certificate_name");
                String classification_name = resultSet.getString("classification_name");
                certificatesList.add(new Certificates(certificate_id, classification_id, student_id, account_id, date_created, certificate_name, classification_name));
            }
            CloseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return certificatesList;
    }
    public boolean deleteCertificate(String certificateID) {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(DELETE_CERTIFICATE);
            statement.setString(1,certificateID);
            int result = statement.executeUpdate();
            if (result>0) {
                JOptionPane.showMessageDialog(null, "Delete Certificate Successfully", "Delete Completed", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Delete Certificate Failed", "Delete Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
