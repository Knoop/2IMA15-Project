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
public class DelaunayTriangle implements Comparable{
    
    protected DelaunayPoint[] points;
    private DelaunayTriangle[] parents;
    private DelaunayTriangle[] children;
    protected DelaunayTriangle[] neighbours;
    
    DelaunayTriangle(DelaunayPoint[] points){
        this.points = points;
        this.parents = new DelaunayTriangle[2];
        this.children = new DelaunayTriangle[3];
        this.neighbours = new DelaunayTriangle[3];
    }
    
    protected boolean insert(Point p){
        boolean inserted = false;
        if(hasPoint(p)){
            throw new IllegalArgumentException("Point already contained in diagram: " + p);
        }
        
        if(contains(p)){
            
            if(this.children[0] == null){
                inserted = true;
                //Set new children
                DelaunayPoint newPoint = new DelaunayPoint((int) p.getX(), (int) p.getY(), false);
                this.children[0] = new DelaunayTriangle(new DelaunayPoint[] {
                    newPoint, this.points[0], this.points[1]
                    });
                this.children[1] = new DelaunayTriangle(new DelaunayPoint[] {
                    newPoint, this.points[1], this.points[2]
                    });
                this.children[2] = new DelaunayTriangle(new DelaunayPoint[] {
                    newPoint, this.points[2], this.points[0]
                    });
                
                //Set parents
                this.children[0].parents[0] = this;
                this.children[1].parents[0] = this;
                this.children[2].parents[0] = this;
                
                //Set children's neighbours
                DelaunayTriangle currentNeighbour = null;
                int oldNeighbourIndex;
                int numNeigh = 3;
                for(int i = 0; i < numNeigh; i++){
                    currentNeighbour = getEdgeNeighbour(this.points[i], this.points[(i+1)%numNeigh]);
                    if(currentNeighbour != null){
                        oldNeighbourIndex = currentNeighbour.getEdgeNeighbourIndex(
                                        this.points[i], this.points[(i+1)%numNeigh]);
                        currentNeighbour.neighbours[oldNeighbourIndex] = this.children[i];
                    }
                    this.children[i].neighbours = new DelaunayTriangle[] {
                        this.children[(i+1)%numNeigh], this.children[(i+2)%numNeigh], 
                        currentNeighbour
                    };
                }                
                
                //Legalize
                for(DelaunayTriangle t: this.children){
                    t.legalizeEdge(t.points[0], t.points[1], t.points[2]);
                }
            }
            else {
                for(DelaunayTriangle t: this.children){
                    if(t != null){
                        inserted = inserted || t.insert(p);
                    }
                    if(inserted) break;
                }
            }
        }
        if(!inserted && isRoot()){
            throw new IllegalStateException("Could not insert point: " + p);
            //Exception in thread "Thread-2" java.lang.IllegalStateException: Could not insert point: java.awt.Point[x=339,y=301]
        }
        return inserted;
    }
    
    
    //To be fixed
    private void legalizeEdge(DelaunayPoint p, DelaunayPoint e1, DelaunayPoint e2){
        DelaunayTriangle targetNeighbour = getEdgeNeighbour(e1, e2);
        if(targetNeighbour == null){
            return;
        }
        DelaunayPoint targetPoint = null;
        for(DelaunayPoint d: targetNeighbour.points){
            if(!d.equals(e1) && !d.equals(e2)){
                targetPoint = d;
            }
        }
        double[] c = circumCenter();
        double r = radius();
        if(targetPoint.distance(c[0], c[1]) < r){
            
            int numChild = 2;
            DelaunayPoint[] edge = new DelaunayPoint[] {e1, e2};
            
            for(int i = 0; i < numChild; i++){
                
                this.children[i] = new DelaunayTriangle(new DelaunayPoint[] {
                    p, edge[i], targetPoint
                    });
                
            }
            
            DelaunayTriangle pNeigh;
            DelaunayTriangle tNeigh;
            for(int i = 0; i < numChild; i++){
                
                targetNeighbour.children[i] = this.children[i]; 
                
                this.children[i].parents[0] = this;   
                this.children[i].parents[1] = targetNeighbour;   
                
                //Set children's neighbours
                tNeigh = targetNeighbour.getEdgeNeighbour(edge[i], targetPoint);
                pNeigh = this.getEdgeNeighbour(p, edge[i]);
                this.children[i].neighbours = new DelaunayTriangle[] {
                    this.children[(i+1)%numChild], 
                    tNeigh, 
                    pNeigh
                };
                if(tNeigh != null){
                    tNeigh.neighbours[tNeigh.getEdgeNeighbourIndex(edge[i], targetPoint)] = this.children[i];
                }
                if(pNeigh !=null){
                    pNeigh.neighbours[pNeigh.getEdgeNeighbourIndex(p, edge[i])] = this.children[i];
                }
            }
            
            
            for(int i = 0; i < numChild; i++){
                
                this.children[i].legalizeEdge(p, targetPoint, edge[i]);
            }
            
        }
    }
    
