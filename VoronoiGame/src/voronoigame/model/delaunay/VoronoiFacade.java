/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model.delaunay;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;
import voronoigame.model.Cell;
import voronoigame.view.voronoi.VoronoiDiagram;

/**
 *
 * @author leo
 */
public class VoronoiFacade implements VoronoiDiagram {
    
    private ArrayList<Point> stillPoints;
    private ArrayList<Point> movingPoints;
    private Point bounds;
    private DelaunayTriangle root;
    
    final private int MARGIN = 1;
    
    public VoronoiFacade(ArrayList<Point> stillPoints, ArrayList<Point> movingPoints){
        this.stillPoints = stillPoints;
        this.movingPoints = movingPoints;
        
        this.bounds = new Point(0,0);
        
        for(Point p: this.stillPoints){
            this.bounds.x = (int) Math.max(p.getX(), this.bounds.x);
            this.bounds.y = (int) Math.max(p.getY(), this.bounds.y);
        }
        for(Point p: this.movingPoints){
            this.bounds.x = (int) Math.max(p.getX(), this.bounds.x);
            this.bounds.y = (int) Math.max(p.getY(), this.bounds.y);
        }
        this.bounds.x += 10;
        this.bounds.y += 10;
        
        initializeDelaunay();
    }
    
    VoronoiFacade(ArrayList<Point> stillPoints, ArrayList<Point> movingPoints, Point bounds){
        this.stillPoints = stillPoints;
        this.movingPoints = movingPoints;
        
        this.bounds = bounds;
        
        initializeDelaunay();
    }
    
    private void initializeDelaunay(){
        
        java.util.Collections.shuffle(this.stillPoints);
        java.util.Collections.shuffle(this.movingPoints);
        
        DelaunayPoint[] rootpoints = {new DelaunayPoint(-this.bounds.x-MARGIN, -MARGIN, true), 
                new DelaunayPoint(this.bounds.x/2, 2*(this.bounds.y+2*MARGIN), true), 
                new DelaunayPoint(2*this.bounds.x+MARGIN, -MARGIN, true)};
        this.root = new DelaunayTriangle(rootpoints);
        
        for(Point p: this.stillPoints){
            System.out.println("inserting " + p);
            this.root.insert(p);
        }
        insertMovingPoints();
    }
    
    private void insertMovingPoints(){
        
        for(int i = 0; i < this.movingPoints.size(); i++){
            this.root.insert(this.movingPoints.get(i));
        }
    }
    
    private void deleteMovingPoints(){
        
        for(int i = this.movingPoints.size() - 1; i >= 0; i--){
            this.root.deleteLeafPoint(this.movingPoints.get(i));
        }
    }

    @Override
    public Collection<Point> getSites() {
        Collection<Point> result = new ArrayList<>();
        result.addAll(this.stillPoints);
        result.addAll(this.movingPoints);
        return result;
    }
    
    public Collection<Point> getSiteNeighbours(Point site){
        TreeSet<DelaunayTriangle> leaves = this.root.findLeaves(site);
        TreeSet<Point> result = new TreeSet<>();
        if(leaves.isEmpty()){
            throw new IllegalArgumentException("Point lies outside the diagram: " + site);
        }
        for (DelaunayTriangle t: leaves){
            for(DelaunayPoint p: t.points){
                if(p.getX() != site.getX() && p.getX() != site.getX() && !p.isSymbolic()){
                    result.add(p);
                }
            }
        }
        
        return result;
    }

    @Override
    public Collection<Point> getVoronoiVertices() {
        TreeSet<DelaunayTriangle> leaves = root.getLeaves();
        ArrayList<Point> result = new ArrayList<>();
        double[] current;
        for(DelaunayTriangle t: leaves){
            current = determineBoundedVertex(t);
            if(current == null) continue;
            Point vertex = new Point((int) current[0], (int) current[1]);
            result.add(vertex);
        }
        result.add(new Point(0, 0));
        result.add(new Point((int) this.bounds.getX(), 0));
        result.add(new Point(0, (int) this.bounds.getY()));
        result.add(this.bounds);
        return result;
    }
    
