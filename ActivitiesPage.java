import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ActivitiesPage {
    private Event event;
    private String activityName; // track if editing

    public ActivitiesPage(Event event) {
        this(event, null); // default: new activity
    }

    public ActivitiesPage(Event event, String activityName) {
        this.event = event;
        this.activityName = activityName;

        JFrame frame = new JFrame(activityName == null ? "Add Activity" : "Edit Activity");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Make fullscreen but keep OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);

        JLabel title = new JLabel(activityName == null ? "Create Activity" : "Edit Activity", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0x1c2e4a));
        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(0x1c2e4a));

        JLabel nameLabel = new JLabel("Activity Name:");
        nameLabel.setForeground(Color.WHITE);
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setBackground(new Color(0x3b4a6b));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 2));

        centerPanel.add(nameLabel);
        centerPanel.add(nameField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] columns = {"Name", "Role", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        final int[] lastAddedRow = {-1};
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
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
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
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
        centerPanel.add(addParticipantBtn);

        frame.add(centerPanel, BorderLayout.CENTER);

        // --- Navigation Buttons: Save, Back, Cancel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBackground(new Color(0x1c2e4a));

        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");
        JButton cancelBtn = new JButton("Cancel");

        for (JButton btn : new JButton[]{saveBtn, backBtn, cancelBtn}) {
            btn.setBackground(new Color(0x23395d));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        // Save → commit changes
        saveBtn.addActionListener(e -> {
            String activityNameInput = nameField.getText().trim();
            if (!activityNameInput.isEmpty()) {
                List<String[]> participants = new ArrayList<>();
                for (int i = 0; i < model.getRowCount(); i++) {
                    String name = (String) model.getValueAt(i, 0);
                    String role = (String) model.getValueAt(i, 1);
                    String contact = (String) model.getValueAt(i, 2);
                    participants.add(new String[]{name, role, contact});
                }
                if (activityName != null) {
                    event.updateActivity(activityName, participants); // update existing
                } else {
                    event.addActivity(activityNameInput, participants); // new
                }
            }
            frame.dispose();
            new EventCustomization(event);
        });

        // Back → return without saving
        backBtn.addActionListener(e -> {
            frame.dispose();
            new EventCustomization(event);
        });

        // Cancel → discard changes and return
        cancelBtn.addActionListener(e -> {
            frame.dispose();
            new EventCustomization(event);
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(backBtn);
        buttonPanel.add(cancelBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().setBackground(new Color(0x1c2e4a));

        // Pre-load existing activity if editing
        if (activityName != null) {
            nameField.setText(activityName);
            List<String[]> participants = event.getParticipantsForActivity(activityName);
            for (String[] p : participants) {
                model.addRow(p);
            }
        }

        frame.setVisible(true);
    }
}
