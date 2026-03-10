package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends javax.swing.JFrame {

    public String email;

    private JLabel titleLabel;
    private JButton logoutButton, placeOrderButton, viewBillButton, changePasswordButton, exitButton;
    private JButton manageCategoryButton, newProductButton, viewEditDeleteButton, verifyUsersButton;
    private ImageIcon backgroundImage;

    public Home() {
    }

    public Home(String userEmail) {
        setTitle("Home - Flower Shop");
        setLayout(new BorderLayout());

        // Background image
        backgroundImage = new ImageIcon("C:\\Users\\patri\\Documents\\IDEA\\FlowerShop\\src\\images\\background.png"); // Set your image path here
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel);

        // Title label
        titleLabel = new JLabel("Welcome", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.PINK);
        backgroundLabel.add(titleLabel, BorderLayout.NORTH);

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topButtonPanel.setOpaque(false); // Transparent panel

        logoutButton = new JButton("Logout");
        placeOrderButton = new JButton("Place Order");
        viewBillButton = new JButton("View Previous Orders");
        changePasswordButton = new JButton("Change Password");
        exitButton = new JButton("Exit");

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePlaceOrder();
            }
        });
        viewBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewBill();
            }
        });
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangePassword();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Close the application
            }
        });

        topButtonPanel.add(logoutButton);
        topButtonPanel.add(placeOrderButton);
        topButtonPanel.add(viewBillButton);
        topButtonPanel.add(changePasswordButton);
        topButtonPanel.add(exitButton);

        backgroundLabel.add(topButtonPanel, BorderLayout.CENTER);

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomButtonPanel.setOpaque(false);

        manageCategoryButton = new JButton("Manage Category");
        newProductButton = new JButton("New Product");
        viewEditDeleteButton = new JButton("View/Edit & Delete Product");
        verifyUsersButton = new JButton("Verify Users");

        manageCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleManageCategory();
            }
        });
        newProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNewProduct();
            }
        });
        viewEditDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewEditDeleteProduct();
            }
        });
        verifyUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleVerifyUsers();
            }
        });

        bottomButtonPanel.add(manageCategoryButton);
        bottomButtonPanel.add(newProductButton);
        bottomButtonPanel.add(viewEditDeleteButton);
        bottomButtonPanel.add(verifyUsersButton);

        backgroundLabel.add(bottomButtonPanel, BorderLayout.SOUTH);

        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        email = userEmail;
        if(!email.equals("admin@gmail.com")) {
            manageCategoryButton.setVisible(false);
            newProductButton.setVisible(false);
            viewEditDeleteButton.setVisible(false);
            verifyUsersButton.setVisible(false);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }

    private void handlePlaceOrder() {
        setVisible(false);
        new PlaceOrderForm(email).setVisible(true);

    }

    private void handleViewBill() {
        setVisible(false);
        new ViewPreviousOrdersForm(email).setVisible(true);
    }

    private void handleChangePassword() {
        new ChangePasswordForm(email).setVisible(true);
    }

    private void handleManageCategory() {
        new ManageCategoryForm().setVisible(true);
    }

    private void handleNewProduct() {
        new NewProductForm().setVisible(true);
    }

    private void handleViewEditDeleteProduct() {
        new ViewEditDeleteProductForm().setVisible(true);
    }

    private void handleVerifyUsers() {
        new VerifyUsersForm().setVisible(true);
    }

    public static void main(String[] args) {
        String email = "user@example.com";
        Home homeForm = new Home(email);
        homeForm.setVisible(true);
    }
}

