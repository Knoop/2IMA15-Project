/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model.delaunay;

import java.awt.Point;
import java.util.Objects;

/**
 *
 * @author Guus
 */
public class Edge
{
    private final Point point1;
    private final Point point2;

    public Point getPoint1()
    {
        return point1;
    }

    public Point getPoint2()
    {
        return point2;
    }
    
    private Edge(Point point1, Point point2)
    {
        this.point1 = point1;
        this.point2 = point2;
    }
    
    public static Edge create(Point a, Point b)
    {
        /*if (a.equals(b))
        {
            throw new IllegalArgumentException("Two points in an edge cannot be equal");
        }*/
        
        Point first;
        Point second;
        
        if (a.x < b.x)
        {
            first = new Point(a);
            second = new Point(b);
        }
        else if (a.x > b.x)
        {
            first = new Point(b);
            second = new Point(a);
        }
        else
        {
            if (a.y < b.y)
            {
                first = new Point(a);
                second = new Point(b);
            }
            else 
            {
                first = new Point(b);
                second = new Point(a);
            }
        }
        
        return new Edge(first, second);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.point1);
        hash = 89 * hash + Objects.hashCode(this.point2);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.point1, other.point1))
        {
            return false;
        }
        if (!Objects.equals(this.point2, other.point2))
        {
            return false;
        }
        return true;
    }
    
    
}
