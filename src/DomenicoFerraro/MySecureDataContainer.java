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

    /** Crea l’identità un nuovo utente della collezione se non esiste già. */
    @Override
    public void createUser(String Id, String passw) {
        if (Id == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();

        if (!checkId(Id)) {                     //Se l'Id non è già presente nella collezione di utenti
            users.add(new User(Id, passw));     //Lo aggiungo
        } else {
            System.out.println("Questo utente esiste già."); //TODO sostituire con una Exception
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

    //Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità
    @Override
    public E get(String Owner, String passw, E data) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        if (data == null) throw new NullPointerException();

        //Cerco un dato condiviso a cui Owner può accedere che ha 'data' come valore
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se il dato l'ho trovato allora lo restituisco
        if (dataFound != null)
            return dataFound.getData();
        //Altrimenti restituisco null
        return null;
    }

    @Override
    public E remove(String Owner, String passw, E data) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        if (data == null) throw new NullPointerException();

        //Cerco un dato condiviso a cui Owner può accedere
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato allora lo cancello
        if (dataFound != null)
            storage.remove(dataFound);
        //Ritorno il dato cancellato
        return null;
    }

    /** Se vengono rispettati i controlli di identità e se esiste un dato condiviso (come quello passato per argomento) a cui Owner può
     * accedere allora restituisce il dato condiviso, altrimenti restituisce null. */
    private SharedData<E> getSharedData(String Owner, String passw, E data) {
        if (logIn(Owner, passw)){  //Se vengono rispettati i controlli di identità
            for(SharedData sd: storage) {   //Per ogni dato nella collezione
                if (sd.getData().equals(data)) {    //Cerco se esiste il dato 'data'
                    //Se lo trovo controllo se Owner può accedervi
                    if (sd.canGetData(Owner)) {
                        //Restituisco il dato condiviso
                        return sd;
                    }
                    //Altrimenti vado avanti perchè potrebbe esistere una copia dello stesso dato a cui Owner può accedere
                }
            }
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

    /** Restituisce true se vengono rispettati i controlli di identità, ovvero se esiste un utente con stesso id e passw, null altrimenti. */
    public boolean logIn(String id, String passw){
        return users.contains(new User(id, passw));
    }

    /** Restituisce true se l'id è già utilizzato, false altrimenti. */
    private boolean checkId(String other){
        for (User us: users)
            if (us.sameId(other))
                return true;
        return false;
    }

    //TODO ricordarsi di togliere getUsers() e getStorage()
    public Vector<User> getUsers() {
        return users;
    }

    public Vector<SharedData<E>> getStorage() {
        return storage;
    }
}
