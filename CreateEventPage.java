import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CreateEventPage {
    public CreateEventPage(List<Event> events) {
        JFrame frame = new JFrame("Create Event");
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Make fullscreen but keep OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);

        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        logoPanel.setBackground(new Color(0x1c2e4a));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x23395d), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel logo = new JLabel("LOGO");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logoPanel.add(logo);

        JPanel logoWrapper = new JPanel(new BorderLayout());
        logoWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        logoWrapper.setBackground(new Color(0x1c2e4a));
        logoWrapper.add(logoPanel, BorderLayout.CENTER);

        frame.add(logoWrapper, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        centerPanel.setBackground(new Color(0x1c2e4a));

        JLabel createLabel = new JLabel("Create");
        createLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        createLabel.setForeground(Color.WHITE);
        createLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(createLabel);

        // Event Name field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel nameLabel = new JLabel("Name of the Event:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField nameField = new JTextField();
        nameField.setBackground(new Color(0x152238));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true)); // edge-hugging
        nameField.setMaximumSize(new Dimension(350, 30)); // reduced length
        centerPanel.add(nameLabel);
        centerPanel.add(nameField);

        // Date field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel dateLabel = new JLabel("Date (MM/DD/YY):");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dateField = new JTextField();
        dateField.setBackground(new Color(0x152238));
        dateField.setForeground(Color.WHITE);
        dateField.setCaretColor(Color.WHITE);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true)); // edge-hugging
        dateField.setMaximumSize(new Dimension(350, 30)); // reduced length
        centerPanel.add(dateLabel);
        centerPanel.add(dateField);

        // Time field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel timeLabel = new JLabel("Time (hh:mmam/pm):");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField timeField = new JTextField();
        timeField.setBackground(new Color(0x152238));
        timeField.setForeground(Color.WHITE);
        timeField.setCaretColor(Color.WHITE);
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2, true)); // edge-hugging
        timeField.setMaximumSize(new Dimension(350, 30)); // reduced length
        centerPanel.add(timeLabel);
        centerPanel.add(timeField);

        frame.add(centerPanel, BorderLayout.CENTER);


        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(new Color(0x1c2e4a));
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

            // Validate name (letters and spaces only, max 50)
            if (name.isEmpty() || name.length() > 50 || !name.matches("^[A-Za-z ]+$")) {
                JOptionPane.showMessageDialog(frame,
                        "Event name must be 1–50 letters only (no symbols or numbers).",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate date (MM/DD/YY)
            if (!date.matches("^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{2}$")) {
                JOptionPane.showMessageDialog(frame,
                        "Date must be in MM/DD/YY format (e.g., 04/11/26).",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate time (hh:mmam/pm)
            if (!time.matches("^(0?[1-9]|1[0-2]):[0-5][0-9](am|pm)$")) {
                JOptionPane.showMessageDialog(frame,
                        "Time must be in hh:mmam/pm format (e.g., 10:00am).",
                        "Invalid Time",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // If all checks pass, save event with name, date, and time
            events.add(new Event(name, date, time));

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

        frame.getContentPane().setBackground(new Color(0x1c2e4a));
        frame.setVisible(true);
    }
}
