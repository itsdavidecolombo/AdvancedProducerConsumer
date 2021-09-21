package pluggable;

import peer.Connection;
import peer.message.Message;

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

    void pullMessage(Message msgVar);
}
