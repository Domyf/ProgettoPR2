package DomenicoFerraro;

import java.util.Vector;

public class SharedData<E> {
    //OVERVIEW: tipo di dato che descrive un dato di tipo generico E, il suo proprietario e gli id di tutti gli utenti
    //          che possono accedervi

    //TYPICAL ELEMENT: <data, ownerId, {othersIDs.get(i) | 0<=i<othersIDs.size()}>
    //IR: data != null && ownerId != null && othersIDs != null && othersIDs.get(i) != null for all 0 <= i < othersIDs.size()
    //    && !(othersIDs.get(i).equals(othersIDs.get(j))) for all 0 <= j < othersIDs.size()

    private E data;                     //Dato
    private String ownerId;             //ID Proprietario
    private Vector<String> othersIDs;   //Vettore di ID degli utenti autorizzati

    /** REQUIRES: data != null && ownerId != null
     *  THROWS: se data == null solleva NullPointerException (unchecked)
     *          se ownerId == null solleva NullPointerException (unchecked)
     *  MODIFIES: this
     *  EFFECTS: Costruisce this con <data, ownerId, {}>
     * */
    public SharedData(E data, String ownerId) throws NullPointerException {
        if (data == null) throw new NullPointerException();
        if (ownerId == null) throw new NullPointerException();
        this.data = data;
        this.ownerId = ownerId;
        othersIDs = new Vector<>();
    }

    /** EFFECTS: Restituisce data */
    public E getData(){
        return data;
    }

    /** REQUIRES: !(othersIDs.contains(other))
     *  THROWS: se othersIDs.contains(other) solleva IllegalArgumentException (unchecked)
     *  EFFECTS: Aggiunge other alla lista di chi può accedere al dato.
     * */
    public void addOther(String other) throws IllegalArgumentException {
        if (othersIDs.contains(other)) throw new IllegalArgumentException();
        othersIDs.add(other);
    }

    /** EFFECTS: Restituisce true se l'id dell'utente passato per argomento può accedere al dato. */
    public boolean canGetData(String id) {
        return id.equals(ownerId) || othersIDs.contains(id);
    }

    /** EFFECTS: restituisce una rappresentazione di this in forma di stringa */
    public String toString(){
        return data.toString();
    }
}
