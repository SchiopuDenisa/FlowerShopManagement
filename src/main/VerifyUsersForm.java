package main;

import model.User;
import dataaccess.Userdata;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VerifyUsersForm extends ManagerForms {

    private JTextField searchField;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public VerifyUsersForm() {
        super("Verify User");

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel searchLabel = new JLabel("Search:");
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchPanel.add(searchField);

        String[] columns = {"ID", "Name", "Email", "Phone", "Address", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(contentPanel, BorderLayout.CENTER);
        setWindowProperties(800, 400);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                getAllRecords("");
            }
        });

        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                userTableMouseClicked(e);
            }
        });

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchFieldKeyReleased(e);
            }
        });
    }

    public void getAllRecords(String email) {
        DefaultTableModel dtm = (DefaultTableModel) userTable.getModel();
        dtm.setRowCount(0);
        ArrayList<User> list = Userdata.getAllRecords(email);
        for (User u : list) {
            dtm.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getAddress(), u.getStatus()});
        }
    }

    private void userTableMouseClicked(MouseEvent e) {
        int index = userTable.getSelectedRow();
        TableModel model = userTable.getModel();
        String email = model.getValueAt(index, 2).toString();
        String status = model.getValueAt(index, 5).toString();

        status = status.equals("true") ? "false" : "true";
        int confirmation = JOptionPane.showConfirmDialog(null, "Do you want to change the status?", "Select", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            Userdata.changeStatus(email, status);
            getAllRecords("");  // Refresh the table
        }
    }

    private void searchFieldKeyReleased(KeyEvent e) {
        String email = searchField.getText();
        getAllRecords(email);
    }

    public static void main(String[] args) {
        VerifyUsersForm form = new VerifyUsersForm();
        form.setVisible(true);
    }

    @Override
    protected void handleClear() {

    }

    @Override
    protected void validateField() {

    }
}
