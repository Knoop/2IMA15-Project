/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view.voronoi;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import voronoigame.model.GameState;

/**
 *
 * @author Guus van Lankveld
 */
public class VoronoiPanel extends JPanel {

    public static final int SITE_RADIUS = 4;
    private final VoronoiPainter painter;
    private final GameState gameState;

    public VoronoiPanel(GameState gameState) {
        this.painter = new VoronoiPainter();
        this.gameState = gameState;
    }
    
    
    public void updatePanel() {
        repaint();
    }    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.painter.paint((Graphics2D)g, this.gameState, WIDTH, HEIGHT);
    }
}
