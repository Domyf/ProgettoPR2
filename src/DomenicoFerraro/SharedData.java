package DomenicoFerraro;

import java.util.Vector;

public class SharedData<E> {

    private E data;
    private String ownerId;
    private Vector<String> othersIDs;

    public SharedData(E data, String ownerId) {
        if (data == null) throw new NullPointerException();
        if (ownerId == null) throw new NullPointerException();
        this.data = data;
        this.ownerId = ownerId;
        othersIDs = new Vector<>();
    }

    /** Restituisce il dato. */
    public E getData(){
        return data;
    }

    /** Aggiunge un nuovo utente alla lista di chi può accedere al dato. */
    public void addOther(String other) {
        othersIDs.addElement(other);
    }

    /** Restituisce true se l'id dell'utente passato per argomento può accedere il dato. */
    public boolean canGetData(String id){
        return id.equals(ownerId);
        //return othersIDs.contains(ownerId);
    }

    public String toString(){
        return data.toString();
    }
}
