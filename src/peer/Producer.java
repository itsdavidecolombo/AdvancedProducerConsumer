package peer;

public class Producer extends BasePeer {

    private final Thread producer;

    public Producer(String nameVar) {
        super(nameVar);
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
                            System.out.println("<<< Producer " + toString() + " is paused >>>");
                            wait();
                            System.out.println("<<< Producer " + toString() + " is resumed >>>");
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("<<< Producer " + toString() + " is stopped >>>");
    }

    @Override
    public synchronized void runInstance() throws RunnableException {
        while(!isConnected()){
            try {
                System.out.println("!!! " + Thread.currentThread().getName() + " is waiting " +
                        "for the connection on " + toString() + " to be opened !!! ");
                wait();
            } catch(InterruptedException e) {
                System.out.println("runInstance() method in Producer has been interrupted: " + e);
            }
        }
        super.runInstance();        // run the PeerNotifier thread
        this.producer.start();      // run the producer thread
    }

    @Override
    public synchronized void resume() throws RunnableException {
        super.resume();     // resume the PeerNotifier thread
        notify();           // wake up the producer thread when pausing
    }

    @Override
    public void stop() throws RunnableException {
        super.stop();
    }

    public String toString(){
        return super.toString();
    }
}
