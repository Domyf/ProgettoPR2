package DomenicoFerraro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class Window {

    private static final int STANDARD_MESSAGE = 0;
    private static final int ERROR_MESSAGE = 1;
    private static final int SUCCESS_MESSAGE = 2;

    private JPanel basePanel;
    private JTextField newUserNameTxtField;
    private JTextField newPasswTxtField;
    private JButton newUserBtn;
    private JTextArea dataArea;
    private JTextField newValueTxtField;
    private JButton newDataBtn;
    private JTextField userNameLogIn;
    private JTextField passwLogIn;
    private JTable usersTable;
    private JButton selectUserFromTableBtn;
    private JButton removeDataBtn;
    private JLabel logLbl;
    private DefaultTableModel usersTableModel;

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
                printLog("Utente creato con successo!", SUCCESS_MESSAGE);
            }
        });

        newDataBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean done = false;
                try {
                    int value = Integer.parseInt(newValueTxtField.getText());
                    done = container.put(userNameLogIn.getText(), passwLogIn.getText(), value);
                    updateDataArea();
                    if (done)
                        printLog("Dato creato con successo!", SUCCESS_MESSAGE);
                    else
                        printLog("Dato non creato!", ERROR_MESSAGE);
                } catch (NumberFormatException ex){
                    String message = "Dato non creato. Il dato inserito è in un formato errato!";
                    System.out.println(message);
                    printLog(message, ERROR_MESSAGE);
                }

            }
        });

        selectUserFromTableBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectUserFromTable();
            }
        });

        removeDataBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(newValueTxtField.getText());
                    container.remove(userNameLogIn.getText(), passwLogIn.getText(), value);
                    updateDataArea();
                } catch (NumberFormatException ex){
                    String message = "Dato non rimosso. Il dato inserito è in un formato errato!";
                    System.out.println(message);
                    printLog(message, ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateUsersArea(){
        usersTableModel.getDataVector().removeAllElements();
        Iterator it = container.getUsers().iterator();
        String text = "";
        while (it.hasNext()) {
            User users = (User) it.next();
            String[] row = {users.getId(), users.getPassw()};
            usersTableModel.addRow(row);
        }
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

    private void selectUserFromTable(){
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            userNameLogIn.setText((String) usersTableModel.getValueAt(selectedRow, 0));
            passwLogIn.setText((String) usersTableModel.getValueAt(selectedRow, 1));
        }
    }

    private void printLog(String text, int type){
        switch (type) {
            case STANDARD_MESSAGE:
                logLbl.setText("Log: "+text);
                break;
            case ERROR_MESSAGE:
                logLbl.setText("<html>Log: <font color='red'>"+text+"</font></html>");
                break;
            case SUCCESS_MESSAGE:
                logLbl.setText("<html>Log: <font color='red'>"+text+"</font></html>");
                break;
        }

    }

    private void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        String[] columnNames = {"Nome Utente", "Password"};
        usersTableModel = new DefaultTableModel(0, columnNames.length){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTableModel.setColumnIdentifiers(columnNames);
        usersTable = new JTable(usersTableModel);
        usersTable.setFillsViewportHeight(true);
    }
}
