package DomenicoFerraro;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        MySecureDataContainer<Integer> container = new MySecureDataContainer<>();

        container.createUser("Domenico", "pass123");
        boolean done = container.put("Domenico", "pass123", 15);
        System.out.println(done);
    }

    public static void printCollection(Iterator it){
        while (it.hasNext())
            System.out.println(it.next());
    }
}
