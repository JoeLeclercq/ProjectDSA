public class Table {
    private int size;
    private String name;
    private AbstractParty party;
    public Table(String name, int size) {
        this.name = name;
        this.size = size;
        party = null;
    }
    /**
 * Returns the name of the table 
 *
 * @return The name of the table.
 */
    public String getName() {
        return name;
    }
/**
 * Returns the size of the table.
 *
 * @return The size of the table.
 */
    public int getSize() {
        return size;
    }
/**
 * Returns party seated at the table.
 *
 * @return The party seated at the table.
 */
    public AbstractParty getParty() {
        return party;
    }
/**
 * Set the party seated at the table.
 *
 * @param party party to be seated at the table
 */
    public void setParty(AbstractParty party) {
        this.party = party;
    }
/**
 * Returns a String representation of the table
 *
 * @return a string containing the table name and number of seats
 */
    public String toString() {
        return "table " + name + " with " + size + " seats";
    }
}
