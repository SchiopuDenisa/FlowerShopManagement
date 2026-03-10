
package main;

import dataaccess.Categorydata;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ManageCategoryForm extends ManagerForms {

    private JTextField categoryField;
    private JButton clearButton, saveButton;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public ManageCategoryForm() {
        super("Manage Category");

        String[] columns = {"ID", "Category"};
        tableModel = new DefaultTableModel(columns, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        categoryField = new JTextField(20);
        clearButton = new JButton("Clear");
        saveButton = new JButton("Save");

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClear();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);

        leftPanel.add(categoryField);
        leftPanel.add(buttonPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        add(mainPanel);

        setWindowProperties(600, 400);

        saveButton.setEnabled(false);

        categoryField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validateField();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                refreshTable();
            }
        });

        categoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableMouseClicked(e);
            }
        });
    }

    private void handleSave() {
        Category category = new Category();
        category.setName(categoryField.getText());
        Categorydata.save(category);
        refreshTable();
        categoryField.setText("");
        saveButton.setEnabled(false);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Category> categories = Categorydata.getAllRecords();
        for (Category c : categories) {
            tableModel.addRow(new Object[]{c.getId(), c.getName()});
        }
    }

    @Override
    protected void handleClear() {
        categoryField.setText("");
        saveButton.setEnabled(false);
    }

    @Override
    protected void validateField() {
        String category = categoryField.getText();
        saveButton.setEnabled(!category.isEmpty());
    }

    private void tableMouseClicked(MouseEvent e) {
        int index = categoryTable.getSelectedRow();
        if (index != -1) {
            TableModel model = categoryTable.getModel();

            int id = Integer.parseInt(model.getValueAt(index, 0).toString());
            String name = model.getValueAt(index, 1).toString();

            int confirmation = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to delete this category: " + name + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                Categorydata.delete(id);
                refreshTable();
            }
        }
    }

    public static void main(String[] args) {
        ManageCategoryForm form = new ManageCategoryForm();
        form.setVisible(true);
    }
}

