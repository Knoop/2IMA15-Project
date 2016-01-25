package voronoigame.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import voronoigame.Util;

/**
 * @author Maurice
 */
public abstract class Cell {

    private final Set<CellLifeCycleListener> listeners;

    private final GameState gameState;

    protected Point point = null;

    protected final double initCircumference, initArea;
    protected double currentCircumference, currentArea;

    protected Type type;
    protected boolean alive;

    public static final double MAX_COMPRESSION_FACTOR = 1/1.5;
    public static final double MAX_EXPANSION_FACTOR = 3;
    
    /**
     * The radius of the nucleus of a cell
     */
    public static final int NUCLEUS_RADIUS = 4;
    
    private boolean focussable;
    private FocusType focusType;

    public double getInitCircumference() {
        return initCircumference;
    }

    public double getInitArea() {
        return initArea;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void updateProperties() {
        double[] properties = Util.calculateProperties(this.point, this.gameState);
        this.currentArea = properties[Util.INDEX_AREA];
        this.currentCircumference = properties[Util.INDEX_CIRCUMFERENCE];
        double ratio = this.getCurrentAreaRatio();
        
        // Scaled out of proportion
        if (this.getScaleRatio() > 1)
            this.kill();
        else
            this.notifyCellPropertyChanged();
        
    }

    private void kill() {
        if (!this.alive)
        {
            return;
        }
        this.alive = false;
        this.gameState.incrementCasualties();
        this.notifyCellKilled();
    }

    public double getCurrentArea() {
        if (this.currentArea < 0) {
            this.updateProperties();
        }
        return currentArea;
    }

    public double getCurrentCircumference() {
        if (this.currentCircumference < 0) {
            this.updateProperties();
        }
        return currentCircumference;
    }

    public double getCurrentAreaRatio() {
        return this.getCurrentArea() / this.getInitArea();
    }
    
    /**
     * Returns the ratio by which the cells dimension have scaled, regardless of whether this is by expansion or compression. 
     * @return The ratio by which the cell has scaled where 0 means not at all and 1 means as large as it may.
     */
    public double getScaleRatio(){
        
        double ratio = this.getCurrentAreaRatio();
        if(ratio < 1)
            return ( 1 - ratio ) / MAX_COMPRESSION_FACTOR;
        else
            return (ratio - 1) / (MAX_EXPANSION_FACTOR - 1);
        
        
    }

    protected Cell(Point point, Type type, GameState gameState, boolean focussable) {
        this.listeners = new HashSet<>();
        this.gameState = gameState;
        this.point = point;
        this.type = type;
        this.focussable = focussable;
        this.focusType = FocusType.NONE;
        this.alive = true;

        double[] properties = Util.calculateProperties(this.point, this.gameState);
        this.currentArea = this.initArea = properties[Util.INDEX_AREA];
        this.currentCircumference = this.initCircumference = properties[Util.INDEX_CIRCUMFERENCE];
    }

    protected void invalidate() {
        this.currentArea = -1d;
        this.currentCircumference = -1d;
    }

    protected final void setType(Type type) {
        this.type = type;
        this.onTypeChanged();
    }

    public final Type getType() {
        return this.type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isFocussable()
    {
        return focussable;
    }

    public void setFocussable(boolean focussable)
    {
        this.focussable = focussable;
    }

    public FocusType getFocusType()
    {
        return focusType;
    }

    public void setFocusType(FocusType focusType)
    {
        if (!this.focussable)
        {
           this.focusType = FocusType.NONE;
           return; 
        }
        this.focusType = focusType;
    }

    /**
     * Called when the type of the cell has changed. When overriding this method
     * you must call {@code super.onTypeChanged()}.
     */
    protected void onTypeChanged() {
        this.notifyCellTypeChanged();
    }

    /**
     * Indicates whether the cell has been compressed. 
     * @return true if the size of this cell is smaller than it's original size,
     * false otherwise.
     */
    public boolean isCompressed() {
        return this.getCurrentAreaRatio() < 1;
    }

    public enum Type {
        HEALTHY, INFECTED, DEFENSE
    }

    public void addCellLifeListener(CellLifeCycleListener listener) {
        this.listeners.add(listener);
    }

    public void removeCellLifeListener(CellLifeCycleListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyCellTypeChanged() {
        for (CellLifeCycleListener listener : listeners) {
            listener.onCellTypeChanged(this);
        }
    }

    private void notifyCellKilled() {
        for (CellLifeCycleListener listener : listeners) {
            listener.onCellKilled(this);
        }
    }

    private void notifyCellPropertyChanged() {
        for (CellLifeCycleListener listener : listeners) {
            listener.onCellPropertyChanged(this);
        }
    }

    public static interface CellLifeCycleListener {

        /**
         * Called when the type of the given cell has changed. This will not be
         * called if the cell was changed to {@code Type.DEAD}. Instead, {@code
         * onCellKilled(Cell cell)} is called.
         *
         * @param cell The cell whose type has changed.
         */
        void onCellTypeChanged(Cell cell);

        /**
         * Called when the given cell was killed.
         *
         * @param cell The cell that was killed.
         */
        void onCellKilled(Cell cell);

        /**
         * Called when the properties of the given cell have changed. This
         * happens if the circumference and/or area of the cell is changed.
         *
         * @param cell The Cell whose properties were changed.
         */
        void onCellPropertyChanged(Cell cell);

    }
}
