package pluggable;

import queue.IQueue;
import queue.QueueListener;
import queue.QueueListenerException;
import runnable.peer.Connection;
import runnable.peer.Message;

import java.util.ArrayList;
import java.util.List;

public class Dashboard implements Pluggable, QueueListener {

    private final List<Connection> connections;
    private IQueue queue = null;

    private static Dashboard theInstance = null;

    public static Dashboard getInstance(){
        if(theInstance == null)
            theInstance = new Dashboard();
        return theInstance;
    }

    private Dashboard(){
        connections = new ArrayList<>();
    }

    @Override
    public void registerToQueue(IQueue q) throws QueueListenerException {
        if(queue != null) {
            String msg = "Failed to register the Dashboard " + this +
                    " to queue " + q + ": queue reference in Dashboard not null";
            throw new QueueListenerException(msg, QueueListenerException.ExceptionCause.ALREADY_REGISTERED);
        }
        queue = q;
    }

    /**
     * This method opens a connection with the PluggableObj upon which is called.
     * @param conn
     */
    @Override
    public void open(Connection conn) {
        // System.out.println("<<< Dashboard opens a connection with " + conn.sender.toString() + " >>>");
        connections.add(conn);
        queue.put("NAME: " + this + ". Connection established successfully. " +
                "TARGET: " + conn.sender.toString());
    }

    /**
     * This method closes a connection with the PluggableObj upon which is called.
     * @param conn
     */
    @Override
    public void close(Connection conn) {
        // System.out.println("<<< Dashboard closes a connection with " + conn.sender.toString() + " >>>");
        connections.remove(conn);
        queue.put("NAME: " + this + ". Connection closed successfully. " +
                "TARGET: " + conn.sender.toString());
    }

    /**
     * This method is used to pull a new message from a connected Peer.
     * This method acts as a dispatch method for deciding how the message has to be processed.
     * @param msgVar
     */
    @Override
    public void pullMessage(Message msgVar) {
        if(Message.PING_MSG.equals(msgVar.getMessageCode())) {
            processPingMessage(msgVar);
        }
    }

    private void processPingMessage(Message message){
        // System.out.println("<<< Dashboard received a PING from " + message.getMessageSender() + " >>>");
        // TODO: define a message scheme
        queue.put("NAME: " + this + ". PING received successfully. " +
                "TARGET: " + message.getMessageSender());
    }

    public String toString(){
        return "@DASHBOARD";
    }
}
