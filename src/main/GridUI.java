package main;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GridUI extends JPanel {

    GridModel  m;
    MouseEvent lastMouseE;

    /**
     * Create the panel.
     */
    public GridUI(GridModel model) {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                printMouseLocation(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                moveGrid(e);
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                zoom(e);
            }
        });
        this.m = model;
        this.m.registerObserver(this);
    }

    private void zoom(MouseWheelEvent e) {
        int rotations = e.getWheelRotation();
        if (rotations < 0) {
            m.zoomIn();
        }
        else if (rotations > 0) {
            m.zoomOut();
        }
    }

    public void updateFromModel() {
        this.repaint();
    }

    public void printMouseLocation(MouseEvent e) {
        Point loc = m.getValFromMouse(this, e);
        System.out.println("X: " + loc.x + " Y: " + loc.y);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m.getZeroPx() == null) {
            m.setZeroPx(new Point(this.getWidth() / 2, this.getHeight() / 2));
        }
        int intervalPx = getIntervalPx();
        int curPx = m.getZeroPx().x;

        // Draw vertical lines
        while (curPx <= this.getWidth()) {
            g.drawLine(curPx, 0, curPx, this.getHeight());
            curPx += intervalPx;
        }
        curPx = m.getZeroPx().x;
        while (curPx >= 0) {
            g.drawLine(curPx, 0, curPx, this.getHeight());
            curPx -= intervalPx;
        }

        // Draw horizontal lines
        curPx = m.getZeroPx().y;
        while (curPx <= this.getHeight()) {
            g.drawLine(0, curPx, this.getWidth(), curPx);
            curPx += intervalPx;
        }
        curPx = m.getZeroPx().y;
        while (curPx >= 0) {
            g.drawLine(0, curPx, this.getWidth(), curPx);
            curPx -= intervalPx;
        }
    }

    private int getIntervalPx() {
        return Math.round((float) m.getSize() / (float) m.getRangeX() * this.getWidth());
    }

    private void moveGrid(MouseEvent e) {
        if (lastMouseE != null) {
            int xDiff = e.getX() - lastMouseE.getX();
            int yDiff = e.getY() - lastMouseE.getY();

            m.moveZeroPx(xDiff, yDiff);
        }
        lastMouseE = e;
    }

}
