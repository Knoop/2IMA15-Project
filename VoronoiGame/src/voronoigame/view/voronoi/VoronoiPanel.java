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

    private final VoronoiPainter painter;
    private GameState gameState;
    
    public void setShowScore(boolean showScore)
    {
        this.painter.setShowScore(showScore);
    }

    public VoronoiPanel() {
        this(true);
    }
    
    public VoronoiPanel(boolean showScore) {
        System.out.println("New VoronoiPanel created");
        this.painter = new VoronoiPainter(showScore);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.gameState != null) {
            this.painter.paint((Graphics2D) g, this.gameState, WIDTH, HEIGHT);
        }
    }
}
