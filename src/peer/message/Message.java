package peer.message;

public class Message {

    private static int ID = 0;

    private final int messageID;
    private final String messageContent;
    private final String messageCode;
    private final String messageSender;

    public Message(String msgcodeVar, String msgcontentVar, String msgsenderVar){
        messageCode = msgcodeVar;
        messageContent = msgcontentVar;
        messageSender = msgsenderVar;
        messageID = ++Message.ID;
    }

    /**
     * Provides the code of a message
     * @return
     */
    public String getMessageCode(){
        return messageCode;
    }

    /**
     * Provides the message content of a message
     * @return
     */
    public String getMessageContent(){
        return messageContent;
    }

    /**
     * Provides the ID of a message
     * @return
     */
    public int getMessageID(){
        return messageID;
    }

    /**
     * Provides the name of the sender of a message
     * @return
     */
    public String getMessageSender(){
        return messageSender;
    }
}
