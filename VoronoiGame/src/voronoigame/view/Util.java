/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.awt.Color;
import java.awt.Point;
import voronoigame.model.Cell;
import voronoigame.model.StationaryCell;

/**
 *
 * @author Guus van Lankveld
 */
public class Util
{
    public static final Color COLOR_HEALTHY = new Color(0x93ed94);
    public static final Color COLOR_INFECTED = new Color(0xff0000);
    public static final Color COLOR_WHITE_CELL = new Color(0xffffff);
    public static final Color COLOR_DEAD = new Color(0x888888);

    public static Color getColorforCell(Cell cell)
    {
        if (cell.getType() == Cell.Type.DEAD)
        {
            return COLOR_DEAD;
        }
        if (cell.getClass() == StationaryCell.class)
        {
            StationaryCell stationaryCell = (StationaryCell) cell;
            switch (stationaryCell.getType())
            {
                case INFECTED:
                    return COLOR_INFECTED;
                default:
                    Color color = COLOR_HEALTHY;
                    double currentRatio = cell.getCurrentAreaRatio();
                    currentRatio = currentRatio < Cell.MAX_SCALE_FACTOR ? currentRatio : Cell.MAX_SCALE_FACTOR;
                    
                    double colorChangeFactor = (currentRatio - 1) / (Cell.MAX_SCALE_FACTOR - 1);
                    
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    
                    int rDiffMax = COLOR_DEAD.getRed() - r;
                    int gDiffMax = COLOR_DEAD.getGreen() - g;
                    int bDiffMax = COLOR_DEAD.getBlue() - b;
                    
                    r += (int)((double)rDiffMax * colorChangeFactor);
                    g += (int)((double)gDiffMax * colorChangeFactor);
                    b += (int)((double)bDiffMax * colorChangeFactor);
                    
                    return new Color(r, g, b);
            }
        }
        return COLOR_WHITE_CELL;
    }

    public static Boolean isInCircle(Point point, Point circleCenter, double circleRadius)
    {
        return point.distance(circleCenter) <= circleRadius;
    }
}
