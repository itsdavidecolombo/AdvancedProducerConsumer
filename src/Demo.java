import peer.Connection;
import pluggable.Dashboard;
import pluggable.IPluggable;
import peer.BasePeer;
import peer.Producer;
import peer.RunnableException;

import java.util.Random;

public class Demo {

    public static void main(String[] args){

        IPluggable dashboard = new Dashboard(); // the pluggable object

// ==========================================================================================
        final BasePeer p1 = new Producer("VermicelloPazzerello");
        p1.openConnection(new Connection(dashboard, p1));
        p1.openConnection(new Connection(dashboard, p1));       // test if the exception is correctly caught
        new Thread(() -> {      // runner Thread for running the BasePeer 1
            try {
                p1.runInstance();
                // this thread expires when the runInstance() method returns
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();

// ==========================================================================================
        final BasePeer p2 = new Producer("LumachinaGentile");
        new Thread(() -> {      // runner Thread for running the BasePeer 2
            try {
                p2.runInstance();
                // this thread expires when the runInstance() method returns
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();
        p2.openConnection(new Connection(dashboard, p2));       // open connection

// ==========================================================================================
        final BasePeer p3 = new Producer("LaMuccaMuu");
        new Thread(() -> {      // runner Thread for running the BasePeer 3
            try {
                p3.runInstance();
                // this thread expires when the runInstance() method returns
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();
        p3.openConnection(new Connection(dashboard, p3));       // open connection

        Random rand = new Random();

        try {
            Thread.sleep(10000);
            p1.pause();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p2.pause();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p3.pause();
            Thread.sleep(5000);
            p1.resume();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p2.resume();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p3.resume();
            Thread.sleep(10000);
            p1.stop();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p2.stop();
            Thread.sleep((long) (1000 * rand.nextDouble()));
            p3.stop();
        } catch(InterruptedException | RunnableException e) {
            e.printStackTrace();
        }

    }
}
