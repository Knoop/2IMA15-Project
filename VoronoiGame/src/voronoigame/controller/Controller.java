/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voronoigame.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import voronoigame.Util;
import voronoigame.model.GameState;
import voronoigame.view.MainView;

/**
 *
 * @author Maurice
 */
public class Controller {

    private final MainView mainView;
    private GameState gameState;

    public Controller() {
        this.mainView = new MainView(this);
        this.mainView.showSelectLevel();
        this.mainView.setVisible(true);
        
    }
    
    public void onLevelSelected(File level){
        
        try{
            this.readLevel(level);
            this.mainView.showLevel(this.gameState);
        }catch(Exception e){
            this.mainView.showFailedToLoadLevel(level, Controller.exceptionToCause(e));
            e.printStackTrace();
        }
    }
    
    private void readLevel(File level) throws IOException{
        
        this.gameState = null;
        this.gameState = GameState.from(new InputStreamReader(new FileInputStream(level)));
    }
    
    public static final int CAUSE_NON_EXISTING_FILE = 1, 
                            CAUSE_FILE_COULD_NOT_BE_OPENED = 2, 
                            CAUSE_FILE_CONTAINS_ERRORS = 3, 
                            CAUSE_UNKNOWN = 4;
    
    private static int exceptionToCause(Exception e){
        
        if(e instanceof FileNotFoundException)
            return Controller.CAUSE_NON_EXISTING_FILE;
        if(e instanceof IOException)
            return Controller.CAUSE_FILE_COULD_NOT_BE_OPENED;
        if(e instanceof NullPointerException)
            return Controller.CAUSE_FILE_CONTAINS_ERRORS;
        
        return Controller.CAUSE_UNKNOWN;
        
        
        
    }

    public File[] getLevels() {
        return Util.LEVEL_FOLDER.listFiles(new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.contains(".") && name.substring(name.lastIndexOf('.')).equalsIgnoreCase(".lvl");
            }
        });
    }
}
