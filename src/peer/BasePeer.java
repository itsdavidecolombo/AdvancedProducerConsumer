package peer;


public abstract class BasePeer extends RunnableInstance {

    private static int ID = 0;

    private final String name;
    private final int id;

    public BasePeer(String nameVar){
        name = nameVar;
        id = ++BasePeer.ID;
    }

}
