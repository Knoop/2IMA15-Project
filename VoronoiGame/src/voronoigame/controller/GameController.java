/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import voronoigame.model.Cell;
import voronoigame.model.MoveableCell;
import voronoigame.Util;
import voronoigame.model.FocusType;
import voronoigame.model.GameState;
import voronoigame.view.VoronoiPanel;

/**
 *
 * @author Guus van Lankveld
 */
public class GameController implements MouseListener, MouseMotionListener {

    private final GameState gameState;
    private final VoronoiPanel voronoiPanel;

    /**
     * The cell that is in focus.
     */
    private Cell focus;

    public GameController(GameState gameState, VoronoiPanel voronoiPanel) {
        this.gameState = gameState;
        this.voronoiPanel = voronoiPanel;
    }

    /**
     * Clears the current focus
     */
    private void clearFocus() {
        if(this.focus == null)
            return;

        this.focus.setFocusType(FocusType.NONE);
        this.focus = null;
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

    @Override
    public void mouseDragged(MouseEvent me) {
        if (this.focus != null && this.focus.getFocusType() != FocusType.NONE) {
            this.gameState.move(this.focus, me.getPoint());
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        for (Point site : this.gameState.getDiagram().getSites()) {
            Point cursorLocation = me.getPoint();
            Cell cell = this.gameState.getCell(site);
            if (Util.isInCircle(cursorLocation, site, VoronoiPanel.SITE_RADIUS)
                    && cell instanceof MoveableCell) {
                this.setFocus(cell, FocusType.HOVER);
                this.voronoiPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }
        }
        this.clearFocus();
        this.voronoiPanel.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (this.focus != null && this.focus.getFocusType() == FocusType.HOVER) {
            this.setFocus(this.focus, FocusType.DRAG);
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        this.clearFocus();
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