    public double[] determineBoundedVertex(DelaunayTriangle t){
        double[] result = null;
        boolean useBounds = false;
        //useBounds, if set to true, trims all cells to the bounding box
        //Currently not correctly implemented, do not set to true.
        assert !useBounds;
        double[] tVertex = t.getVoronoiVertex();
        if(!isInBounds(new Point((int) tVertex[0], (int) tVertex[1])) && useBounds){
            double[] nullVertex = new double[] {-1,-1};
            double[] nVertex = nullVertex;
            for(DelaunayTriangle n: t.neighbours){
                if(n == null) continue;
                nVertex = n.getVoronoiVertex();
                if(isInBounds(new Point((int) nVertex[0], (int) nVertex[1]))){
                    
                    double[] direction = new double[] {tVertex[0] - nVertex[0], tVertex[1] - nVertex[1]};
                    double[] mul = new double[2];
                    for(int i = 0; i < 2; i++){
                        
                        if(direction[i] > 0){
                            
                            if(i == 0) mul[i] = (bounds.getX() - nVertex[i])/direction[i];
                            else mul[i] = (bounds.getY() - nVertex[i])/direction[i];
                            
                        }
                        else{
                            if(direction[i] != 0){
                                mul[i] = (-nVertex[i])/direction[i];
                            } else mul[i] = Double.MAX_VALUE;
                        }
                    }
                    result = new double[] {nVertex[0] + direction[0]*Math.min(mul[0], mul[1]),
                        nVertex[1] + direction[1]*Math.min(mul[0], mul[1])};
                    /*if(result[1] == 0){
                        System.out.println(result[0]);
                        System.out.println(tVertex[0]);
                    }*/
                    break;
                }
            }
            if(nVertex == nullVertex){
                /*for(DelaunayTriangle n: t.neighbours){
                    if(n == null || origin.equals(n)) continue;
                    result = determineBoundedVertex(n, t);
                    break;
                }*/
                return null;
            }
        }
        //Normal vertex procedure
        else {
            result = tVertex;
        }
        return result;
    }

    @Override
    public Collection<Edge> getVoronoiEdges() {
        
        TreeSet<DelaunayTriangle> leaves = root.getLeaves();
        ArrayList<Edge> result = new ArrayList<>();
        double[] current;
        double[] currentNeighbour;
        for(DelaunayTriangle t: leaves){
            current = determineBoundedVertex(t);
            if(current == null) continue;
            Point vertex = new Point((int) current[0], (int) current[1]);
            for(DelaunayTriangle n: t.neighbours){
                if(n != null){
                    currentNeighbour = determineBoundedVertex(n);
                    if(currentNeighbour == null) continue;
                    Point vertexNeighbour = new Point((int) currentNeighbour[0], (int) currentNeighbour[1]);
                    result.add(Edge.create(vertex, vertexNeighbour));
                }
            }
        }
        return result;
    }

    @Override
    public LinkedList<Point> getFaceFromSite(Point site) {
        TreeSet<DelaunayTriangle> leaves = this.root.findLeaves(site);
        LinkedList result = new LinkedList<>();
        
        if(leaves.isEmpty()){
            throw new IllegalArgumentException("Point lies outside the diagram: " + site);
        }

        DelaunayTriangle base = leaves.first();
        double[] xy = determineBoundedVertex(base);
        if(xy != null){
            result.add(new Point((int) xy[0], (int) xy[1]));
        }
        
        DelaunayPoint dSite = null;
        DelaunayPoint turnPoint = null;
        for(int i = base.points.length - 1; i >= 0; i--){
            if(base.points[i].getX() == site.getX() && base.points[i].getY() == site.getY()){
                dSite = base.points[i];
                turnPoint = base.points[(i+1)%base.points.length];
                break;
            }
        }
        if(dSite == null){
            throw new IllegalArgumentException("Point is not a site in the diagram: " + site);
        }
        DelaunayTriangle current = base.getEdgeNeighbour(dSite, turnPoint);
        
        while(!current.equals(base)){

            for(int i = current.points.length - 1; i >= 0; i--){
                if(!current.points[i].equals(dSite) && !current.points[i].equals(turnPoint)){
                    turnPoint = current.points[i];
                    break;
                }
            }
            
            xy = determineBoundedVertex(current);
            if(xy != null){
                result.add(new Point((int) xy[0], (int) xy[1]));
            }
            current = current.getEdgeNeighbour(dSite, turnPoint);
        }
        
        return result;
    }

    @Override
    public Cell getCellFromSite(Point site) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveSite(Point oldSiteLocation, Point newSiteLocation) {
        if(!isInBounds(newSiteLocation)){
            throw new IllegalArgumentException("Point out of bounds: " + newSiteLocation);
        }
        deleteMovingPoints();
        this.movingPoints.remove(oldSiteLocation);
        this.movingPoints.add(newSiteLocation);
        insertMovingPoints();
    }
    
    public boolean isInBounds(Point p){
        if(p.getX() < 0 || p.getY() < 0 ||
            p.getX() > this.bounds.getX() || 
            p.getY() > this.bounds.getY()){
            return false;
        }
        return true;
    }

    @Override
    public void removeSite(Point siteLocation) {
        this.stillPoints.remove(siteLocation);
        this.movingPoints.remove(siteLocation);
        initializeDelaunay();
    }

    @Override
    public int width() {
        return this.bounds.x;
    }

    @Override
    public int height() {
        return this.bounds.y;
    }
    
}
