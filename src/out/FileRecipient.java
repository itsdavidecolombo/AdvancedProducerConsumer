package out;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileRecipient implements Recipient {

    private enum FileRecipientState{
        CREATED, OPENED, CLOSED
    }

    private final String filename;
    private FileRecipientState currentState;
    private BufferedWriter writer;

    public FileRecipient(String nameVar){
        filename = nameVar;
        currentState = FileRecipientState.CREATED;
    }

    /**
     * This method opens the file recipient. Every time this method is called, before opening the file
     * associated to this Recipient instance, it verifies if the file is already opened.
     * If it is, it throws a new Exception.
     * @throws Exception
     */
    public void openFile() throws Exception {
        if(isOpened())
            throw new Exception("File recipient \"" + filename + "\" is already opened!");
        // TODO: specify the append mode
        writer = new BufferedWriter(new FileWriter(filename));
    }

    /**
     * This method closes the file recipient. Every time this method is called, before closing the file
     * associated to this Recipient instance, it verifies if the file is opened.
     * If not, it throws an Exception.
     * @throws Exception
     */
    public void closeFile() throws Exception {
        if(!isOpened())
            throw new Exception("Cannot close file recipient \"" + filename + "\" when is not opened!");
        writer.close();
    }

    @Override
    public void write(String msg) {
        try {
            if(!isOpened())
                throw new Exception("");
            writer.write(msg);
        } catch(IOException e) {
            System.err.println("IOException caught while writing in file recipient \"" + filename + "\"");
            System.err.println("Error writing message: " + msg);
        } catch(Exception e) {
            System.err.println("Exception caught while writing in file recipient \"" + filename + "\"");
        }
    }

    public boolean isOpened(){
        return currentState == FileRecipientState.OPENED;
    }

}
