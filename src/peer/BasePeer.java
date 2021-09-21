package peer;

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
            threadNotifier = new Thread(this, peer.toString());
        }

        public void run(){
            String msgContent;
            Message msg;
            while(!peer.isStopped() && !super.isStopped()){
                try {
                    synchronized(this){                         // checking if the thread should be paused
                        while(isPaused()) {
                            System.out.println("<<< PeerNotifier " + peer.toString() + " is paused >>>");
                            wait();
                            System.out.println("<<< PeerNotifier " + peer.toString() + " is resumed >>>");
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
            System.out.println("<<< PeerNotifier " + peer.toString() + " is stopped >>>");
        }

        @Override
        public void runInstance() throws RunnableException {
            super.runInstance();
            threadNotifier.start();
        }

        @Override
        public synchronized void resume() throws RunnableException {
            super.resume();
            notify();
        }

    }
// ===========================================================================

    private static int ID = 0;

    private final int id;
    private final String name;
    private Connection conn;
    private final PeerNotifier peerNotifier;

    public BasePeer(String nameVar){
        name = nameVar;
        id = ++BasePeer.ID;
        peerNotifier = new PeerNotifier(this);
    }

    /**
     * Final and Synchronized method for opening the connection with a Pluggable object.
     * This method catches exceptions from the Connection object if the user tries to open a connection
     * that is already opened.
     * If no exception is caught, the Connection object reference is stored in the conn instance variable.
     *
     * The notify() primitive method must be called to notify a possible Thread that is waiting for the
     * connection to be opened.
     */
    public synchronized final void openConnection(Connection connVar){
        try {
            connVar.open();
            conn = connVar;
        } catch(ConnException e) {
            System.out.println("ConnException caught: " + e.getMessage());
        }
        notify();       // unlock a thread (if any) that is waiting for the connection to be opened!
    }

    /**
     * Final and Synchronized method for closing a connection with a Pluggable object.
     * This method catches exceptions from the Connection object if the user tries to close a connection
     * without opening it before.
     */
    public final void closeConnection(){
        try {
            conn.close();
        } catch(ConnException e) {
            System.out.println("ConnException caught: " + e.getMessage());
        }
    }

    /**
     * Interface for shipping a message through the Connection.
     * @param msgVar
     */
    public final void shipMessage(Message msgVar){
        try {
            conn.send(msgVar);
        } catch(ConnException e) {
            System.out.println("ConnException caught: " + e.getMessage());
        }
    }

    /**
     * Method that is called by the subclasses for asking if the connection is opened
     * @return
     */
    protected boolean isConnected(){
        return conn != null && conn.isOpened();
    }

    /**
     * BasePeer class is mandated to override this method for reflecting the call to the "peerNotifier" instance.
     * The same goes for pause(), resume() and stop() methods.
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
        closeConnection();
    }

    @Override
    public String toString(){
        return "@" + name + "_" + id;
    }
}
