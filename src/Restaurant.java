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
                ? openPetTables.get(openPetTables.size() - 1) : 0;
        int largestOpenNoPetSize = 
                openNoPetTables.size() > 0
                ? openNoPetTables.get(openNoPetTables.size() - 1) : 0;
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
    public Table partyExits(String name) {
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
            .append(party instanceof Pet
                        ? " (Pet) is leaving the restaurant."
                        : " (NoPet) is leaving the restaurant.");

        return sb.toString();
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
        int listSize = inUseTables.size();
        int min = 0;
        int max = listSize - 1;
        int mid = (max + min) / 2;
        String midItem =
            listSize == 0 ? null : inUseTables.get(mid).getParty().getName();

        while (min < max) {
            if (midItem.compareTo(name) >= 0) {
                max = mid;
            } else {
                min = mid + 1;
            }
            mid = (max + min) / 2;
            midItem = inUseTables.get(mid).getParty().getName();
        }

        if (listSize != 0 && name.equals(midItem)) {
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
            listSize == 0 ? null : list.get(mid).getSize();

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

        name = name.toUpperCase();

        for (int i = 0; i < size && !found; i++) {
            if (partiesWaiting.get(i).getName().equals(name)) {
                found = true;
            }
        }

        size = inUseTables.size();
        for (int i = 0; i < size && !found; i++) {
            if (inUseTables.get(i).getParty().getName().equals(name)) {
                found = true;
            }
        }

        return found;
    }

    public boolean tableNameFree(String name) {
        boolean found = false;
        int size = openPetTables.size();

        name = name.toUpperCase();

        for (int i = 0; i < size && !found; i++) {
            if (openPetTables.get(i).getName().equals(name)) {
                found = true;
            }
        }

        size = openNoPetTables.size();
        for (int i = 0; i < size && !found; i++) {
            if (openNoPetTables.get(i).getName().equals(name)) {
                found = true;
            }
        }

        size = inUseTables.size();
        for (int i = 0; i < size && !found; i++) {
            if (inUseTables.get(i).getName().equals(name)) {
                found = true;
            }
        }

        return found;
    }

    public String availableTableDetails() {
        int size = openPetTables.size();
        String output = "The following " + size 
                + (size > 1 ? " tables are " : " table is ")
                + "available in the pet-friendly section:";

        for(Table t : openPetTables) {
            output += "\n" + t.toString();
        }

        size = openNoPetTables.size();
        output += "\nThe following " + size
                + (size>1?" tables are ":" table is") 
                + "available in the non-pet-friendly section:";

        for(Table t : openNoPetTables) {
            output += "\n" + t.toString();
        }
        return output;
    }

}
