package main;

import dataaccess.Billdata;
import model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ViewPreviousOrdersForm extends JFrame {

    public String userEmail;

    private JLabel titleLabel;
    private JButton closeButton;
    private JTextField dateFilterField;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public ViewPreviousOrdersForm() {

    }
    public ViewPreviousOrdersForm(String email) {
        setTitle("Previous Orders");
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Previous Orders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);

        closeButton = new JButton("X");
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(this::handleClose);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(closeButton, BorderLayout.EAST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        add(titlePanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel searchLabel = new JLabel("Filter by Date:");
        searchPanel.add(searchLabel);

        dateFilterField = new JTextField(20);
        searchPanel.add(dateFilterField);

        String[] columns = {"ID", "Date", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        ordersTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(contentPanel, BorderLayout.CENTER);

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        userEmail = email;
        getAllRecords(email, "");

        dateFilterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String dateFilter = dateFilterField.getText();
                getAllRecords(email, dateFilter);
            }
        });
    }

    public void getAllRecords(String email, String dateFilter) {
        DefaultTableModel dtm = (DefaultTableModel) ordersTable.getModel();
        dtm.setRowCount(0);

        ArrayList<Bill> bills = Billdata.getAllRecords(email, dateFilter);
        for (Bill bill : bills) {
            dtm.addRow(new Object[]{bill.getId(), bill.getDate(), bill.getTotal()});
        }
    }

    private void handleClose(ActionEvent e) {
        setVisible(false); // Close the window
        new Home(userEmail).setVisible(true);
    }

    public static void main(String[] args) {
        String email = "user@example.com";  // Replace this with the actual logged-in user's email
        ViewPreviousOrdersForm form = new ViewPreviousOrdersForm(email);
        form.setVisible(true);
    }
}
