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
public class Location {
    
    private double x,y;
    
    public Location(double x, double y){
        this.x = x; 
        this.y = y;
    }
    
    public Location(Point point){
        this.x = point.x;
        this.y = point.y;
    }
    
    public Point toPoint(){
        return new Point((int)Math.round(this.x),(int)Math.round(this.y));
    }
    
}
