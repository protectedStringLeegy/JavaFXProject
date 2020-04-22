package exThread;

import java.io.*;
import java.util.*;

/* In questo esercizio viene implementata una pipeline di thread che comunicano come segue:
Producer -->pout1-pin1--> Filter -->pout2-pin2--> Consumer
*/

class PipeTestBynaryData {


    public static void main(String[] args) {
        try {
            PipedOutputStream pout1 = new PipedOutputStream();
            PipedInputStream pin1 = new PipedInputStream(pout1);

            PipedOutputStream pout2 = new PipedOutputStream();
            PipedInputStream pin2 = new PipedInputStream(pout2);

            Producer prod = new Producer(pout1);
            Filter filt = new Filter(pin1, pout2);
            Consumer cons = new Consumer(pin2);

            prod.start();
            filt.start();
            cons.start();
        }
        catch (IOException e) {System.out.println(e.getMessage());}
    }
}

/**
 A thread that writes random numbers to an output stream.
 */
class Producer extends Thread {
    private DataOutputStream out;
    private Random rand = new Random();

    /**
     Constructs a producer thread.
     @param os the output stream
     */
    public Producer(OutputStream os) {
        out = new DataOutputStream(os);
    }

    public void run() {
        while (true) {
            try {
                double num = rand.nextDouble();
                out.writeDouble(num);
                out.flush();
                sleep(Math.abs(rand.nextInt() % 1000));
            }
            catch(Exception e) {System.out.println("Error: " + e);}
        }
    }
} // end Producer

/**
 A thread that reads numbers from a stream and writes their
 average to an output stream.
 */
class Filter extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private double total = 0;
    private int count = 0;

    /**
     Constructs a filter thread.
     @param is the output stream
     @param os the output stream
     */
    public Filter(InputStream is, OutputStream os) {
        in = new DataInputStream(is);
        out = new DataOutputStream(os);
    }

    public void run() {
        for (;;) { // loop infinito con il FOR
            try {
                double x = in.readDouble();
                total += x;
                count++;
                if (count != 0) out.writeDouble(total / count); // scrive media in output stream
            }
            catch(IOException e)
            {
                System.out.println("Error: " + e);
            }
        }
    }
}

/**
 A thread that reads numbers from a stream and
 prints out those that deviate from previous inputs
 by a threshold value.
 */
class Consumer extends Thread {
    private double oldx = 0;
    private DataInputStream in;
    private static final double THRESHOLD = 0.01;

    /**
     Constructs a consumer thread.
     @param is the input stream
     */
    public Consumer(InputStream is) {
        in = new DataInputStream(is);
    }

    public void run()
    {
        for(;;) {
            try {
                double x = in.readDouble();
                if (Math.abs(x - oldx) > THRESHOLD){ // se la nuova media differisce rispetto alla precedente di almeno THRESHOLD
                    System.out.println(x);
                    oldx = x;
                }
            }
            catch(IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }
}


