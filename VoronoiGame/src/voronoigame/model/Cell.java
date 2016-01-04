/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model;

import java.awt.Point;

/**
 *
 * @author Maurice
 */
public abstract class Cell {

    protected Point point = null;

    protected final float initCircumference, initArea;

    protected Type type;

    protected Cell(Point point) {
        this(point, Type.HEALTHY);
    }
    
    protected Cell(Point point, Type type) {
        this.point = point;
        this.initArea = 0f;
        this.initCircumference = 0f;
        this.type = type;
    }

    public final void setType(Type type) {
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
    protected void onTypeChanged() {
        
    }

    public enum Type {
        HEALTHY, INFECTED, DEAD
    }
}
