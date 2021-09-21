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

        final BasePeer p1 = new Producer("VermicelloPazzerello");
        Connection connPeer1 = new Connection(dashboard, p1);
        p1.openConnection(connPeer1);   // peer 1 has opened connection
        p1.openConnection(connPeer1);

        /*
         * Run the Peer on a different Thread than the main thread otherwise the main blocks if it should be
         * wait for a connection to be opened
         */
        new Thread(() -> {
            try {
                p1.runInstance();
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();


        final BasePeer p2 = new Producer("LumachinaGentile");
        Connection connPeer2 = new Connection(dashboard, p2);
        new Thread(() -> {
            try {
                p2.runInstance();
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();
        p2.openConnection(connPeer2);


        final BasePeer p3 = new Producer("LaMuccaMuu");
        Connection connPeer3 = new Connection(dashboard, p3);
        new Thread(() -> {
            try {
                p3.runInstance();
            } catch(RunnableException e) {
                e.printStackTrace();
            }
        }).start();
        p3.openConnection(connPeer3);

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
