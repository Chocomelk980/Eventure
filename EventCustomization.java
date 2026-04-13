import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
@SuppressWarnings("all")
public class EventCustomization {
    private JFrame frame;
    private Event event;

    public EventCustomization(Event event) {
        this.event = event;
        frame = new JFrame("Customize: " + event.getName());
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fullscreen with OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setContentPane(new EventureGradientPanel(new BorderLayout()));

        // Banner placeholder (transparent so gradient shows through)
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
        banner.setOpaque(false); // let gradient show
        frame.add(banner, BorderLayout.NORTH);

        // Bottom panel with two columns
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.setOpaque(false); // transparent

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

        activityList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = activityList.getSelectedValue();
                    if (selected != null) {
                        new ActivitiesPage(EventCustomization.this, event, selected);
                    }
                }
            }
        });

        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.setOpaque(false);
        activitiesPanel.add(activityScroll, BorderLayout.CENTER);

        JPanel activitiesBox = new JPanel(new BorderLayout());
        activitiesBox.setPreferredSize(new Dimension(200, 80));
        activitiesBox.setBackground(EventureTheme.CARD_BG); // keep solid for contrast
        activitiesBox.setBorder(BorderFactory.createDashedBorder(new Color(0x23395d), 5, 5));
        JLabel plusAct = new JLabel("+", SwingConstants.CENTER);
        plusAct.setFont(new Font("Segoe UI", Font.BOLD, 40));
        plusAct.setForeground(Color.WHITE);
        activitiesBox.add(plusAct, BorderLayout.CENTER);

        JPanel activitiesWrapper = new JPanel(new BorderLayout());
        activitiesWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        activitiesWrapper.setOpaque(false);
        activitiesWrapper.add(activitiesBox, BorderLayout.CENTER);

        activitiesBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new ActivitiesPage(EventCustomization.this, event);
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

        budgetList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = budgetList.getSelectedValue();
                    if (selected != null) {
                        new BudgetsPage(EventCustomization.this, event, selected);
                    }
                }
            }
        });

        JPanel budgetsPanel = new JPanel(new BorderLayout());
        budgetsPanel.setOpaque(false);
        budgetsPanel.add(budgetScroll, BorderLayout.CENTER);

        JPanel budgetsBox = new JPanel(new BorderLayout());
        budgetsBox.setPreferredSize(new Dimension(200, 80));
        budgetsBox.setBackground(EventureTheme.CARD_BG); // keep solid for contrast
        budgetsBox.setBorder(BorderFactory.createDashedBorder(new Color(0x23395d), 5, 5));
        JLabel plusBud = new JLabel("+", SwingConstants.CENTER);
        plusBud.setFont(new Font("Segoe UI", Font.BOLD, 40));
        plusBud.setForeground(Color.WHITE);
        budgetsBox.add(plusBud, BorderLayout.CENTER);

        JPanel budgetsWrapper = new JPanel(new BorderLayout());
        budgetsWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        budgetsWrapper.setOpaque(false);
        budgetsWrapper.add(budgetsBox, BorderLayout.CENTER);

        budgetsBox.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new BudgetsPage(EventCustomization.this, event);
            }
        });
        budgetsPanel.add(budgetsWrapper, BorderLayout.SOUTH);
        bottomPanel.add(budgetsPanel);

        frame.add(bottomPanel, BorderLayout.CENTER);

        // Home button panel
        JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        homePanel.setOpaque(false);
        JButton homeBtn = new JButton("Home");
        homeBtn.setBackground(new Color(0x23395d));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.addActionListener(e -> {
            frame.dispose();
            new LandingPage();
        });
        homePanel.add(homeBtn);
        frame.add(homePanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
