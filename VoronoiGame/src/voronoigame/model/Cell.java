package voronoigame.model;

import java.awt.Point;

/**
 * @author Maurice
 */
public abstract class Cell
{
    
    private final GameState gameState;

    protected Point point = null;

    protected final float initCircumference, initArea;
    protected float currentArea;
    
    protected Type type;

    public static final double MAX_SCALE_FACTOR = 2;

    public float getInitCircumference()
    {
        return initCircumference;
    }

    public float getInitArea()
    {
        return initArea;
    }

    public float getCurrentArea()
    {
        return currentArea;
    }

    public void setCurrentArea(float currentArea)
    {
        this.currentArea = currentArea;
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
        
        this.initArea = this.area();
        this.initCircumference = this.circumference();
    }
    
    private float area(){
        return this.gameState.area(this);
    }
    
    private float circumference(){
        return this.gameState.circumference(this);
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
