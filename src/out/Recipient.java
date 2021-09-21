package out;

public interface Recipient {

    /**
     * This method is the common interface of a Recipient instance who's
     * main purpose is to write to a recipient.
     * @param msg
     */
    void write(String msg);

}
