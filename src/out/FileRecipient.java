package out;

public class FileRecipient extends Recipient {

    private final String filename;

    public FileRecipient(String nameVar){
        filename = nameVar;
    }

    @Override
    public void write(String msg) {

    }

    public void openFile(){

    }

    public void closeFile(){

    }
}
