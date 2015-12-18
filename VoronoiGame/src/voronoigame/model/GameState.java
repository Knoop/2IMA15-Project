/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.model;

import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Maurice
 */
public class GameState {
    
    private final List<Cell> cells = new ArrayList<>();
    
    public GameState(Collection<Cell> cells){
        this.cells.addAll(cells);
    }
    
    
    
    /**
     * Create a GameState from a Reader.
     * @param reader
     * @return 
     */
    public static GameState from(Reader reader){
        return null;
    }
    
    
}
