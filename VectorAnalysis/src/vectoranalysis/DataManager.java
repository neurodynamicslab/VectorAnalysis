/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectoranalysis;

import NDL_JavaClassLib.*;
import java.io.File;
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

    private boolean newData;
    private boolean useTan2Prj;
    private boolean useRelativeVelocity;
    private boolean rescaleIndividual;

    /**
     * @return the lineSep
     */
    public char getLineSep() {
        return lineSep;
    }

    /**
     * @param lineSep the lineSep to set
     */
    public void setLineSep(char lineSep) {
        this.lineSep = lineSep;
    }

    /**
     * @return the dataSep
     */
    public String getDataSep() {
        return dataSep;
    }

    /**
     * @param dataSep the dataSep to set
     */
    public void setDataSep(String dataSep) {
        this.dataSep = dataSep;
    }

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
     * @param FileNames the DataFileNames to set. 
     * Expect the full path name as given by File().getAbsolutePath()
     */
    public void setDataFileNames(String[] FileNames) {
       // DataFiles = new ArrayList();
       
        if(FileNames.length < 1)
            return;
        for(String fname : FileNames){
            this.DataFileNames.add(fname);
            this.DataFiles.add(new File(fname));
        }
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
        DataFileNames = new ArrayList<>();
        DataFiles = new ArrayList<>();
    }
    private final ArrayList <File> DataFiles;
    private final ArrayList <String> DataFileNames;
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
    
    private int XRes = 0;   
    private int YRes = 0;
    private double pixelAspectRatio = 1.0;
    private boolean scaleforAspectRatio = true;
    private char lineSep = '\n';
    private String dataSep = ",";
   
 /***
     * Call this function to read the data that is present in the files listed in DataManger.DataFile array of this class.
     * The data is supposed to be in the format of x and y co-ordinates listed in a time series stored as text(ascii) file.
     * (i.e. x1 \t y1 \n x2 \t y2\n....EOF). x1,y1 co -respond to co-ordinates at time t1, x2, y2 at time t2.
     * tn+1 is the time sample immediately after tn of an uniformly sampled data. Once read these data are stored in 
     * the internal data structure DataTrace.
     */
    public void readData(){
        DataTrace_ver_3[] currData;
        currData = new DataTrace_ver_3[getDataFileNames().length];
        int count = 0;
        for (String curFile  : getDataFileNames()){
           currData[count] = new DataTrace_ver_3();           
           currData[count].populateData(curFile, getDataSep(), getLineSep(),2,false); 
           count++;
        }
        setTimeData(currData);
        computeAllFields();
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
               if(this.isScaleforAspectRatio())
                   velocity[fileCounter].scaleYaxis(this.getPixelAspectRatio());
               
               accelaration[fileCounter] = velocity[fileCounter].differentiate(false); //once scaled for pixel ratio accelaration doesn't 
                                                                                       //need to be scaled       
               
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
//             System.out.println("The datalength is :"+Idx+","+posiVects.size()
//                                +","+velocityField[fileCounter].getSpace().size()+","+velocityField[fileCounter].getVectors().size());
               fileCounter++;
            }
    }
    /***
     * 
     * @param choice 0: for generating the average field of the vectors as such (no projections) 
     *               1: for generating the average field of projected vectors along a another vector 
     *               2: for generating the average field of projected vectors orthogonal to another vector
     *               >3: calculates only the residence/number of sample  map average. 
     * @param Vector The position vector along/orthogonal to which we get the projections ( can be null for option "0" for choice).
     * @param resiNorm  True if the vector field needs to normalized for the number of samples. 
     *                  Usually it is true as otherwise they will represent incorrect magnitude.
     */
    public void computeAve(int choice, JVector Vector, boolean resiNorm){
        
       if( aveResMap == null) aveResMap =  new JHeatMapArray(XRes,YRes);
       if( aveVelFld == null) aveVelFld = new JVectorSpace(XRes,YRes);
       if( aveAccFld == null) aveAccFld = new JVectorSpace(XRes,YRes);
       int Idx = 0;
       JVectorSpace prjFld,accFldPrj;
        switch(choice){
            
            case 0:             //Calculate average of velocity and accelaration
                                //To do: implement normalisation to residence map
                                //similar to case 1: velocity projection.
                getAveVelFld().getSpace().clear();
                getAveVelFld().getVectors().clear();
                getAveAccFld().getSpace().clear();
                getAveAccFld().getVectors().clear();
                for(var velFld : this.velocityField)
                    getAveVelFld().fillSpace(velFld.getSpace(), velFld.getVectors(), false);
                for(var accFld : this.accelarationField)
                    getAveAccFld().fillSpace(accFld.getSpace(), accFld.getVectors(), false); 
//                for(var resFld : this.residenceMaps)
//                    getAveResMap().appendTimeSeries(resFld.getTimeSeries());  
//                getAveResMap().convertTimeSeriestoArray();
                break;
                
            case 1:                     //Calculate projections along the direction of the position vector provided
                                        //normalise for the residence time or number of samples. 
                                        
                
                getAveVelFld().getSpace().clear();
                getAveVelFld().getVectors().clear();
                getAveAccFld().getSpace().clear();
                getAveAccFld().getVectors().clear();
                //int dataCounter = 0;
                for(var velFld : this.velocityField){
                    
                    if(!velFld.isProjectionStatus()){
                         velFld.setUseTan2(useTan2Prj);
                         accelarationField[Idx].setUseTan2(useTan2Prj);
                         prjFld = velFld.getProjections2point(Vector,true);                         
                         accFldPrj = accelarationField[Idx].getProjections2point(Vector,true);
                    }else{
                        prjFld = velFld.getProjection();
                        accFldPrj = accelarationField[Idx].getProjection();
                    }                    
                    
                   
                    
                    var resMap = this.residenceMaps[Idx++];
                    var norm = covertScaletoNorm(resMap.getPixelArray());
                    var scaledFldvel = (resiNorm)? prjFld.scaleVectors(norm): prjFld;  
                    var scaledAcc =(resiNorm)? accFldPrj.scaleVectors(norm):accFldPrj;
                    
                     if(this.isUseRelativeVelocity()){
                        if(!scaledFldvel.isChkMinMaxandAdd())
                            scaledFldvel.setChkMinMaxandAdd(true);
                        scaledFldvel = scaledFldvel.calibrateVectors(0,Integer.MAX_VALUE);
//                        if(!scaledAcc.isChkMinMaxandAdd())
//                            scaledAcc.setChkMinMaxandAdd(true);
//                        scaledAcc = scaledAcc.calibrateVectors(Float.MIN_VALUE, Float.MAX_VALUE);
                    }
                    
                    
                    getAveVelFld().fillSpace(scaledFldvel.getSpace(),scaledFldvel.getVectors(),false);
                    getAveAccFld().fillSpace(scaledAcc.getSpace(), scaledAcc.getVectors(), false);  
                                       
                    
                    
                }
//                for(var accFld : this.accelarationField){
//                    var accCmp = accFld.getProjections2point(Vector,true);
//                    getAveAccFld().fillSpace(accCmp.getSpace(), accCmp.getVectors(), false); 
//                }
                break;
            case 2:                     //Calculate projections ortogonal to a position vector
                                        
                
                getAveVelFld().getSpace().clear();
                getAveVelFld().getVectors().clear();
                getAveAccFld().getSpace().clear();
                getAveAccFld().getVectors().clear();
                //int dataCounter = 0;
                for(var velFld : this.velocityField){
                    if(!velFld.isProjectionStatus()){
                         velFld.setUseTan2(useTan2Prj);
                         accelarationField[Idx].setUseTan2(useTan2Prj);
                         prjFld = velFld.getProjections2point(Vector,false);
                         accFldPrj = accelarationField[Idx].getProjections2point(Vector,false);
                    }
                    else  {
                        prjFld = velFld.getProjection();
                        accFldPrj = accelarationField[Idx].getProjection();
                    }  
                    var resMap = this.residenceMaps[Idx++];
                    var norm = covertScaletoNorm(resMap.getPixelArray());
                    var scaledFldvel = (resiNorm)? prjFld.scaleVectors(norm): prjFld;  
                    var scaledAcc =(resiNorm)? accFldPrj.scaleVectors(norm):accFldPrj;
                    getAveVelFld().fillSpace(scaledFldvel.getSpace(),scaledFldvel.getVectors(),false);
                    getAveAccFld().fillSpace(scaledAcc.getSpace(), scaledAcc.getVectors(), false);                  
                }               
                break;
            default: //Calculate only the residence map
                aveResMap.getTimeSeries().clear();
                for(var resFld : this.residenceMaps)
                    getAveResMap().appendTimeSeries(resFld.getTimeSeries());  
                break;
        }       
    }

    private Double[][] covertScaletoNorm(double [][] norm) {
        
        if(norm == null)
            return null;
        Double [][] scale = new Double[norm.length][norm[0].length];
        int xIdx = 0, yIdx = 0;
        for(double[] X : norm){
            for(double Y : X){
                scale[xIdx][yIdx++] = 1/Y;//(Y == 0) ? 0 : 1/Y ;      //pixels that are not sampled are set to zero during normalisation
            }
            xIdx++;
            yIdx = 0;
        }
        return scale;
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
        this.newData  = true;
    }
    /**
     * Adds the data file to the manager at a specified location
     * The already existing file is replaced and the file name is returned as string. If the index is larger the 
     * number of files, the file is simply appended returning null. Checking the return value for null tells if the new file
     * is appended or inserted.
     * @param fileNo : Index at which the new file need to be placed (will be used only if the index is within the length of the list
     * @param fName: The filename to be replaced or appended
     * @return : returns the filename of the replaced file
     */
    public String addDataFile(int fileNo, String fName){
        var maxIdx = this.DataFileNames.size()- 1;
        this.newData = true;
        if(fileNo > maxIdx){
            DataFileNames.add(fName);
//            fileCount++;
            return null;
        }else{
            return(DataFileNames.set(fileNo, fName));
        }
       
    }
    public boolean isDataReadComplete(){
        return ! this.newData;
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
        if(!newData)
            readData();                                             //Not an efficient way to calculate everything for all
                                                                    //even for a change of single file
        return residenceMaps;
    }

    /**
     * @return the residenceField
     */
    public JVectorSpace[] getResidenceField() {
        return residenceField;
    }

    /**
     * @return the pixelAspectRatio
     */
    public double getPixelAspectRatio() {
        return pixelAspectRatio;
    }

    /**
     * @param pixelAspectRatio the pixelAspectRatio to set
     */
    public void setPixelAspectRatio(double pixelAspectRatio) {
        this.pixelAspectRatio = pixelAspectRatio;
    }

    /**
     * @return the scaleforAspectRatio
     */
    public boolean isScaleforAspectRatio() {
        return scaleforAspectRatio;
    }

    /**
     * @param scaleforAspectRatio the scaleforAspectRatio to set
     */
    public void setScaleforAspectRatio(boolean scaleforAspectRatio) {
        this.scaleforAspectRatio = scaleforAspectRatio;
    }

    /**
     * @return the useRelativeVelocity
     */
    public boolean isUseRelativeVelocity() {
        return useRelativeVelocity;
    }

    /**
     * @param useRelativeVelocity the useRelativeVelocity to set
     */
    public void setUseRelativeVelocity(boolean useRelativeVelocity) {
        this.useRelativeVelocity = useRelativeVelocity;
    }
    
  }
