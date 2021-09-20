package dashboard;

import peer.message.Message;

public interface IDashboard {

    /**
     * A peer can ship a message to the dashboard by calling shipMessage() method.
     */
    void addMessage(Message message);
}
