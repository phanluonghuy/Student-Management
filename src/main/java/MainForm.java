import DAO.LoginHistoryDAO;
import DAO.SiteDAO;
import DAO.StudentsDAO;
import DAO.UsersDAO;
import Model.Accounts;
import Model.History;
import Model.Students;
import Model.Users;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JButton buttonLogout;
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
    private JButton exportStudentButton;
    private JButton manageCertificateButton;
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
        });

        buttonManagerStudent.addActionListener(e -> {
            onChangeManageData("student");
            fillStudentsData();
        });

        buttonLoginHistory.addActionListener(e -> {
            onChangeManageData("login");
            showLoginHistory();
        });


        exportStudentButton.addActionListener(e -> {
            exportStudentToXls();
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
                }
            }
        });
    }


    private void fillEditText(int selectRow){
        if ("Add User".equals(buttonAddUser.getText()) && txtAge.getText().equals("Age")) {
//            System.out.println(selectRow);
            if (selectRow >= 0 && selectRow < tableData.getModel().getRowCount()){
                edtID.setText(tableData.getValueAt(selectRow, 0).toString());
                edtName.setText(tableData.getValueAt(selectRow, 1).toString());
                edtAge.setText(tableData.getValueAt(selectRow, 2).toString());
                edtPhone.setText(tableData.getValueAt(selectRow, 3).toString());
                edtAddress_Status.setText(tableData.getValueAt(selectRow, 4).toString());
                edtAge.setEnabled(true);
            }
        } else if ("Add Student".equals(buttonAddUser.getText()) && txtAge.getText().equals("Age")) {
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
        }else if (txtPhone.getText().equals("Role")){
            if (selectRow >= 0 && selectRow < tableData.getModel().getRowCount()){
                edtID.setText(tableData.getValueAt(selectRow, 0).toString());
                edtName.setText(tableData.getValueAt(selectRow, 1).toString());
                edtAge.setText(tableData.getValueAt(selectRow, 2).toString());
                edtPhone.setText(tableData.getValueAt(selectRow, 3).toString());
                edtAddress_Status.setText(tableData.getValueAt(selectRow, 4).toString());
                edtAge.setEnabled(true);
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
            importStudentButton.setVisible(false);
            exportStudentButton.setVisible(false);
            manageCertificateButton.setVisible(false);
            clearEdtText();
            txtGender_Img.setVisible(false);
            edtGender_Img.setVisible(false);
            txtDOB.setVisible(false);
            edtDOB.setVisible(false);
            txtGPA.setVisible(false);
            edtGPA.setVisible(false);
        } else if (msg.equals("student")) {
            buttonAddUser.setText("Add Student");
            buttonUpdateUser.setText("Update Student");
            buttonDeleteUser.setText("Delete Student");
            txtAge.setText("Age");
            txtPhone.setText("Phone number");
            txtGender_Img.setText("Gender");
            txtAddress_Status.setText("Address");
            importStudentButton.setVisible(true);
            exportStudentButton.setVisible(true);
            manageCertificateButton.setVisible(true);
            clearEdtText();
            txtGender_Img.setVisible(true);
            edtGender_Img.setVisible(true);
            txtDOB.setVisible(true);
            edtDOB.setVisible(true);
            txtGPA.setVisible(true);
            edtGPA.setVisible(true);
        } else if (msg.equals("login")) {
            txtGender_Img.setText("Image Profile");
            txtAddress_Status.setText("Status");
            txtAge.setText("Password");
            txtPhone.setText("Role");
            txtAddress_Status.setText("Date Perform");
            importStudentButton.setVisible(false);
            exportStudentButton.setVisible(false);
            manageCertificateButton.setVisible(false);
            clearEdtText();
            txtGender_Img.setVisible(false);
            edtGender_Img.setVisible(false);
            txtDOB.setVisible(false);
            edtDOB.setVisible(false);
            txtGPA.setVisible(false);
            edtGPA.setVisible(false);
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

    private void exportStudentToXls(){
        DefaultTableModel model = (DefaultTableModel) tableData.getModel();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Data");
            Cell cell = null;
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
            }

            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    cell = dataRow.createCell(col);
                    cell.setCellValue(model.getValueAt(row, col).toString());
                }
            }
            writeFileXls(workbook, "student_data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFileXls(Workbook workbook, String filename){
        try (FileOutputStream fileOut = new FileOutputStream(filename+".xlsx")) {
            workbook.write(fileOut);
            JOptionPane.showMessageDialog(null, "Exported to Excel successfully");
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    <!-- CRUD Users --!>
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
        tableModel.addColumn("Is Active");

        List<Users> user_data = usersDAO.getUsers();

        for (Users u : user_data) {
            tableModel.addRow(new Object[]{u.getUser_id(), u.getFullname(), u.getAge(),
                    u.getPhone(), u.getIsActive()});
        }
        tableData.setModel(tableModel);
        return tableModel;
    }
//    <!-- --!>

    private void showLoginHistory(){
        DefaultTableModel tableModelHistory = new DefaultTableModel();
        tableModelHistory.addColumn("History ID");
        tableModelHistory.addColumn("User Name");
        tableModelHistory.addColumn("Password");
        tableModelHistory.addColumn("Role");
        tableModelHistory.addColumn("Date Perform");

        List<History> login_history = loginHistoryDAO.getLoginHistory();

        Accounts accounts = new Accounts();

        for (History history : login_history) {
            accounts = loginHistoryDAO.getAccountsById(history.getAccount_id());
            String role_id = accounts.getRole_id();

            tableModelHistory.addRow(new Object[]{history.getHistory_id(), accounts.getUser_name(), accounts.getPassword(),
                    loginHistoryDAO.getRoleByAccId(role_id), history.getDate_perform()});
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
        adjustColumnWidth(3,20);
        adjustColumnWidth(4,250);
        adjustColumnWidth(6, 10);
        return defaultTableModel;
    }

    private void addStudent() {
        if(!edtName.getText().toString().isEmpty() || !edtPhone.getText().isEmpty()
                || !edtGender_Img.getText().toString().isEmpty() || !edtAddress_Status.getText().toString().isEmpty()
                || !edtDOB.getText().toString().isEmpty() ||!edtGPA.getText().isEmpty()) {

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
            float gpa = Math.round(Float.parseFloat(edtGPA.getText()))* 10/10.0f;

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

        float gpa = Math.round(Float.parseFloat(edtGPA.getText()))* 10/10.0f;
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
