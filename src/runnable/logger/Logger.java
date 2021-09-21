package runnable.logger;

import out.Recipient;

public class Logger {

    private final Recipient out;        // where the logged events are actually logged
    private Formatter scheme;     // the scheme to be used to format a log message

    /**
     * Constructor with double arguments. You must specify the output recipient and the formatter scheme.
     * @param outVar
     * @param formatterVar
     */
    public Logger(Recipient outVar, Formatter formatterVar) {
        out = outVar;
        scheme = formatterVar;
    }

    /**
     * Overloaded constructor. Simply specify the output recipient.
     * @param outVar
     */
    public Logger(Recipient outVar){
        out = outVar;
        scheme = FormatterRepo.getInstance().getDefaultFormatter();
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
