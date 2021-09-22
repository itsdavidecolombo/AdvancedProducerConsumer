package runnable.logger;


import java.util.StringTokenizer;

public class Formatter {

    private final String allowedSymbols = "$%&/=?^!_-.<>";

    private static final String DEFAULT_NAME = "DEFAULT";
    private static final String DEFAULT_IDTAG = "_";
    private static final String DEFAULT_NAMETAG = "@";

    public static int ID = 0;

    private final int id;
    private final String name;
    private final String[] formatterSymbols;
    private final StringBuilder sb;

    /**
     * Method that returns the default formatter scheme.
     * @return
     */
    static Formatter defaultFormatter(){
        return new Formatter(DEFAULT_NAME, DEFAULT_NAMETAG, DEFAULT_IDTAG);
    }

    /**
     * Method used for creating a new Formatter instance.
     * @param name: the name of the Formatter scheme
     * @param fArgs: the arguments of the Formatter scheme
     * @return: the instance of the Formatter scheme
     */
    static Formatter make(String name, String ... fArgs){
        return new Formatter(name, fArgs);
    }

    /**
     * Private constructor of the Formatter class.
     * This was done to enforce other classes interested in a Formatter scheme to call the "make()" method for
     * getting whatever Formatter scheme is wanted or "defaultFormatter()" for getting the default scheme.
     * @param nameVar
     * @param fArgs
     */
    private Formatter(String nameVar, String ... fArgs){
        name = nameVar;
        formatterSymbols = fArgs;
        id = ++Formatter.ID;
        sb = new StringBuilder();
    }

    /**
     * This method takes a String object and formats it accordingly to the scheme associated to this Formatter.
     * @param msgContent
     * @return
     */
    public String formatMessage(String msgContent){
        return msgContent;
    }

    /*
    private String parseKeyValuePair(String scheme){
        String[] tokens = scheme.split(";");
        String[] keyValuePair;
        String key, value;
        for(String tok : tokens){
            keyValuePair = tok.split(":");
            key = keyValuePair[0];
            value = keyValuePair[1];
        }
        return "";
    }
     */

    private void clearStringBuilder(){
        sb.setLength(0);
    }

    /**
     * This method compares the names of two Formatter schemes for equality.
     * @param nameToCompare
     * @return
     */
    public boolean equals(String nameToCompare){
        return name.equals(nameToCompare);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        for(int i = 0; i < formatterSymbols.length; i++)
            sb.append("Symbol").
                    append(i).
                    append(": ").
                    append(formatterSymbols[i]).
                    append("\n");
        return sb.toString();
    }

}
