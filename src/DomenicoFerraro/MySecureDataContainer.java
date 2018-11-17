package DomenicoFerraro;

import java.util.Iterator;
import java.util.Vector;

public class MySecureDataContainer<E> implements SecureDataContainer<E> {

    private Vector<User> users;
    private Vector<E> storage;

    private int storageDim;
    private int usersDim;

    public MySecureDataContainer() {
        users = new Vector<>();
        storage = new Vector<>();

        usersDim = 0;
    }

    @Override
    public void createUser(String Id, String passw) {
        User newUser = new User(Id, passw);     //Creo un nuovo oggetto che rappresenta il nuovo utente
        if (!users.contains(newUser)) {         //Se l'utente non è già presente nella collezione di utenti
            users.add(newUser);                 //Lo aggiungo
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

}
