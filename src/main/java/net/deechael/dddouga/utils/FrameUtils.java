package net.deechael.dddouga.utils;

import net.deechael.dddouga.DDDouga;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public final class FrameUtils {

    public final static Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    public final static Font SOURCE_REGULAR;
    public final static Font SOURCE_EXTRA_LIGHT;
    public final static Font SOURCE_LIGHT;
    public final static Font SOURCE_NORMAL;
    public final static Font SOURCE_BOLD;
    public final static Font SOURCE_MEDIUM;
    public final static Font SOURCE_HEAVY;

    static {
        try {
            SOURCE_REGULAR = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-regular.otf")));
            SOURCE_EXTRA_LIGHT = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-extralight.otf")));
            SOURCE_LIGHT = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-light.otf")));
            SOURCE_NORMAL = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-normal.otf")));
            SOURCE_BOLD = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-bold.otf")));
            SOURCE_MEDIUM = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-medium.otf")));
            SOURCE_HEAVY = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(DDDouga.class.getResourceAsStream("/source-heavy.otf")));
        } catch (FontFormatException | IOException e) {
            System.exit(-11);
            throw new RuntimeException(e);
        }
    }

    public static Toolkit getToolkit() {
        return TOOLKIT;
    }

    public static Point center() {
        Dimension dimension = getToolkit().getScreenSize();
        return new Point(dimension.width / 2, dimension.height / 2);
    }

    public static Point center(Dimension size) {
        return center(size.width, size.height);
    }

    public static Point center(int width, int height) {
        Dimension dimension = getToolkit().getScreenSize();
        return new Point((dimension.width - width) / 2, (dimension.height - height) / 2);
    }

    public static Image fromUrl(String url) {
        try (InputStream inputStream = new URL(url).openConnection().getInputStream()){
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void warning(String message) {
        JOptionPane.showMessageDialog(null, message, "DD动画", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(String message) {
        JOptionPane.showMessageDialog(null, message, "DD动画", JOptionPane.ERROR_MESSAGE);
    }

    public static ImageIcon getImageIcon(Image image, int width, int height) {
        if (width == 0 || height == 0) {
            throw new RuntimeException("Size cannot be zero");
        }
        ImageIcon icon = new ImageIcon();
        icon.setImage(image.getScaledInstance(width, height,
                Image.SCALE_DEFAULT));
        return icon;
    }

    public static ImageIcon fromResource(String path) {
        if (!path.startsWith("/"))
            path = "/" + path;
        return new ImageIcon(getToolkit().getImage(DDDouga.class.getResource(path)));
    }

    public static ImageIcon fromResource(String path, int width, int height) {
        if (!path.startsWith("/"))
            path = "/" + path;
        ImageIcon icon = new ImageIcon();
        icon.setImage(getToolkit().getImage(DDDouga.class.getResource(path)).getScaledInstance(width, height,
                Image.SCALE_DEFAULT));
        return icon;
    }

    private FrameUtils() {}

}
