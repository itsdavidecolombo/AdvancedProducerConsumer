package runnable.logger;

import out.Recipient;

public class Logger {

    private final Recipient out;        // where the logged events are actually logged
    private final Formatter scheme;     // the scheme to be used to format a log message

    public Logger(Recipient outVar, Formatter formatterVar) {
        out = outVar;
        scheme = formatterVar;
    }

    public Logger(Recipient outVar){
        out = outVar;
        scheme = FormatterRepo.getInstance().getDefaultFormatter();
    }

    public final void log(String msg){
        // TODO: can change from String message to Event message
    }
}
