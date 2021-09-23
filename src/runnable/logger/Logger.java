package runnable.logger;

import out.IRecipient;
import queue.IQueue;
import queue.IQueueListener;
import queue.QueueListenerException;
import repository.Formatter;
import runnable.RunnableException;
import runnable.RunnableInstance;
import runnable.peer.Message;

import java.time.format.DateTimeFormatter;

public class Logger extends RunnableInstance implements IQueueListener {

    private static int ID = 0;

    private final int id;
    private final IRecipient out;           // where the logged events are actually logged
    private final Formatter scheme;         // the scheme to be used to format a log message
    private final Thread logger;
    private IQueue queue = null;
    private String msgToLog;

    /**
     * Return the logger with the specified Formatter scheme and Recipient.
     * @param outVar
     * @param schemeVar
     * @return
     */
    public static Logger getLogger(IRecipient outVar, Formatter schemeVar){
        return new Logger(outVar, schemeVar);
    }

    /**
     * This constructor method returns a Logger instance that logs events on the outVar Recipient and formats
     * messages based on the formatterVar Formatter scheme.
     * @param outVar
     * @param formatterVar
     */
    private Logger(IRecipient outVar, Formatter formatterVar) {
        id = ++Logger.ID;
        out = outVar;
        scheme = formatterVar;
        logger = new Thread(this);
    }

    @Override
    public void registerQueue(IQueue q) throws QueueListenerException {
        if(queue != null) {
            String msg = this + " is already registered on Queue " + queue;
            throw new QueueListenerException(msg, QueueListenerException.ExceptionCause.ALREADY_REGISTERED);
        }
        queue = q;
        runInstance();
    }

// ===============================================================================================

//                                          THREAD METHODS

// ===============================================================================================
    @Override
    public void run() {
        while(!super.isStopped()){
            try {
                synchronized(this) {
                    while(isPaused()) {
                        wait();
                    }
                }
                // the logger calls the method get() and wait until a new message is available for logging.
                msgToLog = queue.get();
                processLogMessage();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private void processLogMessage(){
        if(msgToLog.equals(Message.CLOSE_MSG))
            super.stop();
        else
            out.write(scheme.formatMessage(msgToLog));
    }

    @Override
    public void runInstance() {
        try {
            if(queue == null) {
                String msgExc = this + " not registered on any Queue.";
                throw new QueueListenerException(msgExc, QueueListenerException.ExceptionCause.NOT_REGISTERED);
            }
            super.runInstance();
            logger.start();
        } catch(RunnableException | QueueListenerException e) {
            System.err.println(this + ": " + e.getClass().getName() +
                    " caught in runInstance() of Logger: " + e.getMessage());
        }
    }

    @Override
    public synchronized void resume(){
        try {
            super.resume();
            notify();
        } catch(RunnableException e) {
            System.err.println(this + ": " + e.getClass().getName() +
                    " caught in resume() of Logger: " + e.getMessage());
            super.stop();
        }
    }

    @Override
    public String toString(){
        return "@LOGGER_" + id;
    }
}
