package org.hps;




public class App 
{


    public static void main( String[] args ) throws InterruptedException {

//        PoissonSampler app = new PoissonSampler(5, 5000);
//        System.out.println(" ########################################################## ");
//
//        Thread gen = new Thread(app);
//
//
//        gen.start();


        /////// Estimating a Linearly increaasing workload


/*
         ##########################################################
*/



        LinearWorkload lw = new LinearWorkload(2.08, 1000, 4);
        System.out.println(" ########################################################## ");

        Thread gen = new Thread(lw);


       gen.start();





        }

    }






