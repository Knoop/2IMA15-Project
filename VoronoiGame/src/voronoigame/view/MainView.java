/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
    
    private void setContent(final ContentPanel panel){
        // If there is an existing panel, remove it
         SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                // Out with the old
                MainView.this.pmContentContainer.setVisible(false);
                if (MainView.this.contentPanel != null) {
                    MainView.this.pmContentContainer.remove(MainView.this.contentPanel);
                    ContentPanel removed = MainView.this.contentPanel;
                    MainView.this.contentPanel = null;
                    removed.notifyPanelRemoved();
                }
                
                // In with the new
                MainView.this.contentPanel = panel;
                MainView.this.pmContentContainer.add(MainView.this.contentPanel);
                MainView.this.contentPanel.notifyPanelAdded();
                MainView.this.pmContentContainer.setVisible(true);
            }
        });
        
    }
    
    public ContentPanel getContent() {
        return this.contentPanel;
    }

    
    void onLevelSelected(int level){
        System.out.println("A level was selected: ");
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
        this.setContent(new GamePanel(gameState, this));
    }

    public void showFailedToLoadLevel(File level, int exceptionToCause) {
        this.showLoading();
        System.out.println("Showing level loading failure");
        this.showSelectLevel();
        // Also create dialog
    }
    
    public void showNoMoreLevels() {
        System.out.println("Showing that no more levels exist");
        this.showDialogAsync(new DialogRequest(
                "Game over",
                "You've played the last level",
                "start over",
                "stop"){

                @Override
                void onChoiceSelected(int choice) {
                    if(choice == 0)
                        MainView.this.controller.onLevelSelected(0);
                    else if(choice == 1)
                        MainView.this.controller.endLevel();
                    }

                });
    }

    public void showLevelCompleted(final GameState gameState) {
        System.out.println("Showing that the level is finished");

        this.showDialogAsync(new DialogRequest(
                gameState.hasWon()? "Level completed":"Level failed",
                gameState.hasWon()?"You've won!":"You've lost!",
                gameState.hasWon()?"continue":"retry",
                "stop"){

                @Override
                void onChoiceSelected(int choice) {
                    if(choice == 0 && gameState.hasWon())
                        MainView.this.controller.nextLevel();
                    else if(choice == 0 && !gameState.hasWon())
                        MainView.this.controller.restart();
                    else if(choice == 1)
                        MainView.this.controller.endLevel();
                    }

                });
    }


    void endLevel() {
        this.controller.endLevel();
    }

    void nextLevel() {
        this.controller.nextLevel();
    }


    /**
     * Shows the dialog that is created from the DialogRequest.
     * @param request The request that indicates how the dialog
     * should be displayed and that will handle the result from the dialog.
     */
    private void showDialog(DialogRequest request){
        request.onChoiceSelected(JOptionPane.showOptionDialog(this,
            request.text,
            request.title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,     //do not use a custom Icon
            request.options,  //the titles of buttons
            request.options[0]) //default button title
        );
    }

    /**
     * Shows the dialog that is created from the DialogRequest.
     * This is done asynchronous.
     * @param request The request that indicates how the dialog should be
     * displayed and that will handle the result from the dialog.
     */
    private void showDialogAsync(final DialogRequest request){
        (new Thread(new Runnable(){
            @Override
            public void run() {
                showDialog(request);
            }
        })).start();
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

    /**
     * Request for showing a dialog. This makes it possible to let dialogs be
     * handled both synchronous as well as asynchronous.
     */
    private static abstract class DialogRequest {
        private final String title;
        private final String text;
        private final String[] options;

        private DialogRequest(String title, String text, String...options){
            this.title = title;
            this.text = text;
            this.options = options;
        }

        abstract void onChoiceSelected(int choice);
    }
}
