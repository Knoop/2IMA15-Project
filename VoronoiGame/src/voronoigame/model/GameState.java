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
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import voronoigame.Util;
import java.util.Set;

import voronoigame.model.delaunay.VoronoiFacade;
import voronoigame.view.voronoi.VoronoiDiagram;

/**
 *
 * @author Maurice
 */
public class GameState extends Observable
{
    
    private static final float CASUALTIES_RATIO = 0.3f;
    private static final int SCORE_MULTIPLIER = 10;
    
    private final Map<Point, Cell> pointCellMap;
    private final Set<Cell> infectedCells;
    private final VoronoiDiagram voronoiDiagram;
    private final int maxCasualties;
    private int currentCasualties;
    
    private int score;
    private final int minimumScore;

    public GameState(Map<Point, Cell.Type> cellTypes, VoronoiDiagram voronoiDiagram)
    {
        this.pointCellMap = new HashMap<>();
        this.voronoiDiagram = voronoiDiagram;
        this.infectedCells = new HashSet<>();
        this.prepareCells(cellTypes);
        this.maxCasualties = Math.round(CASUALTIES_RATIO * cellTypes.size());
        this.currentCasualties = 0;
        
        score = pointCellMap.size() * SCORE_MULTIPLIER;
        minimumScore = score - (maxCasualties * SCORE_MULTIPLIER);
    }

    /**
     * Maps sites to cells and determines the evil cells and also reference them in a special set
     * @param cellTypes 
     */
    private void prepareCells(Map<Point, Cell.Type> cellTypes)
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
            if (cell.getType() == Cell.Type.INFECTED)
            {
                this.infectedCells.add(cell);
            }
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

    public int getScore()
    {
        return score;
    }

    public int getMinimumScore()
    {
        return minimumScore;
    }
    
    public int getMaximumScore() {
        return this.pointCellMap.size() * SCORE_MULTIPLIER;
    }
    
    
    
