package runnable;

import runnable.logger.Loggable;

/**
 * Abstract class that defined the interface for all the RunnableInstances in the project.
 *
 * A RunnableInstance has a state and the methods for managing that state.
 * A concrete subclass of RunnableInstance can be passed to a Thread object for creating the proper behaviour.
 */
public abstract class RunnableInstance extends Loggable implements Runnable {

    /**
     * Enum that defines the allowed state in which a RunnableInstance can be found while is alive.
     */
    private enum RunnableState{
        CREATED, RUNNING, PAUSED, STOPPED
    }

    private RunnableState currentState;

    public RunnableInstance(){
        currentState = RunnableState.CREATED;
    }

    /**
     * The purpose of this method is to check if the RunnableInstance upon which is invoked is in the CREATED state.
     * If it is, then the state is changed into RUNNING.
     * Override this method to effectively start a Thread associated to a RunnableInstance.
     * @throws RunnableException
     */
    public void runInstance() throws RunnableException {
        if(currentState != RunnableState.CREATED) {
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.RUNNING;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.RUNNING;
    }

    /**
     * This method changes the current state of the RunnableInstance to STOPPED whatever the current state is.
     */
    public synchronized void stop() {
        this.currentState = RunnableState.STOPPED;
    }

    /**
     * The purpose of this method is to check if the RunnableInstance upon which is invoked is in the RUNNING.
     * If it is, then the state is changed into PAUSED.
     * A subclass of RunnableInstance is not mandated to override this method.
     * @throws RunnableException
     */
    public synchronized void pause() throws RunnableException {
        if(currentState != RunnableState.RUNNING) {
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.PAUSED;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.PAUSED;
    }

    /**
     * The purpose of this method is to check if the RunnableInstance upon which is invoked is in the PAUSED.
     * If it is, then the state is changed into RUNNING.
     * A subclass of RunnableInstance must override this method and call the notify() method to awake the thread
     * that was in the paused state.
     * @throws RunnableException
     */
    public synchronized void resume() throws RunnableException {
        if(currentState != RunnableState.PAUSED){
            String msg = "Current state: " + currentState + ",\tDesired state: " + RunnableState.RUNNING;
            throw new RunnableException(msg, RunnableException.ExcCause.ILLEGAL_STATE_TRANSITION);
        }
        this.currentState = RunnableState.RUNNING;
    }

    /**
     * Method for checking if the RunnableInstance is in the STOPPED state
     * @return
     */
    public boolean isStopped(){
        return currentState == RunnableState.STOPPED;
    }

    /**
     * Method for checking if the RunnableInstance is in the PAUSED state
     * @return
     */
    public boolean isPaused(){
        return currentState == RunnableState.PAUSED;
    }
}
