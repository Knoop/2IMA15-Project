/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import voronoigame.model.delaunay.VoronoiFacade;
import voronoigame.view.VoronoiDiagram;

/**
 *
 * @author Maurice
 */
public class GameState extends Observable
{

    private final Map<Point, Cell> pointCellMap;
    private final VoronoiDiagram voronoiDiagram;

    public GameState(Map<Point, Cell.Type> cellTypes, VoronoiDiagram voronoiDiagram)
    {
        this.pointCellMap = new HashMap<>();
        this.voronoiDiagram = voronoiDiagram;
        this.mapSitesToCells(cellTypes);
    }

    private void mapSitesToCells(Map<Point, Cell.Type> cellTypes)
    {
        for (Point site : this.voronoiDiagram.getSites())
        {
            Cell cell;
            Cell.Type type = cellTypes.get(site);
            switch (type)
            {
                case DEFENSE:
                    cell = new MoveableCell(site, this);
                    break;
                default:
                    cell = new StationaryCell(site, type, this);
                    break;
            }
            this.pointCellMap.put(site, cell);
        }
    }

    public VoronoiDiagram getDiagram()
    {
        return this.voronoiDiagram;
    }

    public Map<Point, Cell> getPointCellMap()
    {
        return pointCellMap;
    }
    
    
    /**
     * Moves the given cell to the given location
     * @param cell The cell to move
     * @param newLocation The location to move the given cell to.
     */
    public void move(Cell cell, Point newLocation){
        this.getDiagram().moveSite(cell.getPoint(), newLocation);
        this.pointCellMap.remove(cell.getPoint());
        cell.setPoint(newLocation);
        this.pointCellMap.put(cell.getPoint(), cell);
        this.setChanged();
        this.notifyObservers();
    } 
    
    /**
     * Get the cell that is located at the given site
     * @param site The site for which Cell must be located. 
     *         This location must exactly equal the location stored for a cell. 
     *         A point that is within the region of a cell will yield a null value.
     * @return The cell that is located at the given point, 
     *          or null if no such cell exists.
     */
    public Cell getCell(Point site) {
        return this.pointCellMap.get(site);
    }


    /**
     * Create a GameState from a Reader.
     *
     * @param reader
     * @return
     * @throws java.io.IOException
     */
    public static GameState from(Reader reader) throws IOException
    {
        BufferedReader bReader = new BufferedReader(reader);
        String line;
        Rectangle bounddingBox = new Rectangle(new Point(0,0), new Dimension(700, 525));
        Map<Point, Cell.Type> pointTypeMap = new HashMap<>();
        ArrayList<Point> stationaryPoints = new ArrayList<>();
        ArrayList<Point> movingPoints = new ArrayList<>();
        while ((line = bReader.readLine()) != null)
        {
            String[] parts = line.split(":");
            if (parts.length < 1)
            {
                continue;
            }
            switch (parts[0].toLowerCase())
            {
                case "boundingbox":
                    String[] dimensionParts = parts[1].split(",");
                    bounddingBox.width = Integer.parseInt(dimensionParts[0]);
                    bounddingBox.height = Integer.parseInt(dimensionParts[1]);
                    break;
                case "point":
                    String[] pointParts = parts[1].split(",");
                    Point point = new Point(Integer.parseInt(pointParts[0]), Integer.parseInt(pointParts[1]));
                    Cell.Type type = Cell.Type.valueOf(pointParts[2].toUpperCase().trim());
                    pointTypeMap.put(point, type);
                    if (type == Cell.Type.DEFENSE)
                    {
                        movingPoints.add(point);
                    }
                    else
                    {
                        stationaryPoints.add(point);
                    }
                    break;
            }
        }
        VoronoiDiagram diagram = new VoronoiFacade(stationaryPoints, movingPoints);
        return new GameState(pointTypeMap, diagram);
    }

   
}
