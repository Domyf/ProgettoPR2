package DomenicoFerraro;

import java.util.Iterator;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        Vector<Integer> integers = new Vector<>();
        integers.add(10);
        integers.add(15);
        integers.add(18);
        integers.add(32);

        for (Integer it: integers) {
            if (it.equals(15))
                System.out.println("Boh: "+it);
        }
    }

    public static void printCollections(Iterator it1, Iterator it2){
        while (it1.hasNext())
            System.out.println(it1.next()+" "+it2.next());
        System.out.println();
    }
}
