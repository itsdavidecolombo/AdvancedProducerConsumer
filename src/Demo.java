import dashboard.Dashboard;
import dashboard.IDashboard;
import peer.BasePeer;
import peer.Producer;
import peer.RunnableException;
import peer.message.Message;
import peer.message.MessageConstants;

public class Demo {
    public static void main(String[] args){
        IDashboard dashboard = new Dashboard();

        BasePeer c1 = new Producer(dashboard, "@DavideColombo");

        c1.shipMessage(
                new Message(MessageConstants.SPAWN_MSG,
                            "Spwan...",
                            c1.getName()));

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
