package main;

import dataaccess.Userdata;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginForm extends JFrame {

    private JLabel titleLabel, emailLabel, passwordLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, clearButton, exitButton, forgotPasswordButton, signupButton;

    public LoginForm() {
        setTitle("Login");
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());

        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");
        forgotPasswordButton = new JButton("Forgot Password?");
        signupButton = new JButton("Signup");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
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

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignup();
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(forgotPasswordButton);
        buttonPanel.add(signupButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        loginButton.setEnabled(false);

        emailField.addKeyListener(new KeyAdapter() {
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

    private void handleLogin() {

        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = Userdata.login(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(null, "Invalid email or password", "Message", JOptionPane.ERROR_MESSAGE);
        }
        else{
            if(user.getStatus().equals("false")){
                JOptionPane.showMessageDialog(null, "Wait for admin approval!", "Message", JOptionPane.INFORMATION_MESSAGE);
                handleClear();
            }
            if(user.getStatus().equals("true")){
                setVisible(false);
                new Home(email).setVisible(true);
            }
        }
    }

    private void handleClear() {
        emailField.setText("");
        passwordField.setText("");
        loginButton.setEnabled(false);
    }

    public void validateFields(){
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());
        if(!email.equals("") && !password.equals("")){
            loginButton.setEnabled(true);
        }
        else{
            loginButton.setEnabled(false);
        }
    }

    private void handleForgotPassword() {
        JOptionPane.showMessageDialog(this, "Please contact support to reset your password.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSignup() {
        setVisible(false);
        new SignUpForm().setVisible(true);
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}
