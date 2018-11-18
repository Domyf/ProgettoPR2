package DomenicoFerraro;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        new Window(800,600,"TestProgettoPR2");
    }

    public static void printCollection(Iterator it){
        while (it.hasNext())
            System.out.println(it.next());
    }
}
