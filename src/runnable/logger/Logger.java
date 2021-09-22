package runnable.logger;

import out.DefaultRecipient;
import out.IRecipient;
import queue.IQueue;
import queue.IQueueListener;
import queue.QueueListenerException;
import runnable.RunnableException;
import runnable.RunnableInstance;
import runnable.peer.Message;

public class Logger extends RunnableInstance implements IQueueListener {

    private static int ID = 0;

    private final int id;
    private final IRecipient out;        // where the logged events are actually logged
    private final Formatter scheme;     // the scheme to be used to format a log message
    private final Thread logger;
    private IQueue queue = null;
    private String msgToLog;

    /**
     * Return the default Logger instance.
     * @return
     */
    public static Logger getDefaultLogger(){
        return new Logger();
    }

    /**
     * Return the logger with the specified Formatter scheme but the default Recipient.
     * @param schemeVar
     * @return
     */
    public static Logger getLoggerWithFormatter(Formatter schemeVar){
        return new Logger(schemeVar);
    }

    /**
     * Return the logger with the specified Formatter scheme and Recipient.
     * @param outVar
     * @param schemeVar
     * @return
     */
    public static Logger getLoggerWithFormatterAndRecipient(IRecipient outVar, Formatter schemeVar){
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

    /**
     * This constructor method returns a Logger instance that logs events on the DefaultRecipient and
     * formats messages based on the schemeVar Formatter scheme.
     * @param schemeVar
     */
    private Logger(Formatter schemeVar){
        id = ++Logger.ID;
        out = new DefaultRecipient();
        scheme = schemeVar;
        logger = new Thread(this);
    }

    /**
     * This constructor method returns a Logger instance that logs events on the DefaultRecipient and formats
     * messages based on the default Formatter scheme.
     */
    private Logger(){
        id = ++Logger.ID;
        out = new DefaultRecipient();
        scheme = FormatterRepo.getInstance().getDefaultFormatter();
        logger = new Thread(this);
    }

    @Override
    public void registerToQueue(IQueue q) throws QueueListenerException {
        if(queue != null) {
            String msg = "Logger " + this + " is already registered on IQueue " + queue;
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
                doLog();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private void doLog(){
        out.write(
                scheme.formatMessage(msgToLog)
        );
        if(msgToLog.equals(Message.CLOSE_MSG))
            super.stop();
    }

    @Override
    public void runInstance() {
        try {
            if(queue == null)
                throw new Exception("Error: cannot start the Logger because it is not registered to any LogQueue.");
            super.runInstance();
            logger.start();
            System.out.println("STARTING LOGGER");
        } catch(RunnableException e) {
            System.err.println("RunnableException caught in runInstance() of Logger: " + e.getMessage());
        }catch(Exception e){
            System.err.println("Exception caught in runInstance() of Logger: " + e.getMessage());
        }
    }

    @Override
    public synchronized void resume(){
        try {
            super.resume();
            notify();
        } catch(RunnableException e) {
            System.err.println("RunnableException caught in resume() of Logger: " + e.getMessage());
            super.stop();
        }
    }

    @Override
    public String toString(){
        return "@LOGGER_" + id;
    }
}
