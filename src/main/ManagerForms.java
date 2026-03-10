package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ManagerForms extends JFrame {

    private JLabel titleLabel;
    private JButton closeButton;

    public ManagerForms(String title) {
        setTitle(title);
        setLayout(new BorderLayout());

        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);

        closeButton = new JButton("X");
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClose();
            }
        });

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(true);//false
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(closeButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);
    }

    protected void handleClose() {
        setVisible(false);
    }

    protected abstract void handleClear();

    protected abstract void validateField();

    protected void setWindowProperties(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
    }
}