package main;

import model.User;
import dataaccess.Userdata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SignUpForm extends JFrame {

    private JLabel nameLabel, emailLabel, phoneLabel, addressLabel, passwordLabel, titleLabel;
    private JTextField nameField, emailField, phoneField, addressField;
    private JPasswordField passwordField;
    private JButton saveButton, clearButton, exitButton, forgotPasswordButton, loginButton;

    public SignUpForm() {
        setTitle("Sign Up");
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Sign Up", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        addressLabel = new JLabel("Address:");
        passwordLabel = new JLabel("Password:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);
        passwordField = new JPasswordField(20);

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        formPanel.add(emailLabel);
        formPanel.add(emailField);

        formPanel.add(phoneLabel);
        formPanel.add(phoneField);

        formPanel.add(addressLabel);
        formPanel.add(addressField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());

        saveButton = new JButton("Save");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");
        forgotPasswordButton = new JButton("Forgot Password?");
        loginButton = new JButton("Login");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClear();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Close the application
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleForgotPassword();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(forgotPasswordButton);
        buttonPanel.add(loginButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        saveButton.setEnabled(false);

        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
        emailField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
        phoneField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
        addressField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
    }

    private void handleSave() {
        User user = new User();
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());
        user.setPhone(phoneField.getText());
        user.setAddress(addressField.getText());
        user.setPassword(new String(passwordField.getPassword()));
        Userdata.save(user);
        handleClear();
    }

    private void handleClear() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        passwordField.setText("");
        saveButton.setEnabled(false);  // Disable save button after clear
    }

    private void validateFields() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String password = new String(passwordField.getPassword());

        if (!name.equals("") && !email.equals("") && !phone.equals("") && !address.equals("") && !password.equals("")) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }

    private void handleForgotPassword() {
        JOptionPane.showMessageDialog(this, "Please contact support to reset your password.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLogin() {
        setVisible(false);
        new LoginForm().setVisible(true);
    }

    public static void main(String[] args) {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setVisible(true);
    }
}
