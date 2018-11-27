package DomenicoFerraro;

public class User {
    //OVERVIEW: Tipo di dato che descrive la coppia <Id, password>

    //TIPICAL ELEMENT: <id, passw>
    //IR: id != null && passw != null

    private String id;
    private String passw;

    /** MODIFIES: this
     *  EFFECTS: Costruisce this con <id, passw> */
    public User(String id, String passw) {
        this.id = id;
        this.passw = passw;
    }

    /** EFFECTS: Confronta this con l'oggetto passato per argomento. Restituisce true se id e passw sono uguali, false altrimenti*/
    public boolean equals(Object other) {
        User oth = (User) other;
        return id.equals(oth.getId()) && passw.equals(oth.getPassw());
    }

    /** EFFECTS: Restituisce l'id di this */
    public String getId() {
        return id;
    }

    /** EFFECTS: Ritorna true se this ha lo stesso id di other */
    public boolean sameId(String other) {
        return id.equals(other);
    }

    /** EFFECTS: Restituisce la password di this */
    public String getPassw() {
        return passw;
    }

    /** EFFECTS: Restituisce una rappresentazione sottoforma di stringa di this */
    public String toString(){
        return id+" "+passw;
    }
}
