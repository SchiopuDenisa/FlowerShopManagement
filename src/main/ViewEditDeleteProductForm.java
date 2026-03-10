package main;

import dataaccess.Productdata;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ViewEditDeleteProductForm extends ManagerForms {

    private JLabel idLabel, nameLabel, categoryLabel, priceLabel;
    private JTextField idField, nameField, priceField;
    private JComboBox<String> categoryComboBox;
    private JButton updateButton, deleteButton, clearButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ViewEditDeleteProductForm() {
        super("View, Edit & Delete");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        idLabel = new JLabel("ID:");
        idField = new JTextField(20);
        idField.setEditable(false); // ID should be uneditable

        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("Select Category");
        List<String> categories = Productdata.getCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }

        priceLabel = new JLabel("Price:");
        priceField = new JTextField(20);

        formPanel.add(idLabel);
        formPanel.add(idField);

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        formPanel.add(categoryLabel);
        formPanel.add(categoryComboBox);

        formPanel.add(priceLabel);
        formPanel.add(priceField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        updateButton.setEnabled(false);

        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());
        clearButton.addActionListener(e -> handleClear());

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        String[] columns = {"ID", "Name", "Category", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setWindowProperties(600, 400);
        loadProducts();

        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                String productName = (String) tableModel.getValueAt(selectedRow, 1);
                String productCategory = (String) tableModel.getValueAt(selectedRow, 2);
                String productPrice = (String) tableModel.getValueAt(selectedRow, 3);

                idField.setText(String.valueOf(productId));
                nameField.setText(productName);
                priceField.setText(productPrice);
                categoryComboBox.setSelectedItem(productCategory);
            }
        });

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateField();
            }
        });

        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateField();
            }
        });

        categoryComboBox.addItemListener(e -> validateField());
    }

    private void loadProducts() {
        List<Product> products = Productdata.getAllProducts();
        tableModel.setRowCount(0);

        for (Product product : products) {
            String category = Productdata.getCategoryById(product.getCategory());
            tableModel.addRow(new Object[]{product.getId(), product.getName(), category, product.getPrice()});
        }
    }

    @Override
    protected void handleClear() {
        idField.setText("");
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        priceField.setText("");
    }

    @Override
    protected void validateField() {
        boolean isValid = !nameField.getText().isEmpty() &&
                categoryComboBox.getSelectedIndex() != 0 &&
                !priceField.getText().isEmpty();

        updateButton.setEnabled(isValid);
    }

    private void handleUpdate() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || categoryComboBox.getSelectedIndex() == 0 || priceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        int productId = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        String price = priceField.getText();

        int categoryId = Productdata.getCategoryIdByName(category);
        if (categoryId == -1) {
            JOptionPane.showMessageDialog(this, "Invalid category selected.");
            return;
        }

        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setCategory(categoryId);
        product.setPrice(price);

        Productdata.update(product);
        loadProducts();
        handleClear();
    }

    private void handleDelete() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        int productId = Integer.parseInt(idField.getText());
        Productdata.delete(productId);
        loadProducts();
        handleClear();
    }

    public static void main(String[] args) {
        ViewEditDeleteProductForm form = new ViewEditDeleteProductForm();
        form.setVisible(true);
    }
}

