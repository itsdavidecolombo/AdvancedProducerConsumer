package runnable.logger;

import out.DefaultRecipient;
import out.Recipient;
import queue.IQueue;
import queue.QueueListener;
import queue.QueueListenerException;
import runnable.RunnableException;
import runnable.RunnableInstance;

public class Logger extends RunnableInstance implements QueueListener {

    private static int ID = 0;

    private final int id;
    private final Recipient out;        // where the logged events are actually logged
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
    public static Logger getLoggerWithFormatterAndRecipient(Recipient outVar, Formatter schemeVar){
        return new Logger(outVar, schemeVar);
    }

    /**
     * This constructor method returns a Logger instance that logs events on the outVar Recipient and formats
     * messages based on the formatterVar Formatter scheme.
     * @param outVar
     * @param formatterVar
     */
    private Logger(Recipient outVar, Formatter formatterVar) {
        id = ++Logger.ID;
        out = outVar;
        scheme = formatterVar;
        logger = new Thread(this);
        runInstance();
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
        runInstance();
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
        runInstance();
    }

    @Override
    public void registerToQueue(IQueue q) throws QueueListenerException {
        if(queue != null) {
            String msg = "Logger " + this + " is already registered on IQueue " + queue;
            throw new QueueListenerException(msg, QueueListenerException.ExceptionCause.ALREADY_REGISTERED);
        }
        queue = q;
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
    }

    @Override
    public void runInstance() {
        System.err.println("<<< STARTING THE LOGGER >>>");
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
