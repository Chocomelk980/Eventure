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

        // Logo aligned left with margin and visible box
        JLabel logo = new JLabel("LOGO");
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        logoPanel.setBackground(new Color(240, 240, 240));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        logoPanel.add(logo);
        add(logoPanel, BorderLayout.NORTH);

        // Cards aligned left with margin
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Event cards
        for (Event ev : events) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(150, 150));
            card.setBackground(new Color(240, 240, 240));
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

            JLabel label = new JLabel(ev.getName(), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
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
        addCard.setBackground(new Color(220, 220, 220));
        addCard.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 5, 5));

        JLabel plus = new JLabel("+", SwingConstants.CENTER);
        plus.setFont(new Font("Arial", Font.BOLD, 40));
        addCard.add(plus, BorderLayout.CENTER);

        addCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new CreateEventPage(events);
            }
        });

        grid.add(addCard);

        add(grid, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}