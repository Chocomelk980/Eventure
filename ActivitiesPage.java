import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class ActivitiesPage {
    private Event event;
    private String activityName; // track if editing
    private static final Map<String, JDialog> openDialogs = new HashMap<>();

    public ActivitiesPage(EventCustomization parent, Event event) {
        this(parent, event, null); // default: new activity
    }

    public ActivitiesPage(EventCustomization parent, Event event, String activityName) {
        // Check if dialog already exists for this activity
        String dialogKey = event.getId() + "_" + (activityName == null ? "new" : activityName);
        if (openDialogs.containsKey(dialogKey)) {
            openDialogs.get(dialogKey).toFront();
            openDialogs.get(dialogKey).requestFocus();
            return;
        }

        this.event = event;
        this.activityName = activityName;

        JDialog dialog = new JDialog(parent.getFrame(), activityName == null ? "Add Activity" : "Edit Activity", false);
        dialog.setSize(600, 400);
        dialog.setResizable(false);

        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.setContentPane(new EventureGradientPanel(new BorderLayout()));

        JLabel title = new JLabel(activityName == null ? "Create Activity" : "Edit Activity", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);
        dialog.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Activity Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(350, 30));
        nameField.setPreferredSize(new Dimension(350, 30));
        nameField.setBackground(new Color(0x3b4a6b));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(nameLabel);
        centerPanel.add(nameField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel statusLabel = new JLabel("Activity Status:");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JToggleButton openStatusBtn = createStatusButton("Open");
        JToggleButton closedStatusBtn = createStatusButton("Closed");
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(openStatusBtn);
        statusGroup.add(closedStatusBtn);
        openStatusBtn.setSelected(true);
        updateStatusButtonStyles(openStatusBtn, closedStatusBtn);

        openStatusBtn.addActionListener(e -> updateStatusButtonStyles(openStatusBtn, closedStatusBtn));
        closedStatusBtn.addActionListener(e -> updateStatusButtonStyles(openStatusBtn, closedStatusBtn));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statusPanel.setOpaque(false);
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(openStatusBtn);
        statusPanel.add(closedStatusBtn);

        centerPanel.add(statusLabel);
        centerPanel.add(statusPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] columns = {"Name", "Student Number", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        final int[] lastAddedRow = {-1};
        DefaultTableCellRenderer borderedRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(0x23395d));
                } else if (row == lastAddedRow[0]) {
                    c.setBackground(new Color(0x345b8c));
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(0x1c2e4a) : new Color(0x192841));
                }
                c.setForeground(Color.WHITE);
                ((JComponent) c).setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 1)); // add border
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(borderedRenderer);
        }

        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(new Color(0x152238));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setOpaque(true);
                return lbl;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2));
        scrollPane.getViewport().setBackground(new Color(0x1c2e4a));
        centerPanel.add(scrollPane);

        JButton addParticipantBtn = new JButton("+ Add Participant");
        addParticipantBtn.setBackground(new Color(0x23395d));
        addParticipantBtn.setForeground(Color.WHITE);
        addParticipantBtn.setFocusPainted(false);
        addParticipantBtn.addActionListener(e -> {
            model.addRow(new Object[]{"", "", ""});
            lastAddedRow[0] = model.getRowCount() - 1;
            table.repaint();
        });
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel addParticipantPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        addParticipantPanel.setOpaque(false);
        addParticipantPanel.add(addParticipantBtn);
        centerPanel.add(addParticipantPanel);

        dialog.add(centerPanel, BorderLayout.CENTER);

        // Register dialog and clean up on close
        openDialogs.put(dialogKey, dialog);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                openDialogs.remove(dialogKey);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setOpaque(false);

        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");
        JButton cancelBtn = new JButton("Cancel");

        for (JButton btn : new JButton[]{saveBtn, backBtn, cancelBtn}) {
            btn.setBackground(new Color(0x23395d));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        // Save → commit changes with validation
        saveBtn.addActionListener(e -> {
            String activityNameInput = nameField.getText().trim();
            String activityStatus = closedStatusBtn.isSelected() ? "Closed" : "Open";

            // Validate activity name (letters and spaces only, max 50)
            if (activityNameInput.isEmpty() || activityNameInput.length() > 50 || !activityNameInput.matches("^[A-Za-z ]+$")) {
                JOptionPane.showMessageDialog(dialog,
                        "Activity name must be 1–50 letters only (no symbols or numbers).",
                        "Invalid Activity Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String[]> participants = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 0) != null ? ((String) model.getValueAt(i, 0)).trim() : "";
                String studentNumber = model.getValueAt(i, 1) != null ? ((String) model.getValueAt(i, 1)).trim() : "";
                String contact = model.getValueAt(i, 2) != null ? ((String) model.getValueAt(i, 2)).trim() : "";

                // Skip completely empty rows
                if (name.isEmpty() && studentNumber.isEmpty() && contact.isEmpty()) {
                    continue;
                }

                //Validate name
                if (name != null && !name.trim().isEmpty()) {
                    String trimmedName = name.trim();
                    if (trimmedName.length() > 50 || !trimmedName.matches("^[A-Za-z ]+$")) {
                        JOptionPane.showMessageDialog(dialog,
                                "Invalid participant name at row " + (i+1) +
                                        ". Names must be 1–50 letters only (no symbols or numbers).",
                                "Invalid Participant Name",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Validate student number
                if (studentNumber != null && !studentNumber.isEmpty()) {
                    if (!studentNumber.matches("^\\d{2}-\\d{4}-\\d{6}$") &&
                            !studentNumber.matches("^\\d{2}-\\d{2}-\\d{4}-\\d{6}$")) {
                        JOptionPane.showMessageDialog(dialog,
                                "Invalid student number format at row " + (i+1) +
                                        ". Must be like XX-XXXX-XXXXXX or XX-XX-XXXX-XXXXXX.",
                                "Invalid Student Number",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Validate contact (must be exactly 11 digits)
                if (contact != null && !contact.isEmpty()) {
                    if (!contact.matches("^\\d{11}$")) {
                        JOptionPane.showMessageDialog(dialog,
                                "Invalid contact number at row " + (i+1) +
                                        ". Must be exactly 11 digits (e.g., 09123456789).",
                                "Invalid Contact",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                participants.add(new String[]{name, studentNumber, contact});
            }
            if (participants.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "You must add at least one participant before saving.",
                        "No Participants",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String activityNameToPersist = activityName != null ? activityName : activityNameInput;
            if (activityName != null) {
                event.updateActivity(activityName, participants, activityStatus); // update existing
            } else {
                event.addActivity(activityNameInput, participants, activityStatus); // new
            }

            try {
                EventureDatabase.saveActivity(event.getId(), activityNameToPersist, participants, activityStatus);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog,
                        "Failed to save activity to the database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(dialog,
                    "Activity saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dialog.dispose();
            parent.refreshLists();
        });

        // Back → return without saving
        backBtn.addActionListener(e -> {
            dialog.dispose();
        });

        // Cancel → discard changes and return
        cancelBtn.addActionListener(e -> {
            dialog.dispose();
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(backBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.getContentPane().setBackground(new Color(0x1c2e4a));

        // Pre-load existing activity if editing
        if (activityName != null) {
            nameField.setText(activityName);
            String existingStatus = event.getActivityStatus(activityName);
            if ("Closed".equalsIgnoreCase(existingStatus)) {
                closedStatusBtn.setSelected(true);
            } else {
                openStatusBtn.setSelected(true);
            }
            updateStatusButtonStyles(openStatusBtn, closedStatusBtn);

            List<String[]> participants = event.getParticipantsForActivity(activityName);
            for (String[] p : participants) {
                model.addRow(p);
            }
        }

        dialog.setVisible(true);
    }

    private JToggleButton createStatusButton(String label) {
        JToggleButton button = new JToggleButton(label);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0x23395d));
        button.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        button.setBorderPainted(false);
        return button;
    }

    private void updateStatusButtonStyles(AbstractButton openButton, AbstractButton closedButton) {
        styleStatusButton(openButton, new Color(0x1F7A4C), openButton.isSelected());
        styleStatusButton(closedButton, new Color(0x8B1E2D), closedButton.isSelected());
    }

    private void styleStatusButton(AbstractButton button, Color selectedColor, boolean selected) {
        button.setBackground(selected ? selectedColor : new Color(0x23395d));
        button.setForeground(Color.WHITE);
    }
}
