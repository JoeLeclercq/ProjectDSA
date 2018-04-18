public abstract class AbstractParty {
    protected String name;
    protected int size;
    public AbstractParty(String name, int size) {
        this.name = name;
        this.size = size;
    }
    /**
     * Returns the name of the party
     *
     * @return the name of the party
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the size of the size
     *
     * @return the size of the party
     */
    public int getSize() {
        return size;
    }
    /**
     * Returns a string representation of the party
     *
     * @return a string representation of a party with its name and size
     */
    public String toString() {
        return "Customer " + name + " party of " + size;
    }
}
