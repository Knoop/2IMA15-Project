/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model.delaunay;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author leo
 */
public class VoronoiFacade {
    
    private ArrayList<Point2D> stillPoints;
    private ArrayList<Point2D> movingPoints;
    private double boundX, boundY;
    private DelaunayTriangle root;
    
    final private double SUBZERO = -1;
    
    VoronoiFacade(ArrayList<Point2D> stillPoints, ArrayList<Point2D> movingPoints){
        this.stillPoints = stillPoints;
        this.movingPoints = movingPoints;
        boundX = 0;
        boundY = 0;
        for(Point2D p: this.stillPoints){
            boundX = Math.max(p.getX(), boundX);
            boundY = Math.max(p.getY(), boundY);
        }
        for(Point2D p: this.movingPoints){
            boundX = Math.max(p.getX(), boundX);
            boundY = Math.max(p.getY(), boundY);
        }
        root = new DelaunayTriangle(
                new DelaunayPoint(-boundX,SUBZERO), 
                new DelaunayPoint(boundX/2,3*boundY), 
                new DelaunayPoint(2*boundX,SUBZERO));
        java.util.Collections.shuffle(this.stillPoints);
    }
    
}
