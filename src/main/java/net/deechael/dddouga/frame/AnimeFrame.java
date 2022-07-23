package net.deechael.dddouga.frame;

import net.deechael.dddouga.item.Channel;
import net.deechael.dddouga.item.Douga;
import net.deechael.dddouga.utils.FrameUtils;
import net.deechael.dddouga.utils.T80Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.util.List;

public class AnimeFrame extends JFrame {

    public AnimeFrame(Douga item) {
        this.setTitle(item.getName());
        this.setMinimumSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setLocation(FrameUtils.center(this.getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        List<Channel> channels = T80Utils.getEpisodes(item);
        JPanel panel = new JPanel();
        //panel.setSize(this.getWidth() - 16, this.getHeight() - 40);
        panel.setVisible(true);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(this.getWidth() - 16, 350 + (370 * channels.size())));
        JDougaItemLarge douga = new JDougaItemLarge(this, item);
        panel.add(douga);
        new Thread(douga::refreshIcon).start();
        for (int i = 0; i < channels.size(); i++) {
            panel.add(new JChannelItem(this, channels.get(i), i));
        }
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setSize(this.getWidth() - 16, this.getHeight() - 40);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dimension = AnimeFrame.this.getSize();
                //panel.setSize(dimension.width - 16, dimension.height - 40);
                scrollPane.setSize(dimension.width - 16, dimension.height - 40);
                panel.setPreferredSize(new Dimension(AnimeFrame.this.getWidth() - 16, 350 + (370 * channels.size())));
            }
        });
        scrollPane.setViewportView(panel);
        this.add(scrollPane);
    }

}
