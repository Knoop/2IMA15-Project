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
    public static final int COLOR_HEALTHY = 0x93ed94;
    public static final int COLOR_INFECTED = 0xff0000;
    public static final int COLOR_WHITE_CELL = 0xffffff;
    public static final int COLOR_DEAD = 0x888888;

    public static Color getColorforCell(Cell cell)
    {
        if (cell.getType() == Cell.Type.DEAD)
        {
            return new Color(COLOR_DEAD);
        }
        if (cell.getClass() == StationaryCell.class)
        {
            StationaryCell stationaryCell = (StationaryCell) cell;
            switch (stationaryCell.getType())
            {
                case INFECTED:
                    return new Color(COLOR_INFECTED);
                default:
                    Color color = new Color(COLOR_HEALTHY);
                    //Change color when area of cell deviates too much from initarea
                    return color;
            }
        }
        return new Color(COLOR_WHITE_CELL);
    }

    public static Boolean isInCircle(Point point, Point circleCenter, double circleRadius)
    {
        return point.distance(circleCenter) <= circleRadius;
    }
}
