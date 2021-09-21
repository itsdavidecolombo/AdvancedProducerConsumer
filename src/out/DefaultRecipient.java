package out;

public class DefaultRecipient extends Recipient {

    @Override
    public void write(String msg) {
        System.out.print(msg);
    }
}
