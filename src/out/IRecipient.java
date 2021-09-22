package out;

public interface IRecipient {

    /**
     * This method is the common interface of a Recipient instance who's
     * main purpose is to write to a recipient.
     * @param msg
     */
    void write(String msg);

}
