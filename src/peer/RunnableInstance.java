package peer;

/**
 * Abstract class that defined the interface for all the runnable instances in the project.
 *
 * A runnable instance has a state and the methods for managing that state.
 */
public abstract class RunnableInstance implements Runnable {

    private RunnableState currentState;

    /**
     * The only thing that does the RunnableInstance constructor is initialize the currentState variable.
     */
    public RunnableInstance(){
        currentState = RunnableState.CREATED;
    }

    /**
     * You can start a RunnableInstance from it's CREATED state by calling the start() method.
     */
    public void runInstance() throws RunnableException {
        if(currentState != RunnableState.CREATED) {
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.RUNNING;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.RUNNING;
    }

    /**
     * You can stop a RunnableInstance from it's RUNNING or PAUSED state by calling the stop() method.
     */
    public void stop() throws RunnableException {
        if(currentState == RunnableState.CREATED ||
           currentState == RunnableState.STOPPED) {
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.STOPPED;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.STOPPED;
    }

    /**
     * You can pause a RunnableInstance from it's RUNNING state by calling the pause() method.
     */
    public synchronized void pause() throws RunnableException {
        if(currentState != RunnableState.RUNNING) {
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.PAUSED;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.PAUSED;
    }

    /**
     * You can resume a RunnableInstance from it's paused state by calling the resume() method.
     */
    public synchronized void resume() throws RunnableException {
        if(currentState != RunnableState.PAUSED){
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.RUNNING;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.RUNNING;
    }

    public boolean isRunning(){
        return currentState == RunnableState.RUNNING;
    }

    public boolean isStopped(){
        return currentState == RunnableState.STOPPED;
    }

    public boolean isPaused(){
        return currentState == RunnableState.PAUSED;
    }
}
