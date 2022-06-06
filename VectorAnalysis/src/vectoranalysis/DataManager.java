/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectoranalysis;

import NDL_JavaClassLib.*;
import java.util.ArrayList;
/**
 * The current version is designed assuming the user would be doing the analysis of different 
 * groups outside of the program. The user groups the experimental files and runs the analysis in individual groups. 
 * Later versions will incorporate the experimental design. 
 * @author balam
 */
public class DataManager extends Object{

    /**
     * @return the velocityField
     */
    public JVectorSpace[] getVelocityField() {
        return velocityField;
    }

    /**
     * @return the accelarationField
     */
    public JVectorSpace[] getAccelarationField() {
        return accelarationField;
    }

    

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
    public DataTrace_ver_3[] getAccelaration(){
        return accelaration;
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
    private String inPath;
    private String outPath;
    int fileCount;
    private DataTrace_ver_3[] timeData;
    private DataTrace_ver_3[] velocity;
    private DataTrace_ver_3[] accelaration;
    private JVectorSpace[] velocityField, accelarationField;
    //ArrayList <ImageProcessor> heatMap,velMapX,velMapY,velcmpMapX,velcmpMapY,diffXMap,diffYMap,divMap;
    //ImageProcessor aveHMap,aveVelX,aveVelY,aveVelCmpX,aveVelCmpY,aveDiffX,aveDiffY,aveDiv;
    boolean dataReady = false;
    
    private int XRes;   
    private int YRes;
   
 /***
     * Call this function to read the data that is present in the files listed in DataManger.DataFile array of this class.
     * The data is supposed to be in the format of x and y co-ordinates listed in a time series stored as text(ascii) file.
     * (i.e. x1 \t y1 \n x2 \t y2\n....EOF). x1,y1 co -respond to co-ordinates at time t1, x2, y2 at time t2.
     * tn+1 is the time sample immediately after tn of an uniformly sampled data. Once read these data are stored in 
     * the internal data structure DataTrace.
     */
    public void readData(){
        
        setTimeData(new DataTrace_ver_3[DataFileNames.length]);
        for (String curFile  : DataFileNames){
                var newData = new DataTrace_ver_3();
                newData.populateData(curFile); 
        }
        computeAllFields();
    }
    void computeAllFields(){
        int dataCounter = 0;
        this.setVelocity(new DataTrace_ver_3[DataFileNames.length]);
        this.velocityField = new JVectorSpace[DataFileNames.length];
        
        
        for(DataTrace_ver_3 tseries : timeData){
           velocity[dataCounter] = tseries.differentiate(false);
           accelaration[dataCounter] = velocity[dataCounter].differentiate(false);
           
           int Idx = 0;
           ArrayList<JVector> accVectors = null;
           ArrayList<JVector> velVectors = null;
           ArrayList<OrdXYData> spaceVects = null;
           
           
           for(OrdXYErrData vel : velocity[dataCounter]){
               accVectors.add(new JVector(accelaration[dataCounter].get(Idx).getXY()));
               velVectors.add(new JVector(vel.getXY()));
               spaceVects.add(tseries.get(Idx));
               Idx++;
           }
           velocityField[dataCounter] =  new JVectorSpace(getXRes(),getYRes(),true,spaceVects,velVectors);
           accelarationField[dataCounter] = new JVectorSpace(getXRes(),getYRes(),true,spaceVects,accVectors); 
        }
    }
    /**
     * @return the inPath
     */
    public String getInPath() {
        return inPath;
    }

    /**
     * @param inPath the inPath to set
     */
    public void setInPath(String inPath) {
        this.inPath = inPath;
    }

    /**
     * @return the outPath
     */
    public String getOutPath() {
        return outPath;
    }

    /**
     * @param outPath the outPath to set
     */
    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }
  }
