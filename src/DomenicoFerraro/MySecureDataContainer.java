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

    /** Restituisce il numero degli elementi di un utente presenti nella collezione */
    @Override
    public int getSize(String Owner, String passw) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        //Inizializzo un contatore
        int counter = 0;
        //Se sono verificati i controlli di identità
        if (logIn(Owner, passw)) {
            for (SharedData sd: storage) {
                if (sd.canGetData(Owner))
                    counter++;
            }
        } else {
            //TODO Ricordarsi di lanciare exception
        }
        //Restituisco la quantità
        return counter;
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

    /** Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità */
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

    /** Rimuove il dato nella collezione se vengono rispettati i controlli di identità */
    @Override
    public E remove(String Owner, String passw, E data) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        if (data == null) throw new NullPointerException();

        //Cerco un dato condiviso a cui Owner può accedere
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato allora lo cancello e lo restituisco
        if (dataFound != null) {
            storage.remove(dataFound);
            //Ritorno il dato cancellato
            return dataFound.getData();
        }
        //Se non l'ho trovato allora restituisco null
        return null;
    }

    /** Crea una copia del dato nella collezione se vengono rispettati i controlli di identità */
    @Override
    public void copy(String Owner, String passw, E data) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        if (data == null) throw new NullPointerException();

        //Cerco un dato condiviso a cui Owner può accedere
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato allora lo copio
        if (dataFound != null) {
            SharedData<E> copy = new SharedData<>(dataFound.getData(), Owner);
            storage.addElement(copy);
        }
    }

    /** Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità */
    @Override
    public void share(String Owner, String passw, String Other, E data) {
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        if (Other == null) throw new NullPointerException();
        if (data == null) throw new NullPointerException();
        if (Owner.equals(Other)) throw new IllegalArgumentException();  //l'utente non deve condividere il dato con se stesso
        //if (!checkId(Other)) throw new WrongIdException();

        //Cerco un dato condiviso a cui Owner può accedere
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato allora lo condivido con Other
        if (dataFound != null) {
            dataFound.addOther(Other);
        }
    }

    /** Restituisce un iteratore (senza remove) che genera tutti i dati dell’utente in ordine arbitrario se vengono
     * rispettati i controlli di identità */
    @Override
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException{
        if (Owner == null) throw new NullPointerException();
        if (passw == null) throw new NullPointerException();
        //Inizializzo una collezione
        Vector<E> userData = new Vector<>();
        //Se sono verificati i controlli di identità
        if (logIn(Owner, passw)) {
            for (SharedData sd: storage) {  //Cerco tra tutti i dati condivisi
                if (sd.canGetData(Owner))   //E se trovo dati condivisi che l'utente può leggere
                    userData.add((E)sd.getData());  //Aggiungo il dato alla collezione
            }
        }
        //Ritorno un generatore (che non supporta remove()) dei dati presenti nella collezione appena creata
        return new UserDataGen<E>(userData);
    }

    /** Restituisce true se vengono rispettati i controlli di identità, ovvero se esiste un utente con stesso id e passw, null altrimenti. */
    public boolean logIn(String id, String passw){
        return users.contains(new User(id, passw));
    }

    /** Se vengono rispettati i controlli di identità e se esiste un dato condiviso (come quello passato per argomento) a cui Owner può
     * accedere allora restituisce il dato condiviso, altrimenti restituisce null. */
    private SharedData<E> getSharedData(String Owner, String passw, E data) {
        if (logIn(Owner, passw)){  //Se vengono rispettati i controlli di identità
            for (SharedData sd: storage) {   //Per ogni dato nella collezione
                if (sd.canGetData(Owner)) {     //Cerco se esiste il dato 'data'
                    //Se lo trovo controllo se Owner può accedervi
                    if (sd.getData().equals(data)) {
                        //Restituisco il dato condiviso
                        return sd;
                    }
                    //Altrimenti vado avanti perchè potrebbe esistere una copia dello stesso dato a cui Owner invece può accedere
                }
            }
        }
        return null;
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

    private static class UserDataGen<T> implements Iterator<T> {

        private Vector<T> userData;
        private int size;
        private int currentIndex;

        public UserDataGen(Vector<T> userData) {
            if (userData == null) throw new NullPointerException();
            this.userData = userData;
            currentIndex = 0;
            size = userData.size();
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            return userData.get(currentIndex++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
