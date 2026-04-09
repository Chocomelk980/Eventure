import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ActivityDetailsPage {
    public ActivityDetailsPage(String activityName, List<String[]> participants) {
        JFrame frame = new JFrame("Activity: " + activityName);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Participants for " + activityName, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        String[] columns = {"Name", "Role", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        for (String[] p : participants) {
            model.addRow(p);
        }

        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}