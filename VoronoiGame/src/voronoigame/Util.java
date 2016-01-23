/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame;

import java.awt.Point;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import voronoigame.model.GameState;

/**
 *
 * @author Guus van Lankveld
 */
public class Util
{
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
    
    public static File LEVEL_FOLDER = new File("levels");

    public static int INDEX_AREA = 0, INDEX_CIRCUMFERENCE = 1;

    /**
     * Subtracts point b from point a. This means that the x value of the new 
     * point is equal to the x value of the a point minus the x value of the b 
     * point. This is also done for the y value. This operation has no effect on
     * either a or b.
     * @param a The Point from which to subtract point b. 
     * @param b The Point to subtract from point a.
     * @return A new point that is the subtraction of b from a.
     */
    public static Point subtract(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }
    
    /**
     * Adds point b to point a. This means that the x value of the new 
     * point is equal to the x value of the a point plus the x value of the b 
     * point. This is also done for the y value. This operation has no effect on
     * either a or b.
     * @param a The Point to which to add point b. 
     * @param b The Point to which to add point a.
     * @return A new point that is the addition of a and b.
     */
    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }
    
    /**
     * Scales the given point by the given factor. The resulting multiplication
     * is rounded following {@code Math.round(double)}. This has no effect on the given point.
     * @param p The point to be scaled
     * @param factor THe factor by which to scale the point. 
     * @return A new point that is the result of scaling the given point by the
     * given factor. For instance, if (0,5) is scaled by 0.5, then the result is
     * (0,2.5), which is then rounded to (0,3). 
     */
    public static Point scale(Point p, double factor){
        return new Point((int)Math.round(p.x * factor), (int)Math.round(p.y * factor));
    }
}
