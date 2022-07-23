package net.deechael.dddouga.frame;

import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.item.Episode;
import net.deechael.dddouga.utils.FrameUtils;
import net.deechael.dddouga.utils.T80Utils;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerFrame extends JFrame {


    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final JPanel playerPanel = new JPanel();
    private final JPanel buttonsPanel = new JPanel();

    private final JButton pause = new JButton();
    private final JButton play = new JButton();

    public PlayerFrame(Episode episode) {
        Container container = this.getContentPane();
        this.setTitle(episode.getOwner().getName() + " - " + episode.getName());
        this.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setLocation(FrameUtils.center(this.getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        Canvas vs = new Canvas();
        vs.setBackground(Color.black);

        playerPanel.setLayout(new BorderLayout());
        playerPanel.add(vs, BorderLayout.CENTER);

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(vs));
        mediaPlayer.controls().setRepeat(false);

        // Load link is quite slow, should be optimized
        // FIXME
        mediaPlayer.media().prepare(T80Utils.getPlayLink(episode));
        mediaPlayer.media().events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaSubItemAdded(Media media, MediaRef newChild) {
                DDDouga.getLogger().debug("Media sub item was added:");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    DDDouga.getLogger().debug("- mrl=" + mrl);
                }
                DDDouga.getLogger().debug("\n");
                mediaList.release();
            }

            @Override
            public void mediaSubItemTreeAdded(Media media, MediaRef item) {
                DDDouga.getLogger().debug("Media sub item tree was added");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    DDDouga.getLogger().debug("- mrl=" + mrl);
                }
                DDDouga.getLogger().debug("\n");
                mediaList.release();
            }
        });

        // More buttons: Volume, Fullscreen, Download
        // TODO
        buttonsPanel.add(pause);
        buttonsPanel.add(play);


        this.add(playerPanel);
        this.add(buttonsPanel);
        produceButtons();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PlayerFrame.this.mediaPlayer.controls().stop();
            }
        });
    }

    private void produceButtons() {
        // More buttons: Volume, Fullscreen, Download
        // TODO
        pause.setIcon(FrameUtils.fromResource("/pause.png", 50, 50));
        pause.addActionListener(e -> {
            // Play or pause the video has delay that is about 1s, it is not my problem, it is caused by vlcj library
            // FIXME
            mediaPlayer.controls().pause();
            pause.setVisible(false);
            play.setVisible(true);
        });
        pause.setVisible(false);
        pause.setLocation((PlayerFrame.this.getWidth() - pause.getWidth()) / 2, pause.getY());
        play.setIcon(FrameUtils.fromResource("/play.png", 50, 50));
        play.addActionListener(e -> {
            // Play or pause the video has delay that is about 1s, it is not my problem, it is caused by vlcj library
            // FIXME
            mediaPlayer.controls().play();
            play.setVisible(false);
            pause.setVisible(true);
        });
        play.setVisible(true);
        play.setLocation((PlayerFrame.this.getWidth() - play.getWidth()) / 2, play.getY());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pause.setLocation((PlayerFrame.this.getWidth() - pause.getWidth()) / 2, pause.getY());
                play.setLocation((PlayerFrame.this.getWidth() - play.getWidth()) / 2, play.getY());
            }
        });
    }

}
