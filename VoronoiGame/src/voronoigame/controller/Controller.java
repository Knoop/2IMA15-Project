/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.controller;

import java.awt.Point;
import java.util.Map;
import voronoigame.model.Cell;
import voronoigame.model.GameState;
import voronoigame.view.MainView;
import voronoigame.view.VoronoiDiagram;
import voronoigame.view.VoronoiPanel;
import voronoigame.view.VoronoidiagramDummyImpl;

/**
 *
 * @author Maurice
 */
public class Controller {

    private final MainView mainView;
    private final GameState gameState;

    public Controller(Map<Point, Cell.Type> cellTypes) {
        this.mainView = new MainView();
        this.gameState = new GameState(cellTypes, this.makeVoronoiDiagram());
        this.mainView.setVoronoiPanel(this.makePanel());
        this.mainView.setVisible(true);
    }

    /**
     * Creates a new VoronoiPanel using the given VoronoiDiagram.
     *
     * @param voronoiDiagram The VoronoiDiagram for which a panel should be
     * created
     * @return The created panel.
     */
    private VoronoiPanel makePanel() {
        VoronoiPanel panel = new VoronoiPanel(this.gameState);
        VoronoiGameMouseListener mouseListener = new VoronoiGameMouseListener(this.gameState, panel);
        panel.addMouseListener(mouseListener);
        panel.addMouseMotionListener(mouseListener);
        return panel;
    }

    private VoronoiDiagram makeVoronoiDiagram() {
        return new VoronoidiagramDummyImpl();
    }
}
