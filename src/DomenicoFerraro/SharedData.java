package DomenicoFerraro;

import java.util.Vector;

public class SharedData<E> {

    private E data; //Dato
    private String ownerId; //ID Proprietario
    private Vector<String> othersIDs;   //Vettore di ID degli utenti autorizzati

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
    public void addOther(String other) throws IllegalArgumentException {
        if (othersIDs.contains(other)) throw new IllegalArgumentException();
        othersIDs.add(other);
    }

    /** Restituisce true se l'id dell'utente passato per argomento può accedere il dato. */
    public boolean canGetData(String id){
        //return id.equals(ownerId);
        return id.equals(ownerId) || othersIDs.contains(id);
    }

    public String toString(){
        return data.toString();
    }
}
