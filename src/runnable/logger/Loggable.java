package runnable.logger;

public abstract class Loggable {

    private Logger logger = null;

    /**
     * This method is used to set the Logger on a loggable instance.
     * If the logger is null, it is set otherwise an Exception is thrown.
     */
    public final void setLogger(Logger loggerVar){
        try {
            if(logger != null)
                throw new Exception("Logger is already set.");
            logger = loggerVar;
        }catch(Exception ex){
            System.err.println("Caught Exception in setLogger(): " + ex.getMessage());
        }
    }

    /**
     * This method provides an interface for logging events.
     * @param msgVar
     */
    public final void logEvent(String msgVar) {
        try {
            if(logger == null)
                throw new Exception("Logger is null!");
            logger.log(msgVar);
        } catch(Exception e) {
            System.err.println("Exception caught in logEvent(): " + e.getMessage());
        }
    }
}
