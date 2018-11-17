package DomenicoFerraro;

import java.util.HashMap;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        Integer ogg = 90;
        Integer ogg2 = 69;
        String user = "domenico";
        HashMap<String, Integer> oggetti = new HashMap<>(); //K: utenti, V: oggetti E
        oggetti.put(user, ogg);
        oggetti.put(user, ogg2);

        System.out.println("Queste sono le chiavi: ");
        Iterator<Integer> it = oggetti.values().iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }
}
