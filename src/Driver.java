import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Driver{
    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String args[]){
        Restaurant restaurant = new Restaurant();
        String in;
        int tables;
        System.out.println("Enter your restaurant configuration:");
        tables = IOTools.promptPositiveInteger("How many tables does your pet-friendly section have? ");
        for(int i = 0; i < tables; i++){
            addTable(restaurant, true);
        }  
        tables = IOTools.promptPositiveInteger("How many tables does your non-pet-friendly section have? ");
        for(int i = 0; i < tables; i++){
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
        do{
            in = IOTools.promptLine("Make your menu selection now: ");
            switch (in){
                case "0":
                    System.exit();
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
        }while(true);      
    }   

    public static void addParty(Restaurant restaurant){
        String name;
        int size;
        boolean pet;
        name = IOTools.promptLine("Enter customer name : ");
        while(!restaurant.partyNameFree(name)){
            System.out.println("There already exists a customer with this name in the restaurant.\n\tPlease select another name.");
            name = IOTools.promptLine("Enter customer name : ");
        }
        size = IOTools.promptPositiveInteger("Enter number of seats for customer " + name + ": ");
        pet = IOTools.promptYesOrNo("Does your party have pets?");
        if(pet){
            restaurant.partyEnters(new PetParty(name, size));
        }
        else{
            restaurant.partyEnters(new NoPetParty(name, size));
        }
    }

    public static void seatParty(Restaurant restaurant){
        restaurant.seatParty();        
    }

    public static void partyLeaves(Restaurant restaurant){
        Object[] exitInfo;
        String party = IOTools.promptLine("Enter the name of the customer that wants to leave: ");
        exitInfo = restaurant.partyExits(party);
        if(table != null){
            System.out.println("Table " + exitInfo[0].getName() + " with " + exitInfo[0].getSize() + " has been freed.");
            System.out.println("Customer " + party + " party of " + exitInfo[1].getSize() + "(" +(exitInfo[1] instanceof PetParty?"Pet Party":"No Pet") + " is leaving the restaurant");
        }
        else{
            System.out.println("The party " + party + " is not seated currently");
        }
    }
    //table and boolean
    public static void addTable(Restaurant restaurant, boolean pet){
        boolean isDone = false;
        String name;
        while(!isDone){
            name = IOTools.promptLine("Enter the name of the table: ");
            isDone = tableNameFree(name);
        }     
        int size = IOTolls.promptPositiveInteger("How many seats are at this table?");
        restaurant.addTable(new Table(name, size), pet);
    }

    public static void removeTable(Restaurant restaurant){

    }

    public static void displyAvailableTables(Restaurant restaurant){

    }

    public static void displayPartyWait(Restaurant restaurant){

    }

    public static void displayPartyServed(Restaurant restaurant){

    }
}   
