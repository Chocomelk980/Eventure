import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EventCustomization {
    private Event event;

    public EventCustomization(Event event) {
        this.event = event;
        JFrame frame = new JFrame("Customize: " + event.getName());
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Banner placeholder
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(10, 10, getWidth()-20, getHeight()-20, 30, 30);
                g2.drawLine(20, 20, getWidth()-20, getHeight()-20);
                g2.drawLine(getWidth()-20, 20, 20, getHeight()-20);
            }
        };
        banner.setPreferredSize(new Dimension(600, 150));
        frame.add(banner, BorderLayout.NORTH);

        // Bottom panel with two columns
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Activities column
        DefaultListModel<String> activityModel = new DefaultListModel<>();
        for (String a : event.getActivityNames()) {
            activityModel.addElement(a);
        }
        JList<String> activityList = new JList<>(activityModel);
        activityList.setBorder(BorderFactory.createTitledBorder("Activities"));

        // Double-click opens ActivityDetailsPage
        activityList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = activityList.getSelectedValue();
                    if (selected != null) {
                        new ActivityDetailsPage(selected, event.getParticipantsForActivity(selected));
                    }
                }
            }
        });

        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.add(new JScrollPane(activityList), BorderLayout.CENTER);

        // Add Activity box
        JPanel activitiesBox = new JPanel(new BorderLayout());
        activitiesBox.setPreferredSize(new Dimension(200, 80));
        activitiesBox.setBackground(new Color(240, 240, 240));
        activitiesBox.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 5, 5));
        JLabel plusAct = new JLabel("+", SwingConstants.CENTER);
        plusAct.setFont(new Font("Arial", Font.BOLD, 40));
        activitiesBox.add(plusAct, BorderLayout.CENTER);
        activitiesBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new ActivitiesPage(event);
            }
        });
        activitiesPanel.add(activitiesBox, BorderLayout.SOUTH);
        bottomPanel.add(activitiesPanel);

        // Budgets column
        DefaultListModel<String> budgetModel = new DefaultListModel<>();
        for (String b : event.getBudgetNames()) {
            budgetModel.addElement(b);
        }
        JList<String> budgetList = new JList<>(budgetModel);
        budgetList.setBorder(BorderFactory.createTitledBorder("Budgets"));

        // Double-click opens BudgetDetailsPage
        budgetList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = budgetList.getSelectedValue();
                    if (selected != null) {
                        new BudgetDetailsPage(selected, event.getItemsForBudget(selected));
                    }
                }
            }
        });

        JPanel budgetsPanel = new JPanel(new BorderLayout());
        budgetsPanel.add(new JScrollPane(budgetList), BorderLayout.CENTER);

        // Add Budget box
        JPanel budgetsBox = new JPanel(new BorderLayout());
        budgetsBox.setPreferredSize(new Dimension(200, 80));
        budgetsBox.setBackground(new Color(240, 240, 240));
        budgetsBox.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 5, 5));
        JLabel plusBud = new JLabel("+", SwingConstants.CENTER);
        plusBud.setFont(new Font("Arial", Font.BOLD, 40));
        budgetsBox.add(plusBud, BorderLayout.CENTER);
        budgetsBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new BudgetsPage(event);
            }
        });
        budgetsPanel.add(budgetsBox, BorderLayout.SOUTH);
        bottomPanel.add(budgetsPanel);

        frame.add(bottomPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}