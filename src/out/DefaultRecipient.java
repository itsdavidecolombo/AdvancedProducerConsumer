package out;

public class DefaultRecipient implements IRecipient {

    @Override
    public void write(String msg) {
        System.err.println(msg);
    }
}
