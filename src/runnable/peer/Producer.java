package runnable.peer;

import queue.IQueue;
import runnable.RunnableException;

public class Producer extends BasePeer {

    private final Thread producer;

    public Producer(String nameVar, IQueue queueVar){
        super(nameVar, queueVar);
        producer = new Thread(this, toString());
    }

    @Override
    public void run() {
        System.out.println("<<< Producer " + this + " is started >>>");
        while(!isStopped()){
            for(int i = 0; i < 5; i++){
                try {
                    Thread.sleep(1000);
                    System.out.println("Producer " + this + " puts " + i);

                    synchronized(this){
                        while(isPaused()) {
                            queue.put(this + "\tSTATUS: PAUSED");
                            wait();
                            queue.put(this + "\tSTATUS: RESUMED");
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        queue.put(this + "\tSTATUS: STOPPED");
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
            queue.put(this + "\tInterruptedException caught in runInstance(): " + e.getMessage());
        }catch(RunnableException e) {
            queue.put(this + "\tRunnableException caught in runInstance(): " + e.getMessage());
        }
    }

    @Override
    public synchronized void resume() {
        try {
            super.resume();     // resume the PeerNotifier thread
            notify();           // wake up the producer thread when pausing
        } catch(RunnableException e) {
            queue.put(this + "\tRunnableException caught in resume(): " + e.getMessage());
            super.stop();
        }
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
