import java.util.ArrayList;

public class BudgetsPage {
    private Event event;
    private String budgetName; // track if editing

    public BudgetsPage(Event event) {
        this(event, null); // default: new budget
    }

    public BudgetsPage(Event event, String budgetName) {
        this.event = event;
        this.budgetName = budgetName;

        JFrame frame = new JFrame(budgetName == null ? "Add Budget" : "Edit Budget");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Make fullscreen but keep OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);

        JLabel title = new JLabel(budgetName == null ? "Create Budget" : "Edit Budget", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0x1c2e4a));
        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(0x1c2e4a));

        JLabel nameLabel = new JLabel("Budget Category:");
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

        String[] columns = {"Item", "Amount", "Price per Item", "Total Spent"};
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
                    c.setBackground(new Color(0x345b8c)); // highlight new row
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

        JButton addItemBtn = new JButton("+ Add Item");
        addItemBtn.setBackground(new Color(0x23395d));
        addItemBtn.setForeground(Color.WHITE);
        addItemBtn.setFocusPainted(false);
        addItemBtn.addActionListener(e -> {
            model.addRow(new Object[]{"", "", "", ""});
            lastAddedRow[0] = model.getRowCount() - 1;
            table.repaint();
        });
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(addItemBtn);

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
        // Save → commit changes with validation
        saveBtn.addActionListener(e -> {
            String budgetNameInput = nameField.getText().trim();

            // Validate budget name (letters + spaces only, max 50)
            if (budgetNameInput.isEmpty() || budgetNameInput.length() > 50 || !budgetNameInput.matches("^[A-Za-z ]+$")) {
                JOptionPane.showMessageDialog(frame,
                        "Budget name must be 1–50 letters only (no symbols or numbers).",
                        "Invalid Budget Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String[]> items = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String item = model.getValueAt(i, 0) != null ? ((String) model.getValueAt(i, 0)).trim() : "";
                String amount = model.getValueAt(i, 1) != null ? ((String) model.getValueAt(i, 1)).trim() : "";
                String pricePerItem = model.getValueAt(i, 2) != null ? ((String) model.getValueAt(i, 2)).trim() : "";
                String totalSpent = model.getValueAt(i, 3) != null ? ((String) model.getValueAt(i, 3)).trim() : "";

                // Skip completely empty rows
                if (item.isEmpty() && amount.isEmpty() && pricePerItem.isEmpty() && totalSpent.isEmpty()) {
                    continue;
                }

                // Validate item name
                if (!item.isEmpty() && (item.length() > 50 || !item.matches("^[A-Za-z ]+$"))) {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid item name at row " + (i+1) +
                                    ". Must be 1–50 letters only (no symbols or numbers).",
                            "Invalid Item Name",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate amount (must be integer > 0)
                if (!amount.isEmpty() && !amount.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid amount at row " + (i+1) +
                                    ". Must be a whole positive number.",
                            "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate price per item (numeric, up to 2 decimals)
                if (!pricePerItem.isEmpty() && !pricePerItem.matches("^\\d+(\\.\\d{1,2})?$")) {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid price per item at row " + (i+1) +
                                    ". Must be a positive number (up to 2 decimals).",
                            "Invalid Price",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate total spent (numeric, up to 2 decimals)
                if (!totalSpent.isEmpty() && !totalSpent.matches("^\\d+(\\.\\d{1,2})?$")) {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid total spent at row " + (i+1) +
                                    ". Must be a positive number (up to 2 decimals).",
                            "Invalid Total Spent",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                items.add(new String[]{item, amount, pricePerItem, totalSpent});
            }

            // Require at least one item
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "You must add at least one budget item before saving.",
                        "No Items",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save or update
            if (budgetName != null) {
                event.updateBudget(budgetName, items); // update existing
            } else {
                event.addBudget(budgetNameInput, items); // new
            }

            JOptionPane.showMessageDialog(frame,
                    "Budget saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

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

        // Pre-load existing budget if editing
        if (budgetName != null) {
            nameField.setText(budgetName);
            List<String[]> items = event.getItemsForBudget(budgetName);
            for (String[] i : items) {
                model.addRow(i);
            }
        }

        frame.setVisible(true);
    }
}
