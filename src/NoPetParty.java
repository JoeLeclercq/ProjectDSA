public class NoPetParty extends AbstractParty {
    public NoPetParty(String name, int size) {
        super(name, size);
    }
/**
 * Returns the string representation of a Party with (No Pet) after it
 *
 * @return 
 */
    public String toString() {
        return super.toString() + "(No Pet)";
    }
}
