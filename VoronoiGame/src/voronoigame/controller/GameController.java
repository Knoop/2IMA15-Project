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
import java.util.logging.Level;
import voronoigame.model.Cell;
import voronoigame.model.MoveableCell;
import voronoigame.Util;
import voronoigame.model.FocusType;
import voronoigame.model.GameState;
import voronoigame.view.voronoi.VoronoiPanel;

/**
 *
 * @author Guus van Lankveld
 */
public class GameController implements MouseListener, MouseMotionListener {

    private final GameState gameState;
    
    private final CursorState cursorState;

    private final GameRunner runner;

    public GameController(GameState gameState) {
        this.gameState = gameState;
        this.cursorState = new CursorState();
        this.runner = new GameRunner();
        this.runner.start();
    }
    
    private void performStep(long interval) {
        if (this.cursorState.focus != null && this.cursorState.focus.getFocusType() != FocusType.NONE && this.cursorState.location != null )
            synchronized(this.gameState){
                this.gameState.moveTowards(this.cursorState.focus, this.cursorState.location, interval);
            }
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        this.cursorState.drag(me.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        for (Point site : this.gameState.getDiagram().getSites()) {
            Point cursorLocation = me.getPoint();
            Cell cell = this.gameState.getCell(site);
            if (Util.isInCircle(cursorLocation, site, VoronoiPanel.SITE_RADIUS)
                    && cell instanceof MoveableCell) {
                this.cursorState.setFocus(cell, FocusType.HOVER);
                return;
            }
        }
        this.cursorState.clearFocus();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        this.cursorState.drag(me.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        this.cursorState.clearFocus();
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        this.runner.resume();
    }

    @Override
    public void mouseExited(MouseEvent me) {
        this.runner.pause();
    }

    public void stop() {
        this.runner.stop = true;
    }
    

    private class GameRunner implements Runnable{

        private static final int MAX_REFRESH_RATE = 960;
        private static final long MIN_REFRESH_INTERVAL = 1000 / MAX_REFRESH_RATE;
        private boolean run = false, stop = false;
        private Thread runner;

        private long last;
        
        @Override
        public void run(){
            long time;
            while(this.run && !this.stop){
                // Update the timer and calculate the elapsed time
                time = this.last;
                this.last = System.currentTimeMillis();
                time = this.last - time;
                
                // Perform a step for the elapsed time
                GameController.this.performStep(time);
                
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
                this.last = System.currentTimeMillis();
                this.runner = new Thread(this);
                this.runner.start();
            }
        }
    }

    private class CursorState {

        /**
         * The location of the cursor in the world
         */
        private Point location;

        /**
         * The cell that is currently in focus, or null if there is no such cell. 
         */
        private Cell focus = null;

        private void clearFocus(){
 
            if(this.focus == null)
                return;

            this.focus.setFocusType(FocusType.NONE);
            this.focus = null;
            this.location = null;
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

        /**
         * Turns the focus of the current cell into a drag type focus if it was a hover type focus.
         */
        private void drag(Point point){
            if (this.focus != null && this.focus.getFocusType() == FocusType.HOVER)
                this.setFocus(this.focus, FocusType.DRAG);  
            this.location = point;
        }
    }
}