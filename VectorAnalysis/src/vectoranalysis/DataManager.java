/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectoranalysis;

import NDL_JavaClassLib.*;
import ij.*;
/**
 *
 * @author balam
 */
public class DataManager {

    /**
     * @return the timeData
     */
    public DataTrace_ver_3[] getTimeData() {
        return timeData;
    }

    /**
     * @param timeData the timeData to set
     */
    public void setTimeData(DataTrace_ver_3[] timeData) {
        this.timeData = timeData;
    }

    /**
     * @return the velocity
     */
    public DataTrace_ver_3[] getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity[] to set
     */
    public void setVelocity(DataTrace_ver_3[] velocity) {
        this.velocity = velocity;
    }

    /**
     * @return the XRes: the resolution in 'x' dimension (width) of the image
     */
    public int getXRes() {
        return XRes;
    }

    /**
     * @param XRes  Use this to set the resolution in 'x' dimension (width) of the image
     */
    public void setXRes(int XRes) {
        this.XRes = XRes;
    }

    /**
     * @return the YRes: the resolution in 'y' dimension (height) of the image
     */
    public int getYRes() {
        return YRes;
    }

    /**
     * @param YRes Use this to set the resolution in 'x' dimension (width) of the image
     */
    public void setYRes(int YRes) {
        this.YRes = YRes;
    }
    
    String [] DataFileNames;
    int fileCount;
    private DataTrace_ver_3[] timeData;
    private DataTrace_ver_3[] velocity;
    ImagePlus[] heatMap,velMapX,velMapY,velcmpMapX,velcmpMapY,diffXMap,diffYMap,divMap;
    boolean dataReady = false;
    
    private int XRes;   
    private int YRes;
   
    /***
     * Call this function to read the data that is present in the files present in DataManger.DataFile array of this class.
     * The data is supposed to be in the format of x and y co-ordinates listed in a time series stored as text(ascii) file.
     * (i.e. x1 \t y1 \n x2 \t y2\n....EOF). x1,y1 co -respond to co-ordinates at time t1, x2, y2 at time t2.
     * tn+1 is the time sample immediately after tn of an uniformly sampled data. Once read these data are stored in 
     * the internal data structure DataTrace.
     */
    void readData(){
        
        setTimeData(new DataTrace_ver_3[DataFileNames.length]);
        for (String curFile  : DataFileNames){
                var newData = new DataTrace_ver_3();
                newData.populateData(curFile);
//                
                
        }
    }
    void createresidencetimeMap(){
        
    }   
    void createVxmap(){
    }
    void createVymap(){
        
    }
  }
