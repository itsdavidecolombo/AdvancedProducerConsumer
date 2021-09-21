package runnable.logger;

import out.DefaultRecipient;
import out.Recipient;
import runnable.RunnableInstance;

public class Logger extends RunnableInstance {

    private final Recipient out;    // where the logged events are actually logged
    private Formatter scheme;       // the scheme to be used to format a log message
    private final Thread logger;

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
        logger = createLoggerThread();
    }

    /**
     * This constructor method returns a Logger instance that logs events on the DefaultRecipient and
     * formats messages based on the schemeVar Formatter scheme.
     * @param schemeVar
     */
    private Logger(Formatter schemeVar){
        out = new DefaultRecipient();
        scheme = schemeVar;
        logger = createLoggerThread();
    }

    /**
     * This constructor method returns a Logger instance that logs events on the DefaultRecipient and formats
     * messages based on the default Formatter scheme.
     */
    private Logger(){
        out = new DefaultRecipient();
        scheme = FormatterRepo.getInstance().getDefaultFormatter();
        logger = createLoggerThread();
    }

    /**
     * Overloaded constructor. Simply specify the output recipient.
     * @param outVar
     */
    public Logger(Recipient outVar){
        out = outVar;
        scheme = FormatterRepo.getInstance().getDefaultFormatter();
        logger = createLoggerThread();
    }

    private Thread createLoggerThread(){
        return new Thread(this);
    }

    @Override
    public void run() {
        // TODO: start the logger thread
    }

    /**
     * This method sets the formatter scheme to schemeVar.
     * @param schemeVar: the new formatter scheme
     */
    public void setFormatter(Formatter schemeVar){
        scheme = schemeVar;
    }

    /**
     * This method provides an interface for logging formatted String messages to the associated recipient.
     * @param msg: the message to be logged
     */
    public final void log(String msg){
        String formatterMsg = scheme.formatMessage(msg);
        out.write(formatterMsg);
    }

}
