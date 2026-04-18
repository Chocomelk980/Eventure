import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends JFrame {
    public LandingPage() {
        setTitle("EVENTURE");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new EventureGradientPanel(new BorderLayout()));

        // Make fullscreen but keep OS controls
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        add(EventureBranding.createTopLogoHeader(), BorderLayout.NORTH);
        add(buildCardsWrapper(), BorderLayout.CENTER);
        setVisible(true);
    }

    public JFrame getFrame() {
        return this;
    }

    public void refreshEvents() {
        getContentPane().removeAll();
        add(EventureBranding.createTopLogoHeader(), BorderLayout.NORTH);
        add(buildCardsWrapper(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel buildCardsWrapper() {
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        grid.setOpaque(false);

        for (Event event : loadEvents()) {
            grid.add(createEventCard(event));
        }
        grid.add(createAddCard());

        JPanel cardsContainer = new JPanel(new BorderLayout());
        cardsContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EventureTheme.BORDER, 4),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardsContainer.setOpaque(false);
        cardsContainer.add(grid, BorderLayout.CENTER);

        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        outerWrapper.setOpaque(false);
        outerWrapper.add(cardsContainer, BorderLayout.CENTER);
        return outerWrapper;
    }

    private List<Event> loadEvents() {
        List<Event> events = new ArrayList<>();
        try {
            events = EventureDatabase.loadAllEvents();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to load events from the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return events;
    }

    private JPanel createEventCard(Event event) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(170, 170));
        card.setBackground(EventureTheme.CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(EventureTheme.BORDER, 3));

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 10, 10, 10));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFocusPainted(false);
        deleteButton.setBackground(new Color(0x8B1E2D));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.addActionListener(e -> deleteEvent(event));
        
        JButton editButton = new JButton("Edit");
        editButton.setFocusPainted(false);
        editButton.setBackground(new Color(0x2E5B9A));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.addActionListener(e -> openEvent(event));

        JLabel label = new JLabel(
                "<html><div style='text-align:center;'>" + event.getName()
                        + "<br/><span style='font-size:10px;'>" + event.getDate()
                        + " " + event.getTime() + "</span></div></html>",
                SwingConstants.CENTER
        );
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(label);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createAddCard() {
        JPanel addCard = new JPanel(new BorderLayout());
        addCard.setPreferredSize(new Dimension(170, 170));
        addCard.setBackground(EventureTheme.CARD_BG);
        addCard.setBorder(BorderFactory.createDashedBorder(EventureTheme.BORDER, 5, 5));

        JLabel plus = new JLabel("+", SwingConstants.CENTER);
        plus.setFont(new Font("Arial", Font.BOLD, 40));
        plus.setForeground(Color.WHITE);
        addCard.add(plus, BorderLayout.CENTER);

        MouseAdapter createListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CreateEventPage();
            }
        };
        addCard.addMouseListener(createListener);
        plus.addMouseListener(createListener);

        return addCard;
    }

    private void openEvent(Event event) {
        try {
            Event fullEvent = EventureDatabase.loadEventWithDetails(event.getId());
            if (fullEvent == null) {
                throw new SQLException("Event not found: id=" + event.getId());
            }
            dispose();
            new EventCustomization(fullEvent);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to load event details from the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEvent(Event event) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + event.getName() + "\"? This cannot be undone.",
                "Delete Event",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = EventureDatabase.deleteEvent(event.getId());
            if (!deleted) {
                throw new SQLException("Event not found: id=" + event.getId());
            }
            refreshEvents();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to delete the event from the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}
