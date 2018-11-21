package DomenicoFerraro;

public class UserAccessDeniedException extends Exception {

    public UserAccessDeniedException(){
        super();
    }

    public UserAccessDeniedException(String s) {
        super(s);
    }
}
