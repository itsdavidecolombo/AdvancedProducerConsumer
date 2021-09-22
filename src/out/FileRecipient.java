package out;

import runnable.peer.Message;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileRecipient implements IRecipient {

    private static final String ROOT_DIR = "./log/";
    private static final String LOG_FILE = "logfile";

    private final String dir;
    private final String filename;
    private final boolean append;
    private PrintWriter writer;

    public FileRecipient(String nameVar, String extension, boolean appendLog){
        dir = nameVar;
        filename = LOG_FILE+extension;
        append = appendLog;
        openFile();         // open the file
    }

    private void openFile() {
        try {
            String fullPath = ROOT_DIR + dir + "/" + filename;
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fullPath, append)));
        } catch(IOException e) {
            System.err.println("IOException occurred while opening FileRecipient " + filename + ": " + e.getMessage());
        }
    }

    private void closeFile() {
        writer.close();
    }

    @Override
    public void write(String msg) {
        if(msg.equals(Message.CLOSE_MSG))
            closeFile();
        else {
            writer.println(msg);
            writer.flush();
        }
    }
}
