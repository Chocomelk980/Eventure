import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CreateEventPage {
    public CreateEventPage(List<Event> events) {
        JFrame frame = new JFrame("Create Event");
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        logoPanel.setBackground(new Color(240, 240, 240));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel logo = new JLabel("LOGO");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logoPanel.add(logo);
        frame.add(logoPanel, BorderLayout.NORTH);

        // Center panel with Create label + dashed box + form
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel createLabel = new JLabel("Create");
        createLabel.setFont(new Font("Arial", Font.BOLD, 22));
        createLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(createLabel);

        // Dashed box with "+"
        JPanel dashedBox = new JPanel(new BorderLayout());
        dashedBox.setPreferredSize(new Dimension(200, 100));
        dashedBox.setMaximumSize(new Dimension(200, 100));
        dashedBox.setBackground(new Color(220, 220, 220));
        dashedBox.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 5, 5));
        JLabel plus = new JLabel("+", SwingConstants.CENTER);
        plus.setFont(new Font("Arial", Font.BOLD, 40));
        dashedBox.add(plus, BorderLayout.CENTER);
        dashedBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(dashedBox);

        // Event Name field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel nameLabel = new JLabel("Name of the Event:");
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        centerPanel.add(nameLabel);
        centerPanel.add(nameField);

        // Date field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField();
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        centerPanel.add(dateLabel);
        centerPanel.add(dateField);

        // Time field
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel timeLabel = new JLabel("Time:");
        JTextField timeField = new JTextField();
        timeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        centerPanel.add(timeLabel);
        centerPanel.add(timeField);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Buttons at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        // Rounded button look
        saveBtn.setFocusPainted(false);
        cancelBtn.setFocusPainted(false);
        saveBtn.setBackground(new Color(70, 130, 180));
        saveBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(200, 200, 200));
        cancelBtn.setForeground(Color.BLACK);

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        saveBtn.addActionListener(e -> {
            events.add(new Event(nameField.getText()));
            frame.dispose();
            new LandingPage();
        });
        cancelBtn.addActionListener(e -> {
            frame.dispose();
            new LandingPage();
        });

        frame.setVisible(true);
    }
}