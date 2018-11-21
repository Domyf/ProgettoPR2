package DomenicoFerraro;

public class UserNotExistsException extends Exception {

    public UserNotExistsException() {
        super();
    }

    public UserNotExistsException(String s) {
        super(s);
    }
}
