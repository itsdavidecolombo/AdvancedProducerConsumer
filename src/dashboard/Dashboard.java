package dashboard;

import peer.message.Message;
import peer.message.MessageConstants;

public class Dashboard implements IDashboard {

    @Override
    public void send(Message message) {
        switch(message.getMessageCode()){
            case MessageConstants.PING_MSG:
                System.out.println("<<< Dashboard received a PING from" + message.getMessageSender() + " >>>");
                break;
            case MessageConstants.SPAWN_MSG:
                System.out.println("<<< Dashboard received a SPAWN from" + message.getMessageSender() + " >>>");
                break;
            case MessageConstants.KILL_MSG:
                System.out.println("<<< Dashboard received a KILL from" + message.getMessageSender() + " >>>");
                break;
        }
    }

}
