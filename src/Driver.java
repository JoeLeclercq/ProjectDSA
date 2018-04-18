import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Driver {
    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String args[]) {
        Restaurant restaurant = new Restaurant();
        String in;
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
        System.out.println("Select from the following menu:" +
                           "    0.	Close the restaurant." +
                           "    1.	Customer party enters the restaurant." +
                           "    2.	Customer party is seated and served." +
                           "    3.	Customer party leaves the restaurant." +
                           "    4.	Add a table." +
                           "    5.	Remove a table." +
                           "    6.	Display available tables." +
                           "    7.	Display info about waiting customer parties." +
                           "    8.	Display info about customer parties being served.");
        do {
            in = IOTools.promptLine("Make your menu selection now: ");
            switch (in) {
            case "0":
                System.exit(1);
                break;
            case "1":
                addParty(restaurant);
                break;
            case "2":
                seatParty(restaurant);
                break;
            case "3":
                partyLeaves(restaurant);
                break;
            case "4":
                addTable(restaurant);
                break;
            case "5":
                removeTable(restaurant);
                break;
            case "6":
                displayAvailableTables(restaurant);
                break;
            case "7":
                displayPartyWait(restaurant);
                break;
            case "8":
                displayPartyServed(restaurant);
                break;
            default:
                System.out.println("Not an acceptable menu selection");
            }
        } while(true);
    }
    /**
     * Helper method to add a party to the restaurant.
     * Input includes a customer name, the size of the party,
     * and whether they are a pet or no pet-party.
     * A party will be invalid if a party with the same name is
     * in the restaurant already.
     *
     * @param restaurant The Restaurant object that will have a party added to it.
     */
    public static void addParty(Restaurant restaurant) {
        String name;
        int size;
        boolean pet;
        name = IOTools.promptLine("Enter customer name : ");
        while(!restaurant.partyNameFree(name)) {
            System.out.println("There already exists a customer with this name in the restaurant.\n\tPlease select another name.");
            name = IOTools.promptLine("Enter customer name : ");
        }
        size = IOTools.promptPositiveInteger("Enter number of seats for customer " + name + ": ");
        pet = IOTools.promptYesNo("Does your party have pets?");
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
        if(restaurant.partyWaitingSize()==0) {
            System.out.println("No customers to serve!");
        }
        else {
            Table table;
            AbstractParty party;
            boolean notSeat = true;
            int count = 0;
            while(notSeat) {
                table = restaurant.seatParty(count++);
                party = table.getParty();
                if(table.getSize()>0) {
                    System.out.println("Serving customer " + party.getName() + " party of " + party.getSize() + (party instanceof PetParty?" (Pet)": " (No Pet)") + " at table " + table.getName() + " with " + table.getSize() + " seats");
                    notSeat = false;
                }
                else {
                    System.out.println("Could not find a table with " + party.getSize() + " seats for customer " + party.getName() + "!");
                }
                if(count==restaurant.partyWaitingSize()) {
                    System.out.println("No party can be served!");
                }
            }
        }
    }
    /**
     * Allows a party to leave their table.
     * Reads in a party name from the user.
     * Used to call the partyExits method of the restaurant class.
     * If the info received from partyExits is null then the party is said to not be seated.
     *
     * @param restaurant The Restaurant object where a party will attempt to leave.
     */
    public static void partyLeaves(Restaurant restaurant) {
        if(restaurant.inUseTableSize()==0) {
            System.out.println("No customer is being served!");
        }
        else {
            Object[] exitInfo;
            String party = IOTools.promptLine("Enter the name of the customer that wants to leave: ");
            exitInfo = restaurant.partyExits(party);
            if(exitInfo != null) {
                System.out.println("Table " + ((Table)exitInfo[0]).getName() + " with " + ((Table)exitInfo[0]).getSize() + " has been freed.");
                System.out.println("Customer " + party + " party of " + ((AbstractParty)exitInfo[1]).getSize() + "(" +(exitInfo[1] instanceof PetParty?"Pet Party":"No Pet") + " is leaving the restaurant");
            }
            else {
                System.out.println("The party " + party + " is not seated currently");
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
    public static void addTable(Restaurant restaurant) {
        addTable(restaurant, (IOTools.promptLine("To which section would you like to add this table?(P/N)").equals("P")?true:false));
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
    public static void addTable(Restaurant restaurant, boolean pet) {
        boolean isDone = false;
        String name;
        while(!isDone) {
            name = IOTools.promptLine("Enter table name: ");
            isDone = restaurant.tableNameFree(name, pet);
        }
        int size = IOTools.promptPositiveInteger("How many seats are at this table?");
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
    public static void removeTable(Restaurant restaurant) {
        String section = IOTools.promptLine("From which section do you want to remove a table from:(P/N) ");
        String name = IOTools.promptLine("Enter the name of the table to be removed: ");
        boolean[] removed = restaurant.removeTable(name,section);
        //where the first boolean is true for table exists in section, false otherwise; and the second is true for in use, false for not
        if(!removed[0]) {
            System.out.println("This table doesn't exists in the " + (section.equalsIgnoreCase("p")?"":"non" + " pet-friendly section! Please enter another table name."));
        }
        else {
            if(removed[1]) {
                System.out.println("Can't remove a table that is currently in use");
            }

            else {
                System.out.println("Table " + name + " has been removed");
            }
        }
    }
    public static void displayAvailableTables(Restaurant restaurant) {
        restaurant.availableTableDetails();
    }

    public static void displayPartyWait(Restaurant restaurant) {
        restaurant.petWaitingDetails();
        restaurant.noPetWaitingDetails();
    }

    public static void displayPartyServed(Restaurant restaurant) {
        restaurant.inUseTableDetails();
    }
}
