/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import voronoigame.model.Cell;
import voronoigame.model.GameState;
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
        Color returnColor = COLOR_WHITE_CELL;
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
                    
                    returnColor = new Color(r, g, b);
            }
        }
        
        if (cell.isFocussed())
        {
            returnColor = returnColor.brighter();
        }
        
        return returnColor;
    }

    public static Boolean isInCircle(Point point, Point circleCenter, double circleRadius)
    {
        return point.distance(circleCenter) <= circleRadius;
    }
    
    /**
     * Calculates the area of a list of points. An algorithm found at http://www.mathopenref.com/coordpolygonarea.html
     * is used for the calculation. It is assumed that the polygon formed by 
     * the given list of points is not self intersecting.
     * @param points A list of points. This should contain at least 3 elements. 
     *              When less than 3 elements are provided, an exception is thrown. 
     *              This list is assumed to form a non-self-intersecting polygon.
     * @return The area of the polygon formed by the given list of points. 
     * @throws IllegalArgumentException 
     *              If the given list of points contains less than 3 elements. 
     */
    public static double calculateArea(List<Point> points){
        
        if(points.size() < 3)
            throw new IllegalArgumentException("Expected at least 3 elements");
        
        Iterator<Point> iterator = points.iterator();
        Point cur = iterator.next(); 
        double area = 0d;
        
        // Iterate over all points and perform each required step
        while(iterator.hasNext()){
            Point next = iterator.next();
            area += cur.x * next.y - cur.y * next.x;
            cur = next;
        }
        
        // repeat the step once more for the first and last(cur) points
        area += cur.x * points.get(0).y - cur.y * points.get(0).x;
        
        // The area now is half of the absolute value
        return Math.abs(area) / 2;
    }
    
    /**
     * Calculates the circumference of a list of points. This calculates the 
     * distance between every consecutive points, looping back to the first 
     * element.
     * @param points A list of points. This should contain at least 3 elements. 
     *              When less than 3 elements are provided, an exception is thrown. 
     *              This list is assumed to form a convex hull, which is why the 
     *              distance between all consecutive elements is used.
     * @return The circumference of the convex hull formed by the given list of points. 
     * @throws IllegalArgumentException 
     *              If the given list of points contains less than 3 elements. 
     */
    public static double calculateCircumference(List<Point> points){
        
        if(points.size() < 3)
            throw new IllegalArgumentException("Expected at least 3 elements");
        
        Iterator<Point> iterator = points.iterator();
        Point cur = iterator.next(); 
        double circumference = 0d;
        
        // Iterate over all points
        while(iterator.hasNext()){
            Point next = iterator.next();
            circumference += cur.distance(next);
            cur = next;
        }
        
        // Add the distance between the first (0) and last (cur)
        circumference += points.get(0).distance(cur);
        
        return circumference;
    }
    
    public static double[] calculateProperties(Point point, GameState gameState) {
        List<Point> points = gameState.getDiagram().getFaceFromSite(point); 
        double[] properties = new double[2];
        properties[INDEX_AREA] = Util.calculateArea(points);
        properties[INDEX_CIRCUMFERENCE] = Util.calculateCircumference(points);
        return properties;    
    }
    
    public static int INDEX_AREA = 0, INDEX_CIRCUMFERENCE = 1;
}
