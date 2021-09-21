package runnable.peer;

import pluggable.IPluggable;

public class Connection {

    private enum ConnectionState{
        CREATED, OPENED, CLOSED
    }

    public final IPluggable receiver;
    public final BasePeer sender;
    private ConnectionState connState;

    public Connection(IPluggable recVar, BasePeer sendVar){
        sender = sendVar;
        receiver = recVar;
        connState = ConnectionState.CREATED;
    }

    /**
     * Method that opens the connection to the specified receiver
     */
    void open() throws ConnException {
        if(isOpened())
            throw new ConnException("Connection is already opened!");
        receiver.open(this);
        connState = ConnectionState.OPENED;
    }

    /**
     * Method that closes the connection to the specified receiver.
     */
    void close() throws ConnException {
        if(!isOpened())
            throw new ConnException("Cannot close a connection that has not already been opened!");
        receiver.close(this);
        connState = ConnectionState.CLOSED;
    }

    /**
     * This method allows to send a message to the receiver at which is associated the current connection instance
     * @param msgVar
     */
    void send(Message msgVar) throws ConnException {
        if(!isOpened())
            throw new ConnException("Cannot send a message when connection is not opened!");
        receiver.pullMessage(msgVar);
    }

    boolean isOpened(){
        return connState == ConnectionState.OPENED;
    }

    boolean isClosed(){
        return connState == ConnectionState.CLOSED;
    }
}
