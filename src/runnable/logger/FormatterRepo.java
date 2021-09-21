package runnable.logger;

import java.util.ArrayList;
import java.util.List;

public class FormatterRepo {

    private final List<Formatter> availableFormatterSchemes;
    private final Formatter defaultFormatter;

    private static FormatterRepo theInstance = null;

    /**
     * This method return the instance of the FormatterRepo and if the instance is null, it creates it.
     * @return
     */
    public static FormatterRepo getInstance(){
        if(theInstance == null)
            theInstance = new FormatterRepo();
        return theInstance;
    }

    /**
     * Private constructor compliant to the Singleton design pattern.
     */
    private FormatterRepo(){
        availableFormatterSchemes = new ArrayList<>();
        defaultFormatter = Formatter.defaultFormatter();
    }

    /**
     * This method creates a new Formatter scheme if and only if no formatter scheme already exists with the
     * specified name.
     * If already exists, an Exception is thrown.
     * @param name
     * @param fArgs
     * @return
     * @throws Exception
     */
    public Formatter newFormatter(String name, String ... fArgs) {
        Formatter newIn = defaultFormatter;
        try {
            for(Formatter av : availableFormatterSchemes)
                if(av.equals(name))
                    throw new Exception("The name \"" + name + "\" is already in use.");
            newIn = Formatter.make(name, fArgs);
            availableFormatterSchemes.add(newIn);
        } catch(Exception e) {
            System.err.println("Exception caught in newFormatter():" +
                    "\nMessage: " + e.getMessage());
        }
        return newIn;
    }

    /**
     * This method search for the corresponding Formatter scheme based on the nameVar argument and if
     * any exists it returns that instance, otherwise throws an Exception.
     * @param nameVar
     * @return
     * @throws Exception
     */
    public Formatter getFormatterByName(String nameVar) {
        try {
            for(Formatter av : availableFormatterSchemes)
                if(av.equals(nameVar))
                    return av;
            throw new Exception("No Formatter found with name \"" + nameVar + "\"");
        } catch(Exception e) {
            System.err.println("Exception caught in getFormatterByName():" +
                    "\nMessage: " + e.getMessage());
        }
        return defaultFormatter;
    }

    /**
     * This method returns the default Formatter from the repository. The default Formatter is created
     * when the FormatterRepository is created.
     * This ensures that this method never returns null.
     * @return
     */
    public Formatter getDefaultFormatter(){
        return defaultFormatter;
    }

}
