public class Restaurant{
    ListRA<AbstractParty> partiesWaiting;
    ListRA<Table> openPetTables;
    ListRA<Table> openNoPetTables;
    ListRA<Table> inUseTables;

    public Restaurant() {
        partiesWaiting = new ListRA<AbstractParty>();    
        openPetTables = new ListRA<Table>();
        openNoPetTables = new ListRA<Table>();
        inUseTables = new ListRA<Table>();
    }

    /**
     * Adds a party to the list of waiting parties.
     */
    public void partyEnters(AbstractParty party) {
        partiesWaiting.add(party);
    }

    /**
     * The restaurant exits the party with the given name and frees
     * the table they're at, if they exist.
     * @param name The name of the party.
     * @return 
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
                int addIndex = searchOpenTables(openPetTables, table.getSize());
                openPetTables.add(addIndex >= 0 ? addIndex : -addIndex - 1, table);        
            } else {
                int addIndex = searchOpenTables(openNoPetTables, table.getSize());
                openNoPetTables.add(addIndex >= 0 ? addIndex : -addIndex - 1, table);        
            }
            return table;
        } else {
            return null;
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
    


}
