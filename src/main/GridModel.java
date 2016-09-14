package main;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GridModel {

    List<GridUI> observers = new ArrayList<GridUI>();

    public enum Style {
        dots, lines;
    }

    private boolean   gridVisible;
    private Style     style;
    private float     size;
    private float     alt;
    // Range values are in mils
    private int       rangeX;
    private final int minRangeX;
    private Point     zeroPx;
    private float     zoomPct;

    public GridModel() {
        style = Style.lines;
        size = 50;
        alt = 25;
        rangeX = 1500;
        minRangeX = 100;
        zoomPct = 1.15f;
    }

    public void registerObserver(GridUI grid) {
        observers.add(grid);
    }

    public boolean isGridVisible() {
        return gridVisible;
    }

    public void setGridVisible(boolean gridVisible) {
        this.gridVisible = gridVisible;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public double getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public int getRangeX() {
        return rangeX;
    }

    public void setRangeX(int rangeX) {
        this.rangeX = rangeX;
    }

    public int getRangeY(GridUI grid) {
        int intervalPx = Math.round((float) size / (float) rangeX * grid.getWidth());
        return Math.round((float) grid.getHeight() / (float) intervalPx * size);
    }

    public Point getValFromMouse(GridUI grid, MouseEvent e) {
        Point mousePx = new Point(e.getX(), e.getY());
        return getValFromPx(grid, mousePx);
    }

    public Point getValFromPx(GridUI grid, Point px) {
        float zeroXPct = (float) zeroPx.x / grid.getWidth();
        float zeroYPct = 1f - ((float) zeroPx.y / grid.getHeight());

        float ptXPct = (float) px.x / grid.getWidth();
        float ptYPct = 1f - ((float) px.y / grid.getHeight());

        ptXPct -= zeroXPct;
        ptYPct -= zeroYPct;

        Point val = new Point(
                Math.round(ptXPct * this.getRangeX()),
                Math.round(ptYPct * this.getRangeY(grid)));

        return val;
    }

    public Point getZeroPx() {
        return zeroPx;
    }

    public void setZeroPx(Point zero) {
        zeroPx = zero;
        updateObservers();
    }

    public void moveZeroPx(int x, int y) {
        x += zeroPx.x;
        y += zeroPx.y;
        Point newZero = new Point(x, y);
        setZeroPx(newZero);
    }

    public void zoomIn() {
        rangeX /= zoomPct;
        if (rangeX < minRangeX) {
            rangeX = minRangeX;
        }
        printRange();
        updateObservers();
    }

    public void zoomOut() {
        rangeX *= zoomPct;
        printRange();
        updateObservers();
    }

    private void updateObservers() {
        for (GridUI grid : observers) {
            grid.repaint();
        }
    }

    private void printRange() {
        System.out.println("RangeX: " + rangeX);
    }
}
