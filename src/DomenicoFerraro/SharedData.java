package DomenicoFerraro;

import java.util.Vector;

public class SharedData<E> {

    private E data;
    private int ownerId;
    private Vector<Integer> othersID;

    public SharedData(E data, int ownerId) {
        if (data == null) throw new IllegalArgumentException();
        if (ownerId < 1) throw new IllegalArgumentException();
        this.data = data;
        this.ownerId = ownerId;
        othersID = new Vector<>();
    }

    public E getData(){
        return data;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Vector<Integer> getOthersID() {
        return othersID;
    }
}
