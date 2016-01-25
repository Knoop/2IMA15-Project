/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voronoigame.view.voronoi;

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
     * Gets all neighbours of a site in the voronoi diagram
     * @return 
     */
    public Collection<Point> getSiteNeighbours(Point site);
    
    /**
     * Gets all vertices in the voronoi diagram.
     * @return 
     */
    public Collection<Point> getVoronoiVertices();
    /**
     * Gets all vertices in the voronoi diagram.
     * @return 
     */
    public Collection<Point> getVoronoiVerticesBySite(Point site);
    
    /**
     * Gets all edges in the voronoi diagram.
     * @return A set of point pairs (array of points with size 2)
     */
    public Collection<Edge> getVoronoiEdges();
    
    /**
     * Gets all edges in the voronoi diagram.
     * @return A set of point pairs (array of points with size 2)
     */
    public Collection<Edge> getVoronoiEdgesBySite(Point site);
    
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
    
    /**
     * Indicates the width of the voronoi diagram. 
     * @return the width of the voronoi diagram. 
     */
    public int width();
    
    /**
     * Indicates the height of the voronoi diagram. 
     * @return the height of the voronoi diagram
     */
    public int height();

    
    /**
     * Gets the set of all points that have been updated since the last flush.
     * @return the set of points to be updated
     */
    public Collection<Point> getUpdatedPoints();

    
    /**
     * Empties the queue of points to be updated in the view
     */
    public void flushUpdatedPoints();
    
}
