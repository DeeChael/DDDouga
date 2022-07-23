package net.deechael.dddouga.frame;

import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.item.Douga;
import net.deechael.dddouga.utils.FrameUtils;
import net.deechael.dddouga.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class JDougaItemLarge extends JPanel {

    private final static Icon NOT_FOUND = new ImageIcon(FrameUtils.getToolkit().getImage(Objects.requireNonNull(DDDouga.class.getResource("/notfound.png"))));

    private Douga item;
    private JLabel imageLabel;

    public JDougaItemLarge(JFrame parent, Douga item) {
        this.item = item;
        this.setVisible(true);
        this.setSize(730, 300);
        this.setLocation(20, 20);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        imageLabel = new JLabel(NOT_FOUND);
        imageLabel.setVisible(true);
        imageLabel.setSize(230, 300);
        JLabel textLabel = new JLabel("<html><body>" + StringUtils.lineByLength(item.getName(), 16).replace("\n", "<br>") + "</body></html>");
        textLabel.setFont(FrameUtils.SOURCE_MEDIUM.deriveFont(25f));
        textLabel.setVisible(true);
        textLabel.setSize(500, 300);
        this.add(imageLabel, BorderLayout.NORTH);
        this.add(textLabel, BorderLayout.SOUTH);
    }

    public void refreshIcon() {
        synchronized (item.getImage()) {
            Image image = item.getImage();
            imageLabel.setIcon(image != null ? FrameUtils.getImageIcon(image, 230, 300) : NOT_FOUND);
        }
    }

}
