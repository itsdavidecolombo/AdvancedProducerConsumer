package pluggable;

import peer.Connection;
import peer.message.Message;
import peer.message.MessageConstants;

import java.util.ArrayList;
import java.util.List;

public class Dashboard implements IPluggable {

    private final List<Connection> connections;

    public Dashboard(){
        connections = new ArrayList<>();
    }

    /**
     * This method is called by peers for adding messages to the Dashboard Queue.
     * Since we are dealing with a multi-threaded environment, the method must be synchronized.
     * @param message
     */

    @Override
    public void open(Connection conn) {
        System.out.println("<<< Dashboard opens connection with " + conn.sender.toString() + " >>>");
        // TODO: check if a connection related to the Peer how has sent the request is already opened
        connections.add(conn);
    }

    @Override
    public void close(Connection conn) {
        System.out.println("<<< Dashboard closes connection with " + conn.sender.toString() + " >>>");
        connections.remove(conn);
    }

    @Override
    public void pullMessage(Message msgVar) {
        if(MessageConstants.PING_MSG.equals(msgVar.getMessageCode())) {
            processPingMessage(msgVar);
        }
    }

    private void processPingMessage(Message message){
        System.out.println("<<< Dashboard received a PING from " + message.getMessageSender() + " >>>");
    }

}
