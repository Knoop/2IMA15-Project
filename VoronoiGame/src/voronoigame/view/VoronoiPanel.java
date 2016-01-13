/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import voronoigame.controller.GameController;
import voronoigame.model.GameState;

/**
 *
 * @author Guus van Lankveld
 */
public class VoronoiPanel extends ContentPanel {

    public static final int SITE_RADIUS = 4;
    private GameController gameController;
    private final VoronoiPainter painter;
    private final GameState gameState;

    public VoronoiPanel(GameState gameState, MainView parent) {
        super(parent);
        this.painter = new VoronoiPainter();
        this.gameState = gameState;
        this.useGameController(new GameController(gameState, this));
    }
    
    private void useGameController(GameController gameController) {
        this.gameController = new GameController(gameState, this);
        this.addMouseListener(gameController);
        this.addMouseMotionListener(gameController);
    }
    
    public void updatePanel() {
        repaint();
    }    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.painter.paint((Graphics2D)g, this.gameState, WIDTH, HEIGHT);
    }


    @Override
    protected void onPanelAdded() { }

    @Override
    protected void onPanelRemoved() { }

}
