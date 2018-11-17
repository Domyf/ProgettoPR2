package DomenicoFerraro;

import java.util.Iterator;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        Vector<Integer> integers = new Vector<>();
        Vector<Boolean> flags = createFalseFlagsVector(4);
        flags.set(1, true);
        flags.set(3, true);
        integers.add(10);
        integers.add(15);
        integers.add(18);
        integers.add(32);
        printCollection(integers.iterator(), flags.iterator());
        Integer diciotto = 28;
        int pos = integers.indexOf(diciotto);
        if (pos >= 0) {
            System.out.println(diciotto+" è presente nella posizione " + pos + ", lo rimuovo.");
            integers.remove(pos);
            flags.remove(pos);
        } else {
            System.out.println(diciotto+" non è presente.\n");
        }
        printCollection(integers.iterator(), flags.iterator());

        integers.add(64);
        flags.add(false);
        printCollection(integers.iterator(), flags.iterator());
    }

    public static void printCollection(Iterator it1, Iterator it2){
        while (it1.hasNext())
            System.out.println(it1.next()+" "+it2.next());
        System.out.println();
    }

    private static Vector<Boolean> createFalseFlagsVector(int dim){
        Vector<Boolean> vector = new Vector<>(dim);
        for (int i=0; i<dim; i++)
            vector.add(false);
        return vector;
    }
}
