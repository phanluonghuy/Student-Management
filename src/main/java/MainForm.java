import DAO.LoginHistoryDAO;
import DAO.SiteDAO;
import DAO.StudentsDAO;
import DAO.UsersDAO;
import Model.Accounts;
import Model.History;
import Model.Students;
import Model.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JButton buttonLogout;
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
    private JTextField edtGender_Img;
    private JTextField edtAddress_Status;
    private JTextField edtDOB;
    private JTextField edtGPA;
    private JLabel txtGender_Img;
    private JLabel txtAddress_Status;
    private JLabel txtDOB;
    private JLabel txtGPA;
    private JButton clearButton;
    private JLabel imgView;
    private JButton importStudentButton;
    private JButton manageCertificateButton;
    private JButton exportStudentButton;
    private SiteDAO siteDAO = new SiteDAO();
    private UsersDAO usersDAO = new UsersDAO();
    private LoginHistoryDAO loginHistoryDAO = new LoginHistoryDAO();
    private StudentsDAO studentsDAO = new StudentsDAO();

    public MainForm(HashMap<String,String> user) {
        setContentPane(panelMain);
        setTitle("ADMIN");
        labelName.setText("Hi, " + user.get("user_name"));
        labelRole.setText("You are logged as " + user.get("role"));
        setSize(1200,700);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edtSearch.setForeground(Color.GRAY);
        edtSearch.setText("Search here...");
        searchPlaceHolder();
        onChangeManageData("user");
        showAllUsers();
        fillData();
        disabledField();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>();
        sorter.setModel(showAllUsers());
        tableData.setRowSorter(sorter);


        buttonAddUser.addActionListener(e -> {
            if ("Add User".equals(buttonAddUser.getText())){
                addUser();
            } else if ("Add Student".equals(buttonAddUser.getText())) {
                addStudent();
            }
        });

        buttonDeleteUser.addActionListener(e -> {
            if ("Delete User".equals(buttonDeleteUser.getText())){
                int selectRow = tableData.getSelectedRow();
                if (selectRow != -1){
                    deleteUser(tableData.getValueAt(selectRow, 0).toString());
                }else{
                    JOptionPane.showMessageDialog(this, "There is no data to delete", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else if ("Delete Student".equals(buttonDeleteUser.getText())) {
                int selectRow = tableData.getSelectedRow();
                if(selectRow != - 1) {
                    deleteStudent(tableData.getValueAt(selectRow, 0).toString());
                }else {
                    JOptionPane.showMessageDialog(this, "There is no data to delete", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonUpdateUser.addActionListener(e -> {
            if ("Update User".equals(buttonUpdateUser.getText())){
                int selectRow = tableData.getSelectedRow();
                if(selectRow != -1){
                    updateUser(tableData.getValueAt(selectRow, 0).toString());
                }
            } else if ("Update Student".equals(buttonUpdateUser.getText())) {
                int selectRow = tableData.getSelectedRow();
                if(selectRow != -1) {
                    updateStudent(tableData.getValueAt(selectRow, 0).toString());
                }
            }
        });

        clearButton.addActionListener(e -> {
            clearEdtText();
        });


        mangeUsersButton.addActionListener(e -> {
            onChangeManageData("user");
            showAllUsers();
            sorter.setModel(showAllUsers());
            tableData.setRowSorter(sorter);
        });

        buttonManagerStudent.addActionListener(e -> {
            onChangeManageData("student");
            fillStudentsData();
            sorter.setModel(fillStudentsData());
            tableData.setRowSorter(sorter);
        });

        buttonLoginHistory.addActionListener(e -> {
            showLoginHistory();
        });

        edtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                    sorter.setRowFilter(RowFilter.regexFilter(edtSearch.getText()));
            }
        });
    }

//    UI Function

    private void fillData() {
        ListSelectionModel selectionModel = tableData.getSelectionModel();
        tableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                int selectRow = tableData.getSelectedRow();
                if (selectRow != -1) {
                    fillEditText(selectRow);
                }else{
                    JOptionPane.showMessageDialog(this, "No data on row", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }


    private void fillEditText(int selectRow){
        if ("Add User".equals(buttonAddUser.getText())) {
//            System.out.println(selectRow);
            if (selectRow >= 0 && selectRow < tableData.getModel().getRowCount()){
                edtID.setText(tableData.getValueAt(selectRow, 0).toString());
                edtName.setText(tableData.getValueAt(selectRow, 1).toString());
                edtAge.setText(tableData.getValueAt(selectRow, 2).toString());
                edtPhone.setText(tableData.getValueAt(selectRow, 3).toString());
                edtGender_Img.setText(tableData.getValueAt(selectRow, 4).toString());
                edtAddress_Status.setText(tableData.getValueAt(selectRow, 5).toString());
                edtAge.setEnabled(true);
            }
        } else if ("Add Student".equals(buttonAddUser.getText())) {
            if(selectRow >=0 && selectRow < tableData.getModel().getRowCount()){
                edtID.setText(tableData.getValueAt(selectRow, 0).toString());
                edtName.setText(tableData.getValueAt(selectRow, 1).toString());
                edtPhone.setText(tableData.getValueAt(selectRow, 5).toString());
                edtGender_Img.setText(tableData.getValueAt(selectRow, 3).toString());
                edtAddress_Status.setText(tableData.getValueAt(selectRow,4).toString());
                edtDOB.setText(tableData.getValueAt(selectRow,2).toString());
                edtGPA.setText(tableData.getValueAt(selectRow,6).toString());
                edtAge.setEnabled(false);
            }
        }
    }

    private void onChangeManageData(String msg){
        if (msg.equals("user")){
            buttonAddUser.setText("Add User");
            buttonUpdateUser.setText("Update User");
            buttonDeleteUser.setText("Delete User");
            txtGender_Img.setText("Image Profile");
            txtAddress_Status.setText("Status");
            clearEdtText();
            txtDOB.setVisible(false);
            edtDOB.setVisible(false);
            txtGPA.setVisible(false);
            edtGPA.setVisible(false);
        } else if (msg.equals("student")) {
            buttonAddUser.setText("Add Student");
            buttonUpdateUser.setText("Update Student");
            buttonDeleteUser.setText("Delete Student");
            txtGender_Img.setText("Gender");
            txtAddress_Status.setText("Address");
            clearEdtText();
            txtDOB.setVisible(true);
            edtDOB.setVisible(true);
            txtGPA.setVisible(true);
            edtGPA.setVisible(true);
        }
    }

    private void clearEdtText(){
        edtID.setText("");
        edtName.setText("");
        edtAge.setText("");
        edtPhone.setText("");
        edtGender_Img.setText("");
        edtAddress_Status.setText("");
        edtDOB.setText("");
        edtGPA.setText("");
    }

    private void searchPlaceHolder(){
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

//    Logic Function
    private void updateUser(String id){
        String name = edtName.getText();
        Integer age = Integer.parseInt(edtAge.getText());
        String phone = edtPhone.getText();
        String img = edtGender_Img.getText();
        String status = edtAddress_Status.getText();
        usersDAO.updateUsers(id, new Users(id, name, age, phone, img, status));
        clearEdtText();
        showAllUsers().fireTableDataChanged();
    }

    private void deleteUser(String id){
        usersDAO.deleteUsers(id);
        clearEdtText();
        showAllUsers().fireTableDataChanged();
    }

    private void addUser(){
        if(!edtName.getText().toString().isEmpty() || !edtAge.getText().toString().isEmpty() || !edtPhone.getText().isEmpty()
        || !edtGender_Img.getText().toString().isEmpty() || !edtAddress_Status.getText().toString().isEmpty()){

            String name = edtName.getText();
            Integer age = Integer.parseInt(edtAge.getText());
            String phone = edtPhone.getText();
            String img_profile = edtGender_Img.getText();
            String status = edtAddress_Status.getText();
            usersDAO.addUsers(new Users("", name, age, phone, img_profile, status));
            clearEdtText();
            showAllUsers().fireTableDataChanged();
        }else{
            JOptionPane.showMessageDialog(this, "You must fill all fields to add user", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addEmptyRow(DefaultTableModel tableModel){
        Object[] empRow = null;
        tableModel.addRow(empRow);
    }

    private DefaultTableModel showAllUsers(){

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("User ID");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Age");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Image Profile");
        tableModel.addColumn("Is Active");

        List<Users> user_data = usersDAO.getUsers();

        for (Users u : user_data) {
            tableModel.addRow(new Object[]{u.getUser_id(), u.getFullname(), u.getAge(),
                    u.getPhone(), u.getImg_profile(), u.getIsActive()});
        }
        tableData.setModel(tableModel);
        adjustColumnWidth(0, 20);
        adjustColumnWidth(1, 150);
        adjustColumnWidth(2, 20);
        adjustColumnWidth(3, 150);

        return tableModel;
    }

    private void showLoginHistory(){
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

    //Students Part
    private DefaultTableModel fillStudentsData() {
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Student ID");
        defaultTableModel.addColumn("Full Name");
        defaultTableModel.addColumn("Birthday");
        defaultTableModel.addColumn("Gender");
        defaultTableModel.addColumn("Address");
        defaultTableModel.addColumn("Phone");
        defaultTableModel.addColumn("GPA");

        List<Students> listStudents = studentsDAO.getStudentList();
        for(Students students : listStudents) {
            defaultTableModel.addRow(
                    new Object[]{
                            students.getStudent_id(),
                            students.getFull_name(),
                            students.getBirthday(),
                            students.getGender(),
                            students.getHome_address(),
                            students.getPhone(),
                            students.getGPA()
            });
        }

        tableData.setModel(defaultTableModel);
//        tableData.setRowSorter(sorter);

        adjustColumnWidth(3,20);
        adjustColumnWidth(4,250);
        adjustColumnWidth(6, 10);
        return defaultTableModel;
    }

    private void addStudent() {
        if(!edtName.getText().isEmpty() || !edtPhone.getText().isEmpty()
                || !edtGender_Img.getText().isEmpty() || !edtAddress_Status.getText().isEmpty()
                || !edtDOB.getText().isEmpty() ||!edtGPA.getText().isEmpty()) {

            String name = edtName.getText();
            String studentListId = "SL001";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birth = null;
            try {
                birth = dateFormat.parse(edtDOB.getText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String gender = edtGender_Img.getText();
            String address = edtAddress_Status.getText();
            String phone = edtPhone.getText();
            String gpaText = edtGPA.getText();

            float gpa = Float.parseFloat(gpaText);
            String formatted_gpa =  String.format("%.2f", gpa);
            gpa = Float.parseFloat(formatted_gpa);
            //Add comment in line 400
            //Add comment in line 401
            //Add comment in line 402
            studentsDAO.addStudent(new Students("", studentListId, name, birth, gender, address, phone,gpa));
            clearEdtText();
            fillStudentsData().fireTableDataChanged();
        }else{
            JOptionPane.showMessageDialog(this, "You must fill all fields before add students", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    private void deleteStudent(String id) {
        studentsDAO.deleteStudent(id);
        clearEdtText();
        fillStudentsData().fireTableDataChanged();
    }

    private void updateStudent(String id) {
        String name = edtName.getText();
        String phone = edtPhone.getText();
        String gender = edtGender_Img.getText();
        String address = edtAddress_Status.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = null;
        try {
            birth = dateFormat.parse(edtDOB.getText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String gpaText = edtGPA.getText();
        float gpa = Float.parseFloat(gpaText);
        String formatted_gpa =  String.format("%.2f", gpa);
        gpa = Float.parseFloat(formatted_gpa);

        //Comment in line 436
        studentsDAO.updateStudent(new Students(id, "", name, birth,  gender, address, phone, gpa));
        clearEdtText();
        fillStudentsData().fireTableDataChanged();
    }
    private void adjustColumnWidth(int columnIndex, int width) {
        TableColumn column = tableData.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
    }

    private void disabledField() {
        edtID.setEnabled(false);
    }
}
