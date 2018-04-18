import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Driver {
    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String args[]) throws IOException {
        Restaurant restaurant = new Restaurant();
        int command;
        int tables;
        System.out.println("Enter your restaurant configuration:");
        tables = IOTools.promptPositiveInteger("How many tables does your pet-friendly section have? ");
        for(int i = 0; i < tables; i++) {
            addTable(restaurant, true);
        }
        tables = IOTools.promptPositiveInteger("How many tables does your non-pet-friendly section have? ");
        for(int i = 0; i < tables; i++) {
            addTable(restaurant, false);
        }
        System.out.println("Select from the following menu:\n" +
                           "    0.	Close the restaurant.\n" +
                           "    1.	Customer party enters the restaurant.\n" +
                           "    2.	Customer party is seated and served.\n" +
                           "    3.	Customer party leaves the restaurant.\n" +
                           "    4.	Add a table.\n" +
                           "    5.	Remove a table.\n" +
                           "    6.	Display available tables.\n" +
                           "    7.	Display info about waiting customer parties.\n" +
                           "    8.	Display info about customer parties being served.");
        do {
            command = IOTools.promptInteger("Make your menu selection "
                + "now: ", 0, 8);
            switch (command) {
                case 0:
                    System.exit(1);
                    break;
                case 1:
                    addParty(restaurant);
                    break;
                case 2:
                    seatParty(restaurant);
                    break;
                case 3:
                    partyLeaves(restaurant);
                    break;
                case 4:
                    addTable(restaurant);
                    break;
                case 5:
                    removeTable(restaurant);
                    break;
                case 6:
                    displayAvailableTables(restaurant);
                    break;
                case 7:
                    displayPartyWait(restaurant);
                    break;
                case 8:
                    displayPartyServed(restaurant);
                    break;
                default:
                    System.out.println("Not an acceptable menu selection");
            }
            System.out.println();
        } while(true);
    }
    /**
     * Helper method to add a party to the restaurant.
     * Input includes a customer name, the size of the party,
     * and whether they are a pet or no pet party.
     * A party will be invalid if a party with the same name is
     * in the restaurant already.
     *
     * @param restaurant The Restaurant object that will have a party added to it.
     */
    public static void addParty(Restaurant restaurant) throws IOException {
        String name;
        int size;
        boolean pet;
        name = IOTools.promptLine("Enter customer name : ");
        while(!restaurant.partyNameFree(name)) {
            System.out.println("There already exists a customer with this name in the restaurant.\n\tPlease select another name.");
            name = IOTools.promptLine("Enter customer name: ").trim();
        }
        size = IOTools.promptPositiveInteger("Enter number of seats for customer " + name + ": ");
        pet = IOTools.promptYesNo("Does your party have pets? (y/n): ");
        if(pet) {
            restaurant.partyEnters(new PetParty(name, size));
        }
        else {
            restaurant.partyEnters(new NoPetParty(name, size));
        }
    }
    /**
     * Helper method to seat a party already within the restaurant.
     * Used to call the seatParty method of the Restaurant object.
     * The program will attempt to find a table for any customer
     * starting with the customers that arrived first.
     *
     * @param restaurant The Restaurant object where a party will be seated.
     */
    public static void seatParty(Restaurant restaurant) {
        System.out.println(restaurant.seatParty());
    }
    /**
     * Allows a party to leave their table.
     * Reads in a party name from the user.
     * Used to call the partyExits method of the restaurant class.
     * If the info received from partyExits is null then the party is said to not be seated.
     *
     * @param restaurant The Restaurant object where a party will attempt to leave.
     */
    public static void partyLeaves(Restaurant restaurant)
            throws IOException {
        if(restaurant.seatedParties()==0) {
            System.out.println("No customer is being served!");
        }
        else {
            String party = IOTools.promptLine("Enter the name of the "
                    + "customer that wants to leave: ").trim();
            String exitInfo = restaurant.partyExits(party);
            if(exitInfo != null) {
                System.out.println(exitInfo);
            }
            else {
                System.out.println("The party " + party + " is not "
                    + "seated currently");
            }
        }
    }
    /**
     * Overloaded addTable method allows the user to decide if the table
     * has pets or not. This method calls the other addTable with a boolean
     * representing the table's pet preference.
     *
     * @param restaurant The Restaurant object that will be passed to the other addTable mathod.
     */
    public static void addTable(Restaurant restaurant) throws IOException {
        addTable(restaurant, 
            IOTools.promptLine("To which section would you like to add " 
                + "this table? (P/N): ").equalsIgnoreCase("P"));
    }

    /**
     * Adds a table to the restaurant.
     * Reads in a table name from the user and calls tableNameFree to
     * determine the validity of the name. If valid, the size of the
     * table is then input and the restaurant object's method addTable
     * is called.
     *
     * @param restaurant The Restaurant object that has a table added.
     * @param pet boolean, true for pet table, false for no pet
     */
    public static void addTable(Restaurant restaurant, boolean pet) 
            throws IOException {
        boolean isDone = false;
        String name = null;
        while(!isDone) {
            name = IOTools.promptLine("Enter table name: ").trim();
            isDone = restaurant.tableNameFree(name, pet);
            if(!isDone){
                System.out.println("This table already exists! Please "
                    + "enter another table name.");
            }
        }
        int size = IOTools.promptPositiveInteger("Enter number of seats: ");
        restaurant.addTable(new Table(name, size), pet);
    }
    /**
     * Removes a table from the restaurant if it is not currently in use.
     * Reads in which section the user wishes to remove a table from and
     * then reads in the table name to be removed. If the table does not
     * exist, or is in a different section, then a message saying the
     * table does not exist is printed. If the table is in use this is output
     * and the table is not removed. Otherwise the table is removed from its
     * section.
     *
     * @param restaurant The Restaurant object that will have a table removed.
     */
    public static void removeTable(Restaurant restaurant)
            throws IOException {
        System.out.println("You are now removing a table");
        String section = IOTools.promptLine("From which section would you "
                + "like to remove this table? (P/N): ");
        while(!section.equalsIgnoreCase("P") && !section.equalsIgnoreCase("N")){
            section = IOTools.promptLine("Invalid entry, enter P or N: ");
        }
        boolean pet = section.equalsIgnoreCase("P");
        String name = IOTools.promptLine("Enter table name: ");
        System.out.println(restaurant.removeTable(name,pet));
    }

    /**
    * Method to print out the table that do not have a party.
    * @param restaurant The Restaurant whose table details will be printed out.
    */
    public static void displayAvailableTables(Restaurant restaurant) {
        System.out.println(restaurant.availableTableDetails());
    }

    /**
     * Method to print out the details of the parties currently not seated.
     * First the pet parties details will be printed, then the no pet party
     * details will be printed.
     * @param restaurant The Restaurant whose party details will be printed out.
     */
    public static void displayPartyWait(Restaurant restaurant) {
        System.out.println(restaurant.waitingDetails());
    }

    /**
     * Method to print out the information of the current tables in use
     * and the parties at the tables.
     * @param restaurant The Restaurant whose in use table details will be printed out.
     */
    public static void displayPartyServed(Restaurant restaurant) {
        System.out.println(restaurant.inUseTableDetails());
    }
}
