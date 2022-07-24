/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NDL_JavaClassLib;

import java.util.ArrayList;

/**
 * Class to hold, initialize and create heat map pixel array from  time series XY data. 
 * @author balam
 */
public class JHeatMapArray extends Object{

    /**
     * @return the xRes
     */
    public int getxRes() {
        return xRes;
    }

    /**
     * @param xRes the xRes to set
     */
    public void setxRes(int xRes) {
        this.xRes = xRes;
    }

    /**
     * @return the yRes
     */
    public int getyRes() {
        return yRes;
    }

    /**
     * @param yRes the yRes to set
     */
    public void setyRes(int yRes) {
        this.yRes = yRes;
    }

    /**
     * @return the timeSeries
     */
    public DataTrace_ver_3 getTimeSeries() {
        return timeSeries;
    }

    /**
     * @param timeSeries the timeSeries to set
     */
    public void setTimeSeries(DataTrace_ver_3 timeSeries) {
        this.timeSeries = timeSeries;
    }

    /**
     * 
     * @return the pixelArray
     */
    public double[][] getPixelArray() {
        convertTimeSeriestoArray();
        return pixelArray;
    }
    public double[][] getPixelArray(int xRes, int yRes){
        this.setxRes(xRes);
        this.setyRes(yRes);
        
        this.convertTimeSeriestoArray(xRes, yRes);
        return pixelArray;
    }

    /**
     * @param pixelArray the pixelArray to set
     */
    public void setPixelArray(double[][] pixelArray) {
        this.pixelArray = pixelArray;
    }
    
    private int xRes = 0;
    private int yRes = 0;
    private double [][] pixelArray;
    private DataTrace_ver_3 timeSeries;
    
    /**
     *
     * @param xRes
     * @param yRes
     */
    public JHeatMapArray(int xRes, int yRes){
        this.setxRes(xRes);
        this.setyRes(yRes);
        setTimeSeries(new DataTrace_ver_3());
    }
    public JHeatMapArray(DataTrace_ver_3 trace){
        setTimeSeries(trace);
        this.setxRes(Math.round((float)(timeSeries.getXMax()-timeSeries.getXMin())));
        this.setyRes(Math.round((float)(timeSeries.getYMax()-timeSeries.getYMin())));
    }
    public void convertTimeSeriestoArray(int xRes,int yRes){
        
        pixelArray = new double[xRes][yRes];
        
        ArrayList<Double> xPosi, yPosi;
        xPosi = timeSeries.getX();
        yPosi = timeSeries.getY();
        int Idx = 0;
        int x,y;
        for(Double xDouble : xPosi){
            
            Double yDouble = yPosi.get(Idx);
            x = (int)Math.round(xDouble);
            y = (int)Math.round(yDouble);
            if(x <= xRes && y <= yRes && x >= 0 && y >= 0)
                pixelArray [x][y] += 1;
            Idx++;
        }
        
    }
    public void convertTimeSeriestoArray(){
        this.timeSeries.resetStat();
        this.convertTimeSeriestoArray( Math.round((float)(timeSeries.getXMax()-timeSeries.getXMin()))
                                        ,Math.round((float)(timeSeries.getYMax()-timeSeries.getYMin())));
    }
    /***
     * Method to convert two dimensional pixel array to linear 1D array.They are useful during
     * creation of images using ImageJ. Make sure to call convertTimeSeriestoArray()before calling
     * this function. The sequence of steps should be initialize the time series data, convertTimeSeriestoArray()
     * and then convert it to 1D array (linear array).
     * @return : returns a linear double array with xRes x yRes elements. 
     */
    public double [] to1DArray(){
        double [] OneD = new double[xRes*yRes];
        int xIdx = 0, yIdx = 0;
        for(double [] y : pixelArray){
            for(double val : y){
                OneD[xIdx + (yIdx*xRes)] = val;
                xIdx++;
            }
            yIdx++;
            xIdx = 0;
        }
        return OneD;
    }
}