    protected DelaunayTriangle getEdgeNeighbour(DelaunayPoint e1, DelaunayPoint e2){
        int i = getEdgeNeighbourIndex(e1,e2);
        if(i == -1){
            return null;
        }
        return this.neighbours[i];
    }
    
    protected int getEdgeNeighbourIndex(DelaunayPoint e1, DelaunayPoint e2){
        
        boolean has1, has2;
        int nullIndex = -1;
        for(int i = 0; i < this.neighbours.length; i++){
            if(this.neighbours[i] != null){
                has1 = false;
                has2 = false;
                for(DelaunayPoint p: this.neighbours[i].points){
                    if(p.equals(e1)){
                        has1 = true;
                    }
                    if(p.equals(e2)){
                        has2 = true;
                    }
                }
                if(has1 && has2){
                    return i;
                }
            } else nullIndex = i;
        }
        return nullIndex;
    }
    
    //TO DO: Use fancy deletion procedure
    protected void delete(Point p){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //TO DO: Add full delete
    protected void deleteLeafPoint(Point p){
        if(contains(p)){
            
            for(DelaunayTriangle t: this.children){
                if(t != null){
                    t.deleteLeafPoint(p);
                }
            }
            
            if(hasPoint(p)){
                for(DelaunayTriangle t: this.parents){
                    if(t != null){
                        //Reset parent children, fix later for middle deletes
                        for(int i = 0; i < t.children.length; i++){
                            if(t.children[i] != null){
                                if(t.children[i].equals(this)){
                                    t.children[i] = null;
                                }
                            }
                        }
                
                        //Correct parent neighbours
                        DelaunayPoint[] sharedEdge = new DelaunayPoint[2];
                        DelaunayTriangle sharedNeighbour;
                        int edgeIndex = 0;
                        for(DelaunayPoint d: this.points){
                            if(t.hasPoint(d)){
                                sharedEdge[edgeIndex] = d;
                                edgeIndex = 1;
                            }
                        }
                        sharedNeighbour = this.getEdgeNeighbour(sharedEdge[0], sharedEdge[1]);
                        t.neighbours[t.getEdgeNeighbourIndex(sharedEdge[0], sharedEdge[1])] = sharedNeighbour;
                    
                        //Correct neighbour neighbours
                        if(sharedNeighbour != null){
                            int sNeighIndex = sharedNeighbour.getEdgeNeighbourIndex(sharedEdge[0], sharedEdge[1]);
                            if(sharedNeighbour.neighbours[sNeighIndex].equals(this)){
                                sharedNeighbour.neighbours[sNeighIndex] = t;
                            }
                        }
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
    
    public TreeSet<DelaunayTriangle> getLeaves(){
        TreeSet result = new TreeSet<>();
        if(this.children[0] == null){
            result.add(this);
        }
        else {
            for(DelaunayTriangle t: this.children){
                if(t != null){
                    result.addAll(t.getLeaves());
                }
            }
        }
        return result;
    }
    
    public double[] getVoronoiVertex(){
        if(isSymbolic()){
            //return new double[2];
        }
        return circumCenter();
    }
    
    public boolean isSymbolic(){
        return symbolicDegree() > 0;
    }
    
    public int symbolicDegree(){
        int r = 0;
        for(DelaunayPoint p: this.points){
            if(p.isSymbolic()){
                r++;
            }
        }
        return r;
    }
    
    public boolean isRoot(){
        return symbolicDegree() == 3;
    }
    
    public boolean hasPoint(Point p){
        
        boolean hasPoint = false;
        for(DelaunayPoint d: this.points){
            hasPoint = hasPoint || (p.getX() == d.getX() && p.getY() == d.getY());
        }
        return hasPoint;
    }
    
    protected boolean contains(Point p){
        if(hasPoint(p)){
            return true;
        }
        boolean result = true;
        int numPoints = this.points.length; //Should be 3
        Point base;
        Point edge;
        Point opposite;
        double py, oy;
        double a, b;
        for(int i = numPoints - 1; i >= 0; i--){
            base = this.points[i];
            edge = new Point((int) this.points[i].getX() - (int) this.points[(i+1)%numPoints].getX(), 
                (int) this.points[i].getY() - (int) this.points[(i+1)%numPoints].getY());
            opposite = this.points[(i+2)%numPoints]; 
                    
            if(edge.getX() == 0){
                if(p.getX() < base.getX()){
                    py = -1;
                } else {
                    py = 1;
                }
                if(opposite.getX() < base.getX()){
                    oy = -1;
                } else {
                    oy = 1;
                }
        
                
            } else{
                a = edge.getY()/edge.getX();
                b = base.getY() - edge.getY()*(base.getX()/edge.getX());
            
                py = p.getY() - (b + a*p.getX());
                oy = opposite.getY() - (b + a*opposite.getX());  
            }
            
            
            
            if(Math.signum(py) != Math.signum(oy) && py != 0 && oy != 0){
                result = false;
            }
        }
        return result;
    }
    
    public double[] circumCenter(){
        double[] result = new double[2];
        
        double[] px = new double[2]; //Position vectors
        double[] py = new double[2]; //Position vectors
        px[0] = (this.points[0].getX() + this.points[1].getX())/2;
        py[0] = (this.points[0].getY() + this.points[1].getY())/2;
        px[1] = (this.points[1].getX() + this.points[2].getX())/2;
        py[1] = (this.points[1].getY() + this.points[2].getY())/2;
        
        double[] dx = new double[2]; //Direction vectors
        double[] dy = new double[2]; //Direction vectors
        dy[0] = this.points[1].getX() - px[0];
        dx[0] = py[0] - this.points[1].getY();
        dy[1] = this.points[1].getX() - px[1];
        dx[1] = py[1] - this.points[1].getY();
        
        if(dx[0] != 0 && dx[1] != 0){
            double a1, b1, a2, b2; //Values for functions yi = ai*x + bi
            a1 = dy[0]/dx[0];
            b1 = py[0] - px[0]*a1;
            a2 = dy[1]/dx[1];
            b2 = py[1] - px[1]*a2;
        
            double af, bf; //Values for the combined function
            af = a1-a2;
            bf = b1-b2;
            result[0] = -bf/af;
            result[1] = b1 + a1*result[0];
        } else{
            int z = -1;
            for(int i = 0; i < 2; i++){
                if(dx[i] == 0){
                    z = i;
                }
            }
            double af, bf;
            af = dy[(z+1)%2]/dx[(z+1)%2];
            bf = py[(z+1)%2] - px[(z+1)%2]*af;
            result[0] = px[z];
            result[1] = bf + af*result[0];
        }
        return result;
    }
    
    public double radius(){
        double[] center = circumCenter();
        return points[0].distance(center[0], center[1]);
    }

    @Override
    public int compareTo(Object t) {
        int totalX1 = 0, totalY1 = 0;
        for(DelaunayPoint p: this.points){
            totalX1 += (int) p.getX();
            totalY1 += (int) p.getY();
        }
        if((t instanceof DelaunayTriangle)){
            DelaunayTriangle triangle = (DelaunayTriangle) t;
            int totalX2 = 0, totalY2 = 0;
            for(DelaunayPoint p: triangle.points){
                totalX2 += (int) p.getX();
                totalY2 += (int) p.getY();
            }
            if(totalX1 == totalX2){
                if(totalY1 == totalY2){
                    return 0;
                } else {
                    return totalY1 - totalY2;
                }
            } else {
                return totalX1 - totalX2;
            }
        }
        return -1;
    }
    
}
