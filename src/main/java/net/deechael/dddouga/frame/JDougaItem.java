package net.deechael.dddouga.frame;

import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.item.Douga;
import net.deechael.dddouga.utils.FrameUtils;
import net.deechael.dddouga.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class JDougaItem extends JPanel {

    private final static Icon NOT_FOUND = new ImageIcon(FrameUtils.getToolkit().getImage(Objects.requireNonNull(DDDouga.class.getResource("/notfound.png"))));

    private Douga item;
    private JLabel imageLabel;

    public JDougaItem(JFrame parent, Douga item) {
        this.item = item;
        this.setVisible(true);
        this.setSize(180, 280);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        imageLabel = new JLabel(NOT_FOUND);
        imageLabel.setVisible(true);
        imageLabel.setSize(180, 240);
        JLabel textLabel = new JLabel("<html><body>" + StringUtils.lineByLength(item.getName(), 12).replace("\n", "<br>") + "</body></html>");
        textLabel.setVisible(true);
        textLabel.setSize(90, 40);
        this.add(imageLabel, BorderLayout.NORTH);
        this.add(textLabel, BorderLayout.SOUTH);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AnimeFrame(item);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JDougaItem.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JDougaItem.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void refreshIcon() {
        Image image = item.getImage();
        imageLabel.setIcon(image != null ? FrameUtils.getImageIcon(image, 180, 240) : NOT_FOUND);
    }


}
