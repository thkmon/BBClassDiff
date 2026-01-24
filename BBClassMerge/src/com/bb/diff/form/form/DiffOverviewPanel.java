package com.bb.diff.form.form;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.BBDiffNextButtonMouseListener;

/**
 * 이클립스 스타일의 Diff Overview Panel
 * 우측에 수직 막대 형태로 diff 위치를 직사각형 박스로 표시
 */
public class DiffOverviewPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color DIFF_COLOR = new Color(255, 255, 255);
    private static final Color DIFF_SELECTED_COLOR = new Color(0, 0, 0);
    private static final Color BORDER_COLOR = new Color(160, 160, 160);

    public DiffOverviewPanel() {
        setBackground(BACKGROUND_COLOR);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getY());
            }
        });
    }

    private void handleClick(int clickY) {
        if (CommonConst.diffPointList1 == null || CommonConst.diffPointList1.size() == 0) {
            return;
        }

        int panelHeight = getHeight();
        int totalLines = getTotalLines();
        if (totalLines <= 0)
            totalLines = 1;

        int closestIndex = -1;
        int closestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < CommonConst.diffPointList1.size(); i++) {
            int lineNumber = CommonConst.diffPointList1.get(i);
            int y = (int) ((double) lineNumber / totalLines * panelHeight);
            int distance = Math.abs(clickY - y);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }

        if (closestIndex >= 0 && closestDistance < 30) {
            CommonConst.currentDiffPointIndex = closestIndex;
            BBDiffNextButtonMouseListener.showDiffPoint(true);
            CommonConst.currentDiffPointIndex--;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (CommonConst.diffPointList1 == null || CommonConst.diffPointList1.size() == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelHeight = getHeight();
        int panelWidth = getWidth();
        int totalLines = getTotalLines();
        if (totalLines <= 0)
            totalLines = 1;

        for (int i = 0; i < CommonConst.diffPointList1.size(); i++) {
            int lineNumber = CommonConst.diffPointList1.get(i);
            int y = (int) ((double) lineNumber / totalLines * panelHeight);

            int lineCount = 1;
            if (CommonConst.diffLineCountList1 != null && CommonConst.diffLineCountList1.size() > i) {
                lineCount = CommonConst.diffLineCountList1.get(i);
            }
            
            // 라인 수에 비례하여 높이 계산 (최소 2px)
            int boxHeight = Math.max(2, (int) ((double) lineCount / totalLines * panelHeight));

            if (i == CommonConst.currentDiffPointIndex) {
                g2d.setColor(DIFF_SELECTED_COLOR);
            } else {
                g2d.setColor(DIFF_COLOR);
            }

            g2d.fillRect(2, y, panelWidth - 4, boxHeight);
            g2d.setColor(BORDER_COLOR);
            g2d.drawRect(2, y, panelWidth - 4, boxHeight);
        }
    }

    private int getTotalLines() {
        if (CommonConst.leftFileContent != null && CommonConst.leftFileContent.getScrollPane() != null) {
            int max = CommonConst.leftFileContent.getScrollPane().getVerticalScrollBar().getMaximum();
            return max / 16;
        }
        return 100;
    }
}
