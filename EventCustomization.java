import javax.swing.*;
import javax.swing.border.TitledBorder;
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

        // Make fullscreen but keep OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);

        // Banner placeholder
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0x23395d));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(10, 10, getWidth()-20, getHeight()-20, 30, 30);
                g2.drawLine(20, 20, getWidth()-20, getHeight()-20);
                g2.drawLine(getWidth()-20, 20, 20, getHeight()-20);
            }
        };
        banner.setPreferredSize(new Dimension(600, 150));
        banner.setBackground(new Color(0x1c2e4a));
        frame.add(banner, BorderLayout.NORTH);

        // Bottom panel with two columns
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.setBackground(new Color(0x1c2e4a));

        // Activities column
        DefaultListModel<String> activityModel = new DefaultListModel<>();
        for (String a : event.getActivityNames()) {
            activityModel.addElement(a);
        }
        JList<String> activityList = new JList<>(activityModel);
        activityList.setBackground(new Color(0x4a5d7c));
        activityList.setForeground(Color.WHITE);
        activityList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane activityScroll = new JScrollPane(activityList);
        TitledBorder activitiesBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x23395d), 2),
                "Activities"
        );
        activitiesBorder.setTitleColor(Color.WHITE);
        activitiesBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        activityScroll.setBorder(activitiesBorder);

        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(new Color(0x23395d));
                } else {
                    if (index % 2 == 0) {
                        label.setBackground(new Color(0x3b4a6b));
                    } else {
                        label.setBackground(new Color(0x4a5d7c));
                    }
                }

                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return label;
            }
        };
        activityList.setCellRenderer(renderer);

        // FIXED: double-click passes selected name
        activityList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = activityList.getSelectedValue();
                    if (selected != null) {
                        frame.dispose();
                        new ActivitiesPage(event, selected);
                    }
                }
            }
        });

        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.setBackground(new Color(0x1c2e4a));
        activitiesPanel.add(activityScroll, BorderLayout.CENTER);

        JPanel activitiesBox = new JPanel(new BorderLayout());
        activitiesBox.setPreferredSize(new Dimension(200, 80));
        activitiesBox.setBackground(new Color(0x1c2e4a));
        activitiesBox.setBorder(BorderFactory.createDashedBorder(new Color(0x23395d), 5, 5));
        JLabel plusAct = new JLabel("+", SwingConstants.CENTER);
        plusAct.setFont(new Font("Segoe UI", Font.BOLD, 40));
        plusAct.setForeground(Color.WHITE);
        activitiesBox.add(plusAct, BorderLayout.CENTER);

        JPanel activitiesWrapper = new JPanel(new BorderLayout());
        activitiesWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        activitiesWrapper.setBackground(new Color(0x1c2e4a));
        activitiesWrapper.add(activitiesBox, BorderLayout.CENTER);

        activitiesBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new ActivitiesPage(event);
            }
        });
        activitiesPanel.add(activitiesWrapper, BorderLayout.SOUTH);
        bottomPanel.add(activitiesPanel);

        // Budgets column
        DefaultListModel<String> budgetModel = new DefaultListModel<>();
        for (String b : event.getBudgetNames()) {
            budgetModel.addElement(b);
        }
        JList<String> budgetList = new JList<>(budgetModel);
        budgetList.setBackground(new Color(0x4a5d7c));
        budgetList.setForeground(Color.WHITE);
        budgetList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane budgetScroll = new JScrollPane(budgetList);
        TitledBorder budgetsBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x23395d), 2),
                "Budgets"
        );
        budgetsBorder.setTitleColor(Color.WHITE);
        budgetsBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        budgetScroll.setBorder(budgetsBorder);

        budgetList.setCellRenderer(renderer);

        // FIXED: double-click passes selected name
        budgetList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = budgetList.getSelectedValue();
                    if (selected != null) {
                        frame.dispose();
                        new BudgetsPage(event, selected);
                    }
                }
            }
        });

        JPanel budgetsPanel = new JPanel(new BorderLayout());
        budgetsPanel.setBackground(new Color(0x1c2e4a));
        budgetsPanel.add(budgetScroll, BorderLayout.CENTER);

        JPanel budgetsBox = new JPanel(new BorderLayout());
        budgetsBox.setPreferredSize(new Dimension(200, 80));
        budgetsBox.setBackground(new Color(0x1c2e4a));
        budgetsBox.setBorder(BorderFactory.createDashedBorder(new Color(0x23395d), 5, 5));
        JLabel plusBud = new JLabel("+", SwingConstants.CENTER);
        plusBud.setFont(new Font("Segoe UI", Font.BOLD, 40));
        plusBud.setForeground(Color.WHITE);
        budgetsBox.add(plusBud, BorderLayout.CENTER);

        JPanel budgetsWrapper = new JPanel(new BorderLayout());
        budgetsWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        budgetsWrapper.setBackground(new Color(0x1c2e4a));
        budgetsWrapper.add(budgetsBox, BorderLayout.CENTER);

        budgetsBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new BudgetsPage(event);
            }
        });
        budgetsPanel.add(budgetsWrapper, BorderLayout.SOUTH);
        bottomPanel.add(budgetsPanel);

        frame.add(bottomPanel, BorderLayout.CENTER);

        // --- NEW: Home button at bottom center ---
        JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        homePanel.setBackground(new Color(0x1c2e4a));

        JButton homeBtn = new JButton("Home");
        homeBtn.setBackground(new Color(0x23395d));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);

        homeBtn.addActionListener(e -> {
            frame.dispose();
            new LandingPage(); // replace with your actual home page class
        });

        homePanel.add(homeBtn);
        frame.add(homePanel, BorderLayout.SOUTH);

        frame.getContentPane().setBackground(new Color(0x1c2e4a));
        frame.setVisible(true);
    }
}
