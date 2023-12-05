import DAO.LoginHistoryDAO;
import DAO.SiteDAO;
import Model.Accounts;
import Model.History;
import Model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
    private JTextField edtID;
    private JTextField edtName;
    private JTextField edtAge;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField edtSearch;
    private JLabel txtAge;
    private JLabel txtPhone;
    private JLabel txtID;
    private JLabel txtName;
    private JTextField edtPhone;
    private JTextField edtGender;
    private JTextField edtAddress;
    private JTextField edtDOB;
    private JTextField edtGPA;
    private JLabel txtGender;
    private JLabel txtAddress;
    private JLabel txtDOB;
    private JLabel txtGPA;
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
        edtSearch.setForeground(Color.GRAY);
        edtSearch.setText("Search here...");
        showAllUsers();

        buttonLoginHistory.addActionListener(e -> {
            showLoginHistory();
        });
        mangeUsersButton.addActionListener(e -> {
            buttonAddUser.setText("Add User");
            showAllUsers();
        });
        buttonAddUser.addActionListener(e -> {
           if ("Add User".equals(buttonAddUser.getText())){
               addUser();
           } else if ("Add Student".equals(buttonAddUser.getText())) {

           }
        });
        edtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (edtSearch.getText().equals("Search here...")) {
                    edtSearch.setText("");
                    edtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (edtSearch.getText().isEmpty()) {
                    edtSearch.setForeground(Color.GRAY);
                    edtSearch.setText("Search here...");
                }
            }
        });
    }

    public void addUser(){

    }

    public void addEmptyRow(DefaultTableModel tableModel){
        Object[] empRow = null;
        tableModel.addRow(empRow);
    }

    public DefaultTableModel showAllUsers(){
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
        addEmptyRow(tableModel);
        tableData.setModel(tableModel);
        return tableModel;
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
        addEmptyRow(tableModelHistory);
        tableData.setModel(tableModelHistory);
    }
}
