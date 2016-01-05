package voronoigame.model;

import java.awt.Point;

/**
 * @author Maurice
 */
public abstract class Cell
{

    protected Point point = null;

    protected float initCircumference, initArea, currentArea;

    protected Type type;

    public static final double MAX_SCALE_FACTOR = 2;

    public float getInitCircumference()
    {
        return initCircumference;
    }

    public void setInitCircumference(float initCircumference)
    {
        this.initCircumference = initCircumference;
    }

    public float getInitArea()
    {
        return initArea;
    }

    public void setInitArea(float initArea)
    {
        this.initArea = initArea;
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

    protected Cell(Point point, float initArea)
    {
        this(point, initArea, Type.HEALTHY);
    }

    protected Cell(Point point, float initArea, Type type)
    {
        this.point = point;
        this.initArea = initArea;
        this.initCircumference = 0f;
        this.currentArea = initArea;
        this.type = type;
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
    protected void onTypeChanged()
    {

    }

    public enum Type
    {
        HEALTHY, INFECTED, DEAD
    }
}
