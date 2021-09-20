package peer;


import dashboard.IDashboard;
import peer.exception.RunnableException;
import peer.message.Message;
import peer.message.MessageConstants;

public abstract class BasePeer extends RunnableInstance {

// ===========================================================================
    /**
     * Inner class that notifies messages to the dashboard
     */
    private class PeerNotifier extends RunnableInstance {

        private final BasePeer peer;
        private final Thread threadNotifier;

        PeerNotifier(BasePeer peerVar){
            peer = peerVar;
            threadNotifier = new Thread(this, name);
        }

        public void run(){
            String msgContent;
            Message msg;
            while(!peer.isStopped() && !super.isStopped()){
                try {
                    synchronized(this){                         // checking if the thread should be paused
                        while(isPaused()) {
                            System.out.println("<<< PeerNotifier " + name + " is paused >>>");
                            wait();
                            System.out.println("<<< PeerNotifier " + name + " is resumed >>>");
                        }
                    }

                    Thread.sleep(5000);       // sleep
                    msgContent = "Ping...";         // message content
                    msg = new Message(MessageConstants.PING_MSG, msgContent, name);
                    peer.shipMessage(msg);          // shipping the message
                } catch(InterruptedException e) {
                    e.printStackTrace();
                    // TODO: log event with a logger
                }
            }
            System.out.println("<<< PeerNotifier " + name + " is stopped >>>");
        }

        @Override
        public void runInstance() throws RunnableException {
            super.runInstance();        // set the PeerNotifier runnable state to "RUNNING"
            threadNotifier.start();     // this causes the run method of "PeerNotifier" to be called
        }

        @Override
        public synchronized void resume() throws RunnableException {
            super.resume();             // set the PeerNotifier runnable state to "RUNNING"
            notify();                   // notify all the thread that are waiting on this object
        }

    }
// ===========================================================================

    private static int ID = 0;

    protected final int id;
    protected final String name;
    protected final IDashboard dashboard;
    protected final PeerNotifier peerNotifier;

    public BasePeer(IDashboard dashVar, String nameVar){
        id = ++BasePeer.ID;
        name = nameVar;
        dashboard = dashVar;
        peerNotifier = new PeerNotifier(this);
        openConnection();   // open the connection directly from the constructor
    }

    /**
     * private method for opening the connection with the dashboard. This method is called automatically
     * only once during the constructor invocation.
     */
    private void openConnection(){
        dashboard.send(new Message(MessageConstants.SPAWN_MSG, "Spawn...", name));
    }

    /**
     * Interface for shipping a message to the Dashboard
     * @param msg
     */
    public void shipMessage(Message msg){
        dashboard.send(msg);
    }

    /**
     * Call to the superclass method and the same on the peerNotifier. The same goes for pause(), resume() and stop().
     * @throws RunnableException
     */
    @Override
    public void runInstance() throws RunnableException {
        super.runInstance();
        peerNotifier.runInstance();
    }

    @Override
    public void pause() throws RunnableException {
        super.pause();
        peerNotifier.pause();
    }

    @Override
    public void resume() throws RunnableException {
        super.resume();
        peerNotifier.resume();
    }

    @Override
    public void stop() throws RunnableException {
        super.stop();
        peerNotifier.stop();
    }
}
