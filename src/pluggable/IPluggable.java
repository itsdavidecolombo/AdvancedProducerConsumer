package pluggable;

import runnable.peer.Connection;
import runnable.peer.Message;

public interface IPluggable {

    /**
     * Interface for opening a connection with the Dashboard
     * @param conn
     */
    void open(Connection conn);

    /**
     * Interface for closing a connection with the Dashboard
     * @param conn
     */
    void close(Connection conn);

    /**
     * Interface for receiving a message through the connection
     * @param msgVar
     */
    void pullMessage(Message msgVar);
}
