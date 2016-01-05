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
    private int boundX, boundY;
    private DelaunayTriangle root;
    
    final private int MARGIN = 1;
    
    VoronoiFacade(ArrayList<Point> stillPoints, ArrayList<Point> movingPoints){
        this.stillPoints = stillPoints;
        this.movingPoints = movingPoints;
        java.util.Collections.shuffle(this.stillPoints);
        java.util.Collections.shuffle(this.movingPoints);
        
        this.boundX = 0;
        this.boundY = 0;
        
        for(Point p: this.stillPoints){
            this.boundX = (int) Math.max(p.getX(), boundX);
            this.boundY = (int) Math.max(p.getY(), boundY);
        }
        for(Point p: this.movingPoints){
            this.boundX = (int) Math.max(p.getX(), boundX);
            this.boundY = (int) Math.max(p.getY(), boundY);
        }
        
        initializeDelaunay();
        insertMovingPoints();
    }
    
    private void initializeDelaunay(){
        
        DelaunayPoint[] rootpoints = {new DelaunayPoint(-boundX-MARGIN, -MARGIN, true), 
                new DelaunayPoint(boundX/2, 2*(boundY+2*MARGIN), true), 
                new DelaunayPoint(2*boundX+MARGIN, -MARGIN, true)};
        this.root = new DelaunayTriangle(rootpoints);
        
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
        
        for(int i = this.movingPoints.size(); i <= 0; i--){
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
