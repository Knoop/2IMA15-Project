/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model.delaunay;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.TreeSet;

/**
 *
 * @author leo
 */
public class DelaunayTriangle {
    
    private DelaunayPoint[] points;
    private Point bounds;
    private DelaunayTriangle[] parents;
    private DelaunayTriangle[] children;
    private DelaunayTriangle[] neighbours;
    
    DelaunayTriangle(DelaunayPoint[] points, Point bounds){
        this.points = points;
        this.bounds = bounds;
        this.parents = new DelaunayTriangle[2];
        this.children = new DelaunayTriangle[3];
        this.neighbours = new DelaunayTriangle[3];
    }
    
    protected void insert(Point p){
        boolean hasPoint = false;
        for(DelaunayPoint d: this.points){
            hasPoint = hasPoint || (p.getX() == d.getX() && p.getY() == d.getY());
        }
        if(hasPoint){
            return;
        }
        
        if(contains(p)){
            if(this.children[0] == null){
                //Set new children
                DelaunayPoint newPoint = new DelaunayPoint((int) p.getX(), (int) p.getY(), false);
                this.children[0] = new DelaunayTriangle(new DelaunayPoint[] {
                    newPoint, this.points[0], this.points[1]
                    }, this.bounds);
                this.children[1] = new DelaunayTriangle(new DelaunayPoint[] {
                    newPoint, this.points[1], this.points[2]
                    }, this.bounds);
                this.children[2] = new DelaunayTriangle(new DelaunayPoint[] {
                    newPoint, this.points[2], this.points[0]
                    }, this.bounds);
                
                //Set parents
                this.children[0].parents[0] = this;
                this.children[1].parents[0] = this;
                this.children[2].parents[0] = this;
                
                //Set children's neighbours
                this.children[0].neighbours = new DelaunayTriangle[] {
                    this.children[1], this.children[2], 
                    getEdgeNeighbour(this.points[0], this.points[1])
                };
                this.children[1].neighbours = new DelaunayTriangle[] {
                    this.children[0], this.children[2], 
                    getEdgeNeighbour(this.points[1], this.points[2])
                };
                this.children[2].neighbours = new DelaunayTriangle[] {
                    this.children[0], this.children[1], 
                    getEdgeNeighbour(this.points[2], this.points[0])
                };
                
                //Legalize
                for(DelaunayTriangle t: this.children){
                    t.legalizeEdge(t.points[0], t.points[1], t.points[2]);
                }
            }
            else {
                for(DelaunayTriangle t: this.children){
                    if(t != null){
                        t.insert(p);
                    }
                }
            }
        }
    }
    
    private void legalizeEdge(DelaunayPoint p, DelaunayPoint e1, DelaunayPoint e2){
        DelaunayTriangle targetNeighbour = getEdgeNeighbour(e1, e2);
        DelaunayPoint targetPoint = null;
        for(DelaunayPoint d: targetNeighbour.points){
            if(!d.equals(e1) && !d.equals(e2)){
                targetPoint = d;
            }
        }
        double[] c = circumCenter();
        double r = radius();
        if(targetPoint.distance(c[0], c[1]) < r){
            
            this.children[0] = new DelaunayTriangle(new DelaunayPoint[] {
                p, e1, targetPoint
                }, this.bounds);
            this.children[1] = new DelaunayTriangle(new DelaunayPoint[] {
                p, e2, targetPoint
                }, this.bounds);
            
            targetNeighbour.children[0] = this.children[0];
            targetNeighbour.children[1] = this.children[1];
            
            this.children[0].parents[0] = this;
            this.children[1].parents[0] = this;
            
            this.children[0].parents[1] = targetNeighbour;
            this.children[1].parents[1] = targetNeighbour;
            
            //Set children's neighbours
            this.children[0].neighbours = new DelaunayTriangle[] {
                this.children[1], 
                this.getEdgeNeighbour(e1, targetPoint), 
                this.getEdgeNeighbour(p, e2)
            };
            this.children[1].neighbours = new DelaunayTriangle[] {
                this.children[0], 
                this.getEdgeNeighbour(e2, targetPoint), 
                this.getEdgeNeighbour(p, e2)
            };
            
            this.children[0].legalizeEdge(p, targetPoint, e1);
            this.children[1].legalizeEdge(p, targetPoint, e2);
        }
    }
    
    private DelaunayTriangle getEdgeNeighbour(DelaunayPoint e1, DelaunayPoint e2){
        boolean has1, has2;
        for(DelaunayTriangle t: neighbours){
            if(t != null){
                has1 = false;
                has2 = false;
                for(DelaunayPoint p: t.points){
                    if(p.equals(e1)){
                        has1 = true;
                    }
                    if(p.equals(e2)){
                        has2 = true;
                    }
                }
                if(has1 && has2){
                    return t;
                }
            }
        }
        return null;
    }
    
    protected void delete(Point p){
        boolean hasPoint = false;
        for(DelaunayPoint d: this.points){
            hasPoint = hasPoint || (p.getX() == d.getX() && p.getY() == d.getY());
        }
        if(hasPoint){
            if(this.children[0] == null){
                for(DelaunayTriangle t: this.parents){
                    if(t != null){
                        t.children = new DelaunayTriangle[3];
                    }
                }
            }
            else {
                for(DelaunayTriangle t: this.children){
                    if(t != null){
                        t.delete(p);
                    }
                }
            }
        }
    }
    
    public TreeSet<DelaunayTriangle> findAll(Point p){
        TreeSet result = new TreeSet<>();
        if(contains(p)){
            result.add(this);
            for(DelaunayTriangle t: this.children){
                    if(t != null){
                        result.addAll(t.findAll(p));
                    }
            }
        }
        return result;
    }
    
    public TreeSet<DelaunayTriangle> findLeaves(Point p){
        TreeSet result = new TreeSet<>();
        if(contains(p)){
            if(this.children[0] == null){
                result.add(this);
            }
            else {
                for(DelaunayTriangle t: this.children){
                    if(t != null){
                        result.addAll(t.findLeaves(p));
                    }
                }
            }
        }
        return result;
    }
    
    public double[] getVoronoiVertex(Point p){
        if(isSymbolic()){
            //return new double[2];
        }
        return circumCenter();
    }
    
    public boolean isSymbolic(){
        for(DelaunayPoint p: this.points){
            if(p.isSymbolic()){
                return true;
            }
        }
        return false;
    }
    
    protected boolean contains(Point p){
        boolean result = true;
        int numPoints = this.points.length; //Should be 3
        Point base;
        Point edge;
        Point opposite;
        double px, py, ox, oy;
        for(int i = numPoints - 1; i <= 0; i--){
            base = this.points[i];
            edge = new Point((int) this.points[i].getX() - (int) this.points[(i+1)%numPoints].getX(), 
                (int) this.points[i].getY() - (int) this.points[(i+1)%numPoints].getY());
            opposite = this.points[(i+2)%numPoints];
            
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
        d1x = this.points[1].getX() - p1x;
        d1y = p1y - this.points[1].getY();
        d2x = this.points[1].getX() - p2x;
        d2y = p2y - this.points[1].getY();
        
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
