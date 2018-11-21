package DomenicoFerraro;

import java.util.Iterator;

public interface SecureDataContainer<E> {
    /* OVERVIEW: Tipo modificabile che descrive una collezione di oggetti di tipo E i quali vengono memorizzati e condivisi
                in un sistema di utenti con un meccanismo che preserva la sicurezza dei dati.
       TYPICAL ELEMENT: {<user_0, {data_0, ... , data_k}>, ... ,<user_n, {data_0, ... , data_f}>}
                        dove user_i != user_j per ogni i,j tale che 0 <= i < j < n
                        TODO aggiornare gli elementi tipici
     */

    //Crea l’identità un nuovo utente della collezione
    public void createUser(String Id, String passw) throws NullPointerException;

    //Restituisce il numero degli elementi di un utente presenti nella collezione
    public int getSize(String Owner, String passw) throws NullPointerException, UserAccessDeniedException;

    //Inserisce il valore del dato nella collezione se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;

    //Ottiene una copia del valore del dato nella collezione se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;

    //Rimuove il dato nella collezione se vengono rispettati i controlli di identità
    public E remove(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;

    //Crea una copia del dato nella collezione se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException, UserAccessDeniedException;

    //Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, IllegalArgumentException, UserAccessDeniedException;

    //restituisce un iteratore (senza remove) che genera tutti i dati dell’utente in ordine arbitrario se vengono
    // rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, UserAccessDeniedException;

    // TODO altre operazioni da definire a scelta
}
