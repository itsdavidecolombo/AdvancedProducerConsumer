package runnable.logger;

import queue.IQueue;
import queue.IQueueListener;
import queue.QueueListenerException;
import runnable.peer.Message;

import java.util.ArrayList;
import java.util.List;

public class FormatterRepo implements IQueueListener {

    private final List<Formatter> availableFormatterSchemes;
    private final Formatter defaultFormatter;
    private IQueue queue = null;

    private static FormatterRepo theInstance = null;

    /**
     * This method return the instance of the FormatterRepo and if the instance is null, it creates it.
     * @return
     */
    public static FormatterRepo getInstance(){
        if(theInstance == null)
            theInstance = new FormatterRepo();
        return theInstance;
    }

    /**
     * Private constructor compliant to the Singleton design pattern.
     */
    private FormatterRepo(){
        availableFormatterSchemes = new ArrayList<>();
        defaultFormatter = Formatter.defaultFormatter();
    }

    /**
     * This method puts a message on the LogQueue that tells the Logger to stop.
     * This method can be called by main() when the FormatterRepo is no longer needed
     * because all the setup was done.
     */
    public void disconnect(){
        queue.put(Message.CLOSE_MSG);
    }

    @Override
    public void registerToQueue(IQueue q) throws QueueListenerException {
        if(queue != null){
            String msgExc = this + " is already registered on Queue " + queue;
            throw  new QueueListenerException(msgExc, QueueListenerException.ExceptionCause.ALREADY_REGISTERED);
        }
        queue = q;
    }

    /**
     * This method creates a new Formatter scheme if and only if no formatter scheme already exists with the
     * specified name.
     * If already exists, an Exception is thrown.
     * @param name
     * @param fArgs
     * @return
     * @throws Exception
     */
    public Formatter newFormatter(String name, String ... fArgs) throws QueueListenerException {
        isRegisteredToQueue();      // check if the FormatterRepo is registered on a Queue

        Formatter newIn = defaultFormatter;
        try {
            for(Formatter av : availableFormatterSchemes)
                if(av.equals(name))
                    throw new Exception("The name \"" + name + "\" is already in use.");
            newIn = Formatter.make(name, fArgs);
            availableFormatterSchemes.add(newIn);
            queue.put(this + " added new formatter named: " + newIn);
        } catch(Exception e) {
            queue.put(this + " caught Exception in newFormatter(): " + e.getMessage());
        }
        return newIn;
    }

    /**
     * This method search for the corresponding Formatter scheme based on the nameVar argument and if
     * any exists it returns that instance, otherwise throws an Exception.
     * @param nameVar
     * @return
     * @throws Exception
     */
    public Formatter getFormatterByName(String nameVar) throws QueueListenerException {
        isRegisteredToQueue();      // check: if it fails, throws and exception

        try {
            for(Formatter av : availableFormatterSchemes)
                if(av.equals(nameVar))
                    return av;
            throw new Exception("No Formatter found with name \"" + nameVar + "\"");
        } catch(Exception e) {
            queue.put(this + " caught Exception in getFormatterByName(): " + e.getMessage());
        }
        return defaultFormatter;
    }

    /**
     * This method checks if the FormatterRepository is registered on a Queue for logging events and if not
     * throws QueueListenerException
     * @throws QueueListenerException
     */
    private void isRegisteredToQueue() throws QueueListenerException {
        if(queue == null) {
            String msgExc = this + " is not registered to any Queue.";
            throw new QueueListenerException(msgExc, QueueListenerException.ExceptionCause.NOT_REGISTERED);
        }
    }

    /**
     * This method returns the default Formatter from the repository. The default Formatter is created
     * when the FormatterRepository is created.
     * This ensures that this method never returns null.
     * @return
     */
    public Formatter getDefaultFormatter(){
        return defaultFormatter;
    }

    @Override
    public String toString() { return "@FORMATTER_REPOSITORY"; }
}
