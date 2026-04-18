import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
@SuppressWarnings("all")
public class EventCustomization {
    private static final DecimalFormat BUDGET_FORMAT = new DecimalFormat("#,##0.00");
    private static final String DATE_INPUT_PATTERN = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/(25|26|27|28|29|30)$";
    private static final String TIME_INPUT_PATTERN = "^(0?[1-9]|1[0-2]):[0-5][0-9](?i)(am|pm)$";
    private static final String BUDGET_INPUT_PATTERN = "^\\d+(\\.\\d{1,2})?$";
    private static final BigDecimal MAX_TOTAL_BUDGET = new BigDecimal("500000");

    private JFrame frame;
    private Event event;
    private JPanel dashboardPanel;
    private DefaultListModel<String> activityModel;
    private DefaultListModel<String> budgetModel;

    public EventCustomization(Event event) {
        this.event = event;
        frame = new JFrame("Customize: " + event.getName());
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fullscreen with OS controls
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setContentPane(new EventureGradientPanel(new BorderLayout()));

        dashboardPanel = buildDashboardPanel();
        frame.add(dashboardPanel, BorderLayout.NORTH);

        // Bottom panel with two columns
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.setOpaque(false); // transparent

        // Activities column
        activityModel = new DefaultListModel<>();
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

        DefaultListCellRenderer stripedRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setOpaque(true);
                label.setBackground(getListRowColor(index, isSelected));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return label;
            }
        };
        activityList.setCellRenderer(createActivityListRenderer());

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
        budgetModel = new DefaultListModel<>();
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

        budgetList.setCellRenderer(stripedRenderer);

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
        JButton homeBtn = new JButton("Back to Homepage");
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

    private ListCellRenderer<? super String> createActivityListRenderer() {
        return new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(
                    JList<? extends String> list,
                    String value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
                rowPanel.setOpaque(true);
                rowPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                rowPanel.setBackground(getListRowColor(index, isSelected));

                JLabel nameLabel = new JLabel(value);
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                nameLabel.setOpaque(false);

                String activityStatus = event.getActivityStatus(value);
                JLabel statusLabel = new JLabel(activityStatus);
                statusLabel.setForeground(getActivityStatusColor(activityStatus));
                statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                statusLabel.setOpaque(false);

                rowPanel.add(nameLabel, BorderLayout.WEST);
                rowPanel.add(statusLabel, BorderLayout.EAST);
                return rowPanel;
            }
        };
    }

    private Color getListRowColor(int index, boolean isSelected) {
        if (isSelected) {
            return new Color(0x23395d);
        }
        return index % 2 == 0 ? new Color(0x3b4a6b) : new Color(0x4a5d7c);
    }

    private Color getActivityStatusColor(String status) {
        if ("Closed".equalsIgnoreCase(status)) {
            return new Color(0xF5B6B6);
        }
        return new Color(0xB7F5C6);
    }

    private JPanel buildDashboardPanel() {
        JPanel dashboardWrapper = new JPanel(new BorderLayout());
        dashboardWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        dashboardWrapper.setOpaque(false);

        JPanel dashboard = new JPanel(new BorderLayout(24, 0));
        dashboard.setPreferredSize(new Dimension(600, 160));
        dashboard.setBackground(new Color(0x1B2D49));
        dashboard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x28456E), 3, true),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));

        JPanel overviewPanel = new JPanel();
        overviewPanel.setOpaque(false);
        overviewPanel.setLayout(new BoxLayout(overviewPanel, BoxLayout.Y_AXIS));

        String eventStatus = event.getEventStatus();
        Color eventStatusColor = getEventStatusColor(eventStatus);
        BigDecimal remainingBudget = event.getRemainingBudgetAmount();
        Color remainingBudgetColor = getRemainingBudgetColor(remainingBudget);

        JLabel overviewLabel = new JLabel("Event Dashboard");
        overviewLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        overviewLabel.setForeground(new Color(0xA8C1E8));
        overviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        overviewPanel.add(overviewLabel);
        overviewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        overviewPanel.add(nameLabel);
        overviewPanel.add(Box.createVerticalGlue());

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createDashboardCard("Event Status", eventStatus, eventStatusColor));
        statsPanel.add(createDashboardCard("Total Budget", formatBudget(remainingBudget), remainingBudgetColor, this::editTotalBudget));
        statsPanel.add(createDashboardCard("Date", event.getDate(), Color.WHITE, this::editDate));
        statsPanel.add(createDashboardCard("Time", event.getTime(), Color.WHITE, this::editTime));

        dashboard.add(overviewPanel, BorderLayout.CENTER);
        dashboard.add(statsPanel, BorderLayout.EAST);
        dashboardWrapper.add(dashboard, BorderLayout.CENTER);
        return dashboardWrapper;
    }

    private JPanel createDashboardCard(String title, String value) {
        return createDashboardCard(title, value, Color.WHITE, null);
    }

    private JPanel createDashboardCard(String title, String value, Color valueColor) {
        return createDashboardCard(title, value, valueColor, null);
    }

    private JPanel createDashboardCard(String title, String value, Color valueColor, Runnable onClick) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(0x324A70));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x516C96), 2, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(130, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(0xB9D0F5));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel("<html>" + value + "</html>");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(valueColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        if (onClick != null) {
            makeDashboardCardClickable(card, onClick, title);
            makeDashboardCardClickable(titleLabel, onClick, title);
            makeDashboardCardClickable(valueLabel, onClick, title);
        }

        return card;
    }

    private void makeDashboardCardClickable(Component component, Runnable onClick, String title) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (component instanceof JComponent) {
            ((JComponent) component).setToolTipText("Click to edit " + title.toLowerCase() + ".");
        }
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });
    }

    private Color getEventStatusColor(String eventStatus) {
        if ("Concluded".equals(eventStatus)) {
            return new Color(0xF5B6B6);
        }
        if ("In Progress".equals(eventStatus)) {
            return new Color(0xB7F5C6);
        }
        return new Color(0xFFF1B3);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void refreshLists() {
        // Refresh activity list
        activityModel.clear();
        for (String a : event.getActivityNames()) {
            activityModel.addElement(a);
        }
        
        // Refresh budget list
        budgetModel.clear();
        for (String b : event.getBudgetNames()) {
            budgetModel.addElement(b);
        }

        refreshDashboard();
    }

    private void refreshDashboard() {
        if (dashboardPanel != null) {
            frame.remove(dashboardPanel);
        }
        dashboardPanel = buildDashboardPanel();
        frame.add(dashboardPanel, BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }

    private Color getRemainingBudgetColor(BigDecimal remainingBudget) {
        if (remainingBudget.compareTo(BigDecimal.ZERO) < 0) {
            return new Color(0xF5B6B6);
        }
        if (remainingBudget.compareTo(BigDecimal.ZERO) == 0) {
            return new Color(0xFFF1B3);
        }
        return new Color(0xB7F5C6);
    }

    private String formatBudget(BigDecimal amount) {
        return BUDGET_FORMAT.format(amount);
    }

    private void editDate() {
        String updatedDate = promptForUpdatedValue(
                "Edit Date",
                "Enter a new date (MM/DD/YY):",
                event.getDate()
        );
        if (updatedDate == null) {
            return;
        }
        if (!updatedDate.matches(DATE_INPUT_PATTERN)) {
            JOptionPane.showMessageDialog(frame,
                    "Date must be in MM/DD/YY format with a year from 25 to 30 (e.g., 04/11/26).",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        persistEventDetails(updatedDate, event.getTime(), event.getTotalBudget());
    }

    private void editTime() {
        String updatedTime = promptForUpdatedValue(
                "Edit Time",
                "Enter a new time (hh:mmam/pm):",
                event.getTime()
        );
        if (updatedTime == null) {
            return;
        }
        if (!updatedTime.matches(TIME_INPUT_PATTERN)) {
            JOptionPane.showMessageDialog(frame,
                    "Time must be in hh:mmam/pm format (e.g., 10:00am).",
                    "Invalid Time",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        persistEventDetails(event.getDate(), updatedTime, event.getTotalBudget());
    }

    private void editTotalBudget() {
        String updatedBudget = promptForUpdatedValue(
                "Edit Total Budget",
                "Enter a new total budget:",
                event.getTotalBudget()
        );
        if (updatedBudget == null) {
            return;
        }
        if (!updatedBudget.matches(BUDGET_INPUT_PATTERN)) {
            JOptionPane.showMessageDialog(frame,
                    "Total budget must be a positive number with up to 2 decimals (e.g., 15000.00).",
                    "Invalid Budget",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal updatedBudgetAmount = new BigDecimal(updatedBudget);
        if (updatedBudgetAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                updatedBudgetAmount.compareTo(MAX_TOTAL_BUDGET) > 0) {
            JOptionPane.showMessageDialog(frame,
                    "Total budget must be greater than 0 and not exceed 500,000.00.",
                    "Invalid Budget",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        persistEventDetails(event.getDate(), event.getTime(), updatedBudget);
    }

    private String promptForUpdatedValue(String title, String message, String currentValue) {
        String updatedValue = (String) JOptionPane.showInputDialog(
                frame,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                currentValue
        );
        if (updatedValue == null) {
            return null;
        }
        return updatedValue.trim();
    }

    private void persistEventDetails(String date, String time, String totalBudget) {
        try {
            boolean updated = EventureDatabase.updateEventDetails(event.getId(), date, time, totalBudget);
            if (!updated) {
                throw new java.sql.SQLException("Event not found: id=" + event.getId());
            }

            event.setDate(date);
            event.setTime(time);
            event.setTotalBudget(totalBudget);
            refreshDashboard();
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Failed to update the event details.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
