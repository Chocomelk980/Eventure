import javax.swing.*;

public class EventureApp {
    public static void main(String[] args) {
        // Set Nimbus Look & Feel for modern styling
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the Landing Page
        SwingUtilities.invokeLater(() -> {
            new LandingPage();
        });
    }}
