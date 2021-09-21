package runnable.peer;

import runnable.RunnableException;
import runnable.logger.Logger;

public class Producer extends BasePeer {

    private final Thread producer;

    public Producer(String nameVar) {
        super(nameVar);
        producer = new Thread(this, toString());
    }

    public Producer(String nameVar, Logger loggerVar){
        super(nameVar, loggerVar);
        producer = new Thread(this, toString());
    }

    @Override
    public void run() {
        System.out.println("<<< Producer " + toString() + " is started >>>");
        while(!isStopped()){
            for(int i = 0; i < 5; i++){
                try {
                    Thread.sleep(1000);
                    System.out.println("Producer " + toString() + " puts " + i);

                    synchronized(this){
                        while(isPaused()) {
                            super.logger.log("NAME: " + toString()+ ". " +
                                    "STATUS: paused.");
                            // System.out.println("<<< Producer " + toString() + " is paused >>>");
                            wait();
                            super.logger.log("NAME: " + toString()+ ". " +
                                    "STATUS: resumed.");
                            //System.out.println("<<< Producer " + toString() + " is resumed >>>");
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        super.logger.log("NAME: " + toString()+ ". " +
                "STATUS: stopped.");
        // System.out.println("<<< Producer " + toString() + " is stopped >>>");
    }

    /**
     * This method is qualified as synchronized because the thread that calls this method can be blocked
     * if the connection is not opened.
     *
     * The caller thread is left in wait state until another thread opens the connection on this Peer instance
     * and notifies that a connection has been opened.
     */
    @Override
    public synchronized void runInstance() {
        try {
            while(!isConnected()){      // wait loop until a connection is not established
                wait();
            }
            super.runInstance();        // run the PeerNotifier thread
            this.producer.start();      // run the producer thread
        } catch(InterruptedException e){
            super.logger.log("NAME: " + toString() +
                    ". InterruptedException caught in runInstance(). " +
                    e.getMessage());
            // System.err.println("InterruptedException caught in Producer runInstance(): " + e.getMessage());
        }catch(RunnableException e) {
            super.logger.log("NAME: " + toString() +
                    ". RunnableException caught in runInstance(). " +
                    e.getMessage());
            // System.err.println("RunnableException caught in Producer runInstance(): " + e.getMessage());
        }
    }

    @Override
    public synchronized void resume() {
        try {
            super.resume();     // resume the PeerNotifier thread
            notify();           // wake up the producer thread when pausing
        } catch(RunnableException e) {
            super.logger.log("NAME: " + toString() +
                    ". RunnableException caught in resume(). " +
                    e.getMessage());
            // System.err.println("RunnableException caught in Producer resume(): " + e.getMessage());
            super.stop();
        }
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
