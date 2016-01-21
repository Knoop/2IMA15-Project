/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import javax.swing.JPanel;

/**
 *
 * @author Maurice
 */
public abstract class ContentPanel extends JPanel{
    
    protected final MainView parent;
    
    private boolean isCurrentlyActive = false;
    
    protected ContentPanel(MainView parent){ 
        this.parent = parent;
    }
    
    void notifyPanelAdded(){
        this.isCurrentlyActive = true;
        super.setSize(this.getParent().getSize());
        this.onPanelAdded();
        this.setVisible(true);
    }
    
    void notifyPanelRemoved(){
        this.isCurrentlyActive = false;
        this.setVisible(false);
        this.onPanelRemoved();
    }
    protected abstract void onPanelAdded();
    protected abstract void onPanelRemoved();
    
    
    protected boolean currentlyActive() {
        return this.isCurrentlyActive;
    }
}
