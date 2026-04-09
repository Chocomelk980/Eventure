import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BudgetDetailsPage {
    public BudgetDetailsPage(String budgetName, List<String[]> items) {
        JFrame frame = new JFrame("Budget: " + budgetName);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Items for " + budgetName, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        // Table with Category + Amount
        String[] columns = {"Category", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Populate rows
        for (String[] item : items) {
            model.addRow(item);
        }

        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            frame.dispose();
            new EventCustomization(new Event(budgetName));
            // NOTE: replace with your actual event reference if needed
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
