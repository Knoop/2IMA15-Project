/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model.delaunay;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author leo
 */
public class DelaunayTriangle {
    
    private DelaunayPoint[] points;
    
    DelaunayTriangle(DelaunayPoint[] points){
        this.points = points;
    }
    
    protected void insert(Point p){
        
    }
    
    protected void delete(Point p){
        
    }
    
    protected void findAll(Point p){
        
    }
    
    protected void findLeaf(Point p){
        
    }
    
    protected boolean contains(Point p){
        boolean result = true;
        int numPoints = this.points.length; //Should be 3
        Point base;
        Point edge;
        Point opposite;
        double px, py, ox, oy;
        for(int i = numPoints - 1; i <= 0; i--){
            base = points[i];
            edge = new Point((int) points[i].getX() - (int) points[(i+1)%numPoints].getX(), 
                (int) points[i].getY() - (int) points[(i+1)%numPoints].getY());
            opposite = points[(i+2)%numPoints];
            
            px = p.getX() - base.getX();
            py = p.getY() - base.getY();
            ox = opposite.getX() - base.getX();
            oy = opposite.getY() - base.getY();
            
            py = py + edge.getY() * (px/edge.getX());
            oy = oy + edge.getY() * (ox/edge.getX());     
                    
            if(py == 0){
                return true;
            }
            if(Math.signum(py) != Math.signum(oy)){
                result = false;
            }
        }
        return result;
    }
    
    public double[] circumCenter(){
        double[] result = new double[2];
        
        double p1x, p1y, p2x, p2y; //Position vectors
        p1x = (this.points[0].getX() + this.points[1].getX())/2;
        p1y = (this.points[0].getY() + this.points[1].getY())/2;
        p2x = (this.points[1].getX() + this.points[2].getX())/2;
        p2y = (this.points[1].getY() + this.points[2].getY())/2;
        
        double d1x, d1y, d2x, d2y; //Direction vectors
        d1x = points[1].getX() - p1x;
        d1y = p1y - points[1].getY();
        d2x = points[1].getX() - p2x;
        d2y = p2y - points[1].getY();
        
        double a1, b1, a2, b2; //Values for functions yi = ai*x + bi
        a1 = d1y/d1x;
        b1 = p1y - d1y*(p1x/d1x);
        a2 = d2y/d2x;
        b2 = p2y - d2y*(p2x/d2x);
        
        double af, bf; //Values for the combined function
        af = a1-a2;
        bf = b1-b2;
        result[0] = -af/bf;
        result[1] = b1 + a1*result[0];
        
        return result;
    }
    
    public double radius(){
        double[] center = circumCenter();
        return points[0].distance(center[0], center[1]);
    }
    
}
