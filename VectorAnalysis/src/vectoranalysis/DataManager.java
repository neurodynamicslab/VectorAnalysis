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
 * groups outside of this class. This class manages all the data files corresponding to one group
 * in a trial.
 * It is assumed that the user groups the experimental files and runs the analysis in individual groups. 
 * Later versions will/might incorporate the experimental design. 
 * Notionally the index refers to animal ID (#)aUID.
 * 
 * 
 * @author balam
 */
public class DataManager extends Object{

    /**
     * @param accelaration the accelaration to set
     */
    public void setAccelaration(DataTrace_ver_3[] accelaration) {
        this.accelaration = accelaration;
    }

    /**
     * @return the DataFileNames
     */
    public String[] getDataFileNames() {
        String [] fileNames = new String[DataFileNames.size()];
        int Count = 0;
        for(String name : DataFileNames)
            fileNames[Count++]=name;
        return fileNames;
    }

    /**
     * @param DataFileNames the DataFileNames to set
     */
    public void setDataFileNames(String[] FileNames) {
        for(String fname : FileNames)
            this.DataFileNames.add(fname);
    }

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
    
    public DataManager(){
        //Path currentPath = Paths.get("");
        outPath = inPath = "";
//        fileCount = 0;
        DataFileNames = new ArrayList<String>();
    }
    
    private ArrayList <String> DataFileNames;
    private String inPath = "";
    private String outPath = "";
    //int fileCount  = 0;
    private DataTrace_ver_3[] timeData;
    private DataTrace_ver_3[] velocity;
    private DataTrace_ver_3[] accelaration;
    private JVectorSpace[] velocityField, accelarationField;
    //ArrayList <ImageProcessor> heatMap,velMapX,velMapY,velcmpMapX,velcmpMapY,diffXMap,diffYMap,divMap;
    //ImageProcessor aveHMap,aveVelX,aveVelY,aveVelCmpX,aveVelCmpY,aveDiffX,aveDiffY,aveDiv;
    boolean dataReady = false;
    
    private int XRes = 0;   
    private int YRes = 0;
   
 /***
     * Call this function to read the data that is present in the files listed in DataManger.DataFile array of this class.
     * The data is supposed to be in the format of x and y co-ordinates listed in a time series stored as text(ascii) file.
     * (i.e. x1 \t y1 \n x2 \t y2\n....EOF). x1,y1 co -respond to co-ordinates at time t1, x2, y2 at time t2.
     * tn+1 is the time sample immediately after tn of an uniformly sampled data. Once read these data are stored in 
     * the internal data structure DataTrace.
     */
    public void readData(){
        DataTrace_ver_3[] newData;
        newData = new DataTrace_ver_3[getDataFileNames().length];
        int count = 0;
        for (String curFile  : getDataFileNames()){
                 newData[count] = new DataTrace_ver_3();
                //add the path name
                
                newData[count].populateData(curFile); 
                count++;
        }
        setTimeData(newData);
        computeAllFields();
    }
    private void computeAllFields(){
        //int dataCounter = 0;
        int maxFileNo = getDataFileNames().length;
        
        this.setVelocity(new DataTrace_ver_3[maxFileNo]);
        this.setAccelaration(new DataTrace_ver_3[maxFileNo]);
        this.velocityField = new JVectorSpace[maxFileNo];
        this.accelarationField = new JVectorSpace[maxFileNo];
        DataTrace_ver_3 velo;
        int fileCounter = 0 ;
            for(DataTrace_ver_3 tseries : timeData){
               velocity[fileCounter] = tseries.differentiate(false);
               accelaration[fileCounter] = velocity[fileCounter].differentiate(false);

               int Idx = 0;
               ArrayList<JVector> accVectors = new ArrayList<>();
               ArrayList<JVector> velVectors = new ArrayList<>();
               ArrayList<OrdXYData> spaceVects = new ArrayList<>();
               
               velo = velocity[fileCounter];
               for(OrdXYErrData vel : velo){
                   accVectors.add(new JVector(accelaration[fileCounter].get(Idx).getXY()));
                   velVectors.add(new JVector(vel.getXY()));
                   spaceVects.add(tseries.get(Idx));
                   Idx++;
               }
               velocityField[fileCounter] =  new JVectorSpace(getXRes(),getYRes(),false,spaceVects,velVectors);
               accelarationField[fileCounter] = new JVectorSpace(getXRes(),getYRes(),false,spaceVects,accVectors);
               fileCounter++;
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
    /**
     * 
     * @param fName string containing the name of the data file 
     */
    public void addDataFile(String fName){
        this.DataFileNames.add(fName);
//        fileCount++;
    }
    public String addDataFile(int fileNo, String fName){
        var maxIdx = this.DataFileNames.size()- 1;
        if(fileNo > maxIdx){
            DataFileNames.add(fName);
//            fileCount++;
            return null;
        }else{
            return(DataFileNames.set(fileNo, fName));
        }
    }
    public int getfileNo(String fName){
        return DataFileNames.indexOf(fName);
    }
    public int getFileCount(){
        return this.DataFileNames.size();
    }
    
  }
