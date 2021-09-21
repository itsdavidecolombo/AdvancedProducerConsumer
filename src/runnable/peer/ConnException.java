package runnable.peer;

public class ConnException extends Exception {

    private final String msg;
    public ConnException(String msgVar){
        msg = msgVar;
    }

    public String getMessage(){
        return msg;
    }
}
