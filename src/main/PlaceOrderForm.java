
package main;

import dataaccess.Billdata;
import dataaccess.Productdata;
import model.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PlaceOrderForm extends JFrame {

    public String userEmail;

    private JLabel titleLabel;
    private JButton closeButton;
    private JLabel billIdLabel;
    private JLabel billIdValue;
    private JLabel nameLabel, phoneLabel, emailLabel;
    private JTextField nameField, phoneField, emailField;
    private JLabel categoryLabel, searchLabel;
    private JComboBox<String> categoryComboBox;
    private JTextField searchField;
    private JTable productTable;
    private JTable cartTable;
    private JTextField productNameField, priceField, totalField;
    private JSpinner quantitySpinner;
    private JButton clearButton, addToCartButton;
    private JLabel grandTotalLabel;
    private JLabel grandTotalValue;
    private JButton generateBillButton;

    public PlaceOrderForm() {
    }

    public PlaceOrderForm(String email) {
        setTitle("Place Order");
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Place Order");
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

        JPanel billIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        billIdLabel = new JLabel("Bill ID: ");
        billIdValue = new JLabel("--");
        billIdPanel.add(billIdLabel);
        billIdPanel.add(billIdValue);

        JPanel customerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        nameLabel = new JLabel("Name:");
        phoneLabel = new JLabel("Phone:");
        emailLabel = new JLabel("Email:");
        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        customerPanel.add(nameLabel);
        customerPanel.add(nameField);
        customerPanel.add(phoneLabel);
        customerPanel.add(phoneField);
        customerPanel.add(emailLabel);
        customerPanel.add(emailField);

        Billdata.populateBillDetailsByEmail(email, billIdValue, nameField, phoneField, emailField);

        JPanel searchPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Product Search"));
        categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("Select Category");
        searchLabel = new JLabel("Search:");
        searchField = new JTextField();
        searchPanel.add(categoryLabel);
        searchPanel.add(categoryComboBox);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        List<String> categories = Productdata.getCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }

        String[] productColumns = { "Name" };
        DefaultTableModel productTableModel = new DefaultTableModel(productColumns, 0);
        productTable = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);

        JPanel productDetailsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        productNameField = new JTextField();
        priceField = new JTextField();
        totalField = new JTextField();
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        productDetailsPanel.add(new JLabel("Product Name:"));
        productDetailsPanel.add(productNameField);
        productDetailsPanel.add(new JLabel("Price:"));
        productDetailsPanel.add(priceField);
        productDetailsPanel.add(new JLabel("Total:"));
        productDetailsPanel.add(totalField);
        productDetailsPanel.add(new JLabel("Quantity:"));
        productDetailsPanel.add(quantitySpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this::clearFields);
        addToCartButton = new JButton("Add to Bill");
        addToCartButton.addActionListener(this::addToCart);

        buttonPanel.add(clearButton);
        buttonPanel.add(addToCartButton);

        productDetailsPanel.add(new JLabel());
        productDetailsPanel.add(buttonPanel);

        String[] cartColumns = { "Name", "Price", "Quantity", "Total" };
        DefaultTableModel cartTableModel = new DefaultTableModel(cartColumns, 0);
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        grandTotalLabel = new JLabel("Grand Total: ");
        grandTotalValue = new JLabel("0");
        generateBillButton = new JButton("Generate Bill");
        generateBillButton.addActionListener(this::generateBill);

        bottomPanel.add(grandTotalLabel);
        bottomPanel.add(grandTotalValue);
        bottomPanel.add(generateBillButton);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 3 columns layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(billIdPanel, BorderLayout.NORTH);
        leftPanel.add(customerPanel, BorderLayout.CENTER);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(searchPanel, BorderLayout.NORTH);
        middlePanel.add(productScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(productDetailsPanel, BorderLayout.NORTH);
        rightPanel.add(cartScrollPane, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(rightPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setSize(2000, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userEmail = email;

        loadFilteredProducts("", "");

        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            String searchTerm = searchField.getText().trim();
            loadFilteredProducts(selectedCategory.equals("Select Category") ? "" : selectedCategory, searchTerm);
        });

        searchField.addCaretListener(e -> {
            String searchTerm = searchField.getText().trim();
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            loadFilteredProducts(selectedCategory.equals("Select Category") ? "" : selectedCategory, searchTerm);
        });

        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Get selected product from the table
                        String productName = (String) productTable.getValueAt(selectedRow, 0);
                        Product selectedProduct = Productdata.getProductByName(productName);
                        if (selectedProduct != null) {
                            productNameField.setText(selectedProduct.getName());
                            priceField.setText(selectedProduct.getPrice());
                            updateTotalField();
                        }
                    }
                }
            }
        });
    }

    private void updateTotalField() {
        try {
            double price = Double.parseDouble(priceField.getText());
            int quantity = (int) quantitySpinner.getValue();
            double total = price * quantity;
            totalField.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            totalField.setText("0.00");
        }
    }

    private void handleClose(ActionEvent e) {
        setVisible(false);
        new Home(userEmail).setVisible(true);
    }

    private void clearFields(ActionEvent e) {
        productNameField.setText("");
        priceField.setText("");
        totalField.setText("");
        quantitySpinner.setValue(1);  // Reset to 1 since it's the minimum allowed quantity
    }

    private void addToCart(ActionEvent e) {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        String name = productNameField.getText();
        String price = priceField.getText();
        String quantity = quantitySpinner.getValue().toString();

        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        double priceValue = Double.parseDouble(price);
        int quantityValue = Integer.parseInt(quantity);
        double total = priceValue * quantityValue;

        model.addRow(new Object[]{name, price, quantity, total});
        updateGrandTotal();
        clearFields(null);
    }

    private void updateGrandTotal() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        double grandTotal = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            double total = Double.parseDouble(model.getValueAt(i, 3).toString());
            grandTotal += total;
        }
        grandTotalValue.setText(String.format("%.2f", grandTotal));
    }

    private void generateBill(ActionEvent e) {
        try {
            String grandTotalText = grandTotalValue.getText().trim();

            if (grandTotalText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "The grand total cannot be empty.");
                return;
            }
            grandTotalText = grandTotalText.replace(",", ".");
            grandTotalText = grandTotalText.replaceAll("[^\\d.]", "");
            double grandTotal = Double.parseDouble(grandTotalText);

            if (grandTotal <= 0) {
                JOptionPane.showMessageDialog(this, "The grand total must be greater than 0.");
                return;
            }

            Billdata.save(userEmail, grandTotal);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Invalid grand total value. Please ensure it is a valid number.");
            ex.printStackTrace();
        }
    }

    private void loadFilteredProducts(String category, String search) {
        List<Product> products = Productdata.getAllProducts();
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        for (Product product : products) {
            String productCategory = Productdata.getCategoryById(product.getCategory());

            if ((category == null || category.isEmpty() || productCategory.equals(category)) &&
                    (search == null || search.isEmpty() || product.getName().toLowerCase().contains(search.toLowerCase()))) {
                model.addRow(new Object[]{product.getName()});
            }
        }
    }

    public static void main(String[] args) {
        String email = "user@example.com";
        PlaceOrderForm form = new PlaceOrderForm(email);
        form.setVisible(true);
    }
}
