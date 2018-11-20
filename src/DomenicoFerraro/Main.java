package DomenicoFerraro;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        //new Window(800,600,"TestProgettoPR2");
        //Creo il dato, l'utente e la collezione
        Integer diociotto = 18;
        User dom = new User("Domenico", "pass123");
        MySecureDataContainer<Integer> container = new MySecureDataContainer<>();

        //Aggiungo l'utente
        container.createUser(dom.getId(), dom.getPassw());
        //Aggiungo il dato
        boolean done = container.put(dom.getId(), dom.getPassw(), diociotto);
        //Stampo se ho avuto successo o meno
        System.out.println("Il dato è stato inserito: "+done);

        //Stampo i dati presenti nella collezione
        printCollection(container.getStorage().iterator());
        //Provo ad ottenere il dato che ho inserito prima
        Integer copia = container.get(dom.getId(), dom.getPassw(), diociotto);
        //Stampo il dato ottenuto
        System.out.println("La copia vale: "+copia);
        //copia = 20;
        container.remove(dom.getId(), dom.getPassw(), diociotto);
        System.out.println("Cancello il dato");
        printCollection(container.getStorage().iterator());
    }

    private static void printCollection(Iterator it){
        System.out.print("La collezione contiene: [");
        while (it.hasNext())
            System.out.print(it.next()+" ");
        System.out.println("]");
    }
}
