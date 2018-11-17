package DomenicoFerraro;

import java.util.Iterator;
import java.util.Vector;

public class MySecureDataContainer<E> implements SecureDataContainer<E> {

    private Vector<User> users;
    private Vector<E> storage;
    private Vector<Vector<Boolean>> flags;

    private int storageDim;
    private int usersDim;

    public MySecureDataContainer() {
        users = new Vector<>();
        storage = new Vector<>();
        flags = new Vector<>();

        usersDim = 0;
        storageDim = 0;
    }

    @Override
    public void createUser(String Id, String passw) {
        User newUser = new User(Id, passw);     //Creo un nuovo oggetto che rappresenta il nuovo utente
        if (!users.contains(newUser)) {         //Se l'utente non è già presente nella collezione di utenti
            users.add(newUser);                 //Lo aggiungo
            flags.add(createFalseFlagsVector(storageDim)); //Creo la colonna della matrice relativa al nuovo utente con tutti i flag false.
            usersDim++;                         //Incremento la dimensione degli utenti
        }
    }

    @Override
    public int getSize(String Owner, String passw) {
        return 0;
    }

    @Override
    public boolean put(String Owner, String passw, E data) {
        return false;
    }

    @Override
    public E get(String Owner, String passw, E data) {
        return null;
    }

    @Override
    public E remove(String Owner, String passw, E data) {
        //TODO verificare identità utente
        //TODO far rimuovere solo all'utente che ha flag true sul dato (quindi ne ha accesso)
        int pos = storage.indexOf(data);
        if (pos >= 0) { //Il dato è presente
            storage.remove(pos);
            for (int i=0; i<usersDim; i++)  //Per ogni utente
                flags.get(i).remove(pos);   //Prendo il vettore di flag e ne rimuovo il flag in posizione pos
            storageDim--;
        } else { //Il dato non è presente
            //TODO gestire dato non presente
        }
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

    /** Restituisce un Vector di booleani tuti falsi grande dim */
    private Vector<Boolean> createFalseFlagsVector(int dim){
        Vector<Boolean> vector = new Vector<>(dim);
        for (int i=0; i<dim; i++)
            vector.add(false);
        return vector;
    }
}
