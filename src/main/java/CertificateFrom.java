import DAO.CertificatesDAO;
import Model.Certificates;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class CertificateFrom extends JFrame {
    private JTable tableCer;
    private JPanel panelMain;
    private JButton buttonAdd;
    private JButton ButtonUpdate;
    private JButton buttonDelete;

    private final CertificatesDAO certificatesDAO = new CertificatesDAO();

    private List<Certificates> certificatesList;
    private String studentID;

    public CertificateFrom(String studentID) {
        setContentPane(panelMain);
        setTitle("Certificate");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableCer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.studentID = studentID;
        loadData();

        buttonDelete.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null,
                    "Do you want to delete?", "Select an Option...", JOptionPane.YES_NO_OPTION);
            if (input == 1) return;
            String Certificate_ID = tableCer.getValueAt(tableCer.getSelectedRow(), 0).toString();
            if (certificatesDAO.deleteCertificate(Certificate_ID)) loadData();
        });
    }

    private void loadData() {
        try {
            certificatesList =  certificatesDAO.getCertificates(studentID);
        } catch (SQLException e){
            e.printStackTrace();
        }
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Certificate Name");
        tableModel.addColumn("Date Created");
        tableModel.addColumn("Certificate Levels");
        for (Certificates c : certificatesList) {
            tableModel.addRow(new Object[]{c.getCertificate_id(),c.getCertificate_name(),c.getDate_created(),c.getClassification_name() });
        }
        tableCer.setModel(tableModel);
    }
}
