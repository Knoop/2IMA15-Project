/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view.voronoi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import javax.swing.Painter;
import voronoigame.model.Cell;
import voronoigame.model.FocusType;
import voronoigame.model.GameState;
import voronoigame.model.delaunay.Edge;

/**
 *
 * @author Maurice
 */
public class VoronoiPainter implements Painter
{

    private boolean showScore;

    public boolean isShowScore()
    {
        return showScore;
    }

    public void setShowScore(boolean showScore)
    {
        this.showScore = showScore;
    }
    
    public VoronoiPainter(boolean showScore) {
        this.showScore = showScore;
    }
    
    /**
     * Regular cell colors are the following:
     * <ol start=0>
     * <li>CELL #2ddc2f</li>
     * <li>CORE #1ba21c</li>
     * <li>STRESS (expanded) #92ed93</li>
     * <li>STRESS (compressed) #188f19</li>
     * <li>DEAD #072a07</li>
     * </ol>
     */
    public static final Color[] COLOR_REGULAR =
    {
        new Color(0x2ddc2f), // CELL 
        new Color(0x1ba21c), // CORE
        new Color(0x92ed93), // STRESS_EXPANDED
        new Color(0x188f19), // STRESS_COMPRESSED
        new Color(0x072a07), // DEAD
    };

    /**
     * Infected cell colors are the following:
     * <ol start=0>
     * <li>CELL #d32523</li>
     * <li>CORE #911918</li>
     * <li>STRESS (expanded) #ea8382</li>
     * <li>STRESS (compressed) #901918</li>
     * <li>DEAD #2b0707</li>
     * </ol>
     */
    public static final Color[] COLOR_INFECTED =
    {
        new Color(0xd32523), // CELL
        new Color(0x911918), // CORE
        new Color(0xea8382), // STRESS_EXPANDED
        new Color(0x901918), // STRESS_COMPRESSED
        new Color(0x2b0707), // DEAD
    };

    /**
     * White cell colors are the following:
     * <ol start=0>
     * <li>CELL #eeeeee</li>
     * <li>CORE #c8c8c8</li>
     * <li>STRESS (expanded) #b3b3b3</li>
     * <li>STRESS (compressed) #515151</li>
     * <li>DEAD #161616</li>
     * </ol>
     */
    public static final Color[] COLOR_WHITE_CELL =
    {
        new Color(0xeeeeee), // CELL
        new Color(0xc8c8c8), // CORE
        new Color(0xb3b3b3), // STRESS_EXPANDED
        new Color(0x515151), // STRESS_COMPRESSED
        new Color(0x161616), // DEAD
    };

    private static final Stroke EDGE_STROKE = new BasicStroke(1);

    private static Color getTypedColor(Color[] range, PaintType paintType)
    {
        return range[paintType.ordinal()];
    }

    /**
     * Get the color range for the given cell.
     * @param cell
     * @return 
     */
    private static Color[] getRange(Cell cell){
        switch(cell.getType()){
            case HEALTHY:
                return COLOR_REGULAR;
            case INFECTED:
                return COLOR_INFECTED;
            case DEFENSE:
                return COLOR_WHITE_CELL;
            default:
                throw new AssertionError(cell.getType().name());
        }
    }

    /**
     * Determines the color of the nucleus of the cell
     * @param cell
     * @return 
     */
    private static Color getColorForNucleus(Cell cell) {
        return getTypedColor(getRange(cell), PaintType.CORE);
    }
    
    /**
     * Determines the color that must be given to a cell.
     *
     * @param cell The cell for which to determine the color
     * @return The calculated color for the given cell based on the required
     * paint type.
     */
    public static Color getColorForCell(Cell cell) {
        Color[] range = getRange(cell);
        Color returnColor = !cell.isAlive() ? 
                getTypedColor(range, PaintType.DEAD) : 
                getCellColorWithStress(range, cell.isCompressed(), cell.getScaleRatio());

        if (cell.getFocusType() == FocusType.HOVER || cell.getFocusType() == FocusType.DRAG)
            returnColor = returnColor.brighter();

        return returnColor;
    }

