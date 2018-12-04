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

    /*  Invariant representation
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

    private HashMap<String, String> users;          //Id -> passw
    private Vector<E> dataVector;                   //Vector di tutti i dati
    private HashMap<String, Vector<E>> storage;     //Id -> Dati che Id vede

    /** EFFECTS: Inizializza this = <{}, {}, {}>*/
    public HashingDataContainer() {
        users = new HashMap<>();
        storage = new HashMap<>();
        dataVector = new Vector<>();
    }

    //REQUIRES: Id != null && passw != null && <Id, p> non appartiene a this con p qualsiasi
    //THROWS: se Id == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Id, p> appartiene a this, con p qualsiasi, solleva UserAlreadyExistsException (checked)
    //MODIFIES: this
    //EFFECTS: Associa il valore passw alla chiave Id e associa un vettore vuoto alla chiave Id
    @Override
    public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyExistsException {
        if (Id == null || passw == null)
            throw new NullPointerException();
        if (checkId(Id)) throw new UserAlreadyExistsException();  //Se l'ID esiste già

        users.put(Id, passw);               //Lo aggiungo
        storage.put(Id, new Vector<>());    //Inizializzo con un vettore vuoto
    }

    //REQUIRES: Owner != null && passw != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: ritorna la quantità di coppie <Owner, data_j> appartenenti a this per ogni 0<=j<n
    @Override
    public int getSize(String Owner, String passw) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        return storage.get(Owner).size();
    }

    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //MODIFIES: this
    //EFFECTS: aggiunge data in this e una nuova coppia <Owner, data> in this e restituisce true se queste aggiunte hanno
    //         avuto successo, false altrimenti.
    @Override
    public boolean put(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException {
        if (Owner == null || passw == null || data == null)
            throw new NullPointerException();
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito

        if (dataVector.add(data))
            return storage.get(Owner).add(data);

        return false;
    }

    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: se esiste data in this ed esiste la coppia <Owner, data_t> in this tale che data_t == data
    //         ritorna data_t, null altrimenti.
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

    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //MODIFIES: this
    //EFFECTS: se esiste data in this && esiste la coppia <Owner, data_t> in this tale che data_t == data allora rimuove
    //         data e <Owner, data_t> da this. Se la rimozione è avvenuta con successo restituisce data_t, null altrimenti.
    //         Se non esiste data in this oppure non esiste la coppia <Owner, data_t> in this tale che data_t == data
    //         allora restituisce null.
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

    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this && <Owner, data> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene in this solleva UserAccessDeniedException (checked)
    //        se <Owner, data> non appartiene in this solleva IllegalArgumentException (unchecked)
    //MODIFIES: this
    //EFFECTS: duplica data in this e aggiunge una nuova coppia <Owner, data> in this
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

    //REQUIRES: Owner != null && passw != null && data != null && Other != null && !Owner.equals(Other) &&
    //          <Owner, passw> appartiene a this && <Owner, data> appartiene a this && <Other, p> appartiene a this (con p qualsiasi)
    //          && <Other, data> non appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se Other == null solleva NullPointerException (unchecked)
    //        se Owner.equals(Other) solleva IllegalArgumentException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //        se <Other, p> non appartiene a this solleva UserNotExistsException (checked)
    //        se <Owner, data> non appartiene a this solleva IllegalArgumentException (unchecked)
    //        se <Other, data> appartiene a this solleva IllegalArgumentException (unchecked)
    //MODIFIES: this
    //EFFECTS: aggiunge una nuova coppia <Other, data> in this.
    @Override
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, IllegalArgumentException, UserAccessDeniedException, UserNotExistsException {
        if (Owner == null || passw == null || Other == null || data == null)
            throw new NullPointerException();
        if (!checkId(Other)) throw new UserNotExistsException();
        if (Owner.equals(Other)) throw new IllegalArgumentException();  //l'utente non deve condividere il dato con se stesso
        if (!logIn(Owner, passw)) throw new UserAccessDeniedException();  //Controllo di identità fallito
        if (storage.get(Other).contains(data)) throw new IllegalArgumentException();    //Se l'utente other può vedere già il dato

        if (storage.get(Owner).contains(data))  //Se Owner può vedere il dato
            storage.get(Other).add(data);       //Aggiungo il dato a other
        else     //Se Owner non può vedere il dato
            throw new IllegalArgumentException();
    }

    //REQUIRES: Owner != null && passw != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: restituisce un Iterator di elementi di tipo E il quale itera l'insieme
    //         {data_t | <Owner, data_t> appartiene a this per ogni 0 <= t < n && data_t appartiene a this}
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
