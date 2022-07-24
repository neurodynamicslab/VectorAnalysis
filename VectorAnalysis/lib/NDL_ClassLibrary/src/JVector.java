package NDL_JavaClassLib;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author balam
 * @param <N>
 */
public class JVector<N extends Number> {
    
    ArrayList<N> Components;
    public JVector(N [] C){
        for(N c : C)
            Components.add(c);
    }
   public JVector(ArrayList<N> C){
       for(N c : C)
            Components.add(c);
   }
   public void addVectors(ArrayList<N> C){
       for(N c : C)
            Components.add(c);
   }
    /**
     * @return the normsReady
     */
    public boolean isNormsReady() {
        return normsReady;
    }

    /**
     * @param normsReady the normsReady to set
     */
    private void setNormsReady(boolean normsReady) {
        this.normsReady = normsReady;
    }

    /**
     * @return the L1Norm
     */
    public double getL1Norm() {
        if(! isNormsReady() )
            this.calculateNorm();   
        return L1Norm;
    }

    /**
     * @return the L2Norm
     */
    public double getL2Norm() {
        if(! isNormsReady() )
            this.calculateNorm();
        return  L2Norm ;
    }

    /**
     * @return the maxNorm
     */
    public double getMaxNorm() {
        if(! isNormsReady() )
            this.calculateNorm();
        return maxNorm ;
    }

    private void setNorm(double absNorm, double sqNorm, double maxNorm) {
        this.L1Norm = absNorm;
        this.L2Norm = sqNorm;
        this.maxNorm = maxNorm;
        this.setNormsReady(true);
    }   
    
    public Number getComponent(int idx){
           return Components.get(idx);  
    }
    public int getNComponents(){
        return this.Components.size();
    }
    public double[] getNorm(){       
      if(!this.isNormsReady())
           this.calculateNorm();
      double [] norms = new double[3];
      norms[1] = this.getL1Norm();
      norms[2] = this.getL2Norm();
      norms[3] = this.getMaxNorm();
      return norms; 
    }
    private void calculateNorm(){     
        double sumSq = 0;
        double absSum = 0;
        double max = 0;
        //int nComponents = 0;
        for(Number N : Components){
            double comp = (double)N;
            absSum += java.lang.Math.abs(comp);
            sumSq += comp * comp;
            max = comp > max ? comp : max ;
         //nComponents++;
        }
         this.setNorm(absSum,java.lang.Math.sqrt(sumSq),max);
    }
    public double dotProduct(JVector secondVector){
        double product = 1.0;
        int idx = 0;
        if(this.Components.size() != secondVector.getNComponents()){
            for(Number N : Components){
                product += ((double)N * (double)secondVector.getComponent(idx));
                idx++;
            }
            return product;
        }else 
            return -1;
    }
    public double findAngle(JVector targetVector){
        double angle; 
        if(!isNormsReady())
            this.calculateNorm();
        angle = java.lang.Math.acos(this.dotProduct(targetVector)/(this.L2Norm * targetVector.getL2Norm()));
        return angle;
    }
    //private ArrayList <Number> Components;
    private double L1Norm,L2Norm,maxNorm;
    private boolean normsReady;
}
