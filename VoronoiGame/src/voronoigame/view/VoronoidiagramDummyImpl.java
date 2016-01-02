/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import voronoigame.model.Cell;
import voronoigame.model.MoveableCell;
import voronoigame.model.StationaryCell;

/**
 *
 * @author Guus van Lankveld
 */
public class VoronoidiagramDummyImpl implements VoronoiDiagram
{
    
    Point s1 = new Point(174, 188);
    Point s2 = new Point(317, 261);
    Point s3 = new Point(495, 118);
    Point s4 = new Point(480, 296);
    Point s5 = new Point(150, 371);
    
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
        for (Point[] edge : getVoronoiEdges())
        {
            if (!vertices.contains(edge[0]))
            {
                vertices.add(edge[0]);
            }
            if (!vertices.contains(edge[1]))
            {
                vertices.add(edge[1]);
            }
        }
        return vertices;
    }
    
    @Override
    public Collection<Point[]> getVoronoiEdges()
    {
        Set<Point[]> edges = new HashSet<>();
        for (Point site : getSites())
        {
            LinkedList<Point> face = getFaceFromSite(site);
            Point vertex = face.pop();
            while (!face.isEmpty())
            {
                Point nextVertex = face.pop();
                Point[] edge = new Point[2];
                edge[0] = new Point(vertex);
                edge[1] = new Point(nextVertex);
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
        if (site == s1)
        {
            return new MoveableCell(site);
        }
        if (site == s4)
        {
            StationaryCell evilCell = new StationaryCell(site);
            evilCell.setType(Cell.Type.INFECTED);
            return evilCell;
        }
        return new StationaryCell(site);
    }
    
}
