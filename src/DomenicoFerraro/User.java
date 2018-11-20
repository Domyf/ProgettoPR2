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
        User oth = (User) other;
        return id.equals(oth.getId()) && passw.equals(oth.getPassw());
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
