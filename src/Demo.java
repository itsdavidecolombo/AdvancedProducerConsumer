import dashboard.Dashboard;
import dashboard.IDashboard;
import peer.BasePeer;
import peer.Producer;
import peer.exception.RunnableException;

public class Demo {
    public static void main(String[] args){
        IDashboard dashboard = new Dashboard();
        BasePeer c1 = new Producer(dashboard,
                "@DavideColombo");

        try {
            c1.runInstance();
            Thread.sleep(10000);
            c1.pause();
            Thread.sleep(5000);
            c1.resume();
            Thread.sleep(10000);
            c1.stop();
        } catch(InterruptedException | RunnableException e) {
            e.printStackTrace();
        }

    }
}
