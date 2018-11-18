package DomenicoFerraro;

public class User {

    private String id;
    private String passw;

    public User(String id, String passw) {
        this.id = id;
        this.passw = passw;
    }

    /** Confronto tra oggetti. Restituisce true se id e passw sono uguali, false altrimenti*/
    public boolean equals(User other) {
        return id.equals(other.getId()) && passw.equals(other.getPassw());
    }

    public String getId() {
        return id;
    }

    public boolean sameId(String other) {
        return id.equals(other);
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }
}
