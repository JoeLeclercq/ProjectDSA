public class PetParty extends AbstractParty {
    public PetParty(String name, int size) {
        super(name, size);
    }
    /**
     * Returns a string representation of a Pet Party
     *
     * @return string representation of party with (Pet) after
     */
    public String toString() {
        return super.toString() + "(Pet)";
    }
}
