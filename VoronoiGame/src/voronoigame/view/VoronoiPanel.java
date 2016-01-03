/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voronoigame.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import voronoigame.model.Cell;
import voronoigame.model.StationaryCell;

/**
 *
 * @author Guus van Lankveld
 */
public class VoronoiPanel extends JPanel
{
    private static final int FRAMERATE = 60;
    
    public static final int SITE_RADIUS = 4;
    private static final Stroke EDGE_STROKE = new BasicStroke(1);
    
    private VoronoiDiagram voronoiDiagram;
    private Boolean updateNeeded;
    private final Timer renderTimer;
    
    public Cell dragging;
    public Cell hover;
    
    private final TimerTask renderStep = new TimerTask()
        {
            @Override
            public void run()
            {
                if (!updateNeeded)
                {
                    return;
                }
                updatePanel();
            }
        };

    public VoronoiDiagram getVoronoiDiagram()
    {
        return voronoiDiagram;
    }
    
    public VoronoiPanel(VoronoiDiagram voronoiDiagram)
    {
        this.voronoiDiagram = voronoiDiagram;
        this.updateNeeded = true;
        this.renderTimer = new Timer();
        this.renderTimer.scheduleAtFixedRate(this.renderStep, 0, 1000/FRAMERATE);
        this.dragging = null;
        this.hover = null;
        
        this.addMouseListener(new VoronoiGameMouseListener(this));
        this.addMouseMotionListener(new VoronoiMouseMotionListener(this));
    }
    
    public void updatePanel()
    {
        repaint();
        updateNeeded = false;
    }
    
    public void updateSize()
    {
        super.setSize(this.getParent().getSize());
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        for (Point site : this.voronoiDiagram.getSites())
        {
            LinkedList<Point> face = this.voronoiDiagram.getFaceFromSite(site);
            Polygon polygon = new Polygon();
            while (!face.isEmpty())
            {
                Point vertex = face.pop();
                polygon.addPoint(vertex.x, vertex.y);
            }
            g2.setColor(Util.getColorforCell(voronoiDiagram.getCellFromSite(site)));
            g2.fillPolygon(polygon);
            
            g2.setColor(Color.BLACK);
            g2.fillOval(site.x - SITE_RADIUS, site.y - SITE_RADIUS, SITE_RADIUS*2, SITE_RADIUS*2);
        }
        
        for (Point[] edge : voronoiDiagram.getVoronoiEdges())
        {
            g2.setColor(Color.BLACK);
            g2.setStroke(EDGE_STROKE);
            g2.drawLine(edge[0].x, edge[0].y, edge[1].x, edge[1].y);
        }
    }
}