    /**
     * Determines the color that should be used to display the given amount of
     * compression/expansion, depending on the provided color range.
     * @param range The range of colors that must be used
     * @param compressed flag indicating whether the factor indicates
     * compression {@code true} or expansion {@code false}
     * @param factor The factor with which the cell is stressed. This value must
     * be between 0 and 1, where 0 is no stress and 1 equals the maximum amount
     * of expanding or compressing stress. 
     * @return The color that should be used to indicate the given amount of
     * stress on a cell that uses the given color range.
     */
    private static Color getCellColorWithStress(Color[] range, boolean compressed, double factor){
        Color base = getTypedColor(range, PaintType.CELL);
        Color stress = getTypedColor(range, compressed ? PaintType.STESS_COMPRESSED : PaintType.STRESS_EXPANDED);
        
        int rDiffMax = stress.getRed() - base.getRed();
        int gDiffMax = stress.getGreen() - base.getGreen();
        int bDiffMax = stress.getBlue() - base.getBlue();

        return new Color( 
                base.getRed() + (int) ((double) rDiffMax * factor),
                base.getGreen() + (int) ((double) gDiffMax * factor),
                base.getBlue() + (int) ((double) bDiffMax * factor));
    }

    @Override
    public void paint(Graphics2D g2, Object object, int width, int height)
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRect(0, 0, width, height);
        final GameState gameState = (GameState) object;

        synchronized (gameState)
        {
            for (Point site : gameState.getDiagram().getSites())
            {
                LinkedList<Point> face = gameState.getDiagram().getFaceFromSite(site);
                Polygon polygon = new Polygon();
                while (!face.isEmpty())
                {
                    Point vertex = face.pop();
                    polygon.addPoint(vertex.x, vertex.y);
                }

                Cell cell = gameState.getCell(site);

                g2.setColor(VoronoiPainter.getColorForCell(cell));
                g2.fillPolygon(polygon);

                g2.setColor(VoronoiPainter.getColorForNucleus(cell));
                g2.fillOval(site.x - Cell.NUCLEUS_RADIUS, site.y - Cell.NUCLEUS_RADIUS, Cell.NUCLEUS_RADIUS * 2, Cell.NUCLEUS_RADIUS * 2);
            }

            for (Edge edge : gameState.getDiagram().getVoronoiEdges())
            {
                g2.setColor(Color.BLACK);
                g2.setStroke(EDGE_STROKE);
                g2.drawLine(edge.getPoint1().x, edge.getPoint1().y, edge.getPoint2().x, edge.getPoint2().y);
            }
            
            if (showScore)
            {
                //draw the score
                drawScore(g2, gameState.getScore(), gameState.getMaximumScore(), gameState.getMinimumScore());
            }
        }
    }
    
    private static final Color[] SCORE_BINS = {new Color(0xffffff), new Color(0xffa000), new Color(0xff0000)};
    
    private void drawScore(Graphics2D g, int score, int maximumScore, int minimumScore) {
        FontRenderContext frc = 
                new FontRenderContext(null, true, true);

        Font font = g.getFont().deriveFont(Font.BOLD, 16);

        String scoreString = String.format("Score: %d", score);

        Rectangle2D r2D = font.getStringBounds(scoreString, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int x = (g.getClipBounds().width / 2) - (rWidth / 2) - rX;
        int y = ((int)r2D.getHeight() / 2) + 10;
        
        //Determine score color
        int scoreRange = maximumScore - minimumScore;
        int binSize = scoreRange / SCORE_BINS.length;
        Color scoreColor = SCORE_BINS[0];
        for (int i = 1; i < SCORE_BINS.length; i++)
        {
            int checkScore = maximumScore - (binSize * i);
            if (score < checkScore)
            {
                scoreColor = SCORE_BINS[i];
                continue;
            }
            break;
        }

        g.setFont(font);
        g.setColor(scoreColor);
        g.drawString(scoreString, x, y);
    }

    public enum PaintType {
        CELL, CORE, STRESS_EXPANDED, STESS_COMPRESSED, DEAD, 
    }
}
