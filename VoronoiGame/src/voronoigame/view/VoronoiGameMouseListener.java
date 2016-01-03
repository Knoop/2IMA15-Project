/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voronoigame.view;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Guus van Lankveld
 */
public class VoronoiGameMouseListener implements MouseListener
{
    private final VoronoiPanel voronoiPanel;
    
    public VoronoiGameMouseListener(VoronoiPanel voronoiPanel)
    {
        this.voronoiPanel = voronoiPanel;
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
