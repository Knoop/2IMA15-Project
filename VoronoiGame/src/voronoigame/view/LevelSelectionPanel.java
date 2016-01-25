/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import voronoigame.model.GameState;

/**
 *
 * @author Maurice
 */
public class LevelSelectionPanel extends ContentPanel {

    /**
     * The possible files to choose from
     */
    private final File[] levels;

    private final GameState[] gameStateCache;

    /**
     * The index of the selected level
     */
    private int selected = 0;

    /**
     * Creates new form LevelSelectionPanel
     *
     * @param parent The parent that has created this panel
     * @param levels The levels from which the user can choose, represented as
     * files.
     */
    public LevelSelectionPanel(MainView parent, File[] levels) {
        super(parent);
        this.levels = levels;
        this.gameStateCache = new GameState[this.levels.length];
        initComponents();

        // Set the levels
        this.levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.levelList.setListData(this.getLevelNames());
        this.levelList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    LevelSelectionPanel.this.onLevelSelectionChanged(((JList) e.getSource()).getSelectedIndex());
                }
            }
        });

        // Force preview update
        this.levelList.setSelectedIndex(this.selected);
        this.onLevelSelectionChanged(this.selected);
        this.voronoiPreview.setShowScore(false);
    }

    /**
     * Called when the selected level has changed. This will show a preview for
     * the selected level.
     *
     * @param selectedIndex The index from the list of levels that has been
     * selected. This corresponds directly to the indices in the {@code levels}
     * array.
     */
    protected void onLevelSelectionChanged(int selectedIndex) {
        System.out.println("Selected level " + selectedIndex);
        this.selected = selectedIndex;
        // If it ain't cached, try to cache it
        if (this.gameStateCache[this.selected] == null) {
            try {
                this.gameStateCache[this.selected] = GameState.from(new InputStreamReader(new FileInputStream(levels[this.selected])));
            } catch (Exception e) {
            }
        }

        this.setPreview(this.gameStateCache[this.selected]);
    }

    private void setPreview(GameState gameState) {
        System.out.println("Updating preview. Showing level: " + (gameState != null));
        this.voronoiPreview.setGameState(gameState);
    }

    private String[] getLevelNames() {
        String[] levelNames = new String[levels.length];
        for (int i = 0; i < levels.length; ++i) {
            levelNames[i] = getDisplayName(levels[i]);
        }
        return levelNames;
    }

    /**
     * Determines the display name of a level file. The display name is the name
     * of the file without the extension of the file. F.I. level0.lvl becomes
     * level0.
     *
     * @param level The level file for which to obtain the display name
     * @return The display name of the given level file.
     */
    private String getDisplayName(File level) {
        return level.getName().substring(0, level.getName().lastIndexOf('.'));
    }

    @Override
    protected void onPanelAdded() {
    }

    @Override
    protected void onPanelRemoved() {
    }

    private int getSelectedLevelIndex(){
        return this.selected;
    }
    private File getSelectedLevel() {
        return levels[this.selected];
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        start = new javax.swing.JButton();
        levelList = new javax.swing.JList<>();
        voronoiPreview = new voronoigame.view.voronoi.VoronoiPanel();

        setLayout(new java.awt.BorderLayout());

        start.setText("Start");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });
        add(start, java.awt.BorderLayout.PAGE_END);

        levelList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        add(levelList, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout voronoiPreviewLayout = new javax.swing.GroupLayout(voronoiPreview);
        voronoiPreview.setLayout(voronoiPreviewLayout);
        voronoiPreviewLayout.setHorizontalGroup(
            voronoiPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        voronoiPreviewLayout.setVerticalGroup(
            voronoiPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        add(voronoiPreview, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        if (this.currentlyActive()) {
            parent.onLevelSelected(this.getSelectedLevelIndex());
        }
    }//GEN-LAST:event_startActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> levelList;
    private javax.swing.JButton start;
    private voronoigame.view.voronoi.VoronoiPanel voronoiPreview;
    // End of variables declaration//GEN-END:variables

}
