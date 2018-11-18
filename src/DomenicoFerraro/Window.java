package DomenicoFerraro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class Window {

    private JPanel basePanel;
    private JTextArea usersArea;
    private JTextField newUserNameTxtField;
    private JTextField newPasswTxtField;
    private JButton newUserBtn;
    private JTextArea dataArea;
    private JTextField newValueTxtField;
    private JButton newDataBtn;
    private JTextField userNameLogIn;
    private JTextField passwLogIn;

    private MySecureDataContainer<Integer> container;

    public Window(int width, int height, String title){
        JFrame frame = new JFrame(title);
        container = new MySecureDataContainer<>();
        setLookAndFeel();
        frame.setContentPane(basePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(width, height);
        initListeners();
        frame.setVisible(true);
    }

    private void initListeners(){
        newUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.createUser(newUserNameTxtField.getText(), newPasswTxtField.getText());
                updateUsersArea();
            }
        });

        newDataBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.put(userNameLogIn.getText(), passwLogIn.getText(), Integer.parseInt(newValueTxtField.getText()));
                updateDataArea();
            }
        });
    }

    private void updateUsersArea(){
        Iterator it = container.getUsers().iterator();
        String text = "";
        while (it.hasNext())
            text += it.next()+"\n";
        usersArea.setText(text);
    }

    private void updateDataArea(){
        Iterator it = container.getStorage().iterator();
        String text = "";
        while (it.hasNext()){
            SharedData data = (SharedData) it.next();
            text += data.getData()+"\n";
        }
        dataArea.setText(text);
    }

    private void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
