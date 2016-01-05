/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.controller;

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

    public Controller() {
        this.mainView = new MainView();
        this.mainView.setVoronoiPanel(this.makePanel(this.makeVoronoiDiagram()));
        this.mainView.setVisible(true);
    }

    /**
     * Creates a new VoronoiPanel using the given VoronoiDiagram.
     *
     * @param voronoiDiagram The VoronoiDiagram for which a panel should be
     * created
     * @return The created panel.
     */
    private VoronoiPanel makePanel(VoronoiDiagram voronoiDiagram) {
        VoronoiPanel panel = new VoronoiPanel(voronoiDiagram);
        VoronoiGameMouseListener mouseListener = new VoronoiGameMouseListener(panel);
        panel.addMouseListener(mouseListener);
        panel.addMouseMotionListener(mouseListener);
        return panel;
    }

    private VoronoiDiagram makeVoronoiDiagram() {
        return new VoronoidiagramDummyImpl();
    }
}
