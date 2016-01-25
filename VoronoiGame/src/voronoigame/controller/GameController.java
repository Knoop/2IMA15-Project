/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import voronoigame.model.Cell;
import voronoigame.model.MoveableCell;
import voronoigame.Util;
import voronoigame.model.FocusType;
import voronoigame.model.GameState;
import voronoigame.model.GameState.MoveOperation;

/**
 *
 * @author Guus van Lankveld
 */
public class GameController implements MouseListener, MouseMotionListener {

    private final GameState gameState;

    private final GameRunner runner;

    /**
     * The location of the cursor in the world
     */
    private Point lastMouseLocation;

    /**
     * The cell that is currently in focus, or null if there is no such cell. 
     */
    private Cell focus = null;
    
    /**
     * The MoveOperation that must be used to update the location of the
     * selected cell in case the mouse is dragging. When no cell is selected
     * this will be null.
     */
    private MoveOperation currentMoveOperation;

    public GameController(GameState gameState) {
        this.gameState = gameState;
        this.runner = new GameRunner();
    }
    
    private void performStep() {
        System.out.println("this.lastMouseLocation:"+this.lastMouseLocation);
        if (this.currentMoveOperation != null && this.lastMouseLocation != null )
            this.currentMoveOperation.moveTowards(this.lastMouseLocation);
            
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        this.lastMouseLocation = me.getPoint();
        System.out.println("Moved mouse to "+me.getPoint());
        System.out.println("Focus: "+(this.focus == null? "none":this.focus.getPoint())+ " moveop: "+(this.currentMoveOperation != null));
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        Point cursorLocation = me.getPoint();
        for (Point site : this.gameState.getDiagram().getSites()) {
            Cell cell = this.gameState.getCell(site);
            if (Util.isInCircle(cursorLocation, site, Cell.NUCLEUS_RADIUS)
                    && cell instanceof MoveableCell) {
                this.setFocus(cell, FocusType.HOVER);
                return;
            }
        }
        this.clearFocus();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        this.runner.resume();
        this.lastMouseLocation = me.getPoint();
        if(this.focus != null)
        {
            System.out.println("Focus exists at "+me.getPoint()+" focus: "+this.focus.getPoint());
            this.currentMoveOperation = this.gameState.move(this.focus);
        }
        else
            System.out.println("No focus");
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        System.out.println("Clearing focus and move operation");
        this.clearFocus();
        this.runner.pause();
        this.currentMoveOperation.finish();
        this.currentMoveOperation = null;
        this.lastMouseLocation = null;
    }

    @Override
    public void mouseEntered(MouseEvent me) { }

    @Override
    public void mouseExited(MouseEvent me) { }

    public void stop() {
        this.runner.stop = true;
    }
    

    /**
     * The GameRunner updates the GameState every few milliseconds. It moves the
     * selected cell towards the cursor with every step. 
     */
    private class GameRunner implements Runnable{

        private static final int MAX_REFRESH_RATE = 960;
        private static final long MIN_REFRESH_INTERVAL = 1000 / MAX_REFRESH_RATE;
        private boolean run = false, stop = false;
        private Thread runner;
        
        @Override
        public void run(){
            long time;
            while(this.run && !this.stop){
                // Perform a step for the elapsed time
                time = -System.currentTimeMillis();
                GameController.this.performStep();
                time += System.currentTimeMillis();

                //System.out.println("Took "+time+"ms to perform step");
                // If the elapsed time is below the minimal refresh interval, sleep until the refresh rate is back to what it is supposed to be. 
                if(MIN_REFRESH_INTERVAL > 0 && MIN_REFRESH_INTERVAL > time)
                    try { Thread.sleep(MIN_REFRESH_INTERVAL - time); } 
                    catch (InterruptedException ex) { }
            }
            this.runner = null;
        }

        private void pause(){
            this.run = false;
        }

        private void resume(){
            if(!this.run && !this.stop){
                this.run = true;
                this.start();
            }
        }

        private void start(){
            if(this.run && this.runner == null){
                this.runner = new Thread(this);
                this.runner.start();
            }
        }
    }

    private void clearFocus(){

        if(this.focus == null)
            return;

        this.focus.setFocusType(FocusType.NONE);
        this.focus = null;
        this.lastMouseLocation = null;
    }

    /**
     * Sets the current focus on the given cell as the given focus type.
     *
     * @param focus
     * @param focusType
     */
    private void setFocus(Cell focus, FocusType focusType) {
        if (focusType == FocusType.NONE) {
            this.clearFocus();
        } else {
            System.out.println("Setting focus");
            this.focus = focus;
            this.focus.setFocusType(focusType);
        }
    }
}