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
public class DelaunayPoint extends Point implements Comparable{

    private boolean symbolic;
    
    DelaunayPoint(int x, int y, boolean sym){
        super(x,y);
        this.symbolic = sym;
    }
    
    public boolean isSymbolic(){
        return this.symbolic;
    }

    @Override
    public int compareTo(Object t) {
        if(t instanceof DelaunayPoint){
            DelaunayPoint p = (DelaunayPoint) t;
            if(x == p.x){
                if(y == p.y){
                    return 0;
                } else {
                    return y - p.y;
                }
            } else {
                return x - p.x;
            }
        }
        return -1;
    }
    
}
