package org.hps;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class RLS {
    private int num_vars;
    private double lamda;

    public int getNum_vars() {
        return num_vars;
    }

    public void setNum_vars(int num_vars) {
        this.num_vars = num_vars;
    }

    public double getLamda() {
        return lamda;
    }

    public void setLamda(double lamda) {
        this.lamda = lamda;
    }

    private RealMatrix P;

    public RealMatrix w;

    private RealMatrix aRealMatrix;
    RealMatrix bRealMatrix;
    RealMatrix cRealMatrix;
    private double a_priori_error;

    RLS(int num_vars, double lamda){

        this.num_vars = num_vars;
        this.lamda = lamda;
        P = MatrixUtils.createRealIdentityMatrix(num_vars);
        double [][] ww = new double[num_vars][1];
        for(int i=0; i<num_vars; i++)
            ww[i][0] =0;
        w = new Array2DRowRealMatrix(ww);
        this.a_priori_error =0;


    }




    public void add_obs(RealMatrix X, double t){
        RealMatrix kn, Pn,k, xt,kd1, Pnn;
        double kd, Pd;
       kn = P.multiply(X);


        kd= (((X.transpose()).multiply(P)).multiply(X)).getEntry(0,0) + lamda;


        k= kn.scalarMultiply(1/kd);


        Pn = (((P.multiply(X)).multiply(X.transpose())).multiply(P));
        Pd = (((X.transpose()).multiply(P)).multiply(X)).getEntry(0,0) + lamda;
        Pnn = Pn.scalarMultiply(1/Pd);
        P = (P.subtract(Pnn)).scalarMultiply( (1/lamda));
        a_priori_error = t - (w.transpose().multiply(X)).getEntry(0,0);
        w = w.add(k.scalarMultiply(a_priori_error));
    }


    public double get_error(){
        return a_priori_error;
    }

}
