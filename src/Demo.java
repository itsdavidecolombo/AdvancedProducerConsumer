import runnable.logger.Formatter;
import runnable.logger.FormatterRepo;
import runnable.peer.Connection;
import pluggable.Dashboard;
import pluggable.IPluggable;
import runnable.peer.BasePeer;
import runnable.peer.Producer;
import runnable.RunnableException;

import java.util.Random;

public class Demo {

    public static void main(String[] args){

        FormatterRepo formatterRepository = FormatterRepo.getInstance();
        Formatter got;

        try {
            Thread.sleep(1000);
            got = formatterRepository.getFormatterByName("myformatter");
            System.out.println(got.toString());
            Thread.sleep(1000);
            got = formatterRepository.getDefaultFormatter();
            System.out.println(got.toString());

            formatterRepository.newFormatter("MyFormatter", "$", "#", "-");
            Thread.sleep(1000);
            got = formatterRepository.getFormatterByName("MyFormatter");
            System.out.println(got.toString());

        } catch(InterruptedException e) {
            e.printStackTrace();
        }

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
