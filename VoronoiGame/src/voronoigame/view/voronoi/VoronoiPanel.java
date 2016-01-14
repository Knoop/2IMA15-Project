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
    private GameState gameState;

    public VoronoiPanel() {
        System.out.println("New VoronoiPanel created");
        this.painter = new VoronoiPainter();

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.repaint();
    }

    public void updatePanel() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.gameState != null) {
            this.painter.paint((Graphics2D) g, this.gameState, WIDTH, HEIGHT);
        }
    }
}
