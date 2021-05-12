package org.hps;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.time.Instant;

public class LinearWorkload extends Thread {

    double tangent;
    int delay;
    int regressorOrder;


    private double[][] regressor;

    private RLS rls;


    LinearWorkload(double tangent, int delay, int regressorOrder){

        this.tangent =tangent;
        this.delay = delay;
        this.regressorOrder = regressorOrder;

        rls = new RLS(regressorOrder, 0.99);

        regressor = new double[1][this.regressorOrder];

        for (int i=0; i<rls.getNum_vars(); i++)
            regressor[0][i] =0;

    }


    double sample(int second) {
        if(second <120)
        return 2.083 * second;
        else
            return 2.083 *120;
    }




    @Override
    public void run() {

        double s;

        Instant start = Instant.now();
        double  predictedSample;
        RealMatrix mregressor;


         int seconds = 0;
        while (true) {



            System.out.println(" Regressor is :");
            printRegressor();
            mregressor = new Array2DRowRealMatrix(regressor);

            predictedSample = (this.rls.w.transpose().multiply(mregressor.transpose())).getEntry(0,0);

            System.out.println(" Estimated Linear Sample is: " +  predictedSample);

            s =  sample(seconds);

            System.out.println(" Linear tangent:" + tangent + "  sampled :" + s + "  seconds elpased :" + seconds);
            System.out.println(" sleeping for: " + delay);

            rls.add_obs(mregressor.transpose(), s);




            for (int i=0; i<regressorOrder -1; i++)
                regressor[0][i] =  regressor[0][i+1];

            regressor[0][regressorOrder-1] = s;

         /*   Instant now = Instant.now();
            long elapsedTime = Duration.between(start, now).toMinutes();

            if(elapsedTime > 1) {
                changeMean(7);
                 start = Instant.now();
            }
*/
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            seconds++;




        }
    }

    private void printRegressor(){
        for (int i=0; i<this.regressorOrder ; i++)
            System.out.println( "regressor[0]["+ i + "] = " + regressor[0][i]);
    }





}
