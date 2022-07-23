import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VLCJTest {

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final Frame mainFrame;

    private final JLabel urlLabel;
    private final JTextField urlTextField;
    private final JButton playButton;

    public static void main(String[] args) throws Exception {


        SwingUtilities.invokeLater(() -> new VLCJTest().start());

        Thread.currentThread().join();
    }

    public VLCJTest() {
        mainFrame = new Frame("vlcj YouTube Test");
        //mainFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        mainFrame.setSize(800, 600);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit(0);
            }
        });
        mainFrame.setLayout(new BorderLayout());

        JPanel cp = new JPanel();
        cp.setBackground(Color.black);
        cp.setLayout(new BorderLayout());

        JPanel ip = new JPanel();
        ip.setBorder(new EmptyBorder(4, 4, 4, 4));
        ip.setLayout(new BoxLayout(ip, BoxLayout.X_AXIS));

        urlLabel = new JLabel("URL:");
        urlLabel.setDisplayedMnemonic('u');
        urlLabel.setToolTipText("Enter a URL in the format http://www.youtube.com/watch?v=000000");
        urlTextField = new JTextField(40);
        urlTextField.setFocusAccelerator('u');
        urlTextField.setToolTipText("Enter a URL in the format http://www.youtube.com/watch?v=000000");
        playButton = new JButton("Play");
        playButton.setMnemonic('p');

        urlTextField.setText("https://www.youtube.com/watch?v=prB-gL0rq7E");

        ip.add(urlLabel);
        ip.add(urlTextField);
        ip.add(playButton);

        cp.add(ip, BorderLayout.NORTH);

        Canvas vs = new Canvas();
        vs.setBackground(Color.black);
        cp.add(vs, BorderLayout.CENTER);

        mainFrame.add(cp, BorderLayout.CENTER);

        urlTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });

        factory = new MediaPlayerFactory();

        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(vs));

        mediaPlayer.controls().setRepeat(false);

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void buffering(MediaPlayer mediaPlayer, float newCache) {
                System.out.println("Buffering " + newCache);
            }
        });
    }

    private void start() {
        mainFrame.setVisible(true);
    }

    private void play() {
        mediaPlayer.media().prepare("https://new.qqaku.com/20220709/neSH8kD7/index.m3u8");
        mediaPlayer.media().events().addMediaEventListener(new MediaEventAdapter() {
            @Override
            public void mediaSubItemAdded(Media media, MediaRef newChild) {
                System.out.println("item added");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    System.out.println("mrl=" + mrl);
                }
                mediaList.release();
            }

            @Override
            public void mediaSubItemTreeAdded(Media media, MediaRef item) {
                System.out.println("item tree added");
                MediaList mediaList = mediaPlayer.media().subitems().newMediaList();
                for (String mrl : mediaList.media().mrls()) {
                    System.out.println("mrl=" + mrl);
                }
                mediaList.release();
            }
        });
        mediaPlayer.controls().play();
    }

    private void exit(int value) {
        mediaPlayer.controls().stop();
        mediaPlayer.release();
        factory.release();
        System.exit(value);
    }

}
