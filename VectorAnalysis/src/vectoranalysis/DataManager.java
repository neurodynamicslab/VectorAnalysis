/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectoranalysis;

import java.io.File;
import NDL_JavaClassLib.*;
import ij.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author balam
 */
public class DataManager {
    
    File [] DataFile;
    int fileCount;
    DataTrace_ver_inwrks timeData[], velocity[];
    ImagePlus heatMap,velMapX,velMapY,velcmpMapX,velcmpMapY,diffXMap,diffYMap,divMap;
    boolean dataReady = false;
   
    void readData(){
        FileReader fileReader ;
        for (File curFile  : DataFile){
              
            if(curFile.exists()){
                try {
                    fileReader = new FileReader(curFile);
                } catch (FileNotFoundException ex) {
                    //Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                    javax.swing.JOptionPane.showMessageDialog(null, "could not open the datad");
                }
            }
                
        }
    }
    void createresidencetimeMap(){
        
    }   
    void createVxmap(){
    }
    void createVymap(){
        
    }
  }
