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
import voronoigame.model.Cell;
import voronoigame.view.VoronoiDiagram;

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
    
    VoronoiFacade(ArrayList<Point> stillPoints, ArrayList<Point> movingPoints){
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
            this.root.insert(p);
        }
    }
    
    private void insertMovingPoints(){
        
        for(int i = 0; i < this.movingPoints.size(); i++){
            this.root.insert(this.movingPoints.get(i));
        }
    }
    
    private void deleteMovingPoints(){
        
        for(int i = this.movingPoints.size() - 1; i <= 0; i--){
            this.root.delete(this.movingPoints.get(i));
        }
    }

    @Override
    public Collection<Point> getSites() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Point> getVoronoiVertices() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Point[]> getVoronoiEdges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<Point> getFaceFromSite(Point site) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cell getCellFromSite(Point site) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveSite(Cell cell, Point newSiteLocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