    /**
     * Moves the given cell to the given location
     * @param cell The cell to move
     * @param point The location to move the given cell to.
     */
    private void move(Cell cell, Point point){

        // If the point is out of bounds, move it to within bounds
        if(point.x < 0 || point.y < 0 || point.x >= this.voronoiDiagram.width() || point.y >= this.voronoiDiagram.height())
            point = new Point(
                    Math.min(Math.max(point.x, 0), this.voronoiDiagram.width()-1),
                    Math.min(Math.max(point.y, 0), this.voronoiDiagram.height()-1));
        this.getDiagram().moveSite(cell.getPoint(), point);
        this.pointCellMap.remove(cell.getPoint());
        cell.setPoint(point);
        this.pointCellMap.put(cell.getPoint(), cell);
        this.setChanged();
        this.updateCellStates();
        
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
    
    private void updateCellStates() {
        for (Cell cell : this.pointCellMap.values())
        {
            if (!(cell instanceof MoveableCell))
            {
                cell.updateProperties();
            }
        }
    }
    
    public void incrementCasualties() {
        this.currentCasualties++;
        score -= SCORE_MULTIPLIER;
    }
    
    public Set<Cell> getLivingInfectedCells()
    {
        Set<Cell> cells = new HashSet<>();
        for(Cell cell : this.infectedCells)
        {
            if (!cell.isAlive())
            {
                continue;
            }
            cells.add(cell);
        }
        return cells;
    }

    /**
     * Indicates whether this GameState has finished. This should not 
     * distinguish between whether the player has won or lost, only that the 
     * player should not be allowed to play further.
     * @return true if the game has finished and the player is not allowed to do
     * any more moves, false otherwise.
     */
    public boolean isFinished() {
        return this.currentCasualties > this.maxCasualties || this.getLivingInfectedCells().isEmpty();
    }

    /**
     * Indicates whether the player has won in the current state. A value of 
     * true should indicate that the player has won. This does not mean that a 
     * value of false indicates that the user has lost.
     * @return true if the player has won in the current state, 
     * false otherwise. This includes situations in which the game is not finished!
     */
    public boolean hasWon() {
        return this.isFinished() && this.currentCasualties <= this.maxCasualties 
                && this.getLivingInfectedCells().isEmpty();
    }
    
    /**
     * Starts a move operation on this GameState. This allows you to move a cell
     * in the gamestate. Any previously issued MoveOperation for the given cell will be finished and will no longer be usable.
     * @param cell The Cell that must be moved by the returned operation.
     * @return The MoveOperation that can be used to (repeatedly) move a cell in
     * the gamestate.
     */
    public MoveOperation move(Cell cell) {
        this.finishMoveOperation(cell);
        MoveOperation op = new MoveOperation(cell);
        this.currentOperations.put(cell, op);
        return op;
    }
    
    /**
     * Finishes the current move operation for the given cell if applicable
     * @param cell The Cell for which to remove the current move operation. 
     */
    private void finishMoveOperation(Cell cell){
        if(currentOperations.containsKey(cell))
            this.currentOperations.remove(cell).finish();
    }
    
        /**
     * A mapping containing all operations that are currently active. 
     */
    private final Map<Cell, MoveOperation> currentOperations = new HashMap<>();

    /**
     * A MoveOperation allows a single cell to be moved in the GameState. All
     * checks about where a cell can and cannot go are performed by this class.
     */
    public class MoveOperation{
        
        /**
         * The maximum amount of distance a cell may travel per millisecond. This is calculated as 100pixels per second, thus 0.1pixel per millisecond.
         */
        private static final double MAX_DISTANCE_PER_MS = 0.12;
    
        /**
         * The real x and y, which will be rounded for the GameState. 
         */
        private double realX,realY;
        
        /**
         * Indicates whether the value of this MoveOperation has been finished. If the MoveOperation is finished, it can no longer apply its updates. 
         */
        private boolean finished = false;
        
        /**
         * The Cell to which this move operation is applicable
         */
        private final Cell cell;
        
        /**
         * The time in milliseconds that the last operation occured.
         */
        private long lastOperation;
        
        
        private MoveOperation(Cell cell){
            this.cell = cell;
            this.realX = cell.point.x;
            this.realY = cell.point.y;
            this.lastOperation = System.currentTimeMillis();
        }
        
        /**
         * Moves the cell contained by this move operation from its current 
         * location towards the given point. The Cell may and up at the given 
         * point, however this is not guaranteed. The new location of the cell 
         * is determined by the maximum velocity with which a cell may move in 
         * one turn, as well as by the presence of nucleii of other cells that
         * may be in the way. As the maximum amount of velocity is constant, the
         * maximum amount of movement by the cell is dependent on the amount of
         * time that has passed since the last movement, thus indicating how
         * much distance should have been travelled.
         * @param towards The point towards which the cell must move
         */
        public void moveTowards(Point towards){
            double length = towards.distance(realX, realY);
            double maxLength = this.interval() * MAX_DISTANCE_PER_MS;

            double reachedX, reachedY; 
            if(length <= maxLength) {
                reachedX = towards.x;
                reachedY = towards.y;
            } else {
                // Reached point is the direction of towards scaled to how far off the towards point is.
                reachedX = this.realX + ((towards.x - cell.point.x)* maxLength / length);
                reachedY = this.realY + ((towards.y - cell.point.y)* maxLength / length);
            }
            
            Point closest = Util.getClosest(GameState.this.voronoiDiagram.getSiteNeighbours(cell.point), reachedX, reachedY);
            if(closest == null || closest.distance(reachedX, reachedY) >= Cell.NUCLEUS_RADIUS*2){
                this.realX = reachedX;
               this.realY = reachedY;
                this.apply();
            }
            else
            {
                System.out.println("Not performing move, it would intersect another core.");
            }
        }
        
        /**
         * Applies the current realX and realY to the position of the cell in
         * the gamestate. This means that realX and realY are converted to a
         * java.awt.Point.
         */
        private void apply(){
            synchronized(GameState.this){
                if(this.finished)
                    throw new IllegalStateException(
                            "A finished MoveOperation can not apply its state");
                GameState.this.move(cell, 
                        new Point((int)Math.round(realX), (int)Math.round(realY)));
            }
        }

        public void finish() {
            this.finished = true;
            GameState.this.finishMoveOperation(this.cell);
        }

        /**
         * Creates a new interval. This set the lastOperation to now and returns
         * the time in milliseconds since the previous operation.
         * @return The amount of milliseconds between the previous operation and
         * now.
         */
        private double interval() {
            long interval = System.currentTimeMillis() - this.lastOperation; 
            this.lastOperation = System.currentTimeMillis();
            return interval;
        }
    }
   
}
