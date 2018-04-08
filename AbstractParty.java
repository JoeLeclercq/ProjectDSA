public abstract class AbstractParty{
    private String name;
    private int size;
    
    public AbstractParty(String name, int size){
        this.name = name;
        this.size = size;
    }   
    
    public String getName(){
        return name;
    }

    public int getSize(){
        return size;
    }
}
