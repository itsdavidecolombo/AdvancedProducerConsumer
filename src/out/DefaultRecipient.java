package out;

public class DefaultRecipient implements Recipient {

    @Override
    public void write(String msg) {
        System.err.print(msg);
    }
}
