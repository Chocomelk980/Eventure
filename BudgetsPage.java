import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BudgetsPage {
    private Event event;

    public BudgetsPage(Event event) {
        this.event = event;

        JFrame frame = new JFrame("Add Budget");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Create Budget", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        frame.add(title, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Budget name
        JLabel nameLabel = new JLabel("Budget Name:");
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        centerPanel.add(nameLabel);
        centerPanel.add(nameField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Budget items table
        String[] columns = {"Category", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        centerPanel.add(scrollPane);

        // Add item button
        JButton addItemBtn = new JButton("+ Add Item");
        addItemBtn.addActionListener(e -> model.addRow(new Object[]{"", ""}));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(addItemBtn);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Save/Cancel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            String budgetName = nameField.getText().trim();
            if (!budgetName.isEmpty()) {
                List<String[]> items = new ArrayList<>();
                for (int i = 0; i < model.getRowCount(); i++) {
                    String category = (String) model.getValueAt(i, 0);
                    String amount = (String) model.getValueAt(i, 1);
                    items.add(new String[]{category, amount});
                }
                event.addBudget(budgetName, items);
            }
            frame.dispose();
            new EventCustomization(event);
        });

        cancelBtn.addActionListener(e -> {
            frame.dispose();
            new EventCustomization(event);
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
