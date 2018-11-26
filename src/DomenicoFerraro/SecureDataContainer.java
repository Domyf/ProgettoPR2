package DomenicoFerraro;

import java.util.Iterator;

public interface SecureDataContainer<E> {
    /* OVERVIEW: Tipo modificabile che descrive una collezione di n oggetti di tipo generico E i quali vengono memorizzati e condivisi
                in un sistema di m utenti con un meccanismo che preserva la sicurezza dei dati.

       TYPICAL ELEMENT: coppia <insieme di utenti, {<dato, sottoinsieme degli utenti che possono accedervi>}> ovvero
                        < {<ID_0, pass_0>, ... , <ID_m-1, pass_m-1> | ID_i != ID_j per ogni 0 <= i < j < m},
                          {<data_k, {ID_t | 0 < t < s < m && ID_t != ID_s}> }>

             //TODO decidere se sostituire con {utenti} && {<dato, sottoinsieme degli utenti che possono accedervi>}
     */

    //Crea l’identità un nuovo utente della collezione
    public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyExistsException;
    //REQUIRES: Owner != null && passw != null && <Owner, passw> non appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> appartiene a this solleva UserAlreadyExistsException (checked)
    //MODIFIES: this
    //EFFECTS: aggiunge una nuova coppia <Id, passw> all'insieme degli utenti.

    //Restituisce il numero degli elementi di un utente presenti nella collezione
    public int getSize(String Owner, String passw) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: ritorna la quantità di coppie <dato, inseme di utenti che possono accedervi> nelle quali Owner appartiene
    //         nella seconda componente.

    //Inserisce il valore del dato nella collezione se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //MODIFIES: this
    //EFFECTS: aggiunge una nuova coppia <data, {Owner}> a this e restituisce true se l'aggiunta è avvenuta con successo,
    //         false altrimenti.

    //Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: se esiste <data, {Id_0, ... , Owner, ... , Id_m-1}> ritorna data, null altrimenti.


    //Rimuove il dato nella collezione se vengono rispettati i controlli di identità
    public E remove(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //MODIFIES: this
    //EFFECTS: se esiste la coppia <data, {Id_0, ... , Owner, ... , Id_m-1}> la rimuove da this e ritorna data, null altrimenti.

    //Crea una copia del dato nella collezione se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException, IllegalArgumentException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this && <data, {..., Owner, ...}> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //        se non esiste <data, {..., Owner, ...}> solleva IllegalArgumentException (unchecked)
    //MODIFIES: this
    //EFFECTS: aggiunge la nuova coppia <data, {Owner}> a this

    //Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, IllegalArgumentException, UserAccessDeniedException, UserNotExistsException;
    //REQUIRES: Owner != null && passw != null && data != null && Other != null && <Owner, passw> appartiene a this
    //          && <data, {..., Owner, ...}> appartiene a this && <Other, p> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se Other == null solleva NullPointerException (unchecked)
    //        se Owner.equals(Other) solleva IllegalArgumentException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //        se <Other, p> non appartiene a this solleva UserNotExistsException (checked)
    //        se non esiste <data, {..., Owner, ...}> solleva IllegalArgumentException (unchecked)
    //MODIFIES: this
    //EFFECTS: modifica <data, {..., Owner, ...}> in <data, {..., Owner, Other, ...}>

    //Restituisce un iteratore (senza remove) che genera tutti i dati dell’utente in ordine arbitrario se vengono
    //rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserAccessDeniedException;

    // TODO altre operazioni da definire a scelta
}
