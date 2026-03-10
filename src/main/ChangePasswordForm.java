package main;

import dataaccess.Userdata;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChangePasswordForm extends JFrame {

    public String userEmail;

    private JLabel titleLabel;
    private JButton closeButton;
    private JLabel oldPasswordLabel, newPasswordLabel, confirmPasswordLabel;
    private JPasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    private JButton updateButton, clearButton;

    public ChangePasswordForm(){
    }

    public ChangePasswordForm(String email) {
        setTitle("Change Password");
        setLayout(new BorderLayout());

        // Title label and Close button
        titleLabel = new JLabel("Change Password");
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
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordField = new JPasswordField(20);
        formPanel.add(oldPasswordLabel);
        formPanel.add(oldPasswordField);

        newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField(20);
        formPanel.add(newPasswordLabel);
        formPanel.add(newPasswordField);

        confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        updateButton = new JButton("Update");
        updateButton.addActionListener(this::handleUpdate);
        updateButton.setEnabled(false);  // Initially disabled

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this::handleClear);

        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userEmail = email;
        addFieldListeners();
    }

    private void addFieldListeners() {
        DocumentListener fieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateFields();
            }
        };

        oldPasswordField.getDocument().addDocumentListener(fieldListener);
        newPasswordField.getDocument().addDocumentListener(fieldListener);
        confirmPasswordField.getDocument().addDocumentListener(fieldListener);
    }

    private void validateFields() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty() && newPassword.equals(confirmPassword)) {
            updateButton.setEnabled(true);
        } else {
            updateButton.setEnabled(false);
        }
    }

    private void handleClose(ActionEvent e) {
        setVisible(false);
    }

    private void handleUpdate(ActionEvent e) {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        boolean result = Userdata.changePassword(userEmail, oldPassword, newPassword);
        if (result) {
            setVisible(false);
        }
    }

    private void handleClear(ActionEvent e) {
        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    public static void main(String[] args) {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setVisible(true);
    }
}
