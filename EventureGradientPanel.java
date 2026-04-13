import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class EventureGradientPanel extends JPanel {
    public EventureGradientPanel() {
        super();
        setOpaque(true);
        setBackground(EventureTheme.BLUE_3);
    }

    public EventureGradientPanel(LayoutManager layout) {
        super(layout);
        setOpaque(true);
        setBackground(EventureTheme.BLUE_3);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float[] fractions = new float[]{0f, 0.25f, 0.5f, 0.75f, 1f};
            Color[] colors = new Color[]{
                    EventureTheme.BLUE_1,
                    EventureTheme.BLUE_2,
                    EventureTheme.BLUE_3,
                    EventureTheme.BLUE_4,
                    EventureTheme.BLUE_5
            };

            LinearGradientPaint paint = new LinearGradientPaint(
                    new Point2D.Float(0, 0),
                    new Point2D.Float(w, h),
                    fractions,
                    colors
            );

            g2.setPaint(paint);
            g2.fillRect(0, 0, w, h);
        } finally {
            g2.dispose();
        }
    }
}

