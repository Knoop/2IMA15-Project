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
        java.util.Collections.shuffle(this.stillPoints);
        java.util.Collections.shuffle(this.movingPoints);
        
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
        insertMovingPoints();
    }
    
    VoronoiFacade(ArrayList<Point> stillPoints, ArrayList<Point> movingPoints, Point bounds){
        this.stillPoints = stillPoints;
        this.movingPoints = movingPoints;
        java.util.Collections.shuffle(this.stillPoints);
        java.util.Collections.shuffle(this.movingPoints);
        
        this.bounds = bounds;
        
        initializeDelaunay();
        insertMovingPoints();
    }
    
    private void initializeDelaunay(){
        
        DelaunayPoint[] rootpoints = {new DelaunayPoint(-this.bounds.x-MARGIN, -MARGIN, true), 
                new DelaunayPoint(this.bounds.x/2, 2*(this.bounds.y+2*MARGIN), true), 
                new DelaunayPoint(2*this.bounds.x+MARGIN, -MARGIN, true)};
        this.root = new DelaunayTriangle(rootpoints, this.bounds);
        
        for(Point p: this.stillPoints){
            System.out.println("inserting " + p);
            this.root.insert(p);
        }
    }
    
    private void insertMovingPoints(){
        
        for(int i = 0; i < this.movingPoints.size(); i++){
            this.root.insert(this.movingPoints.get(i));
        }
    }
    
    private void deleteMovingPoints(){
        
        for(int i = this.movingPoints.size() - 1; i >= 0; i--){
            this.root.delete(this.movingPoints.get(i));
        }
    }

    @Override
    public Collection<Point> getSites() {
        Collection<Point> result = new ArrayList<>();
        result.addAll(this.stillPoints);
        result.addAll(this.movingPoints);
        return result;
    }

    @Override
    public Collection<Point> getVoronoiVertices() {
        TreeSet<DelaunayTriangle> leaves = root.getLeaves();
        ArrayList<Point> result = new ArrayList<>();
        double[] current;
        for(DelaunayTriangle t: leaves){
            current = t.getVoronoiVertex();
            Point vertex = new Point((int) current[0], (int) current[1]);
            result.add(vertex);
        }
        return result;
    }

    @Override
    public Collection<Point[]> getVoronoiEdges() {
        
        TreeSet<DelaunayTriangle> leaves = root.getLeaves();
        ArrayList<Point[]> result = new ArrayList<>();
        double[] current;
        double[] currentNeighbour;
        for(DelaunayTriangle t: leaves){
            current = t.getVoronoiVertex();
            Point vertex = new Point((int) current[0], (int) current[1]);
            for(DelaunayTriangle n: t.neighbours){
                if(n != null){
                    currentNeighbour = n.getVoronoiVertex();
                    Point vertexNeighbour = new Point((int) currentNeighbour[0], (int) currentNeighbour[1]);
                    result.add(new Point[] {vertex, vertexNeighbour});
                }
            }
        }
        return result;
    }

    @Override
    public LinkedList<Point> getFaceFromSite(Point site) {
        TreeSet<DelaunayTriangle> leaves = this.root.findLeaves(site);
        LinkedList result = new LinkedList<>();
        if(leaves.size() < 1){
            result.add(root.points[0]);
            result.add(root.points[1]);
            result.add(root.points[2]);
            return result;
        }
        DelaunayTriangle base = leaves.first();
        double[] xy = base.getVoronoiVertex();
        result.add(new Point((int) xy[0], (int) xy[1]));
        
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
            result.add(root.points[0]);
            result.add(root.points[1]);
            result.add(root.points[2]);
            return result;
        }
        DelaunayTriangle current = base.getEdgeNeighbour(dSite, turnPoint);
        
        while(!current.equals(base)){

            for(int i = current.points.length - 1; i >= 0; i--){
                if(!current.points[i].equals(dSite) && !current.points[i].equals(turnPoint)){
                    turnPoint = current.points[i];
                    break;
                }
            }
            
            xy = current.getVoronoiVertex();
            result.add(new Point((int) xy[0], (int) xy[1]));
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
        deleteMovingPoints();
        this.movingPoints.remove(oldSiteLocation);
        this.movingPoints.add(newSiteLocation);
        insertMovingPoints();
    }
    
}
