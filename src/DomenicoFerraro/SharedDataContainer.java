package DomenicoFerraro;

import java.util.Iterator;
import java.util.Vector;

public class SharedDataContainer<E> implements SecureDataContainer<E> {

    private Vector<User> users;
    private Vector<SharedData<E>> storage;

    public SharedDataContainer() {
        users = new Vector<>();
        storage = new Vector<>();
    }

    /** Crea l’identità un nuovo utente della collezione se non esiste già. */
    @Override
    public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyExistsException {
        if (Id == null || passw == null)
            throw new NullPointerException();
        if (checkId(Id)) throw new UserAlreadyExistsException();  //Se l'ID esiste già

        users.add(new User(Id, passw));     //Lo aggiungo
    }

    /** Restituisce il numero degli elementi di un utente presenti nella collezione */
    @Override
    public int getSize(String Owner, String passw) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        //Inizializzo un contatore
        int counter = 0;
        //Itero per ogni dato
        for (SharedData sd: storage) {  //Itero tutti i dati condivisi
            if (sd.canGetData(Owner))   //Se trovo un dato a cui Owner può accedere
                counter++;              //Incremento il contatore
        }

        //Restituisco il contatore
        return counter;
    }

    /** Inserisce il valore del dato nella collezione se vengono rispettati i controlli di identità. */
    @Override
    public boolean put(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        // Inserisco il dato
        return storage.add(new SharedData<>(data, Owner));
    }

    /** Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità */
    @Override
    public E get(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

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
    public E remove(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        //Cerco un dato condiviso a cui Owner può accedere
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato allora lo cancello e lo restituisco
        if (dataFound != null) {
            if (storage.remove(dataFound))  //Rimuovo e se ho avuto successo
                return dataFound.getData(); //Ritorno il dato cancellato
            else
                return null;
        }
        //Se non l'ho trovato allora restituisco null
        return null;
    }

    /** Crea una copia del dato nella collezione se vengono rispettati i controlli di identità */
    @Override
    public void copy(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException, IllegalArgumentException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        //Cerco un dato condiviso a cui Owner può accedere
        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato allora lo copio
        if (dataFound != null) {
            SharedData<E> duplicate = new SharedData<>(dataFound.getData(), Owner);
            storage.add(duplicate);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità (tutti*/
    @Override
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, IllegalArgumentException, UserAccessDeniedException, UserNotExistsException {
        if (Owner == null || passw == null || Other == null || data == null)
            throw new NullPointerException();
        if (!checkId(Other)) throw new UserNotExistsException();
        if (Owner.equals(Other)) throw new IllegalArgumentException();  //l'utente non deve condividere il dato con se stesso
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        SharedData<E> dataFound = getSharedData(Owner, passw, data);
        //Se l'ho trovato
        if (dataFound != null) {
            dataFound.addOther(Other);  //Lo condivido. Solleva IllegalArgumentException se il dato era già stato condiviso
        } else {
            throw new IllegalArgumentException();   //Non ho trovato il dato
        }
    }

    /** Restituisce un iteratore (senza remove) che genera tutti i dati dell’utente in ordine arbitrario se vengono
     * rispettati i controlli di identità */
    @Override
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        //Inizializzo una collezione
        Vector<E> userData = new Vector<>();
        for (SharedData sd: storage) {  //Cerco tra tutti i dati condivisi
            if (sd.canGetData(Owner))   //E se trovo dati condivisi che l'utente può leggere
                userData.add((E)sd.getData());  //Aggiungo il dato alla collezione
        }

        //Ritorno un generatore (che non supporta remove()) dei dati presenti nella collezione appena creata
        return new UserDataGen<E>(userData);
    }

    /** Restituisce true se vengono rispettati i controlli di identità, ovvero se esiste un utente con stesso id e passw, null altrimenti. */
    private boolean logIn(String id, String passw){
        return users.contains(new User(id, passw));
    }

    /** Se esiste un dato condiviso come quello passato per argomento a cui Owner può
     * accedere allora restituisce il dato condiviso, altrimenti restituisce null. */
    private SharedData<E> getSharedData(String Owner, String passw, E data) {
        for (SharedData<E> sd: storage) {   //Per ogni dato nella collezione
            if (sd.canGetData(Owner)) {     //Cerco se esiste il dato 'data'
                //Se lo trovo controllo se Owner può accedervi
                if (sd.getData().equals(data)) {
                    //Restituisco il dato condiviso
                    return sd;
                }
                //Altrimenti vado avanti perchè potrebbe esistere una copia dello stesso dato a cui Owner invece può accedere
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

        public UserDataGen(Vector<T> userData) throws NullPointerException {
            if (userData == null) throw new NullPointerException();
            this.userData = new Vector<>(userData);
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
