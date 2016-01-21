/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import voronoigame.model.Cell;
import voronoigame.model.GameState;
import voronoigame.model.MoveableCell;
import voronoigame.model.StationaryCell;
import voronoigame.model.delaunay.Edge;

/**
 * This is a dummy class only used to test if the voronoi diagram is drawn correctly and the event listeners/handlers are working correctly
 * 
 */
public class VoronoidiagramDummyImpl implements VoronoiDiagram
{
    
    static Point s1 = new Point(174, 188);
    static Point s2 = new Point(317, 261);
    static Point s3 = new Point(495, 118);
    static Point s4 = new Point(480, 296);
    static Point s5 = new Point(150, 371);
    
    public static Map<Point, Cell.Type> getDummyCellTypeMap()
    {
        Map<Point, Cell.Type> cellTypes = new HashMap<>();
        cellTypes.put(s1, Cell.Type.DEFENSE);
        cellTypes.put(s2, Cell.Type.HEALTHY);
        cellTypes.put(s3, Cell.Type.HEALTHY);
        cellTypes.put(s4, Cell.Type.INFECTED);
        cellTypes.put(s5, Cell.Type.HEALTHY);
        
        return cellTypes;
    }

    public VoronoidiagramDummyImpl()
    {
    }
    
    @Override
    public Collection<Point> getSites()
    {
        Set<Point> sites = new HashSet<>();
        sites.add(s1);
        sites.add(s2);
        sites.add(s3);
        sites.add(s4);
        sites.add(s5);
        return sites;
    }
    
    @Override
    public Collection<Point> getVoronoiVertices()
    {
        Set<Point> vertices = new HashSet<>();
        for (Edge edge : getVoronoiEdges())
        {
            if (!vertices.contains(edge.getPoint1()))
            {
                vertices.add(edge.getPoint1());
            }
            if (!vertices.contains(edge.getPoint2()))
            {
                vertices.add(edge.getPoint2());
            }
        }
        return vertices;
    }
    
    @Override
    public Collection<Edge> getVoronoiEdges()
    {
        Set<Edge> edges = new HashSet<>();
        for (Point site : getSites())
        {
            LinkedList<Point> face = getFaceFromSite(site);
            Point vertex = face.pop();
            while (!face.isEmpty())
            {
                Point nextVertex = face.pop();
                Edge edge = Edge.create(vertex, nextVertex);
                if (!edges.contains(edge))
                {
                    edges.add(edge);
                }
                vertex = nextVertex;
            }
        }
        return edges;
    }
    
    @Override
    public LinkedList<Point> getFaceFromSite(Point site)
    {
        LinkedList<Point> face = new LinkedList<>();
        if (site == s1)
        {
            face.add(new Point(0,0));
            face.add(new Point(300,0));
            face.add(new Point(318,81));
            face.add(new Point(214,287));
            face.add(new Point(0,260));
        } 
        else if (site == s2)
        {
            face.add(new Point(318,81));
            face.add(new Point(416,202));
            face.add(new Point(352,494));
            face.add(new Point(214,287));
        }
        else if (site == s3)
        {
            face.add(new Point(300,0));
            face.add(new Point(700,0));
            face.add(new Point(700,225));
            face.add(new Point(416,202));
            face.add(new Point(318,81));
        }
        else if (site == s4)
        {
            face.add(new Point(416,202));
            face.add(new Point(700,225));
            face.add(new Point(700,525));
            face.add(new Point(357,525));
            face.add(new Point(352,494));
        }
        else if (site == s5)
        {
            face.add(new Point(0,260));
            face.add(new Point(214,287));
            face.add(new Point(352,494));
            face.add(new Point(357,525));
            face.add(new Point(0,525));
        }
        return face;
    }
    
    @Override
    public Cell getCellFromSite(Point site)
    {
        return null;
    }

    @Override
    public void moveSite(Point oldSiteLocation, Point newSiteLocation)
    {
        System.out.println("Moving cell site: " + oldSiteLocation.toString() + " to new location: " + newSiteLocation.toString());
    }
}
