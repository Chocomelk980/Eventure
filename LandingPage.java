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

        // Top-left logo (symbol + typography)
        add(EventureBranding.createTopLogoHeader(), BorderLayout.NORTH);

        // Cards aligned left with margin
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        grid.setOpaque(false);

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

        // Event cards
        for (Event ev : events) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(150, 150));
            card.setBackground(EventureTheme.CARD_BG);
            card.setBorder(BorderFactory.createLineBorder(EventureTheme.BORDER, 3));

            JLabel label = new JLabel(ev.getName(), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            card.add(label, BorderLayout.CENTER);

            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    try {
                        Event fullEvent = EventureDatabase.loadEventWithDetails(ev.getId());
                        if (fullEvent == null) {
                            throw new SQLException("Event not found: id=" + ev.getId());
                        }
                        dispose();
                        new EventCustomization(fullEvent);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(LandingPage.this,
                                "Failed to load event details from the database.",
                                "Database Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            grid.add(card);
        }

        // “+” card with dashed border
        JPanel addCard = new JPanel(new BorderLayout());
        addCard.setPreferredSize(new Dimension(150, 150));
        addCard.setBackground(EventureTheme.CARD_BG);
        addCard.setBorder(BorderFactory.createDashedBorder(EventureTheme.BORDER, 5, 5));

        JLabel plus = new JLabel("+", SwingConstants.CENTER);
        plus.setFont(new Font("Arial", Font.BOLD, 40));
        plus.setForeground(Color.WHITE);
        addCard.add(plus, BorderLayout.CENTER);

        addCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CreateEventPage();
            }
        });

        grid.add(addCard);

        // Container panel with border around all cards
        JPanel cardsContainer = new JPanel(new BorderLayout());
        cardsContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EventureTheme.BORDER, 4),
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // inner margins
        ));
        cardsContainer.setOpaque(false);
        cardsContainer.add(grid, BorderLayout.CENTER);

        // Outer wrapper to add space between border and application edge
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // outer margins
        outerWrapper.setOpaque(false);
        outerWrapper.add(cardsContainer, BorderLayout.CENTER);

        add(outerWrapper, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}
