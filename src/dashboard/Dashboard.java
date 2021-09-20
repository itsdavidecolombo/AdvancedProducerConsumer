package dashboard;

import peer.message.Message;
import peer.message.MessageConstants;

import java.util.ArrayList;
import java.util.List;

public class Dashboard implements IDashboard {

    private final List<String> connectedPeers;

    public Dashboard(){
        connectedPeers = new ArrayList<>();
    }

    /**
     * This method is called by peers for adding messages to the Dashboard Queue.
     * Since we are dealing with a multi-threaded environment, the method must be synchronized.
     * @param message
     */
    @Override
    public synchronized void addMessage(Message message) {
        switch(message.getMessageCode()){
            case MessageConstants.PING_MSG:
                processPingMessage(message);
                break;
            case MessageConstants.SPAWN_MSG:
                processSpawnMessage(message);
                break;
            case MessageConstants.KILL_MSG:
                processKillMessage(message);
                break;
        }
    }

    private void processPingMessage(Message message){
        System.out.println("<<< Dashboard received a PING from " + message.getMessageSender() + " >>>");
    }

    private void processSpawnMessage(Message message){
        System.out.println("<<< Dashboard received a SPAWN from " + message.getMessageSender() + " >>>");
        connectedPeers.add(message.getMessageSender());
    }

    private void processKillMessage(Message message){
        System.out.println("<<< Dashboard received a KILL from " + message.getMessageSender() + " >>>");
        connectedPeers.remove(message.getMessageSender());
    }

}
