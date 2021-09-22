package queue;

public interface QueueListener {

    /**
     * This method provides the interface for registering to a queue.
     * @param q
     */
    void registerToQueue(IQueue q) throws QueueListenerException;

}
