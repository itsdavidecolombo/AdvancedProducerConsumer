package queue;

public interface IQueue {

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
