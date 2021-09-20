import dashboard.Dashboard;
import dashboard.IDashboard;
import peer.BasePeer;
import peer.Producer;
import peer.exception.RunnableException;

import java.util.Random;

public class Demo {
    public static void main(String[] args){
        IDashboard dashboard = new Dashboard();
        BasePeer p1 = new Producer(dashboard,
                "@VermicelloPazzerello");
        BasePeer p2 = new Producer(dashboard,
                "@Lumachina29");
        BasePeer p3 = new Producer(dashboard,
                "@LaMuccaMuu");

        Random rand = new Random();

        try {
            p1.runInstance();
            p2.runInstance();
            p3.runInstance();
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
