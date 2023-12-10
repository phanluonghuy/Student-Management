package DAO;

import Model.Certificate;

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
    private static final String GET_CERTIFICATES = "SELECT * FROM certificates WHERE student_id = ?";
    private static final String DELETE_CERTIFICATE = "DELETE FROM certificates WHERE certificate_id = ?";
    private static final String ADD_CERTIFICATE = "INSERT INTO `certificates` (`student_id`, `date_create`, `certificate_name`, `certificate_level`, `expired_date`) \n" +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_CERTIFICATE = "UPDATE `certificates` \n" +
            "SET `certificate_name` = ?, `certificate_level` = ?, `expired_date` = ? \n" +
            "WHERE `certificate_id` = ?;";
    public CertificatesDAO() {
        this.databaseRepository = new DatabaseRepository();
    }

    private Connection getConnection() {
        return databaseRepository.getConnection();
    }

    private void CloseConnection(Connection conn) {
        databaseRepository.closeConnection(conn);
    }

    public List<Certificate> getCertificates(String studentID) throws SQLException {
        List<Certificate> certificateList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CERTIFICATES);
            preparedStatement.setString(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String certificate_id = resultSet.getString("certificate_id");
                String student_id = resultSet.getString("student_id");
                Date date_created = resultSet.getDate("date_create");
                String certificate_name = resultSet.getString("certificate_name");
                String certificate_level = resultSet.getString("certificate_level");
                Date expired_date = resultSet.getDate("expired_date");
                certificateList.add(new Certificate(certificate_id, student_id, date_created, certificate_name, certificate_level,expired_date));
            }
            CloseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return certificateList;
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
    public boolean addCertificate(Certificate certificate) {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(ADD_CERTIFICATE);
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            statement.setString(1,certificate.getStudent_id());
            statement.setDate(2,currentDate);
            statement.setString(3,certificate.getCertificate_name());
            statement.setString(4,certificate.getCertificate_level());
            statement.setDate(5,new java.sql.Date(certificate.getExpired_date().getTime()));
            int result = statement.executeUpdate();
            if (result>0) {
                JOptionPane.showMessageDialog(null, "Add Certificate Successfully", "Add Completed", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Add Certificate Failed", "Add Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCertificate(Certificate certificate) {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE_CERTIFICATE);
            java.util.Date utilDate = certificate.getExpired_date();

            statement.setString(1,certificate.getCertificate_name());
            statement.setString(2,certificate.getCertificate_level());
            statement.setDate(3,new java.sql.Date(certificate.getExpired_date().getTime()));
            statement.setString(4,certificate.getCertificate_id());

            int result = statement.executeUpdate();
            if (result>0) {
                JOptionPane.showMessageDialog(null, "Update Certificate Successfully", "Update Completed", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Update Certificate Failed", "Update Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
