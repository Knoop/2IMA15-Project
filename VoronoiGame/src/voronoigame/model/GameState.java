/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model;

import java.awt.Point;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import voronoigame.view.VoronoiDiagram;

/**
 *
 * @author Maurice
 */
public class GameState {

    private final Map<Point, Cell> pointCellMap;
    private final VoronoiDiagram voronoiDiagram;

    public Map<Point, Cell> getPointCellMap()
    {
        return pointCellMap;
    }
    
    public GameState(Map<Point, Cell.Type> cellTypes, VoronoiDiagram voronoiDiagram){
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
    
    public VoronoiDiagram getDiagram(){
        return this.voronoiDiagram;
    }
    
    /**
     * Create a GameState from a Reader.
     * @param reader
     * @return 
     */
    public static GameState from(Reader reader){
        return null;
    }
    
    
}
