package DomenicoFerraro;

import java.util.Iterator;

public interface SecureDataContainer<E> {
    /* OVERVIEW: Tipo modificabile che descrive una collezione di n oggetti di tipo generico E i quali vengono memorizzati e condivisi
                in un sistema di m utenti con un meccanismo che preserva la sicurezza dei dati.

       TYPICAL ELEMENT: < {<ID_0, pass_0>, ... ,<ID_m-1, pass_m-1> | ID_i != ID_j per ogni 0 <= i < j < m},
                          {data_0, ... ,data_n-1},
                          {<ID_p, data_q> | 0<=p<m && 0<=q<n}>
                          <Id_s, data_t> appartiene a this => ID_s appartiene a this && data_t appartiene a this per
                          ogni 0 <= s < m && 0 <= t < n
     */

    //Crea l’identità un nuovo utente della collezione
    public void createUser(String Id, String passw) throws NullPointerException, UserAlreadyExistsException;
    //REQUIRES: Owner != null && passw != null && <Owner, passw> non appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> appartiene a this solleva UserAlreadyExistsException (checked)
    //MODIFIES: this
    //EFFECTS: aggiunge una nuova coppia <Id, passw> a this.

    //Restituisce il numero degli elementi di un utente presenti nella collezione
    public int getSize(String Owner, String passw) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: ritorna la quantità di coppie <Owner, data_j> appartenenti a this per ogni 0<=j<n

    //Inserisce il valore del dato nella collezione se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //MODIFIES: this
    //EFFECTS: aggiunge data in this e una nuova coppia <Owner, data> in this e restituisce true se queste aggiunte hanno
    //         avuto successo, false altrimenti.

    //Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: se esiste data in this ed esiste la coppia <Owner, data_t> in this tale che data_t == data
    //         ritorna data_t, null altrimenti.


    //Rimuove il dato nella collezione se vengono rispettati i controlli di identità
    public E remove(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;
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

    //Crea una copia del dato nella collezione se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException, IllegalArgumentException;
    //REQUIRES: Owner != null && passw != null && data != null && <Owner, passw> appartiene a this && <Owner, data> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene in this solleva UserAccessDeniedException (checked)
    //        se <Owner, data> non appartiene in this solleva IllegalArgumentException (unchecked)
    //MODIFIES: this
    //EFFECTS: duplica data in this e aggiunge una nuova coppia <Owner, data> in this

    //Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, IllegalArgumentException, UserAccessDeniedException, UserNotExistsException;
    //REQUIRES: Owner != null && passw != null && data != null && Other != null && !Owner.equals(Other) &&
    //          <Owner, passw> appartiene a this && <Owner, data> appartiene a this && <Other, p> appartiene a this (con p qualsiasi)
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se data == null solleva NullPointerException (unchecked)
    //        se Other == null solleva NullPointerException (unchecked)
    //        se Owner.equals(Other) solleva IllegalArgumentException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //        se <Other, p> non appartiene a this solleva UserNotExistsException (checked)
    //        se <Owner, data> non appartiene a this IllegalArgumentException (unchecked)
    //MODIFIES: this
    //EFFECTS: aggiunge una nuova coppia <Other, data> in this.

    //Restituisce un iteratore (senza remove) che genera tutti i dati dell’utente in ordine arbitrario se vengono
    //rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserAccessDeniedException;
    //REQUIRES: Owner != null && passw != null && <Owner, passw> appartiene a this
    //THROWS: se Owner == null solleva NullPointerException (unchecked)
    //        se passw == null solleva NullPointerException (unchecked)
    //        se <Owner, passw> non appartiene a this solleva UserAccessDeniedException (checked)
    //EFFECTS: restituisce un Iterator di elementi di tipo E il quale itera l'insieme
    //         {data_t | <Owner, data_t> appartiene a this per ogni 0 <= t < n && data_t appartiene a this}

    // TODO altre operazioni da definire a scelta
}
