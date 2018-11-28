package DomenicoFerraro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class HashingDataContainer<E> implements SecureDataContainer<E> {
    //OVERVIEW: Tipo modificabile che descrive una collezione di n oggetti di tipo generico E i quali vengono memorizzati e condivisi
    //          in un sistema di m utenti con un meccanismo che preserva la sicurezza dei dati.

    /* TYPICAL ELEMENT: <{<ID_0, pass_0>, ... ,<ID_m-1, pass_m-1> | ID_i != ID_j per ogni 0 <= i < j < m},
                        {data_0, ... ,data_n-1},
                        {<ID_p, data_q> | 0<=p<m && 0<=q<n}>
                       <Id_s, data_t> appartiene a this => ID_s appartiene a this && data_t appartiene a this per
                        ogni 0 <= s < m && 0 <= t < n
     */

    /* Abstraction Function
       AF(c) = <{<Id_i, pass_i> | 0 <= i < c.users.keySet().size() && Id_i = c.users.keySet().get(i) && pass_i = c.users.get(Id_i)},
                {c.dataVector.get(i) | 0 <= i < c.dataVector.size()},
                {<Id_k, data_t> | for all 0<=k<c.users.keySet().size() => Id_k = c.users.keySet().get(k)
                    && for all 0<=t<c.storage.get(Id_k).size() => data_t = c.storage.get(Id_k).get(t)}>
    */

    /*
        IR: users != null && dataVector != null && storage != null
            && users.keySet() != null && users.values() != null
            && users.keySet().get(i) != null per ogni 0 <= i < users.keySet().size()
            && users.values().get(i) != null per ogni 0 <= i < users.values().size()
            && storage.keySet() != null && storage.values() != null
            && storage.keySet().get(i) != null per ogni 0 <= i < storage.keySet().size()
            && storage.values().get(i) != null per ogni 0 <= i < storage.values().size()
            && dataVector.get(i) != null per ogni 0 <= i < dataVector.size()
            && users.keySet().size() == storage.keySet().size()
            && users.keySet().containsAll(storage.keySet())
            && storage.keySet().containsAll(users.keySet())
            && dataVector.contains(storage.values().get(i)) per ogni 0 <= i < storage.values().size()

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
