package queue;

public interface IQueueListener {

    /**
     * This method allows the implementer to register to the Queue passed as argument.
     * @param q
     */
    void registerQueue(IQueue q) throws QueueListenerException;

}
