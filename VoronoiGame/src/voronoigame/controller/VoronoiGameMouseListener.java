/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voronoigame.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import voronoigame.model.Cell;
import voronoigame.model.MoveableCell;
import voronoigame.view.Util;
import voronoigame.view.VoronoiPanel;

/**
 *
 * @author Guus van Lankveld
 */
public class VoronoiGameMouseListener implements MouseListener, MouseMotionListener
{
    private final VoronoiPanel voronoiPanel;
    
    VoronoiGameMouseListener(VoronoiPanel voronoiPanel)
    {
        this.voronoiPanel = voronoiPanel;
    }
    
    @Override
    public void mouseDragged(MouseEvent me)
    {
        if (this.voronoiPanel.dragging == null)
        {
            return;
        }
       
        voronoiPanel.getVoronoiDiagram().moveSite(this.voronoiPanel.dragging, me.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent me)
    {
        for (Point site : this.voronoiPanel.getVoronoiDiagram().getSites())
        {
            Point cursorLocation = me.getPoint();
            Cell cell = this.voronoiPanel.getVoronoiDiagram().getCellFromSite(site);
            if (Util.isInCircle(cursorLocation, site, VoronoiPanel.SITE_RADIUS)
                    && cell.getClass() == MoveableCell.class)
            {
                this.voronoiPanel.hover = cell;
                this.voronoiPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return;
            }
        }
        this.voronoiPanel.hover = null;
        this.voronoiPanel.setCursor(Cursor.getDefaultCursor());
    }
    
    @Override
    public void mouseClicked(MouseEvent me)
    {
    }

    @Override
    public void mousePressed(MouseEvent me)
    {
        if (this.voronoiPanel.hover == null)
        {
            return;
        }
        this.voronoiPanel.dragging = this.voronoiPanel.hover;
    }

    @Override
    public void mouseReleased(MouseEvent me)
    {
        this.voronoiPanel.dragging = null;
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
    }
    
}
