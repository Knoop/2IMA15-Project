/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voronoigame.view;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;
import voronoigame.model.Cell;

/**
 *
 * @author Guus van Lankveld
 */
public interface VoronoiDiagram
{
    public Collection<Point> getSites();
    public Collection<Point> getVoronoiVertices();
    public Collection<Point[]> getVoronoiEdges();
    
    public LinkedList<Point> getFaceFromSite(Point site);
    public Cell getCellFromSite(Point site);
    
    public void moveSite(Cell cell, Point newSiteLocation);
}
