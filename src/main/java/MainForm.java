import javax.swing.*;

public class MainForm extends JFrame {
    private JPanel panelMain;

    public MainForm() {
        setContentPane(panelMain);
        setTitle("Login");
        setSize(550,450);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
