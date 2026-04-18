import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
@SuppressWarnings("all")
public class CreateEventPage {
    public CreateEventPage() {
        JFrame frame = new JFrame("Create Event");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new EventureGradientPanel(new BorderLayout()));

        // Make fullscreen but keep OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);

        // Top-left logo (symbol + typography)
        frame.add(EventureBranding.createTopLogoHeader(), BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        centerPanel.setOpaque(false);

        JLabel createLabel = new JLabel("Create");
        createLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        createLabel.setForeground(Color.WHITE);
        createLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(createLabel);

        // Event Name field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel nameLabel = new JLabel("Name of the Event:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nameField = new JTextField();
        nameField.setBackground(new Color(0x152238));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true)); // edge-hugging
        nameField.setMaximumSize(new Dimension(350, 30)); // reduced length
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(nameLabel);
        centerPanel.add(nameField);

        // Date field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel dateLabel = new JLabel("Date (MM/DD/YY):");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField dateField = new JTextField();
        dateField.setBackground(new Color(0x152238));
        dateField.setForeground(Color.WHITE);
        dateField.setCaretColor(Color.WHITE);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true)); // edge-hugging
        dateField.setMaximumSize(new Dimension(350, 30)); // reduced length
        dateField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(dateLabel);
        centerPanel.add(dateField);

        // Time field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel timeLabel = new JLabel("Time (hh:mm am/pm):");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField timeField = new JTextField();
        timeField.setBackground(new Color(0x152238));
        timeField.setForeground(Color.WHITE);
        timeField.setCaretColor(Color.WHITE);
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true)); // edge-hugging
        timeField.setMaximumSize(new Dimension(350, 30)); // reduced length
        timeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(timeLabel);
        centerPanel.add(timeField);

        // Total budget field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel totalBudgetLabel = new JLabel("Total Budget:");
        totalBudgetLabel.setForeground(Color.WHITE);
        totalBudgetLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalBudgetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField totalBudgetField = new JTextField();
        totalBudgetField.setBackground(new Color(0x152238));
        totalBudgetField.setForeground(Color.WHITE);
        totalBudgetField.setCaretColor(Color.WHITE);
        totalBudgetField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalBudgetField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true));
        totalBudgetField.setMaximumSize(new Dimension(350, 30));
        totalBudgetField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(totalBudgetLabel);
        centerPanel.add(totalBudgetField);

        frame.add(centerPanel, BorderLayout.CENTER);


        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setOpaque(false);
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setFocusPainted(false);
        cancelBtn.setFocusPainted(false);
        saveBtn.setBackground(new Color(0x23395d));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(0x23395d));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions with validation
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String totalBudget = totalBudgetField.getText().trim();

            // Validate name (letters and spaces only, max 50)
            if (name.isEmpty() || name.length() > 50 || !name.matches("^[A-Za-z ]+$")) {
                JOptionPane.showMessageDialog(frame,
                        "Event name must be 1–50 letters only (no symbols or numbers).",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate date (MM/DD/YY) with year limited to 25-30
            if (!date.matches("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/(25|26|27|28|29|30)$")) {
                JOptionPane.showMessageDialog(frame,
                        "Date must be in MM/DD/YY format with a year from 25 to 30 (e.g., 04/11/26).",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate time (hh:mmam/pm)
            if (!time.matches("^(0?[1-9]|1[0-2]):[0-5][0-9](?i)(am|pm)$")) {
                JOptionPane.showMessageDialog(frame,
                        "Time must be in hh:mmam/pm format (e.g., 10:00am).",
                        "Invalid Time",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate total budget
            if (!totalBudget.matches("^\\d+(\\.\\d{1,2})?$")) {
                JOptionPane.showMessageDialog(frame,
                        "Total budget must be a positive number with up to 2 decimals (e.g., 15000.00).",
                        "Invalid Budget",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal totalBudgetAmount = new BigDecimal(totalBudget);
            if (totalBudgetAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                    totalBudgetAmount.compareTo(new BigDecimal("500000")) > 0) {
                JOptionPane.showMessageDialog(frame,
                        "Total budget must be greater than 0 and not exceed 500,000.00.",
                        "Invalid Budget",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // If all checks pass, save event with name, date, time, and total budget
            try {
                EventureDatabase.createEvent(name, date, time, totalBudget);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Failed to save event to the database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show success message
            JOptionPane.showMessageDialog(frame,
                    "Event created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            new LandingPage();
        });

        // Cancel with confirmation dialog
        cancelBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Discard changes?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();
                new LandingPage();
            }
        });

        frame.setVisible(true);
    }
}
