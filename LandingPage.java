import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LandingPage extends JFrame {
    private static java.util.List<Event> events = new ArrayList<>();

    public LandingPage() {
        setTitle("EVENTURE");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Make fullscreen but keep OS controls
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // Logo aligned left with margin and visible box
        JLabel logo = new JLabel("LOGO");
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setHorizontalAlignment(SwingConstants.LEFT);
        logo.setForeground(Color.WHITE);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        logoPanel.setBackground(new Color(0x1c2e4a)); // middle palette color
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x23395d), 3), // lighter tone
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        logoPanel.add(logo);

        // Outer wrapper for logo panel to add margins
        JPanel logoWrapper = new JPanel(new BorderLayout());
        logoWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20)); // margins around logo
        logoWrapper.setBackground(new Color(0x1c2e4a));
        logoWrapper.add(logoPanel, BorderLayout.CENTER);

        add(logoWrapper, BorderLayout.NORTH);

        // Cards aligned left with margin
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        grid.setBackground(new Color(0x1c2e4a)); // middle palette color

        // Event cards
        for (Event ev : events) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(150, 150));
            card.setBackground(new Color(0x1c2e4a)); // middle palette color
            card.setBorder(BorderFactory.createLineBorder(new Color(0x23395d), 3)); // lighter tone

            JLabel label = new JLabel(ev.getName(), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            card.add(label, BorderLayout.CENTER);

            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    new EventCustomization(ev);
                }
            });

            grid.add(card);
        }

        // “+” card with dashed border
        JPanel addCard = new JPanel(new BorderLayout());
        addCard.setPreferredSize(new Dimension(150, 150));
        addCard.setBackground(new Color(0x1c2e4a)); // middle palette color
        addCard.setBorder(BorderFactory.createDashedBorder(new Color(0x23395d), 5, 5)); // lighter tone

        JLabel plus = new JLabel("+", SwingConstants.CENTER);
        plus.setFont(new Font("Arial", Font.BOLD, 40));
        plus.setForeground(Color.WHITE);
        addCard.add(plus, BorderLayout.CENTER);

        addCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CreateEventPage(events);
            }
        });

        grid.add(addCard);

        // Container panel with border around all cards
        JPanel cardsContainer = new JPanel(new BorderLayout());
        cardsContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x23395d), 4), // lighter tone border
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // inner margins
        ));
        cardsContainer.setBackground(new Color(0x1c2e4a)); // middle palette color
        cardsContainer.add(grid, BorderLayout.CENTER);

        // Outer wrapper to add space between border and application edge
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // outer margins
        outerWrapper.setBackground(new Color(0x1c2e4a));
        outerWrapper.add(cardsContainer, BorderLayout.CENTER);

        add(outerWrapper, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(0x1c2e4a)); // middle palette color
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}
