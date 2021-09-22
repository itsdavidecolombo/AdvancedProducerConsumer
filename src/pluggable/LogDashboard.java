package pluggable;

import queue.LogQueue;
import runnable.peer.Connection;
import runnable.peer.Message;

import java.util.ArrayList;
import java.util.List;

public class LogDashboard implements Pluggable {

    private static final int MAX_PEERS_SUPPORTED = 10;
    private final List<LogQueue> queues;


    private static LogDashboard theInstance = null;

    public static LogDashboard getInstance(){
        if(theInstance == null)
            theInstance = new LogDashboard();
        return theInstance;
    }

    private LogDashboard(){
        queues = new ArrayList<>();
    }

    @Override
    public void open(Connection conn) {
        try {
            if(queues.size() >= MAX_PEERS_SUPPORTED)
                throw new Exception("LogDashboard exceeds maximum number of peers supported");
            //queues.add(new LogQueue(conn));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(Connection conn) {
        try {
            if(queues.isEmpty())
                throw new Exception("LogDashboard queues list is empty");
            LogQueue found = searchQueue(conn);
            queues.remove(found);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private LogQueue searchQueue(Connection conn) throws Exception {
        for(LogQueue q : queues)
            if(q.equals(conn))
                return q;
        throw new Exception("LogDashboard: cannot found LogQueue associated to Connection: " + conn.toString());
    }

    @Override
    public void pullMessage(Message msgVar) {
        // TODO:
    }
}
