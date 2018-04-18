public class Restaurant {
    private ListRA<AbstractParty> partiesWaiting;
    private ListRA<Table> openPetTables;
    private ListRA<Table> openNoPetTables;
    private ListRA<Table> inUseTables;

    public Restaurant() {
        partiesWaiting = new ListRA<AbstractParty>();
        openPetTables = new ListRA<Table>();
        openNoPetTables = new ListRA<Table>();
        inUseTables = new ListRA<Table>();
    }

    /**
     * Returns the number of parties being served.
     * @return The number of parties.
     */
    public int seatedParties() {
        return inUseTables.size();
    }

    /**
     * Adds a party to the list of waiting parties.
     */
    public void partyEnters(AbstractParty party) {
        partiesWaiting.add(party);
    }

    public String seatParty() {
        int sizeWaiting = partiesWaiting.size();
        if (sizeWaiting == 0)
            return "No customers to serve!";
        boolean seated = false;
        int largestOpenPetSize = 
                openPetTables.size() > 0
                ? openPetTables.get(openPetTables.size() - 1).getSize()
                : 0;
        int largestOpenNoPetSize = 
                openNoPetTables.size() > 0
                ? openNoPetTables.get(openNoPetTables.size() - 1).getSize()
                : 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sizeWaiting && !seated; i++) {
            AbstractParty party = partiesWaiting.get(i);
            int partySize = party.getSize();
            String partyName = party.getName();
            ListRA<Table> openList = (party instanceof PetParty)
                    ? openPetTables : openNoPetTables;
            boolean openTable = (party instanceof PetParty)
                    ? partySize <= largestOpenPetSize
                    : partySize <= largestOpenNoPetSize;
            
            if (openTable) {
                int index = searchOpenTables(openList, partySize);
                if (index < 0) 
                    index = -index - 1;
                Table table = openList.get(index);
                openList.remove(index);
                partiesWaiting.remove(i);
                table.setParty(party);
                inUseTables.add(
                        -searchInUseTables(partyName)-1, table); 

                sb.append("Serving ").append(party.toString())
                    .append(" at ").append(table.toString());

                seated = true;
            } else {
                sb.append("Could not find a table with ")
                    .append(partySize)
                    .append(partySize > 1 
                            ? " seats for customer "
                            : " seat for customer ")
                    .append(partyName).append("!");
            }
        }
        
        return sb.toString();
    }

    /**
     * The restaurant exits the party with the given name and frees
     * the table they're at, if they exist.
     * @param name The name of the party.
     * @return A string containing information about the party who exited,
     * if it exists, or null otherwise.
     */
    public String partyExits(String name) {
        int index = searchInUseTables(name.toUpperCase());

        if (index >= 0) {
            Table table = inUseTables.get(index);
            AbstractParty party = table.getParty();
            boolean petParty = party instanceof PetParty;

            inUseTables.remove(index);
            table.setParty(null);

            if (petParty) {
                int addIndex = searchOpenTables(openPetTables,
                    table.getSize());
                openPetTables.add(addIndex >= 0 
                        ? addIndex
                        : -addIndex - 1,
                    table);
            } else {
                int addIndex = searchOpenTables(openNoPetTables,
                    table.getSize());
                openNoPetTables.add(addIndex >= 0
                        ? addIndex
                        : -addIndex - 1,
                    table);
            }
            return partyLeavesString(table, party);
        } else {
            return null;
        }
    }

    /**
     * Returns a String containing information on the party exiting the 
     * table specified.
     * @param table The Table to use information from.
     * @param party The party to use information from.
     * @return A string that tells the table name, the seats freed, and the
     * customer leaving the restaurant.
     */
    private String partyLeavesString(Table table, AbstractParty party) {
        StringBuilder sb = new StringBuilder();

        sb.append("Table ").append(table.getName()).append(" with ")
            .append(table.getSize())
            .append(table.getSize() > 1 
                        ? " seats has been freed." 
                        : " seat has been freed.").append("\n");
        sb.append("Customer ").append(party.getName()).append(" party of ")
            .append(party.getSize())
            .append(party instanceof PetParty
                        ? " (Pet) is leaving the restaurant."
                        : " (NoPet) is leaving the restaurant.");

        return sb.toString();
    }

    /**
     * Adds a table into the given section.
     * Precondition: No table already exists with the given name in
     * the specified section.
     * @param Table The Table to be added
     * @param petSection True if this table goes in the Pet section,
     * false otherwise.
     */
    public void addTable(Table table, boolean petSection) {
        ListRA<Table> openList = petSection
                ? openPetTables
                : openNoPetTables;
        int index = searchOpenTables(openList, table.getSize());
        if (index < 0)
            index = -index - 1;

        openList.add(index, table);
    }

    /**
     * Removes a table with the specified name from the specified section,
     * if it exists and is possible. Will not remove a table that is 
     * currently in-use or one that is in the wrong section.
     * @param name The name of the table to remove.
     * @param petSection True if the table should be removed from the pet
     * section, false otherwise.
     * @return A string with information regarding the table's removal.
     */
    public String removeTable(String name, boolean petSection) {
        ListRA<Table> openList = petSection ? openPetTables : openNoPetTables;
        Table correctTable = null;
        int openSize = openList.size();
        int inUseSize = inUseTables.size();

        for (int i = 0; i < openSize && correctTable == null; i++) {
            if (openList.get(i).getName().equalsIgnoreCase(name)) {
                correctTable = openList.get(i);
                openList.remove(i);
            }
        }

        if (correctTable == null) {
            for (int i = 0; i < inUseSize && correctTable == null; i++) {
                if (inUseTables.get(i).getName().equalsIgnoreCase(name)
                        && (petSection 
                        ? inUseTables.get(i).getParty() instanceof PetParty
                        : inUseTables.get(i).getParty() instanceof NoPetParty)) {
                    correctTable = inUseTables.get(i);
                }
            }
            
            if (correctTable == null) { //no table with the given name
                return "This table doesn't exist in the "
                    + (petSection ? "pet-friendly" : "non pet-friendly")
                    + " section!";
            } else { //in use table with the given name
                return "Can't remove a table that is currently in use!";
            }
        } else {
            return "Table " + correctTable.getName()
                + " has been removed.";
        }
    }

    /**
     * Searches for the specified item within the in use tables
     * using a binary search.
     * @param name The name of the party to search for.
     * @return The index of the item within the list, if it exists.
     * If it is not within the list, returns -index - 1 for the index where
     * the item should exist.
     */
    private int searchInUseTables(String name) {
        name = name.toUpperCase();
        int listSize = inUseTables.size();
        int min = 0;
        int max = listSize - 1;
        int mid = (max + min) / 2;
        String midItem =
            listSize == 0 ? null
                : inUseTables.get(mid).getParty().getName().toUpperCase();

        while (min < max) {
            if (midItem.compareTo(name) >= 0) {
                max = mid;
            } else {
                min = mid + 1;
            }
            mid = (max + min) / 2;
            midItem = inUseTables.get(mid).getParty().getName()
                .toUpperCase();
        }

        if (listSize != 0 && name.equalsIgnoreCase(midItem)) {
            return mid;
        } else {
            if (mid == listSize-1 && name.compareTo(midItem) > 0)
                mid++;
            return -mid - 1;
        }
    }

    /**
     * Searches for a table with the specified list within the given list
     * using a binary search.
     * @param list The list to look through, must be sorted by size.
     * @param size The size of a table to search for.
     * @return The index of a table within the list, if one exists.
     * If it is not within the list, returns -index - 1 for the index where
     * the table should exist.
     */
    private int searchOpenTables(ListRA<Table> list, int size) {
        int listSize = list.size();
        int min = 0;
        int max = listSize - 1;
        int mid = (max + min) / 2;
        int midItem =
            listSize == 0 ? -1 : list.get(mid).getSize();


        while (min < max) {
            if (midItem >= size) {
                max = mid;
            } else {
                min = mid + 1;
            }
            mid = (max + min) / 2;
            midItem = list.get(mid).getSize();
        }

        if (listSize != 0 && size == midItem) {
            return mid;
        } else {
            if (mid == listSize-1 && size >= midItem)
                mid++;
            return -mid - 1;
        }
    }

    /**
     * Checks whether a party with a given name exists in the restaurant.
     * @param name The name to search for.
     * @return True if a party waiting or seated has the given name,
     * false otherwise.
     */
    public boolean partyNameFree(String name) {
        boolean found = false;
        int size = partiesWaiting.size();

        for (int i = 0; i < size && !found; i++) {
            if (partiesWaiting.get(i).getName().equalsIgnoreCase(name)) {
                found = true;
            }
        }

        size = inUseTables.size();
        for (int i = 0; i < size && !found; i++) {
            if (inUseTables.get(i).getParty().getName()
                .equalsIgnoreCase(name)) {
                found = true;
            }
        }

        return !found;
    }

    public boolean tableNameFree(String name, boolean petSection) {
        boolean found = false;
        ListRA<Table> openList = 
                petSection ? openPetTables : openNoPetTables;
        int size = openList.size();

        for (int i = 0; i < size && !found; i++) {
            if (openList.get(i).getName().equalsIgnoreCase(name)) {
                found = true;
            }
        }

        size = inUseTables.size();
        for (int i = 0; i < size && !found; i++) {
            if (inUseTables.get(i).getName().equalsIgnoreCase(name)
                    && (petSection
                    ? inUseTables.get(i).getParty() instanceof PetParty
                    : inUseTables.get(i).getParty() instanceof NoPetParty)) {
                found = true;
            }
        }

        return !found;
    }

    public String waitingDetails() {
        if (partiesWaiting.size() == 0) {
            return "No customers are waiting for tables!";
        }
    
        StringBuilder petWaiting = new StringBuilder();
        StringBuilder noPetWaiting = new StringBuilder();
        int size = partiesWaiting.size();
        AbstractParty party;

        for (int i = 0; i < size; i++) {
            party = partiesWaiting.get(i);
            if (party instanceof PetParty) {
                petWaiting.append(party).append("\n");
            } else {
                noPetWaiting.append(party).append("\n");
            }
        }

        return "The following customer parties are waiting for tables: \n"
                + petWaiting.toString().trim() 
                + "\n"
                + noPetWaiting.toString().trim();
    }

    public String availableTableDetails() {
        int size = openPetTables.size();
        String output = "";

        if (size == 0)
            return "There are no available tables.";

        if (size > 0) {
            output += "The following " + size 
                + (size > 1 ? " tables are " : " table is ")
                + "available in the pet-friendly section:";

            for(int i = 0; i < size; i++) {
                output += "\n" + openPetTables.get(i).toString();
            }
        }

        size = openNoPetTables.size();
        if (size > 0) {
            output += "\nThe following " + size
                + (size>1?" tables are ":" table is") 
                + "available in the non-pet-friendly section:";

            for(int i = 0; i < size; i++) {
                output += "\n" + openNoPetTables.get(i).toString();
            }
        }
        return output;
    }

    public String inUseTableDetails() {
        int size = inUseTables.size();
        int pet = 0;
        int noPet = 0;

        if (size == 0) {
            return "No customers are being served!";
        } 

        StringBuilder outputPet= new StringBuilder("");
        StringBuilder outputNoPet = new StringBuilder("");
        Table table;
        String output = "";
        AbstractParty party;

        for(int i = 0; i < size; i++){
            table = inUseTables.get(i);
            party = table.getParty();
            if(party instanceof PetParty){
                outputPet.append("\nServing ").append(party.toString())
                    .append(" at ").append(table.toString());
                pet++;
            }
            else{
                outputNoPet.append("\nServing ").append(party.toString())
                    .append(" at ").append(table.toString());
                noPet++;
            }
        }

        if(pet > 0){
            if(pet == 1){
                output += "The following customer is being served in "
                    + "the pet-friendly section" + outputPet.toString();
            } else {
                output += "The following customers are being served "
                    + "in the pet-friendly section"
                    + outputPet.toString();
            }
        }
        if(noPet > 0){
            if(noPet == 1){
                output += "The following customer is being served in "
                    + "the pet-friendly section"
                    + outputNoPet.toString();
            } else {
                output += "The following customers are being served "
                    + "in the pet-friendly section"
                    + outputNoPet.toString();
            }
        }

        return output;
    }
}
