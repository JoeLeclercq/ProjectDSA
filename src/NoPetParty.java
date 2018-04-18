public class NoPetParty extends AbstractParty {
    public NoPetParty(String name, int size){
        super(name, size);
    }
    
    public String toString(){
        return super.toString() + "(No Pet)";
    }
}
