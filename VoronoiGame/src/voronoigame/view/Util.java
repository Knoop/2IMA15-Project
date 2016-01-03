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

    public static Color getColorforCell(Cell cell)
    {
        if (cell.getClass() == StationaryCell.class)
        {
            StationaryCell stationaryCell = (StationaryCell) cell;
            switch (stationaryCell.getType())
            {
                case INFECTED:
                    return Color.RED;
                default:
                    return Color.GREEN;
            }
        }
        return Color.WHITE;
    }

    public static Boolean isInCircle(Point point, Point circleCenter, double circleRadius)
    {
        return point.distance(circleCenter) <= circleRadius;
    }
}
