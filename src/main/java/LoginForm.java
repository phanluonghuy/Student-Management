import DAO.LoginHistoryDAO;
import DAO.SiteDAO;
import DAO.UsersDAO;
import Model.History;
import Model.Users;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class LoginForm extends JFrame{
    private JPanel panelTitle;
    private JPanel panelMain;
    private JPanel panelFied;
    private JPasswordField textFieldPassword;
    private JButton ButtonLogin;
    private JTextField textFieldUsername;
    private LoginHistoryDAO loginHistoryDAO = new LoginHistoryDAO();
    private SiteDAO siteDAO = new SiteDAO();
    private UsersDAO usersDAO = new UsersDAO();

    public LoginForm() throws HeadlessException {
        setContentPane(panelMain);
        setTitle("Login");
        setSize(550,450);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ButtonLogin.addActionListener(e -> {
            SiteDAO siteDAO = new SiteDAO();
//                System.out.println(textFieldUsername.getText()+ " --- " + new String(textFieldPassword.getPassword()));
            HashMap<String,String> user = siteDAO.login(textFieldUsername.getText(),new String(textFieldPassword.getPassword()));
            String acc_id = loginHistoryDAO.getAccIdByUserNameAndPassword(textFieldUsername.getText().toString(), new String(textFieldPassword.getPassword()));
            Users users = usersDAO.getUserByAccId(acc_id);

            ZoneId indochineZone = ZoneId.of("Asia/Ho_Chi_Minh");
            LocalDateTime indochineTime = LocalDateTime.now(indochineZone);
            Date datePerform = Date.from(indochineTime.atZone(indochineZone).toInstant());

            History history = new History(siteDAO.AUTO_HISTORY_ID(), acc_id, new Timestamp(datePerform.getTime()));
//            System.out.println(history);
            if (user!=null && users.getIsActive().equals("Normal")) {
                loginHistoryDAO.addLoginHistory(history);
                MainForm mainForm = new MainForm(user, acc_id);
                dispose();
            } else {
                JOptionPane.showMessageDialog(panelTitle,"Access Denied!");
                return;
            }
//                System.out.println(user.get("user_name"));
        });
    }
}
