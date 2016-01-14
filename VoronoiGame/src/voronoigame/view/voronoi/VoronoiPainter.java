/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view.voronoi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.LinkedList;
import javax.swing.Painter;
import voronoigame.Util;
import voronoigame.model.Cell;
import voronoigame.model.FocusType;
import voronoigame.model.GameState;
import voronoigame.model.StationaryCell;
import voronoigame.model.delaunay.Edge;
import static voronoigame.view.VoronoiPanel.SITE_RADIUS;

/**
 *
 * @author Maurice
 */
public class VoronoiPainter implements Painter {
/**
     * Healthy cell colors are the following:
     * <ol start=0>
     * <li>CELL 0x93ed94</li>
     * <li>CORE 0x67e669</li>
     * </ol>
     */
    public static final Color[] COLOR_HEALTHY = {new Color(0x93ed94), new Color(0x67e669)};
    /**
     * Infected cell colors are the following:
     * <ol start=0>
     * <li>CELL 0x93ed94</li>
     * <li>CORE 0xcc0000</li>
     * </ol>
     */
    public static final Color[] COLOR_INFECTED = {new Color(0xff0000), new Color(0xcc0000),};
    /**
     * White cell colors are the following:
     * <ol start=0>
     * <li>CELL 0x93ed94</li>
     * <li>CORE 0xe6e6e6</li>
     * </ol>
     */
    public static final Color[] COLOR_WHITE_CELL = {new Color(0xffffff), new Color(0xe6e6e6)};
    /**
     * Dead cell colors are the following:
     * <ol start=0>
     * <li>CELL 0x888888</li>
     * <li>CORE 0x6f6f6f</li>
     * </ol>
     */
    public static final Color[] COLOR_DEAD = {new Color(0x888888),new Color(0x6f6f6f)};

    private static final Stroke EDGE_STROKE = new BasicStroke(1);
    
    
    private static Color getTypedColor(Color[] range, PaintType paintType){
        return range[paintType.ordinal()];
    }
    
    
    /**
     * Determines the color that must be given to a cell. 
     * @param cell The cell for which to determine the color
     * @param paintType The painting type that must be applied. This determines which base color must be used.
     * @return The calculated color for the given cell based on the required paint type.
     */
    public static Color getColorforCell(Cell cell, PaintType paintType)
    {
        Color returnColor = getTypedColor(COLOR_WHITE_CELL, paintType);
        if (cell.getType() == Cell.Type.DEAD)
        {
            return getTypedColor(COLOR_DEAD,paintType);
        }
        if (cell.getClass() == StationaryCell.class)
        {
            StationaryCell stationaryCell = (StationaryCell) cell;
            switch (stationaryCell.getType())
            {
                case INFECTED:
                    return getTypedColor(COLOR_INFECTED,paintType);
                default:
                    Color color = getTypedColor(COLOR_HEALTHY,paintType);
                    double currentRatio = cell.getCurrentAreaRatio();
                    currentRatio = currentRatio < Cell.MAX_SCALE_FACTOR ? currentRatio : Cell.MAX_SCALE_FACTOR;
                    
                    double colorChangeFactor = (currentRatio - 1) / (Cell.MAX_SCALE_FACTOR - 1);
                    
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    
                    int rDiffMax = getTypedColor(COLOR_DEAD,paintType).getRed() - r;
                    int gDiffMax = getTypedColor(COLOR_DEAD,paintType).getGreen() - g;
                    int bDiffMax = getTypedColor(COLOR_DEAD,paintType).getBlue() - b;
                    
                    r += (int)((double)rDiffMax * colorChangeFactor);
                    g += (int)((double)gDiffMax * colorChangeFactor);
                    b += (int)((double)bDiffMax * colorChangeFactor);
                    
                    returnColor = new Color(r, g, b);
            }
        }
        
        if (cell.getFocusType() == FocusType.HOVER || cell.getFocusType() == FocusType.DRAG)
        {
            returnColor = returnColor.brighter();
        }
        
        return returnColor;
    }
    
    @Override
    public void paint(Graphics2D g2, Object object, int width, int height) {
        
        GameState gameState = (GameState)object;
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        for (Point site : gameState.getDiagram().getSites()) {
            LinkedList<Point> face = gameState.getDiagram().getFaceFromSite(site);
            Polygon polygon = new Polygon();
            while (!face.isEmpty()) {
                Point vertex = face.pop();
                polygon.addPoint(vertex.x, vertex.y);
            }
            g2.setColor(VoronoiPainter.getColorforCell(gameState.getPointCellMap().get(site), PaintType.CELL));
            g2.fillPolygon(polygon);

            g2.setColor(VoronoiPainter.getColorforCell(gameState.getPointCellMap().get(site), PaintType.CORE));
            g2.fillOval(site.x - SITE_RADIUS, site.y - SITE_RADIUS, SITE_RADIUS * 2, SITE_RADIUS * 2);
        }

        for (Edge edge : gameState.getDiagram().getVoronoiEdges()) {
            g2.setColor(Color.BLACK);
            g2.setStroke(EDGE_STROKE);
            g2.drawLine(edge.getPoint1().x, edge.getPoint1().y, edge.getPoint2().x, edge.getPoint2().y);
        }
    }

   
    
    public enum PaintType{
        CELL, CORE
    }
}
