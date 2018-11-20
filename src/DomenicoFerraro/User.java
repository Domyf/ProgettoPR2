package DomenicoFerraro;

public class User {

    private String id;
    private String passw;

    public User(String id, String passw) {
        this.id = id;
        this.passw = passw;
    }

    /** Confronto tra oggetti. Restituisce true se id e passw sono uguali, false altrimenti*/
    public boolean equals(Object other) {
        return id.equals(((User)other).getId()) && passw.equals(((User)other).getPassw());
    }

    public String getId() {
        return id;
    }

    /** Ritorna true se this ha lo stesso id di other */
    public boolean sameId(String other) {
        return id.equals(other);
    }

    public String getPassw() {
        return passw;
    }

    public String toString(){
        return id+" "+passw;
    }
}
