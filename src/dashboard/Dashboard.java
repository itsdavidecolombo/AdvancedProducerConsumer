package dashboard;

import peer.message.Message;
import peer.message.MessageConstants;

public class Dashboard implements IDashboard {

    @Override
    public void addMessage(Message message) {
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
    }

    private void processKillMessage(Message message){
        System.out.println("<<< Dashboard received a KILL from " + message.getMessageSender() + " >>>");
    }

}
