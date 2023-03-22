import java.util.Arrays;

public class IntList {
    private int[] storage;
    private int size = 0;

    public IntList(int[] storage) {
        this.storage = storage;
        this.size = storage.length;
    }
    public IntList(){
        this.storage = new int[1];
    }

    public void add(int value) {
        if (size == storage.length) {
            storage = Arrays.copyOf(storage, size*2);
        }
        storage[size++] = value;
    }

    public int get(int index) {
        if (index < size) {
            return storage[index];
        }
        throw new ArrayIndexOutOfBoundsException("No such index!");
    }

    public int size() {
        return size;
    }
}
