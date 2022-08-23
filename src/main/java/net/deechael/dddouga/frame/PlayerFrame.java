package net.deechael.dddouga.frame;

import net.deechael.dddouga.DDDouga;
import net.deechael.dddouga.item.Episode;
import net.deechael.dddouga.utils.FrameUtils;
import org.slf4j.Logger;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.StatusApi;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class PlayerFrame extends JFrame {

    private boolean dragging = false;
    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final JPanel playerPanel = new JPanel();
    private final JPanel buttonsPanel = new JPanel();

    private final JButton pause = new JButton();
    private final JButton play = new JButton();

    public PlayerFrame(Episode episode) {
        Logger logger = DDDouga.getLogger();
        logger.debug("Building video player");
        Container container = this.getContentPane();
        this.setTitle(episode.getOwner().getName() + " - " + episode.getName());
        //this.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.setLayout(null);
        this.setMinimumSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        this.setLocation(FrameUtils.center(this.getSize()));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        Canvas vs = new Canvas();
        vs.setBackground(Color.black);

        playerPanel.setSize(PlayerFrame.this.getWidth() - 12, PlayerFrame.this.getHeight() - 140);
        playerPanel.setLocation(0, 0);
        playerPanel.setLayout(new BorderLayout());
        playerPanel.add(vs, BorderLayout.CENTER);

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(vs));
        mediaPlayer.controls().setRepeat(false);

        logger.debug("Loading video link");
        // Load link is quite slow, should be optimized
        // FIXME
        mediaPlayer.media().prepare(episode.getPlayLink());

        mediaPlayer.media().events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaSubItemAdded(Media media, MediaRef newChild) {
                logger.debug("Media sub item was added:");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    logger.debug("- mrl=" + mrl);
                }
                logger.debug("\n");
                mediaList.release();
            }

            @Override
            public void mediaSubItemTreeAdded(Media media, MediaRef item) {
                logger.debug("Media sub item tree was added");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    logger.debug("- mrl=" + mrl);
                }
                logger.debug("\n");
                mediaList.release();
            }
        });
        this.add(playerPanel);
        this.add(buttonsPanel);

        mediaPlayer.audio().setMute(true);
        mediaPlayer.controls().play();
        try {
            logger.debug("Trying to get the length of the video");
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            logger.error("Error appeared", e);
        }
        mediaPlayer.controls().setPosition(0.0f);
        mediaPlayer.controls().pause();
        mediaPlayer.audio().setMute(false);

        // More buttons: Volume, Fullscreen, Download
        // TODO
        buttonsPanel.setSize(this.getWidth() - 16, 100);
        buttonsPanel.setLocation(0, PlayerFrame.this.getHeight() - 140);
        buttonsPanel.setLayout(null);
        progressBar(buttonsPanel);
        buttonsPanel.add(pause);
        buttonsPanel.add(play);
        mediaPlayer.controls().pause();


        produceButtons();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                playerPanel.setSize(PlayerFrame.this.getWidth() - 12, PlayerFrame.this.getHeight() - 140);
                buttonsPanel.setSize(PlayerFrame.this.getWidth() - 12, 100);
                buttonsPanel.setLocation(0, PlayerFrame.this.getHeight() - 140);
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.debug("CLosing video player");
                PlayerFrame.this.mediaPlayer.controls().stop();
                PlayerFrame.this.mediaPlayer.release();
                PlayerFrame.this.factory.release();
            }
        });
    }

    private void progressBar(JPanel panel) {
        Logger logger = DDDouga.getLogger();
        long lengthLong = this.mediaPlayer.status().length();
        int length;
        if (lengthLong <= 0) {
            logger.warn("Failed to get the length of the video");
            length = 1;
        } else {
            length = (int) (lengthLong / 1000);
        }
        logger.debug("Video length (seconds): " + length);
        JSlider progressBar = new JSlider(0, length);
        progressBar.setValue(0);
        progressBar.setVisible(true);
        progressBar.setLocation(5, 5);
        progressBar.setSize(panel.getWidth() - 10, 10);
        JToolTip toolTip = progressBar.createToolTip();
        if (lengthLong != -1) {
            //progressBar.addChangeListener(e -> PlayerFrame.this.mediaPlayer.controls().setPosition(((float) progressBar.getValue()) / ((float) length)));
            progressBar.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (dragging)
                        return;
                    toolTip.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (dragging)
                        return;
                    toolTip.setVisible(false);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    dragging = true;
                    toolTip.setVisible(true);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (dragging) {
                        toolTip.setTipText(time(progressBar.getValue(), length));
                        toolTip.setToolTipText(time(progressBar.getValue(), length));
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!PlayerFrame.this.mediaPlayer.status().isPlaying()) {
                        mediaPlayer.controls().play();
                        play.setVisible(false);
                        pause.setVisible(true);
                    }
                    dragging = false;
                    toolTip.setVisible(false);
                    PlayerFrame.this.mediaPlayer.controls().setPosition(((float) progressBar.getValue()) / ((float) length));
                }
            });
            this.mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

                @Override
                public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                    if (dragging)
                        return;
                    progressBar.setValue((int) (newTime / 1000));
                    toolTip.setTipText(time(progressBar.getValue(), length));
                    toolTip.setToolTipText(time(progressBar.getValue(), length));
                }

            });
        } else {
            progressBar.setEnabled(false);
        }
        panel.add(progressBar);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                progressBar.setSize(panel.getWidth(), progressBar.getHeight());
            }
        });
    }

    private void produceButtons() {
        // More buttons: Volume, Fullscreen, Download
        // TODO
        pause.setIcon(FrameUtils.fromResource("/pause.png", 50, 50));
        pause.setSize(50, 50);
        pause.addActionListener(e -> {
            // Play or pause the video has delay that is about 1s, it is not my problem, it is caused by vlcj library
            // FIXME
            mediaPlayer.controls().pause();
            pause.setVisible(false);
            play.setVisible(true);
        });
        pause.setVisible(false);
        pause.setLocation((PlayerFrame.this.getWidth() - pause.getWidth()) / 2, pause.getY() + 10);
        play.setIcon(FrameUtils.fromResource("/play.png", 50, 50));
        play.setSize(50, 50);
        play.addActionListener(e -> {
            // Play or pause the video has delay that is about 1s, it is not my problem, it is caused by vlcj library
            // FIXME
            mediaPlayer.controls().play();
            play.setVisible(false);
            pause.setVisible(true);
        });
        play.setVisible(true);
        play.setLocation((PlayerFrame.this.getWidth() - play.getWidth()) / 2, play.getY() + 10);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pause.setLocation((PlayerFrame.this.getWidth() - pause.getWidth()) / 2, pause.getY());
                play.setLocation((PlayerFrame.this.getWidth() - play.getWidth()) / 2, play.getY());
            }
        });
    }

    private static String time(int position, int total) {
        return total >= 3600 ? time(position, true) + "/" + time(total, true) : time(position, false) + "/" + time(total, false);
    }

    private static String time(int time, boolean hour) {
        if (hour || time > 60 * 60) {
            int hours = time / 60 * 60;
            time = time - (hours * 60 * 60);
            int minutes = time / 60;
            int seconds = time - (minutes * 60);
            return hours + ":" + (minutes >= 10 ? minutes : "0" + minutes) + ":" + (seconds >= 10 ? seconds : "0" + seconds);
        } else {
            if (time > 60) {
                int minutes = time / 60;
                int seconds = time - (minutes * 60);
                return minutes + ":" + (seconds >= 10 ? seconds : "0" + seconds);
            } else {
                return "0:" + time;
            }
        }
    }

}
