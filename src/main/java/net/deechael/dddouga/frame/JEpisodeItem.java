package net.deechael.dddouga.frame;

import net.deechael.dddouga.item.Episode;

import javax.swing.*;

public class JEpisodeItem extends JButton {

    public JEpisodeItem(JFrame parent, Episode episode) {
        this.setVisible(true);
        this.setText(episode.getName());
        this.setSize(100, 50);
        this.addActionListener(e -> {
            new PlayerFrame(episode);
        });
    }

}
