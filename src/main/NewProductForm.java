package main;

import dataaccess.Productdata;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewProductForm extends ManagerForms {

    private JTextField nameField, categoryField, priceField;
    private JButton saveButton, clearButton;

    public NewProductForm() {
        super("New Product");

        nameField = new JTextField(20);
        categoryField = new JTextField(20);
        priceField = new JTextField(20);

        saveButton = new JButton("Save");
        clearButton = new JButton("Clear");

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

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setWindowProperties(400, 350);

        saveButton.setEnabled(false);

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateField();
            }
        });

        categoryField.addKeyListener(new KeyAdapter() {
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
    }

    private void handleSave() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String price = priceField.getText();

        int categoryId = Productdata.getCategoryIdByName(category);
        if (categoryId != -1) {
            Product product = new Product();
            product.setName(name);
            product.setCategory(categoryId);
            product.setPrice(price);
            Productdata.save(product);

            handleClear();
        } else {
            JOptionPane.showMessageDialog(this, "Category not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void handleClear() {
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        saveButton.setEnabled(false);
    }

    @Override
    protected void validateField() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String price = priceField.getText();

        saveButton.setEnabled(!name.isEmpty() && !category.isEmpty() && !price.isEmpty());
    }

    public static void main(String[] args) {
        NewProductForm form = new NewProductForm();
        form.setVisible(true);
    }
}

