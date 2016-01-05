/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model.delaunay;

import java.awt.Point;

/**
 *
 * @author leo
 */
public class DelaunayPoint extends Point {

    private boolean symbolic;
    
    DelaunayPoint(int x, int y, boolean sym){
        super(x,y);
        this.symbolic = sym;
    }
    
    public boolean isSymbolic(){
        return this.symbolic;
    }
    
}
