package DomenicoFerraro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class HashingDataContainer<E> implements SecureDataContainer<E> {
    //OVERVIEW:

    /*  TYPICAL ELEMENT:
     */
    /* Abstraction Function
       AF(c) = <{<Id_i, pass_i> | 0 <= i < c.users.keySet().size() && Id_i = c.users.keySet().get(i) && pass_i = c.users.get(Id_i)},
                {c.storage.get(i).getData() | 0 <= i < c.storage.size()},
               {<c.users.get(s).getId(), c.storage.get(t).getData()> | 0<=s<c.user.size()
                    && 0<=t<c.storage.size() && c.storage.get(t).canGetData(c.users.get(s).getId())}>
    */
    private HashMap<String, String> users;
    private Vector<E> dataVector;
    private HashMap<String, Vector<E>> storage;

    public HashingDataContainer() {
        users = new HashMap<>();
        storage = new HashMap<>();
        dataVector = new Vector<>();
    }

    @Override
    public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyExistsException {
        if (Id == null || passw == null)
            throw new NullPointerException();
        if (checkId(Id)) throw new UserAlreadyExistsException();  //Se l'ID esiste già

        users.put(Id, passw);               //Lo aggiungo
        storage.put(Id, new Vector<>());    //Inizializzo con un vettore vuoto
    }

    @Override
    public int getSize(String Owner, String passw) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        return storage.get(Owner).size();
    }

    @Override
    public boolean put(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        if (dataVector.add(data))
            return storage.get(Owner).add(data);

        return false;
    }

    @Override
    public E get(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        //Ottengo i dati a cui Owner può accedere
        Vector<E> userData = storage.get(Owner);

        int index = userData.indexOf(data);
        if (index >= 0)
            return userData.get(index);

        //Altrimenti restituisco null
        return null;
    }

    @Override
    public E remove(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        int removed = 0;

        //Ottengo i dati a cui Owner può accedere
        //Se l'utente può accedere a questo dato
        if (storage.get(Owner).contains(data)) {
            Iterator<Vector<E>> allData = storage.values().iterator();
            while (allData.hasNext()) {
                Vector<E> v = allData.next();
                if (v.remove(data))
                    removed++;
            }
        }

        if (removed > 0)
            return dataVector.remove(dataVector.indexOf(data));
        //Se non l'ho trovato o rimosso allora restituisco null
        return null;
    }

    @Override
    public void copy(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException, IllegalArgumentException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        //Ottengo i dati a cui Owner può accedere
        Vector<E> ownerData = storage.get(Owner);

        int index = ownerData.indexOf(data);
        if (index >= 0) {
            ownerData.add(ownerData.get(index));
            dataVector.add(ownerData.get(index));
        } else
            throw new IllegalArgumentException();
    }

    @Override
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, IllegalArgumentException, UserAccessDeniedException, UserNotExistsException {
        if (Owner == null || passw == null || Other == null || data == null)
            throw new NullPointerException();
        if (!checkId(Other)) throw new UserNotExistsException();
        if (Owner.equals(Other)) throw new IllegalArgumentException();  //l'utente non deve condividere il dato con se stesso
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito


        if (storage.get(Owner).contains(data))  //Se Owner può vedere il dato
            storage.get(Other).add(data);       //Aggiungo il dato a other
        else     //Se Owner non può vedere il dato
            throw new IllegalArgumentException();
    }

    @Override
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        return new UserDataGen<E>(storage.get(Owner));
    }

    /** EFFECTS: Restituisce true se l'id è già utilizzato, false altrimenti. */
    private boolean checkId(String id){
        return users.containsKey(id);
    }

    /** EFFECTS: Restituisce true se vengono rispettati i controlli di identità, ovvero se esiste un utente con stesso id e passw, false altrimenti. */
    private boolean logIn(String id, String passw){
        if (users.get(id) != null)
            return users.get(id).equals(passw);
        return false;
    }

    private static class UserDataGen<T> implements Iterator<T> {
        //OVERVIEW: Iterator dei dati a cui uno specifico utente può accedere

        //TYPICAL ELEMENT: <{userData.get(i) | 0 <= i < size}, size, currentIndex>

        //IR: userData != null && size != null && size >= 0 && 0 <= currentIndex < size && size == userData.size()
        //    && userData.get(i) != null for all 0 <= i < size

        private Vector<T> userData;
        private int size;
        private int currentIndex;

        //EFFECTS: Costruisce this
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
