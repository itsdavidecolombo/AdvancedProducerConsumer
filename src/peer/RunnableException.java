package peer;

public class RunnableException extends Exception {

    /**
     * Define the cause of the exception following the enum fashion
     */
    public enum ExcCause{
        UNDEFINED, ILLEGAL_STATE_TRANSITION;
    }

    private ExcCause cause;
    private String msg;

    public RunnableException(String msgVar, ExcCause causeVar){
        msg = msgVar;
        cause = causeVar;
    }

    @Override
    public String getMessage(){
        String excMsg;
        switch(cause){
            case ILLEGAL_STATE_TRANSITION:
                excMsg = "Detected " + ExcCause.ILLEGAL_STATE_TRANSITION.name() + " cause.\n" +
                         "The state transition you try to do is not allowed.";
                break;
            case UNDEFINED:
                excMsg = "Undefined cause.";
                break;
        }
        excMsg = "Message: " + msg;
        return excMsg;
    }
}
