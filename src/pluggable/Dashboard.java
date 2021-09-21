package pluggable;

import peer.ConnException;
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
     * This method opens a connection with the PluggableObj upon which is called.
     * @param conn
     */
    @Override
    public void open(Connection conn) {
        System.out.println("<<< Dashboard opens a connection with " + conn.sender.toString() + " >>>");
        connections.add(conn);
    }

    /**
     * This method closes a connection with the PluggableObj upon which is called.
     * @param conn
     */
    @Override
    public void close(Connection conn) {
        System.out.println("<<< Dashboard closes a connection with " + conn.sender.toString() + " >>>");
        connections.remove(conn);
    }

    /**
     * This method is used to pull a new message from a connected Peer.
     * This method acts as a dispatch method for deciding how the message has to be processed.
     * @param msgVar
     */
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
