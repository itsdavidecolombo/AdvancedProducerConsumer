package queue;

public interface IQueue {

    String PEER_LOGS_1 = "PEERLOGS1";
    String PEER_LOGS_2 = "PEERLOGS2";
    String PEER_LOGS_3 = "PEERLOGS3";

    String CONN_LOGS_1 = "CONNLOGS1";
    String CONN_LOGS_2 = "CONNLOGS2";
    String CONN_LOGS_3 = "CONNLOGS3";

    String DASHBOARD = "DASHBOARD";
    String CONN_LOGS_DASH = "CONNLOGSDASH";

    String CONN_LOGS_FORMREPO = "CONNLOGSFORMREPO";

    /**
     * This method puts a String message to the Queue instance that implements this interface.
     * @param msg
     */
    void put(String msg);

    /**
     * This method return the head String in the Queue list associated to the Queue instance that
     * implements this interface.
     * @return
     */
    String get();
}
