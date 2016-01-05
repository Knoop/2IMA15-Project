package voronoigame.model;

import java.awt.Point;
import voronoigame.Util;

/**
 * @author Maurice
 */
public abstract class Cell
{
    
    private final GameState gameState;

    protected Point point = null;

    protected final double initCircumference, initArea;
    protected double currentCircumference, currentArea;
    
    protected Type type;

    public static final double MAX_SCALE_FACTOR = 2;

    public double getInitCircumference()
    {
        return initCircumference;
    }

    public double getInitArea()
    {
        return initArea;
    }

    private void updateProperties(){
        double[] properties = Util.calculateProperties(this.point, this.gameState);
        this.currentArea = properties[Util.INDEX_AREA];
        this.currentCircumference = properties[Util.INDEX_CIRCUMFERENCE];
    }
    
    public double getCurrentArea() {
        if(this.currentArea < 0)
            this.updateProperties();
        return currentArea;
    }

    public double getCurrentCircumference(){
        if(this.currentCircumference < 0)
            this.updateProperties();
        return currentCircumference;
    }
    

    public double getCurrentAreaRatio()
    {   
        double maxArea = Math.max(this.getCurrentArea(), this.getInitArea());
        double minArea = Math.min(this.getCurrentArea(), this.getInitArea());
        return maxArea / minArea;
    }

    protected Cell(Point point, Type type, GameState gameState)
    {
        this.gameState = gameState;
        this.point = point;
        this.type = type;
        
        double[] properties = Util.calculateProperties(this.point, this.gameState);
        this.initArea = properties[Util.INDEX_AREA];
        this.initCircumference = properties[Util.INDEX_CIRCUMFERENCE];
    }
    
    protected void invalidate(){
        this.currentArea = -1d;
        this.currentCircumference = -1d;
    }
    
    public final void setType(Type type)
    {
        this.type = type;
        this.onTypeChanged();
    }

    public final Type getType()
    {
        return this.type;
    }

    public Point getPoint()
    {
        return point;
    }

    /**
     * Called when the type of the cell has changed. When overriding this method
     * you must call {@code super.onTypeChanged()}.
     */
    protected void onTypeChanged() { }

    public enum Type
    {
        HEALTHY, INFECTED, DEAD, DEFENSE
    }
}
