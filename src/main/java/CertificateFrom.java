import DAO.CertificatesDAO;
import Model.Certificate;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CertificateFrom extends JFrame {
    private JTable tableCer;
    private JPanel panelMain;
    private JButton buttonDelete;
    private JButton addButton;
    private JButton clearButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField textFieldName;
    private JTextField textFieldLevel;
    private JTextField textFieldDateCreated;
    private JTextField textFieldDateExpired;

    private final CertificatesDAO certificatesDAO = new CertificatesDAO();

    private List<Certificate> certificateList;
    private String studentID;

    public CertificateFrom(String studentID) {
        setContentPane(panelMain);
        setTitle("Certificate");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableCer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.studentID = studentID;
        loadData();
        tableCer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = tableCer.getSelectedRow();
                if (row==-1) return;
                textFieldName.setText(tableCer.getValueAt(row, 1).toString());
                textFieldLevel.setText(tableCer.getValueAt(row, 2).toString());
                textFieldDateCreated.setText(tableCer.getValueAt(row,3).toString());
                textFieldDateExpired.setText(tableCer.getValueAt(row, 4).toString());
            }
        });

        deleteButton.addActionListener(e -> {
            if (textFieldName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panelMain,"No certificate selected!");
                return;
            }
            int input = JOptionPane.showConfirmDialog(null,
                    "Do you want to delete " + textFieldName.getText() +" certificate ?" , "Select an Option...", JOptionPane.YES_NO_OPTION);
            if (input == 1) return;
            String Certificate_ID = tableCer.getValueAt(tableCer.getSelectedRow(), 0).toString();
            if (certificatesDAO.deleteCertificate(Certificate_ID)) loadData();
        });

        clearButton.addActionListener(e -> {
            textFieldName.setText("");
            textFieldLevel.setText("");
            textFieldDateCreated.setText("");
            textFieldDateExpired.setText("");
            tableCer.getSelectionModel().clearSelection();
        });
        addButton.addActionListener(e -> {
            if (tableCer.getSelectedRow()!=-1) {
                JOptionPane.showMessageDialog(null, "Please clear the selected certificate", "Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Certificate certificate = new Certificate(studentID,textFieldName.getText(),textFieldLevel.getText(),sdf.parse(textFieldDateExpired.getText()));
                certificatesDAO.addCertificate(certificate);
                loadData();
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Wrong date format", "Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        });
        saveButton.addActionListener(e -> {
            if (tableCer.getSelectedRow()==-1) {
                JOptionPane.showMessageDialog(null, "Please select a certificate", "Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Certificate certificate = new Certificate(tableCer.getValueAt(tableCer.getSelectedRow(), 0).toString(),studentID,textFieldName.getText(),textFieldLevel.getText(),sdf.parse(textFieldDateExpired.getText()));
                System.out.println(certificate.toString());
                certificatesDAO.updateCertificate(certificate);
                loadData();
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Wrong date format", "Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        });

    }

    private void loadData() {
        try {
            certificateList =  certificatesDAO.getCertificates(studentID);
        } catch (SQLException e){
            e.printStackTrace();
        }
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Certificate Name");
        tableModel.addColumn("Certificate Levels");
        tableModel.addColumn("Date Created");
        tableModel.addColumn("Date Expired");
        for (Certificate c : certificateList) {
            tableModel.addRow(new Object[]{c.getCertificate_id(),c.getCertificate_name(),c.getCertificate_level(),c.getDate_created(),c.getExpired_date() });
        }
        tableCer.setModel(tableModel);
    }
}
