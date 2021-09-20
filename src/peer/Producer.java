package peer;

import dashboard.IDashboard;

public class Producer extends BasePeer {

    private Thread producer;

    public Producer(IDashboard dashVar, String nameVar) {
        super(dashVar, nameVar);
        producer = new Thread(this, name);
    }

    @Override
    public void run() {
        System.out.println("<<< Producer " + name + " is started >>>");
        while(!isStopped()){
            for(int i = 0; i < 5; i++){
                try {
                    Thread.sleep(1000);
                    System.out.println("Producer " + name + " puts " + i);

                    synchronized(this){
                        while(isPaused()) {
                            System.out.println("<<< Producer " + name + " is paused >>>");
                            wait();
                            System.out.println("<<< Producer " + name + " is resumed >>>");
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("<<< Producer " + name + " is stopped >>>");
    }

    @Override
    public void runInstance() throws RunnableException {
        super.runInstance();
        this.producer.start();
    }

    @Override
    public synchronized void resume() throws RunnableException {
        super.resume();
        notify();       // wake up the producer thread when pausing
    }

    @Override
    public void stop() throws RunnableException {
        super.stop();
    }

}
