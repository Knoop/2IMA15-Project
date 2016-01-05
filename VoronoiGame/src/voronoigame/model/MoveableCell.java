/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model;

import java.awt.Point;

/**
 *
 * @author Maurice
 */
public class MoveableCell extends Cell {

    public MoveableCell(Point point, float initArea) {
        super(point, initArea);
    }
 
    protected void updateLocation(Point point){
        
    }  
    
}
