import DAO.DatabaseRepository;
import DAO.LoginHistoryDAO;
import DAO.SiteDAO;
import Model.Accounts;
import Model.History;
import Model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JButton buttonSetting;
    private JButton buttonReports;
    private JButton buttonLoginHistory;
    private JButton buttonManagerStudent;
    private JButton buttonDeleteUser;
    private JButton buttonAddUser;
    private JButton buttonUpdateUser;
    private JLabel labelName;
    private JLabel labelRole;
    private JTable tableData;
    private JButton mangeUsersButton;
    private SiteDAO siteDAO = new SiteDAO();
    private LoginHistoryDAO loginHistoryDAO = new LoginHistoryDAO();

    public MainForm(HashMap<String,String> user) {
        setContentPane(panelMain);
        setTitle("ADMIN");
        labelName.setText("Hi, " + user.get("user_name"));
        labelRole.setText("You are logged as " + user.get("role"));
        setSize(1200,700);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showAllUsers();
//        DefaultTableModel tableModel = new DefaultTableModel();
//
//        tableModel.addColumn("User ID");
//        tableModel.addColumn("Full Name");
//        tableModel.addColumn("Age");
//        tableModel.addColumn("Phone");
//        tableModel.addColumn("Image Profile");
//        tableModel.addColumn("Is Active");
//
//        List<Users> user_data = siteDAO.getUser();
//
//        for (Users u : user_data) {
//            tableModel.addRow(new Object[]{u.getUser_id(), u.getFullname(), u.getAge(),
//                    u.getPhone(), u.getImg_profile(), u.getIsActive()});
//        }
//        tableData.setModel(tableModel);

        buttonLoginHistory.addActionListener(e -> {
            showLoginHistory();
        });
        mangeUsersButton.addActionListener(e -> {
            showAllUsers();
        });
    }

    public void showAllUsers(){
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("User ID");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Age");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Image Profile");
        tableModel.addColumn("Is Active");

        List<Users> user_data = siteDAO.getUser();

        for (Users u : user_data) {
            tableModel.addRow(new Object[]{u.getUser_id(), u.getFullname(), u.getAge(),
                    u.getPhone(), u.getImg_profile(), u.getIsActive()});
        }
        tableData.setModel(tableModel);
    }

    public void showLoginHistory(){
        DefaultTableModel tableModelHistory = new DefaultTableModel();
        tableModelHistory.addColumn("History ID");
        tableModelHistory.addColumn("User Name");
        tableModelHistory.addColumn("Password");
        tableModelHistory.addColumn("Role Id");
        tableModelHistory.addColumn("Date Perform");

        List<History> login_history = loginHistoryDAO.getLoginHistory();

        Accounts accounts = new Accounts();
        for (History history : login_history) {
            accounts = loginHistoryDAO.getAccountsById(history.getAccount_id());

            tableModelHistory.addRow(new Object[]{history.getHistory_id(), accounts.getUser_name(), accounts.getPassword(),
                    accounts.getRole_id(), history.getDate_perform()});
        }
        tableData.setModel(tableModelHistory);
    }

}
