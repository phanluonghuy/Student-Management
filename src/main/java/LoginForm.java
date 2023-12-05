import DAO.DatabaseRepository;
import DAO.SiteDAO;
import com.sun.nio.sctp.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginForm extends JFrame{
    private JPanel panelTitle;
    private JPanel panelMain;
    private JPanel panelFied;
    private JPasswordField textFieldPassword;
    private JButton ButtonLogin;
    private JTextField textFieldUsername;

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

            if (user!=null) {
                MainForm mainForm = new MainForm(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(panelTitle,"Access Denied!");
                return;
            }
//                System.out.println(user.get("user_name"));
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
