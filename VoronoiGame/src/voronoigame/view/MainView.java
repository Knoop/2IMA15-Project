/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.io.File;
import voronoigame.controller.Controller;
import voronoigame.model.GameState;

/**
 *
 * @author Guus van Lankveld
 */
public class MainView extends javax.swing.JFrame {
    
    private ContentPanel contentPanel;
    private final Controller controller;

    /**
     * Creates new form MainView
     * @param controller The controller that tells this view what to do. This Controller must be notified in case certain events occur.
     */
    public MainView(Controller controller) {
        initComponents();
        this.controller = controller;
    }

    /**
     * Removes the current ContentPanel from this frame.
     *
     * @return The removed ContentPanel, or null if there was none.
     */
    public ContentPanel removeContent() {
        
        this.pmContentContainer.setVisible(false);
        if (this.contentPanel != null) {
            this.pmContentContainer.remove(this.contentPanel);
            ContentPanel removed = this.contentPanel;
            this.contentPanel = null;
            removed.notifyPanelRemoved();
            return removed;
        } else {
            return null;
        }
    }
    
    private void setContent(ContentPanel panel){
        // If there is an existing panel, remove it
        this.removeContent();

        this.contentPanel = panel;
        this.pmContentContainer.add(this.contentPanel);
        this.contentPanel.notifyPanelAdded();
        this.pmContentContainer.setVisible(true);
    }
    
    public ContentPanel getContent() {
        return this.contentPanel;
    }

    
    void onLevelSelected(File level){
        System.out.println("A level was selected: "+level.getName());
        this.controller.onLevelSelected(level);
    }
    
    public void showLoading(){
        System.out.println("Showing loading");
        this.setContent(new LoadingPanel(this));
    }
    
    public void showSelectLevel() {
        this.showLoading();
        System.out.println("Showing level selection");
        this.setContent(new LevelSelectionPanel(this, this.controller.getLevels()));
    }

    public void showLevel(GameState gameState) {
        this.showLoading();
        System.out.println("Showing level");
        this.setContent(new VoronoiPanel(gameState, this));
    }

    public void showFailedToLoadLevel(File level, int exceptionToCause) {
        this.showLoading();
        System.out.println("Showing level loading failure");
        this.showSelectLevel();
        // Also create dialog
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmContentContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout pmContentContainerLayout = new javax.swing.GroupLayout(pmContentContainer);
        pmContentContainer.setLayout(pmContentContainerLayout);
        pmContentContainerLayout.setHorizontalGroup(
            pmContentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        pmContentContainerLayout.setVerticalGroup(
            pmContentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pmContentContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pmContentContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pmContentContainer;
    // End of variables declaration//GEN-END:variables

    

}
