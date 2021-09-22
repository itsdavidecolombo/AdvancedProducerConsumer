package runnable.logger;

import out.DefaultRecipient;
import out.Recipient;
import runnable.RunnableException;
import runnable.RunnableInstance;

public class Logger extends RunnableInstance {

    private final Recipient out;        // where the logged events are actually logged
    private final Formatter scheme;     // the scheme to be used to format a log message
    private final Thread logger;

    private boolean avaiableLog;
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
        out = outVar;
        scheme = formatterVar;
        logger = new Thread(this);
        avaiableLog = false;
        runInstance();
    }

    /**
     * This constructor method returns a Logger instance that logs events on the DefaultRecipient and
     * formats messages based on the schemeVar Formatter scheme.
     * @param schemeVar
     */
    private Logger(Formatter schemeVar){
        out = new DefaultRecipient();
        scheme = schemeVar;
        logger = new Thread(this);
        avaiableLog = false;
        runInstance();
    }

    /**
     * This constructor method returns a Logger instance that logs events on the DefaultRecipient and formats
     * messages based on the default Formatter scheme.
     */
    private Logger(){
        out = new DefaultRecipient();
        scheme = FormatterRepo.getInstance().getDefaultFormatter();
        logger = new Thread(this);
        avaiableLog = false;
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

                synchronized(this) {
                    while(!avaiableLog) {
                        System.err.println("======== LOGGER " + out.toString() +
                                " IS WAITING FOR AN AVAILABLE LOG ========");
                        wait();
                    }
                    doLog();        // do the log, process the log message
                    avaiableLog = false;
                    notify();
                }
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void runInstance() {
        try {
            super.runInstance();
            logger.start();
            System.out.println("STARTING LOGGER");
        } catch(RunnableException e) {
            System.err.println("RunnableException caught in runInstance() of Logger: "
                    + e.getMessage());
        }
    }

    @Override
    public synchronized void resume(){
        try {
            super.resume();
            notify();
        } catch(RunnableException e) {
            System.err.println("RunnableException caught in resume() of Logger: " +
                    e.getMessage());
            super.stop();
        }
    }

    /**
     * This method provides an interface for logging formatted String messages to the associated recipient.
     * @param msg: the message to be logged
     */
    public synchronized final void log(String msg){
        while(avaiableLog) {
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println("======== LOGGER " + out.toString() +
                " GOT A NEW LOG ========");
        avaiableLog = true; // new message available
        msgToLog = msg;     // set the message to be logged
        notify();           // notify the logger thread that is waiting for the message to be logged
    }

    private void doLog(){
        String formatterMsg = scheme.formatMessage(msgToLog);
        out.write(formatterMsg);
    }

}
