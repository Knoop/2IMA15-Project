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
import voronoigame.model.delaunay.Edge;

/**
 * Represents a voronoi diagram. This interface is used by the GUI to draw the 
 * voronoi diagram and to indicate that a site in the diagram has changed.
 * 
 */
public interface VoronoiDiagram
{
    /**
     * Gets all sites in the voronoi diagram
     * @return 
     */
    public Collection<Point> getSites();
    
    /**
     * Gets all vertices in the voronoi diagram.
     * @return 
     */
    public Collection<Point> getVoronoiVertices();
    
    /**
     * Gets all edges in the voronoi diagram.
     * @return A set of point pairs (array of points with size 2)
     */
    public Collection<Edge> getVoronoiEdges();
    
    /**
     * Given a site for a voronoi cell, returns its correesponding face.
     * @param site
     * @return A LinkedList representing a polygon (with points in-order) that defines the face.
     */
    public LinkedList<Point> getFaceFromSite(Point site);
    
    /*
    The methods below link the diagram to the game state
    */
    
    /**
     * Returns a voronoigame.model.Cell object based on a given site 
     * (the locarion of the cell's nucleus)
     * @param site
     * @return 
     */
    @Deprecated
    public Cell getCellFromSite(Point site);
    
    /**
     * Updates the gamestate. The specified old site is assigned a new site location.
     * Afterwards, the voronoi diagram needs to be computed again.
     * @param oldSiteLocation
     * @param newSiteLocation 
     */
    public void moveSite(Point oldSiteLocation, Point newSiteLocation);
    
    /**
     * Updates the gamestate. The specified site is removed from the diagram.
     * Afterwards, the voronoi diagram needs to be computed again.
     * @param siteLocation
     */
    public void removeSite(Point siteLocation);
}
