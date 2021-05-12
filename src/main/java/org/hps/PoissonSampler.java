package org.hps;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.time.Duration;
import java.time.Instant;

public class PoissonSampler extends  Thread {

    int lamda;
    int delay;
    PoissonDistribution poissonDistribution;

   private double[][] regressor;

   private RLS rls;


    PoissonSampler(int lamda, int delay){

        this.lamda =lamda;
        poissonDistribution = new PoissonDistribution(lamda);
        this.delay = delay;

         rls = new RLS(3, 0.99);

        regressor = new double[1][rls.getNum_vars()];

        for (int i=0; i<rls.getNum_vars(); i++)
            regressor[0][i] =0;

    }


    int sample() {
        return poissonDistribution.sample();
    }

    void changeMean(int lamda) {

        this.lamda =lamda;

        poissonDistribution = new PoissonDistribution(lamda);

    }


    @Override
    public void run() {

        int s;

        Instant start = Instant.now();
        double  predictedSample;
        RealMatrix mregressor;



        while (true) {



            System.out.println(" Regressor is :");
            printRegressor();
            mregressor = new Array2DRowRealMatrix(regressor);

            predictedSample = (this.rls.w.transpose().multiply(mregressor.transpose())).getEntry(0,0);

            System.out.println(" Estimated poisson sample is: " +  predictedSample);

            s =  sample();

            System.out.println(" Poisson mean:" + lamda + "  sampled :" + s);
            System.out.println(" sleeping for: " + delay);

            rls.add_obs(mregressor.transpose(), s);




            for (int i=0; i<rls.getNum_vars() -1; i++)
                regressor[0][i] =  regressor[0][i+1];

            regressor[0][rls.getNum_vars()-1] = s;

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

        }
    }

    private void printRegressor(){
        for (int i=0; i<rls.getNum_vars() ; i++)
           System.out.println( "regressor[0]["+ i + "] = " + regressor[0][i]);
    }
}
