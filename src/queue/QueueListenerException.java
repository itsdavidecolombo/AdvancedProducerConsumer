package queue;

public class QueueListenerException extends Exception {

    public enum ExceptionCause{
        ALREADY_REGISTERED, NOT_REGISTERED
    }

    private final String msg;
    private final ExceptionCause cause;

    public QueueListenerException(String msgVar, ExceptionCause causeVar){
        msg = msgVar;
        cause = causeVar;
    }

    public String getMessage(){
        return "CAUSE: " + cause + ". MSG: " + msg;
    }
}
