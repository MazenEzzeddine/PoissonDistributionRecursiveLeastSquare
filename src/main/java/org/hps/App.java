package org.hps;




public class App 
{


    public static void main( String[] args ) throws InterruptedException {

        PoissonSampler app = new PoissonSampler(5, 5000);
        System.out.println(" ########################################################## ");

        Thread gen = new Thread(app);


        gen.start();





        }

    }






