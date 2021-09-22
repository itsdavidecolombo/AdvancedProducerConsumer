package queue;


import java.util.LinkedList;
import java.util.Queue;

public class LogQueue implements IQueue {

    private static final int MAX_QUEUE_SIZE = 100;
    private static int ID = 0;

    private final Queue<String> q;
    private final int id;

    public LogQueue(){
        q = new LinkedList<>();
        id = ++LogQueue.ID;
    }

    /**
     * This method puts a message in the LogQueue. It is called by the Peer that wants to log a message.
     * @param msgVar
     */
    public synchronized void put(String msgVar){
        while(q.size() >= MAX_QUEUE_SIZE){
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        q.add(msgVar);
        notifyAll();
    }

    /**
     * This method returns the String message to be logged. It is called by the Logger when
     * it asks to log a message and there is at least a message available in the LogQueue.
     * @return
     */
    public synchronized String get(){
        while(q.isEmpty()){
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();           // notify the single thread of the Logger registered to this LogQueue
        return q.remove();
    }

    @Override
    public String toString(){
        return "@LogQueue_" + id;
    }

}
