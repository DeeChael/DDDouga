package net.deechael.dddouga.frame;

import net.deechael.dddouga.item.Channel;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class JChannelItem extends JPanel {

    public JChannelItem(JFrame parent, Channel channel, int index) {
        this.setVisible(true);
        this.setLayout(null);
        this.setSize(parent.getWidth() - 80, 350);
        this.setLocation(20, 350 + (370 * index));
        this.setBorder(BorderFactory.createTitledBorder(channel.getName()));
        JScrollPane scrollPane = new JScrollPane();
        JPanel inPanel = new JPanel();
        inPanel.setVisible(true);
        inPanel.setLayout(new AutoLineFlowLayout());
        //parent.addComponentListener(new ComponentAdapter() {
        //    @Override
        //    public void componentResized(ComponentEvent e) {
        //        inPanel.setSize(parent.getWidth() - 16, 350);
        //    }
        //});
        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JChannelItem.this.setSize(parent.getWidth() - 60, 300);
                scrollPane.setSize(parent.getWidth() - 60, 300);
                inPanel.setSize(parent.getWidth() - 60, 300);
            }
        });
        channel.getEpisodes().forEach(episode -> inPanel.add(new JEpisodeItem(parent, episode)));
        scrollPane.setViewportView(inPanel);
        this.add(scrollPane);
    }

}
