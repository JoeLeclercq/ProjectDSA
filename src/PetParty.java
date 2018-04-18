public class PetParty extends AbstractParty {
    public PetParty(String name, int size) {
        super(name, size);
    }

    public String toString() {
        return super.toString() + "(Pet)";
    }
}
