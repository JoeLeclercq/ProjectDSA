public class Table{
    private int size;
    private String name;
    private AbstractParty party;
    public Table(String name, int size){
        this.name = name;
        this.size = size;
        party = null;
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        return size;
    }
    public AbstractParty getParty(){
        return party;
    }
    public void setParty(AbstractParty party){
        this.party = party;
    }
    public String toString(){
        return "table " + name + " with " + size + " seats";
    }
}
