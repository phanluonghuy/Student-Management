import DAO.*;
import Model.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// commit
//oke
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
    private JButton manageUsersButton;
    private JTextField edtID;
    private JTextField edtName;
    private JTextField edtAge;
    private JTextField edtSearch;
    private JLabel txtAge;
    private JLabel txtPhone;
    private JLabel txtID;
    private JLabel txtName;
    private JTextField edtPhone;
//    private JTextField edtGender_Img;
//    private JTextField edtAddress_Status;
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
    private JComboBox comboStatus_Country;
    private JComboBox comboBoxGender;
    private JTextField edtGender_Img;
    private JLabel imgViewUser;
    private JTextField edtAddress_Status;
    private JComboBox comboBox_Role;
    private JLabel txtRole;
    private JLabel txtNoticeImg;
    private JButton importCertificateButton;
    private JButton exportCertificateButton;
    //    private JTextField edtAddress_Status;
    private File selectingFile;
    private SiteDAO siteDAO = new SiteDAO();
    private UsersDAO usersDAO = new UsersDAO();
    private LoginHistoryDAO loginHistoryDAO = new LoginHistoryDAO();
    private StudentsDAO studentsDAO = new StudentsDAO();
    private RoleDAO roleDAO = new RoleDAO();
    private AccountsDAO accountsDAO = new AccountsDAO();
    private CertificatesDAO certificatesDAO = new CertificatesDAO();
    private String account_id_global = "";

    public MainForm(HashMap<String,String> user, String account_id) {
        account_id_global = account_id;
        txtNoticeImg.setText("Click this image to change the user image profile");
//        String role_name = roleDAO.getRoleByUserId(usersDAO.getUserByAccId(account_id).getUser_id()).getRole_name();
        String role_name = user.get("role");
        decentrializeUser(role_name);
        System.out.println(role_name);

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
        onChangeManageData(decentrializeUser(role_name));
        fillData();
        disabledField();
        setDefaultImg();
        setDefaultImgForUser(account_id);
        setImgFrame(imgViewUser);
        setImgFrame(imgView);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>();
        if (role_name.equals("Admin")){
            fillUsersStatusData("0");
            fillUsersStudentList("0");
            fillUsersRole("0");
            sorter.setModel(showAllUsers());
            tableData.setRowSorter(sorter);
        } else if (role_name.equals("Manager")) {
            fillDataCountry("0");
            fillDataGender("0");
            sorter.setModel(fillStudentsData());
            tableData.setRowSorter(sorter);
        }else {
            fillDataCountry("0");
            fillDataGender("0");
            sorter.setModel(fillStudentsData());
            tableData.setRowSorter(sorter);
            decentrializeUser(role_name);
        }

        buttonAddUser.addActionListener(e -> {
            if ("Add User".equals(buttonAddUser.getText())){
                addUser();
                sorter.setModel(showAllUsers());
                tableData.setRowSorter(sorter);
            } else if ("Add Student".equals(buttonAddUser.getText())) {
                addStudent();
                sorter.setModel(fillStudentsData());
                tableData.setRowSorter(sorter);
            }
        });

        buttonDeleteUser.addActionListener(e -> {
            if ("Delete User".equals(buttonDeleteUser.getText())){
                int selectRow = tableData.getSelectedRow();
                if(selectRow != - 1) {
                    int resp = JOptionPane.showConfirmDialog(this, "Are you sure to delete "+tableData.getValueAt(selectRow, 0).toString(), "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_NO_OPTION){
                        deleteUser(tableData.getValueAt(selectRow, 0).toString());
                    }else{
                        JOptionPane.showMessageDialog(this, "Delete Canceled", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "There is no data to delete", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                sorter.setModel(showAllUsers());
                tableData.setRowSorter(sorter);
            } else if ("Delete Student".equals(buttonDeleteUser.getText())) {
                int selectRow = tableData.getSelectedRow();
                if(selectRow != - 1) {
                    int resp = JOptionPane.showConfirmDialog(this, "Are you sure to delete "+tableData.getValueAt(selectRow, 0).toString(), "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_NO_OPTION){
//                        System.out.println(tableData.getValueAt(selectRow, 0).toString());
                        deleteStudent(tableData.getValueAt(selectRow, 0).toString());
                    }else{
                        JOptionPane.showMessageDialog(this, "Delete Canceled", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "There is no data to delete", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                sorter.setModel(fillStudentsData());
                tableData.setRowSorter(sorter);
            }
        });

        buttonUpdateUser.addActionListener(e -> {
            if ("Update User".equals(buttonUpdateUser.getText())){
                int selectRow = tableData.getSelectedRow();
                if(selectRow != -1){
                    int resp = JOptionPane.showConfirmDialog(this, "Are you sure to update "+ tableData.getValueAt(selectRow, 0).toString(), "Update Confirmation", JOptionPane.YES_NO_OPTION);
                    if(resp == JOptionPane.YES_NO_OPTION){
                        updateUser(tableData.getValueAt(selectRow, 0).toString());
                    }else {
                        JOptionPane.showMessageDialog(this, "Update Canceled", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                sorter.setModel(showAllUsers());
                tableData.setRowSorter(sorter);
            } else if ("Update Student".equals(buttonUpdateUser.getText())){
                int selectRow = tableData.getSelectedRow();
                if(selectRow != -1){
                    int resp = JOptionPane.showConfirmDialog(this, "Are you sure to update "+ tableData.getValueAt(selectRow, 0).toString(), "Update Confirmation", JOptionPane.YES_NO_OPTION);
                    if(resp == JOptionPane.YES_NO_OPTION){
                        updateStudent(tableData.getValueAt(selectRow, 0).toString());
                    }else {
                        JOptionPane.showMessageDialog(this, "Update Canceled", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                sorter.setModel(fillStudentsData());
                tableData.setRowSorter(sorter);
            }
        });

        clearButton.addActionListener(e -> {
            imgViewUser.setIcon(null);
            clearEdtText();
        });

        manageUsersButton.addActionListener(e -> {
            onChangeManageData("user");
            showAllUsers();
            sorter.setModel(showAllUsers());
            tableData.setRowSorter(sorter);
            fillUsersStatusData("0");
            fillUsersStudentList("0");
            fillUsersRole("0");
            setDefaultImg();
        });

        buttonManagerStudent.addActionListener(e -> {
            onChangeManageData("student");
            decentrializeUser(role_name);
            fillStudentsData();
            sorter.setModel(fillStudentsData());
            tableData.setRowSorter(sorter);
            fillDataGender("0");
            fillDataCountry("0");
        });

        buttonLoginHistory.addActionListener(e -> {
            onChangeManageData("login");
            showLoginHistory();
            sorter.setModel(showLoginHistory());
            tableData.setRowSorter(sorter);
        });

        exportStudentButton.addActionListener(e -> {
            exportStudentToXls("Student Information", "student_data");
        });

        importStudentButton.addActionListener(e -> {
            importXlsStudentFile();
        });

        importCertificateButton.addActionListener(e -> {
            importXlsCertificateFile();
        });

        exportCertificateButton.addActionListener(e -> {
            exportCertificateToXls("Certificate Information", "certificate_data");
        });

        buttonLogout.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "Are you sure to logging out?", "Notice", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_NO_OPTION){
                LoginForm loginForm = new LoginForm();
                dispose();
            }
        });

        edtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter(edtSearch.getText()));
            }
        });

        manageCertificateButton.addActionListener(e -> {
            String studentID = edtID.getText();
            if (studentID.isEmpty()) {
                JOptionPane.showMessageDialog(panelMain,"No student selected!");
                return;
            }
            System.out.println(studentID);
            CertificateFrom certificate = new CertificateFrom(studentID, role_name);
        });

        imgViewUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                getImageWithValidSize();
                JFileChooser fileChooser = new JFileChooser();
                fileImgFilter(fileChooser);
                if ("Add User".equals(buttonAddUser.getText()) && txtPhone.getText().equals("Phone number")){
                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                        try {
                            File selectFile = fileChooser.getSelectedFile();
                            selectingFile = selectFile;
                            ImageIcon icon = getImageWithValidSize(selectFile.toString(), 150, 150);
                            imgViewUser.setIcon(icon);
                            edtGender_Img.setText("");
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You cannot choose image profile in this page!!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        imgView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setProfileImage(account_id);
            }
        });
    }

    private String decentrializeUser(String role_name){
        String showName = "";
        if (role_name.equals("Manager")){
            manageUsersButton.setVisible(false);
            buttonAddUser.setText("Add Student");
            buttonUpdateUser.setText("Update Student");
            buttonDeleteUser.setText("Delete Student");
            buttonLoginHistory.setVisible(false);
            showName = "student";
            fillStudentsData();

        } else if (role_name.equals("Employee")) {
            manageUsersButton.setVisible(false);
            buttonAddUser.setVisible(false);
            buttonDeleteUser.setVisible(false);
            buttonUpdateUser.setVisible(false);
            buttonLoginHistory.setVisible(false);
            importCertificateButton.setVisible(false);
            exportCertificateButton.setVisible(false);
            importStudentButton.setVisible(false);
            exportStudentButton.setVisible(false);
            showName = "student";
            fillStudentsData();
        }else{
            showName = "user";
            showAllUsers();
        }
        return showName;
    }

    private void fileImgFilter(JFileChooser fileChooser){
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".png") || f.getName().endsWith(".jpg")
                        || f.getName().endsWith(".jpeg") || f.getName().endsWith(".PNG")
                        || f.getName().endsWith(".JPEG") || f.getName().endsWith(".JPG")
                        || f.getName().endsWith(".HEIC") || f.getName().endsWith(".SVG");
            }

            @Override
            public String getDescription() {
                return "Image File Only";
            }
        });
    }

    private void setProfileImage(String account_id) {
        JFileChooser fileChooser = new JFileChooser();
        fileImgFilter(fileChooser);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            try {
                File selectFile = fileChooser.getSelectedFile();
                File resourcesFolder = new File("src/main/resources/image");
                if (!resourcesFolder.exists()){
                    resourcesFolder.mkdirs();
                }

                Users users = usersDAO.getUserByAccId(account_id);
                String fileName = users.getUser_id()+".png";
                File saveFile = new File(resourcesFolder, fileName);

                BufferedImage saveImg = ImageIO.read(selectFile);
                ImageIO.write(saveImg, "png", saveFile);
                ImageIcon icon = getImageWithValidSize("src/main/resources/image/"+fileName, 150, 150);
                imgView.setIcon(icon);
                clearEdtText();
                usersDAO.updateUserImgProfileByAccId(account_id, fileName);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
//
    private void setDefaultImgForUser(String account_id){
        Users users = usersDAO.getUserByAccId(account_id);
//        System.out.println(users.getImg_profile());
        try {
            ImageIcon icon = getImageWithValidSize("src/main/resources/image/"+users.getImg_profile(), 130, 130);
            imgView.setIcon(icon);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDefaultImg(){
        try {
            selectingFile = new File("src/main/resources/image/profile_user.png");
            ImageIcon icon = getImageWithValidSize("src/main/resources/image/profile_user.png", 150, 150);
            imgViewUser.setIcon(icon);
            edtGender_Img.setText("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setImgFrame(JLabel jLabel){
        Border border = new LineBorder(Color.BLACK, 2);
        jLabel.setBorder(border);
        jLabel.setOpaque(true);
        Color color = new Color(241, 241, 241);
        jLabel.setBackground(color);
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
            if (selectRow >= 0 && selectRow < tableData.getModel().getRowCount()){
                edtID.setText(tableData.getValueAt(selectRow, 0).toString());
                edtName.setText(tableData.getValueAt(selectRow, 1).toString());
                edtAge.setText(tableData.getValueAt(selectRow, 2).toString());
                edtPhone.setText(tableData.getValueAt(selectRow, 3).toString());
//                edtAddress_Status.setText(tableData.getValueAt(selectRow, 4).toString());
                fillUsersStatusData(tableData.getValueAt(selectRow, 0).toString());
                fillUsersStudentList(tableData.getValueAt(selectRow, 0).toString());
                fillUsersRole(tableData.getValueAt(selectRow, 0).toString());
                edtGender_Img.setText(usersDAO.getUsersById(tableData.getValueAt(selectRow, 0).toString()).getImg_profile());
//                selectingFile = null;
                Accounts accounts = usersDAO.getAccountByUserId(tableData.getValueAt(selectRow, 0).toString());
                edtDOB.setText(accounts.getUser_name());
                edtGPA.setText(accounts.getPassword());
                imgViewUser.setIcon(fillUserImageProfile(selectRow));
                edtAge.setEnabled(true);
            }
        } else if ("Add Student".equals(buttonAddUser.getText()) && txtAge.getText().equals("Age")) {
            if(selectRow >=0 && selectRow < tableData.getModel().getRowCount()){
                edtID.setText(tableData.getValueAt(selectRow, 0).toString());
                edtName.setText(tableData.getValueAt(selectRow, 1).toString());
                edtPhone.setText(tableData.getValueAt(selectRow, 5).toString());
                edtGender_Img.setText(tableData.getValueAt(selectRow, 3).toString());
//                edtAddress_Status.setText(tableData.getValueAt(selectRow,4).toString());
                fillDataGender(tableData.getValueAt(selectRow, 0).toString());
                edtDOB.setText(tableData.getValueAt(selectRow,2).toString());
                edtGPA.setText(tableData.getValueAt(selectRow,6).toString());
                fillDataCountry(tableData.getValueAt(selectRow, 0).toString());
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

    private ImageIcon getImageWithValidSize(String imgPath, int width, int height){
        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage();
        Image resizeImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizeImg);
    }

    private void onChangeManageData(String msg){
        if (msg.equals("user")){
            buttonAddUser.setText("Add User");
            buttonUpdateUser.setText("Update User");
            buttonDeleteUser.setText("Delete User");
            txtGender_Img.setText("Student List");
            txtAge.setText("Age");
            txtPhone.setText("Phone number");
            txtAddress_Status.setText("Status");
            txtDOB.setText("User name");
            txtGPA.setText("Password");
            txtRole.setText("Role");
            clearEdtText();
            txtNoticeImg.setVisible(true);
            txtRole.setVisible(true);
            comboBox_Role.setVisible(true);
            importCertificateButton.setVisible(false);
            exportCertificateButton.setVisible(false);
            importStudentButton.setVisible(false);
            exportStudentButton.setVisible(false);
            manageCertificateButton.setVisible(false);
            imgViewUser.setEnabled(true);
            comboStatus_Country.setVisible(true);
            txtGender_Img.setVisible(true);
            comboBoxGender.setVisible(true);
            edtGender_Img.setVisible(false);
            edtAddress_Status.setVisible(false);
            txtDOB.setVisible(true);
            edtDOB.setVisible(true);
            txtGPA.setVisible(true);
            edtGPA.setVisible(true);
        } else if (msg.equals("student")) {
            buttonAddUser.setText("Add Student");
            buttonUpdateUser.setText("Update Student");
            buttonDeleteUser.setText("Delete Student");
            txtAge.setText("Age");
            txtPhone.setText("Phone number");
            txtGender_Img.setText("Gender");
            txtAddress_Status.setText("Address");
            txtDOB.setText("Birthday");
            txtGPA.setText("GPA");
            importCertificateButton.setVisible(true);
            exportCertificateButton.setVisible(true);
            importStudentButton.setVisible(true);
            exportStudentButton.setVisible(true);
            manageCertificateButton.setVisible(true);
            clearEdtText();
            txtNoticeImg.setVisible(false);
            txtRole.setVisible(false);
            comboBox_Role.setVisible(false);
            imgViewUser.setEnabled(false);
            comboStatus_Country.setVisible(true);
            comboBoxGender.setVisible(true);
            edtAddress_Status.setVisible(false);
//            fillDataDateTime();
            txtGender_Img.setVisible(true);
            edtGender_Img.setVisible(false);
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
            importCertificateButton.setVisible(false);
            exportCertificateButton.setVisible(false);
            manageCertificateButton.setVisible(false);
            clearEdtText();
            txtNoticeImg.setVisible(false);
            txtRole.setVisible(false);
            comboBox_Role.setVisible(false);
            imgViewUser.setEnabled(false);
            edtAddress_Status.setVisible(true);
            comboBoxGender.setVisible(false);
            comboStatus_Country.setVisible(false);
            txtGender_Img.setVisible(false);
            edtGender_Img.setVisible(false);
            txtDOB.setVisible(false);
            edtDOB.setVisible(false);
            txtGPA.setVisible(false);
            edtGPA.setVisible(false);
        }
    }

    private void clearEdtText(){
//        DefaultComboBoxModel<String> comboBoxModelStatus_Country = (DefaultComboBoxModel<String>) comboStatus_Country.getModel();
//        DefaultComboBoxModel<String> comboBoxModelGender = (DefaultComboBoxModel<String>) comboBoxGender.getModel();
//        DefaultComboBoxModel<String> comboBoxModelRole = (DefaultComboBoxModel<String>) comboBox_Role.getModel();
        edtID.setText("");
        edtName.setText("");
        edtAge.setText("");
        edtPhone.setText("");
        edtGender_Img.setText("");
//        comboBoxModelStatus_Country.removeAllElements();
//        comboBoxModelGender.removeAllElements();
//        comboBoxModelRole.removeAllElements();
        edtAddress_Status.setText("");
        edtDOB.setText("");
        edtGPA.setText("");
        selectingFile = new File("src/main/resources/image/profile_user.png");
        setDefaultImg();
        setDefaultImgForUser(account_id_global);
        if ("Add User".equals(buttonAddUser.getText())){
            fillUsersRole("0");
            fillUsersStudentList("0");
            fillUsersStatusData("0");
        }else{
            fillDataGender("0");
            fillDataCountry("0");
        }
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
    private void importXlsStudentFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        Students students = null;
        try {
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                FileInputStream fileInputStream = new FileInputStream(new File(selectedFile.getAbsolutePath()));
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

                Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    String id = row.getCell(0).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    String student_list_id = row.getCell(2).getStringCellValue().trim();
//                    System.out.println(student_list_id);
                    Date DOB = dateFormat.parse(row.getCell(3).getStringCellValue());
                    String Gender = row.getCell(4).getStringCellValue();
                    String Address = row.getCell(5).getStringCellValue();
                    String Phone = row.getCell(6).getStringCellValue();
                    float GPA = (float)row.getCell(7).getNumericCellValue();

                    students = new Students(id, student_list_id, name, DOB, Gender, Address, Phone, GPA);
                    studentsDAO.addStudent(students);
                    fillStudentsData().fireTableDataChanged();
                }
            }
        }catch (IOException | ParseException e){
            throw new RuntimeException(e);
        }

    }
    private void importXlsCertificateFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        Certificate certificate = null;
        try {
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                FileInputStream fileInputStream = new FileInputStream(new File(selectedFile.getAbsolutePath()));
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

                Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    String student_id = row.getCell(0).getStringCellValue();
                    String certificate_name = row.getCell(1).getStringCellValue();
                    String certificate_level = String.valueOf(row.getCell(2).getNumericCellValue());
                    Date expired_date = dateFormat.parse(row.getCell(3).getStringCellValue());
                    certificate = new Certificate(student_id, certificate_name, certificate_level, expired_date);
                    certificatesDAO.addCertificate(certificate);
                }
            }
        }catch (IOException | ParseException e){
            throw new RuntimeException(e);
        }

    }

    private void exportCertificateToXls(String sheetName, String fileName){
        DefaultTableModel tableModel = (DefaultTableModel) tableData.getModel();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            Cell cell = null;
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Certificate ID");
            headerRow.createCell(1).setCellValue("Student ID");
            headerRow.createCell(2).setCellValue("Date Created");
            headerRow.createCell(3).setCellValue("Certificate Name");
            headerRow.createCell(4).setCellValue("Level");
            headerRow.createCell(5).setCellValue("Expired Date");

            certificatesDAO.exportCertificateToXls(sheet);

            writeFileXls(workbook, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportStudentToXls(String sheetName, String fileName){
        DefaultTableModel tableModel = (DefaultTableModel) tableData.getModel();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            Cell cell = null;
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                cell = headerRow.createCell(col);
                cell.setCellValue(tableModel.getColumnName(col));
            }

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    cell = dataRow.createCell(col);
                    cell.setCellValue(tableModel.getValueAt(row, col).toString());
                }
            }
            writeFileXls(workbook, fileName);
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
        try {
            boolean isUniqueUsername = accountsDAO.checkUniqueUsernameWhileUpdate(edtDOB.getText().toString());
            if (isUniqueUsername){
                JOptionPane.showMessageDialog(this, "Username is existed", "Warning", JOptionPane.WARNING_MESSAGE);
            }else{
                String name = edtName.getText();
                Integer age = Integer.parseInt(edtAge.getText());
                String phone = edtPhone.getText();
                //        String img = usersDAO.getUsersById(id).getImg_profile();
                if (!edtGender_Img.getText().equals("")){
                    selectingFile = new File("src/main/resources/image/"+usersDAO.getUsersById(id).getImg_profile());
                    System.out.println(usersDAO.getUsersById(id).getImg_profile());
//                    System.out.println("hii");
                }else{
//                    System.out.println("helo");
                }
                String img = id+".png";
                File resourcesFolder = new File("src/main/resources/image");
                if (!resourcesFolder.exists()){
                    resourcesFolder.mkdirs();
                }
                File saveFile = new File(resourcesFolder, id+".png");

                BufferedImage saveImg = ImageIO.read(selectingFile);
                ImageIO.write(saveImg, "png", saveFile);

                String status = comboStatus_Country.getSelectedItem().toString();
//            account
                String student_list = comboBoxGender.getSelectedItem().toString();

                String username = edtDOB.getText();
                String password = edtGPA.getText();
                String role_id = roleDAO.getRoleIdByRoleName(comboBox_Role.getSelectedItem().toString());
                usersDAO.updateUsers(id, new Users(id, name, age, phone, img, status), new Accounts("", id, student_list, username, password, role_id));
                clearEdtText();
                showAllUsers().fireTableDataChanged();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void deleteUser(String id){
        try {
            usersDAO.deleteUsers(id);
            File userImg = new File("src/main/resources/image/"+id+".png");
            if (userImg.exists()){
                userImg.delete();
            }
            clearEdtText();
            showAllUsers().fireTableDataChanged();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addUser(){
        try {
            boolean isUniqueUsername = accountsDAO.checkUniqueUsername(edtDOB.getText().toString());
            if (isUniqueUsername){
                if(!edtName.getText().toString().isEmpty() || !edtAge.getText().toString().isEmpty() || !edtPhone.getText().isEmpty()
                        || !edtDOB.getText().toString().isEmpty() || !edtGPA.getText().toString().isEmpty()){

                    String id = siteDAO.AUTO_ACC_USER_ID();
                    String name = edtName.getText();
                    Integer age = Integer.parseInt(edtAge.getText());
                    String phone = edtPhone.getText();
                    String img_profile = id+".png";

                    File resourcesFolder = new File("src/main/resources/image");
                    if (!resourcesFolder.exists()){
                        resourcesFolder.mkdirs();
                    }
                    File saveFile = new File(resourcesFolder, siteDAO.AUTO_ACC_USER_ID()+".png");

                    BufferedImage saveImg = ImageIO.read(selectingFile);
                    ImageIO.write(saveImg, "png", saveFile);
//                JOptionPane.showMessageDialog(this, "Image saved", "Notice", JOptionPane.INFORMATION_MESSAGE);

                    String status = comboStatus_Country.getSelectedItem().toString();
//                account

                    String student_list = comboBoxGender.getSelectedItem().toString();
                    String username = edtDOB.getText();
                    String password = edtGPA.getText();
                    String role_id = roleDAO.getRoleIdByRoleName(comboBox_Role.getSelectedItem().toString());
                    usersDAO.addUsers(new Users("", name, age, phone, img_profile, status), new Accounts("", id, student_list, username, password, role_id));
                    clearEdtText();
                    showAllUsers().fireTableDataChanged();
                }else{
                    JOptionPane.showMessageDialog(this, "You must fill all fields to add user", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }else{
                JOptionPane.showMessageDialog(this, "Username is existed", "Warning", JOptionPane.WARNING_MESSAGE);

            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addEmptyRow(DefaultTableModel tableModel){
        Object[] empRow = null;
        tableModel.addRow(empRow);
    }

    private void fillUsersStatusData(String id){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel();
        if (!id.equals("0")){
            String status = usersDAO.getUsersStatus(id);
            if (status.equals("Normal")){
                comboBoxModel.addElement(status);
                comboBoxModel.addElement("Locked");
            }else{
                comboBoxModel.addElement(status);
                comboBoxModel.addElement("Normal");
            }
        }else{
            comboBoxModel.addElement("Normal");
            comboBoxModel.addElement("Locked");
        }
        comboStatus_Country.setModel(comboBoxModel);
    }

    private void fillUsersRole(String id){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel();
        if (id.equals("0")){
            for (Roles r: roleDAO.getRole()) {
                comboBoxModel.addElement(r.getRole_name());
            }
        }else{
            String user_role_name = roleDAO.getRoleByUserId(id).getRole_name();
            comboBoxModel.addElement(user_role_name);
            for (Roles r: roleDAO.getRole()) {
                if (r.getRole_name().equals(user_role_name)){
                    continue;
                }else{
                    comboBoxModel.addElement(r.getRole_name());
                }
            }
        }
        comboBox_Role.setModel(comboBoxModel);
    }
    private void fillUsersStudentList(String id){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel();
        if (id.equals("0")){
            comboBoxModel.addElement("SL001");
//            comboBoxModel.addElement("SL002");
//            comboBoxModel.addElement("SL003");
        }else{
            String student_list = usersDAO.getStudentListByUserId(id);
            comboBoxModel.addElement(student_list);
//            if (student_list.equals("SL001")){
//                comboBoxModel.addElement("SL002");
//                comboBoxModel.addElement("SL003");
//            } else if (student_list.equals("SL002")) {
//                comboBoxModel.addElement("SL001");
//                comboBoxModel.addElement("SL003");
//            } else{
//                comboBoxModel.addElement("SL001");
//                comboBoxModel.addElement("SL002");
//            }

        }
        comboBoxGender.setModel(comboBoxModel);
    }
    private ImageIcon fillUserImageProfile(int selectRow){
        String workingDirectory = System.getProperty("user.dir");
        String folderPath = workingDirectory + File.separator + "src"+ File.separator +"main"+ File.separator + "resources" + File.separator +"image" + File.separator;
        Users users = usersDAO.getUsersById(tableData.getValueAt(selectRow, 0).toString());
        ImageIcon icon = null;
//                System.out.println(users.getImg_profile());
        if (users.getImg_profile().equals(null) || users.getImg_profile().equals("")){
            icon = getImageWithValidSize("src/main/resources/image/profile_user.png", 150, 150);
        }else{
//                    System.out.println("hii");
            icon = getImageWithValidSize(folderPath+users.getImg_profile(), 150, 150);
        }
        return icon;
    }
    private DefaultTableModel showAllUsers(){

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("User ID");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Age");
        tableModel.addColumn("Phone");

        tableModel.addColumn("Student List");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("Role");
        tableModel.addColumn("Is Active");

        List<Users> user_data = usersDAO.getUsers();

        for (Users u : user_data) {
            Accounts accounts = usersDAO.getAccountByUserId(u.getUser_id());
            tableModel.addRow(new Object[]{u.getUser_id(), u.getFullname(), u.getAge(),
                    u.getPhone(), accounts.getStudent_list_id(), accounts.getUser_name(),
                    accounts.getPassword(), roleDAO.getRoleByUserId(u.getUser_id()).getRole_name(), u.getIsActive()});
        }
        tableData.setModel(tableModel);
        adjustColumnWidth(0, 50);
        adjustColumnWidth(1, 150);
        adjustColumnWidth(2, 20);
        adjustColumnWidth(3, 100);
        adjustColumnWidth(4, 50);
        adjustColumnWidth(8, 50);

        return tableModel;
    }
//    <!-- --!>

//    <!-- Login hisstory --!>
    private DefaultTableModel showLoginHistory(){
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
        return tableModelHistory;
    }
//    <!-- --!>
    //Students Part
    private void fillDataGender(String id){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel();
        if (!id.equals("0")){
            String gender = studentsDAO.getStudentById(id).getGender();
            if (gender.equals("Male")){
                comboBoxModel.addElement("Male");
                comboBoxModel.addElement("Female");
            }else{
                comboBoxModel.addElement("Female");
                comboBoxModel.addElement("Male");
            }
        }else{
            comboBoxModel.addElement("Male");
            comboBoxModel.addElement("Female");
        }
        comboBoxGender.setModel(comboBoxModel);
    }

    private void fillDataCountry(String id){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            String apiUrl = "https://provinces.open-api.vn/api/";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String responseData = response.body().string();
                JSONArray data = new JSONArray(responseData);
                String country = "";
                if(!id.equals("0")){
                    country = studentsDAO.getStudentById(id).getHome_address();
                    comboBoxModel.addElement(country);
                }
                for (int i = 0; i < data.length(); i++) {
                    String name = data.getJSONObject(i).getString("name");
                    if (country.equals(name) && !id.equals("0")){
                        continue;
                    }else {
                        comboBoxModel.addElement(name);
                    }
                }
                comboStatus_Country.setModel(comboBoxModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void fillDataDateTime(){

    }
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
            String gender = comboBoxGender.getSelectedItem().toString();
            String address = comboStatus_Country.getSelectedItem().toString();
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
        String gender = comboBoxGender.getSelectedItem().toString();
        String address = comboStatus_Country.getSelectedItem().toString();
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

    //update 698
    private void disabledField() {
        edtID.setEnabled(false);
    }
}
