package net.deechael.dddouga.frame;

import java.awt.*;

public class AutoLineFlowLayout extends FlowLayout {

    public AutoLineFlowLayout() {
        this(20, 20);
    }

    public AutoLineFlowLayout(int hgap, int vgap) {
        super(LEADING, hgap, vgap);
    }

    public Dimension minimumLayoutSize(Container target) {
        return computeSize(target, false);
    }

    public Dimension preferredLayoutSize(Container target) {
        return computeSize(target, true);
    }

    private Dimension computeSize(Container target, boolean minimum) {
        synchronized (target.getTreeLock()) {
            int hgap = getHgap();
            int vgap = getVgap();
            int w = target.getWidth();
            if (w == 0) {
                w = Integer.MAX_VALUE;
            }
            Insets insets = target.getInsets();
            if (insets == null) {
                insets = new Insets(0, 0, 0, 0);
            }
            int reqdWidth = 0;
            int maxwidth = w - (insets.left + insets.right + hgap * 2);
            int n = target.getComponentCount();
            int x = 0;
            int y = insets.top;
            int rowHeight = 0;
            for (int i = 0; i < n; i++) {
                Component c = target.getComponent(i);
                if (c.isVisible()) {
                    Dimension d = minimum ? c.getMinimumSize() : c.getPreferredSize();
                    if ((x == 0) || ((x + d.width) <= maxwidth)) {
                        if (x > 0) {
                            x += hgap;
                        }
                        x += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    } else {
                        x = d.width;
                        y += vgap + rowHeight;
                        rowHeight = d.height;
                    }
                    reqdWidth = Math.max(reqdWidth, x);
                }
            }
            y += rowHeight;
            return new Dimension(reqdWidth + insets.left + insets.right, y);
        }
    }

}
