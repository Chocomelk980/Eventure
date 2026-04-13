import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class EventureBranding {
    // Keep this small; it's used in the header on multiple screens.
    private static final int LOGO_HEIGHT_PX = 32;

    private static volatile ImageIcon cachedSymbolIcon;
    private static volatile ImageIcon cachedTypeIcon;

    private EventureBranding() {
    }

    public static JPanel createTopLogoHeader() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setBackground(EventureTheme.OVERLAY);
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EventureTheme.BORDER, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel symbol = new JLabel();
        ImageIcon symbolIcon = getSymbolIcon();
        if (symbolIcon != null) {
            symbol.setIcon(symbolIcon);
        } else {
            symbol.setText("LOGO");
            symbol.setFont(new Font("Segoe UI", Font.BOLD, 24));
            symbol.setForeground(Color.WHITE);
        }

        JLabel type = new JLabel();
        ImageIcon typeIcon = getTypeIcon();
        if (typeIcon != null) {
            type.setIcon(typeIcon);
        } else {
            type.setText("EVENTURE");
            type.setFont(new Font("Segoe UI", Font.BOLD, 24));
            type.setForeground(Color.WHITE);
        }

        logoPanel.add(symbol);
        logoPanel.add(type);

        JPanel logoWrapper = new JPanel(new BorderLayout());
        logoWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        logoWrapper.setOpaque(false);
        logoWrapper.add(logoPanel, BorderLayout.CENTER);

        return logoWrapper;
    }

    private static ImageIcon getSymbolIcon() {
        if (cachedSymbolIcon != null) {
            return cachedSymbolIcon;
        }
        cachedSymbolIcon = loadAndScaleIcon("/assets/logo-symbol.png", "src/assets/logo-symbol.png", LOGO_HEIGHT_PX);
        return cachedSymbolIcon;
    }

    private static ImageIcon getTypeIcon() {
        if (cachedTypeIcon != null) {
            return cachedTypeIcon;
        }
        cachedTypeIcon = loadAndScaleIcon("/assets/logo-type.png", "src/assets/logo-type.png", LOGO_HEIGHT_PX);
        return cachedTypeIcon;
    }

    private static ImageIcon loadAndScaleIcon(String classpathPath, String fileFallbackPath, int targetHeightPx) {
        BufferedImage img = readImage(classpathPath, fileFallbackPath);
        if (img == null) {
            return null;
        }

        BufferedImage trimmed = trimTransparent(img, 8);
        BufferedImage scaled = scaleToHeight(trimmed, targetHeightPx);
        return new ImageIcon(scaled);
    }

    private static BufferedImage readImage(String classpathPath, String fileFallbackPath) {
        try (InputStream in = EventureBranding.class.getResourceAsStream(classpathPath)) {
            if (in != null) {
                return ImageIO.read(in);
            }
        } catch (IOException ignored) {
            // Fall back to file path below.
        }

        File f = new File(fileFallbackPath);
        if (!f.exists()) {
            return null;
        }

        try {
            return ImageIO.read(f);
        } catch (IOException ignored) {
            return null;
        }
    }

    private static BufferedImage trimTransparent(BufferedImage src, int alphaThreshold) {
        int w = src.getWidth();
        int h = src.getHeight();

        int minX = w;
        int minY = h;
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int a = (src.getRGB(x, y) >>> 24) & 0xFF;
                if (a > alphaThreshold) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return src;
        }

        return src.getSubimage(minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    private static BufferedImage scaleToHeight(BufferedImage src, int targetHeightPx) {
        int srcW = src.getWidth();
        int srcH = src.getHeight();
        if (srcH <= 0) {
            return src;
        }

        if (srcH == targetHeightPx) {
            return src;
        }

        double scale = targetHeightPx / (double) srcH;
        int targetWidthPx = Math.max(1, (int) Math.round(srcW * scale));

        BufferedImage dst = new BufferedImage(targetWidthPx, targetHeightPx, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dst.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(src, 0, 0, targetWidthPx, targetHeightPx, null);
        } finally {
            g.dispose();
        }

        return dst;
    }
}
