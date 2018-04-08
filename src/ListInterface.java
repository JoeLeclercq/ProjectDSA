public interface ListInterface <T> {
    public boolean isEmpty();
    public int size();
    public void add(int index, T item) throws ListIndexOutOfBoundsException;
    public T get(int index) throws ListIndexOutOfBoundsException;
    public void remove(int remove) throws ListIndexOutOfBoundsException;
    public void removeAll();
}

