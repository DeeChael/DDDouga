package net.deechael.dddouga.frame;

import net.deechael.dddouga.item.Douga;
import net.deechael.dddouga.utils.FrameUtils;
import net.deechael.dddouga.utils.T80Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MainFrame {

    private final JFrame frame = new JFrame("DD动画");

    private JTabbedPane currentTabbedPane;
    private JPanel tabMainPanel;
    private JLabel pageLabel;
    private JScrollPane currentMainPanelScrollPanel;
    private JPanel currentMainPanel;

    private int page = 1;

    private int currentDougaAmount = 0;

    private Thread thread;

    public MainFrame() {
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(new Dimension(800, 600));
        frame.setLocation(FrameUtils.center(frame.getSize()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        solveTabBar();
        reloadDougas();
    }

    private void solveTabBar() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setVisible(true);
        tabbedPane.addTab("主页", tabMain());
        tabbedPane.setSize(frame.getWidth() - 16, frame.getHeight() - 40);
        tabbedPane.setLocation(0, 0);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                tabbedPane.setSize(frame.getWidth() - 16, frame.getHeight() - 40);
            }
        });
        this.currentTabbedPane = tabbedPane;
        frame.add(tabbedPane);
    }

    private JPanel tabMain() {
        JPanel mainPanel = new JPanel();
        mainPanel.setVisible(true);
        mainPanel.setLayout(null);
        mainPanel.setSize(frame.getWidth() - 16, frame.getHeight() - 40);
        mainPanel.setLocation(0, 0);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setSize(frame.getWidth() - 16, frame.getHeight() - 40);
            }
        });
        mainPanelScrollPaneButtons().forEach(mainPanel::add);
        pageLabel = new JLabel();
        pageLabel.setSize(81, 27);
        pageLabel.setLocation(253, 10);
        pageLabel.setVisible(true);
        mainPanel.add(pageLabel);
        mainPanel.add(solveMainPanelScrollPane());
        this.tabMainPanel = mainPanel;
        return mainPanel;
    }

    private JScrollPane solveMainPanelScrollPane() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVisible(true);
        scrollPane.setSize(frame.getWidth() - 16, frame.getHeight() - 120);
        scrollPane.setLocation(0, 40);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setSize(frame.getWidth() - 16, frame.getHeight() - 120);
                scrollPane.setPreferredSize(new Dimension(frame.getWidth() - 16, frame.getHeight() - 120));
            }
        });
        this.currentMainPanelScrollPanel = scrollPane;
        return scrollPane;
    }

    private void mainPanel() {
        JPanel panelInScroll = new JPanel();
        panelInScroll.setVisible(true);
        panelInScroll.setLayout(new AutoLineFlowLayout());
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelInScroll.setSize(frame.getWidth() - 16, frame.getHeight() - 80);
            }
        });
        this.currentMainPanel = panelInScroll;
    }

    private List<JButton> mainPanelScrollPaneButtons() {
        JButton reload = new JButton("刷新");
        reload.addActionListener(e -> reloadDougas());
        reload.setVisible(true);
        reload.setSize(81, 27);
        reload.setLocation(10, 10);
        JButton previous = new JButton("上一页");
        previous.addActionListener(e -> {
            if (MainFrame.this.page <= 1) {
                FrameUtils.warning("页数已经到头啦！");
            } else {
                page--;
                reloadDougas();
            }
        });
        previous.setVisible(true);
        previous.setSize(81, 27);
        previous.setLocation(91, 10);
        JButton next = new JButton("下一页");
        next.addActionListener(e -> {
            page++;
            reloadDougas();
        });
        next.setVisible(true);
        next.setSize(81, 27);
        next.setLocation(172, 10);
        return Arrays.asList(reload, previous, next);
    }

    private void updatePage() {
        pageLabel.setText("当前页数：" + page);
    }

    private void reloadDougas() {
        if (thread != null) {
            thread.interrupt();
        }
        updatePage();
        List<Douga> dougas = T80Utils.getPage(page);
        this.currentDougaAmount = dougas.size();
        mainPanel();
        List<JDougaItem> dougaItems = new ArrayList<>();
        for (Douga item : dougas) {
            JDougaItem dougaItem = new JDougaItem(frame, item);
            this.currentMainPanel.add(dougaItem);
            dougaItems.add(dougaItem);
        }
        this.currentMainPanelScrollPanel.setViewportView(this.currentMainPanel);
        thread = new Thread(() -> {
            for (JDougaItem dougaItem : dougaItems) {
                dougaItem.refreshIcon();
            }
        });
        thread.start();
    }

}
