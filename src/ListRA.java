@SuppressWarnings("unchecked")
public class ListRA<T> implements ListInterface<T> {

    private static final int DEFAULT_SIZE = 3;
    protected T[] items;  // an array of list items
    protected int numItems;  // number of items in list

    public ListRA()
    {
        items = (T[]) new Object[DEFAULT_SIZE];
        numItems = 0;
    }
    /**
     * Checks if the list is empty.
     * @return true if empty, false otherwise
     */
    public boolean isEmpty()
    {
        return (numItems == 0);
    }
    /**
     * Returns the size of the list
     * @return the size of the list
     */
    public int size()
    {
        return numItems;
    }
    /**
     * Clears the list by creating a new array and resetting numItems
     */
    public void removeAll()
    {
        items = (T[]) new Object[DEFAULT_SIZE];
        numItems = 0;
    }

    /**
     * Adds an object to a specified index.
     * Resizes the collection to make room, if full.
     * @param index The index to add the element.
     * @param item The item to add.
     */
    public void add(int index, T item) {
        if (index >= 0 && index <= items.length) {
            //array full, resize necessary
            if (numItems == items.length) {
                //resize and add at same time, don't traverse collection twice
                T[] temp = (T[]) new Object[1 + items.length * 3 / 2];

                //copy before index directly
                for (int i = 0; i < index; i++) {
                    temp[i] = items[i];
                }

                //place new object into index
                temp[index] = item;

                //move old items over one
                for (int i = index; i < items.length; i++) {
                    temp[i + 1] = items[i];
                }

                //replace old reference, unlink for garbage collection
                items = temp;
            } else {
                for (int pos = numItems-1; pos >= index; pos--) {
                    items[pos+1] = items[pos];
                }
                items[index] = item;
            }
            numItems++;
        } else {
            throw new ListIndexOutOfBoundsException("Out of bounds " +
                                                    "exception in add.");
        }
    }

    /**
     * Adds an object to the end of the list.
     * Resizes the collection to make room, if full.
     * @param item The item to add.
     */
    public void add(T item) {
        this.add(numItems, item);
    }

    /**
     * Returns the item that is stored in entered index of the list
     * @param index the index to return the item from
     * @return the item stored in the list
     */
    public T get(int index)
    throws ListIndexOutOfBoundsException
    {
        if (index >= 0 && index < numItems)
        {
            return items[index];
        }
        else
        {
            // index out of range
            throw new ListIndexOutOfBoundsException(
                "ListIndexOutOfBoundsException on get");
        }  // end if
    } // end get
    /**
     * Removes an item from the list and shifts all elements with larger indices down one index.
     * @param index the index to be removed from
     * @return the item that was removed
     */
    public void remove(int index)
    throws ListIndexOutOfBoundsException
    {
        if (index >= 0 && index < numItems)
        {
            // delete item by shifting all items at
            // positions > index toward the beginning of the list
            // (no shift if index == size)
            for (int pos = index+1; pos < numItems; pos++) //textbook code modified to eliminate logic error causing ArrayIndexOutOfBoundsException

            {
                items[pos-1] = items[pos];
            }  // end for

            //fixes memory leak, removes inaccessible reference
            items[--numItems] = null;

        }
        else
        {
            // index out of range
            throw new ListIndexOutOfBoundsException(
                "ListIndexOutOfBoundsException on remove");
        }  // end if
    } //end remove

    @Override
    /**
     * Returns a string representation of the list.
     * @return a string representation of the list
     */
    public String toString() {
        if (numItems == 0)
            return "";

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < numItems; i++) {
            sb.append(items[i].toString()).append("\n");
        }

        return sb.substring(0, sb.length() - 1);
    }

}

