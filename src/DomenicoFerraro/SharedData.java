package DomenicoFerraro;

import java.util.Vector;

public class SharedData<E> {

    private E data;
    private String ownerId;
    private Vector<Integer> othersIDs;

    public SharedData(E data, String ownerId) {
        if (data == null) throw new NullPointerException();
        if (ownerId == null) throw new NullPointerException();
        this.data = data;
        this.ownerId = ownerId;
        othersIDs = new Vector<>();
    }

    public E getData(){
        return data;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Vector<Integer> getOthersID() {
        return othersIDs;
    }

    //TODO aggiungere il metodo per condividere this con un utente, quindi inserendo l'Id dell'utente in othersIDs
}
