package DomenicoFerraro;

import java.util.Iterator;
import java.util.Vector;

public class MySecureDataContainer<E> implements SecureDataContainer<E> {

    private Vector<User> users;
    private Vector<SharedData<E>> storage;

    public MySecureDataContainer() {
        users = new Vector<>();
        storage = new Vector<>();
    }

    /** Crea l’identità un nuovo utente della collezione. */
    @Override
    public void createUser(String Id, String passw) {
        if (Id == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();

        if (!checkId(Id)) {                     //Se l'Id non è già presente nella collezione di utenti
            users.add(new User(Id, passw));     //Lo aggiungo
        }
    }

    @Override
    public int getSize(String Owner, String passw) {
        return 0;
    }

    /** Inserisce il valore del dato nella collezione se vengono rispettati i controlli di identità. */
    @Override
    public boolean put(String Owner, String passw, E data) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        boolean canPut = logIn(Owner, passw);      //Controllo l'identità
        if (canPut){  //Se vengono rispettati i controlli di identità
            //Inserisco il dato
            storage.add(new SharedData<>(data, Owner));
        }
        return canPut;   //Ritorno se ho inserito il dato o meno
    }

    @Override
    public E get(String Owner, String passw, E data) {
        return null;
    }

    @Override
    public E remove(String Owner, String passw, E data) {
        return null;
    }

    @Override
    public void copy(String Owner, String passw, E data) {

    }

    @Override
    public void share(String Owner, String passw, String Other, E data) {

    }

    @Override
    public Iterator<E> getIterator(String Owner, String passw) {
        return null;
    }

    private int getUserPosition(String id, String passw) {
        return users.indexOf(new User(id, passw));
    }

    /** Restituisce true se vengono rispettati i controlli di identità, ovvero se esiste un utente con stesso id e passw, null altrimenti. */
    private boolean logIn(String id, String passw){
        return users.contains(new User(id, passw));
    }

    /** Restituisce true se l'id è già utilizzato, false altrimenti. */
    private boolean checkId(String other){
        for (User us: users)
            if (us.sameId(other))
                return true;
        return false;
    }
}
