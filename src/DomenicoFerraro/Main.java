package DomenicoFerraro;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        //new Window(800,600,"TestProgettoPR2");
        //Creo il dato, l'utente e la collezione
        Integer diciotto = 18;
        User dom = new User("Domenico", "pass123");
        MySecureDataContainer<Integer> container = new MySecureDataContainer<>();

        //Aggiungo l'utente
        container.createUser(dom.getId(), dom.getPassw());
        //Aggiungo il dato
        boolean done = container.put(dom.getId(), dom.getPassw(), diciotto);
        //Stampo se ho avuto successo o meno
        System.out.println("Il dato è stato inserito: "+done);

        //Stampo i dati presenti nella collezione
        printCollection(container.getStorage().iterator());
        //Provo ad ottenere il dato che ho inserito prima
        Integer copia = container.get(dom.getId(), dom.getPassw(), diciotto);
        //Stampo il dato ottenuto
        System.out.println("La copia vale: "+copia);
        //copia = 20;

        //Stampo quanti dati ha l'utente dom
        System.out.println(dom.getId()+" ha "+container.getSize(dom.getId(), dom.getPassw())+" dati.");

        //Creo un nuovo utente e lo aggiungo
        User ele = new User("Eleonora", "passEle");
        container.createUser(ele.getId(), ele.getPassw());
        container.share(dom.getId(), dom.getPassw(), ele.getId(), diciotto);
        System.out.println("Ho condiviso il dato con "+ele.getId());

        //Copio il dato
        container.copy(dom.getId(), dom.getPassw(), diciotto);
        printCollection(container.getStorage().iterator());
        //Stampo quanti dati ha l'utente dom
        System.out.println(ele.getId()+" ha "+container.getSize(ele.getId(), ele.getPassw())+" dati.");
        printCollection(container.getIterator(ele.getId(), ele.getPassw()));

        //Cancello il dato
        System.out.println("Cancello il dato");
        Integer deleted = container.remove(dom.getId(), dom.getPassw(), diciotto);
        System.out.println("Ho cancellato "+deleted);
        printCollection(container.getStorage().iterator());
    }

    private static void printCollection(Iterator it){
        System.out.print("La collezione contiene: [");
        while (it.hasNext())
            System.out.print(it.next()+" ");
        System.out.println("]");
    }
}
