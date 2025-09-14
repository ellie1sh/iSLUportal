import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.URL;

public class reqAcc extends JFrame {

    // ArrayList to hold created accounts (ID + Password)
    private static final java.util.List<String> accountLog = new ArrayList<>();

    public reqAcc() {
        setTitle("Student Portal");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ================= Header Panel =================
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));

        JLabel logoLabel = new JLabel();
        URL logoUrl = getClass().getResource("/photos/SLULoginLogo.png");
        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image scaled = icon.getImage().getScaledInstance(180, 90, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaled));
        } else {
            logoLabel.setText("SLU portal");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        }
        
        // Add click handler to logo to navigate to login page
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Dispose current reqAcc window
                dispose();
                // Open login page
                new Login().setVisible(true);
            }
        });
        
        headerPanel.add(logoLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ================= Form Panel =================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel formTitle = new JLabel("Account Request [For Students Only]");
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        formPanel.add(formTitle);

        // ===== Personal Information =====
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBorder(BorderFactory.createTitledBorder("Personal Information"));

        JTextField lnameField = new JTextField(10);
        JTextField fnameField = new JTextField(10);
        JTextField mnameField = new JTextField(10);

        namePanel.add(new JLabel("Last Name:"));
        namePanel.add(lnameField);
        namePanel.add(new JLabel("First Name:"));
        namePanel.add(fnameField);
        namePanel.add(new JLabel("Middle Name:"));
        namePanel.add(mnameField);
        formPanel.add(namePanel);

        // Date of Birth
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dobPanel.setBorder(BorderFactory.createTitledBorder("Date of Birth"));
        JTextField dobField = new JTextField(10);
        dobPanel.add(dobField);
        formPanel.add(dobPanel);

        // ===== Password =====
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login Credentials"));

        JPasswordField passField = new JPasswordField(10);
        loginPanel.add(new JLabel("Create Password:"));
        loginPanel.add(passField);
        formPanel.add(loginPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit");

        // === Submit Action with Unique ID ===
        submitBtn.addActionListener(e -> {
            String lname = lnameField.getText().trim();
            String fname = fnameField.getText().trim();
            String mname = mnameField.getText().trim();
            String dob = dobField.getText().trim();
            String password = new String(passField.getPassword());

            if (lname.isEmpty() || fname.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Generate unique ID using DataManager
                String idNum = DataManager.generateUniqueStudentID();

                // Create StudentInfo object
                StudentInfo studentInfo = new StudentInfo(idNum, lname, fname, mname, dob, password);

                // Save using DataManager
                if (DataManager.saveStudentAccount(studentInfo)) {
                    // Add to local log for display
                    String logEntry = "ID: " + idNum + " | Password: " + password;
                    accountLog.add(logEntry);

                    // Show the new ID to the user
                    JOptionPane.showMessageDialog(this,
                            "✅ Account request saved!\nYour new ID Number is: " + idNum +
                                    "\nPlease remember it along with your password.");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error saving account request. Please try again.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error saving account request: " + ex.getMessage());
            }
        });

        buttonPanel.add(submitBtn);
        formPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= Footer =================
        JLabel footerLabel = new JLabel("Copyright © 2021 TMDD - Software Development. All rights reserved.");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // This method is now handled by DataManager.generateUniqueStudentID()
    // Keeping for backward compatibility but delegating to DataManager
    private String generateUniqueID(String filename) {
        return DataManager.generateUniqueStudentID();
    }

}