public abstract class AbstractParty{
    protected String name;
    protected int size;
    
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
    
    public String toString(){
        return "Customer " + name + " party of " + size;
    }
}
