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
     * @return the aveVelFld
     */
    public JVectorSpace getAveVelFld() {
        return aveVelFld;
    }

    /**
     * @return the aveAccFld
     */
    public JVectorSpace getAveAccFld() {
        return aveAccFld;
    }

    /**
     * @return the aveResMap
     */
    public JHeatMapArray getAveResMap() {
        return aveResMap;
    }    

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
    private JVectorSpace[] velocityField, accelarationField,residenceField;
    
    private JHeatMapArray[] residenceMaps;
    
    private JHeatMapArray aveResMap;
    private JVectorSpace aveVelFld,aveAccFld;
    
    //ArrayList <ImageProcessor> heatMap,velMapX,velMapY,velcmpMapX,velcmpMapY,diffXMap,diffYMap,divMap;
    //ImageProcessor aveHMap,aveVelX,aveVelY,aveVelCmpX,aveVelCmpY,aveDiffX,aveDiffY,aveDiv;
    boolean dataReady = false;
    boolean averageReady = false;
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
        averageReady = false;
    }
    private void computeAllFields(){
        //int dataCounter = 0;
        int maxFileNo = getDataFileNames().length;
        
               
        this.setVelocity(new DataTrace_ver_3[maxFileNo]);
        this.setAccelaration(new DataTrace_ver_3[maxFileNo]);
        
        this.velocityField = new JVectorSpace[maxFileNo];
        this.accelarationField = new JVectorSpace[maxFileNo];
        this.residenceField = new JVectorSpace[maxFileNo];
        
        this.residenceMaps = new JHeatMapArray[maxFileNo];
                
        
        
        DataTrace_ver_3 velo, acc;
        int fileCounter = 0 ;
        int Idx;
        
            for(DataTrace_ver_3 tseries : timeData){
               
               
               residenceMaps[fileCounter] = new JHeatMapArray(getXRes(), getYRes());
               getResidenceMap()[fileCounter].setTimeSeries(tseries);
               getResidenceMap()[fileCounter].convertTimeSeriestoArray();
               //var hmapArray = residenceMaps[fileCounter].getPixelArray();
               
               velocity[fileCounter] = tseries.differentiate(false);
               accelaration[fileCounter] = velocity[fileCounter].differentiate(false);
               
               
               
               ArrayList<JVector> accVectors = new ArrayList<>();
               ArrayList<JVector> velVectors = new ArrayList<>();
               ArrayList<OrdXYData> posiVects = new ArrayList<>();
               //ArrayList<JVector> resiScalars = new ArrayList<>();
               
               velo = velocity[fileCounter];
               acc = accelaration[fileCounter];
               Idx = 0;
               
               for(OrdXYErrData accVect : acc){
                   accVectors.add(new JVector(accVect.getXY()));
                   velVectors.add(new JVector(velo.get(Idx).getXY()));
                   //resiScalars.add(new JVector(hmapArray[][]));
                   posiVects.add(tseries.get(Idx));
                   Idx++;
               }
               
               accelarationField[fileCounter] = new JVectorSpace(getXRes(),getYRes(),false,posiVects,accVectors);
               
               velVectors.add(new JVector(velo.get(Idx).getXY()));
               posiVects.add(tseries.get(Idx));
               
               velocityField[fileCounter] =  new JVectorSpace(getXRes(),getYRes(),false,posiVects,velVectors);
               
               Idx++;
               posiVects.add(tseries.get(Idx));
                              
               fileCounter++;
            }
                  
    }
    public void computeAve(int choice, JVector Vector, boolean resiNorm){
        
        aveResMap = new JHeatMapArray(XRes,YRes);
        aveVelFld = new JVectorSpace(XRes,YRes);
        aveAccFld = new JVectorSpace(XRes,YRes);
        
        switch(choice){
            
            case 0:
                for(var velFld : this.velocityField)
                    getAveVelFld().fillSpace(velFld.getSpace(), velFld.getVectors(), false);
                for(var accFld : this.accelarationField)
                    getAveAccFld().fillSpace(accFld.getSpace(), accFld.getVectors(), false); 
                break;
            case 1:
                for(var velFld : this.velocityField){
                    var velCmp = velFld.getProjections(Vector,true);
                    getAveVelFld().fillSpace(velCmp.getSpace(), velCmp.getVectors(), false);
                }
                for(var accFld : this.accelarationField){
                    var accCmp = accFld.getProjections(Vector,true);
                    getAveAccFld().fillSpace(accCmp.getSpace(), accCmp.getVectors(), false); 
                }
                break;
            case 2:
                for(var velFld : this.velocityField){
                    var velCmp = velFld.getProjections(Vector,false);
                    getAveVelFld().fillSpace(velCmp.getSpace(), velCmp.getVectors(), false);
                }
                for(var accFld : this.accelarationField){
                    var accCmp = accFld.getProjections(Vector,false);
                    getAveAccFld().fillSpace(accCmp.getSpace(), accCmp.getVectors(), false); 
                }
                break;
            default: //Calculate only the residence map
                break;
        }
        if(resiNorm){
            for(var resFld : this.residenceMaps)
                getAveResMap().appendTimeSeries(resFld.getTimeSeries());  

            var norm = getAveResMap().getPixelArray();
            Double [][] scale = new Double[norm.length][norm[0].length];
            int xIdx = 0, yIdx = 0;
            for(var X : norm){
                for(var Y : X){
                    scale[xIdx][yIdx++] = 1/Y ;
                }
                xIdx++;
                yIdx = 0;
            }

            var nAveVel = getAveVelFld().scaleVectors(scale);
            aveVelFld = nAveVel;
            var nAveAcc = getAveAccFld().scaleVectors(scale);
            aveAccFld = nAveAcc;
        }
        
    }
    public void saveAverage(String prefix, boolean saveResi){
        
        var aveVel = new  JVectorCmpImg(getAveVelFld());
        var aveAcc = new  JVectorCmpImg(getAveAccFld());
        
        if(saveResi){
            var aveRes = new  JVectorCmpImg(getXRes(),getYRes(),1);
            aveRes.addScalar(getAveResMap());
            aveRes.saveImages(outPath, prefix+"aveHMap");
        }
        
        aveAcc.saveImages(outPath, prefix+"aveAcc");
        aveVel.saveImages(outPath, prefix+"aveVel");
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
        averageReady = false;
//        fileCount++;
    }
    public String addDataFile(int fileNo, String fName){
        var maxIdx = this.DataFileNames.size()- 1;
        averageReady = false;
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

    /**
     * @return the residenceMaps
     */
    public JHeatMapArray[] getResidenceMap() {
        return residenceMaps;
    }

    /**
     * @return the residenceField
     */
    public JVectorSpace[] getResidenceField() {
        return residenceField;
    }
    
  }
