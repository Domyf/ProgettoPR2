package DomenicoFerraro;

import java.util.Iterator;
import java.util.Scanner;

public class Main {

    private static Scanner reader;
    private static SecureDataContainer<Integer> container;

    public static void main(String[] args) {
        SecureDataContainer<Integer> container = new SharedDataContainer<>();
        //SecureDataContainer<Integer> container = new HashingDataContainer<>();

        User mario = new User("Mario", "passmario");
        User sara = new User("Sara", "passwSara");

        //Creo due utenti e provo a crearne un altro che ha lo stesso id
        try {
            System.out.println("- Creo l'utente "+mario.getId());
            container.createUser(mario.getId(), mario.getPassw());
            System.out.println("Utente "+mario.getId()+" creato!");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Utente già esistente!");
        }

        try {
            System.out.println("\n - Creo l'utente "+mario.getId()+" ma con una password diversa.");
            container.createUser(mario.getId(), "passdiversa");
            System.out.println("Utente "+mario.getId()+" creato!");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Utente già esistente!");
        }

        try {
            System.out.println("\n - Creo l'utente "+sara.getId());
            container.createUser(sara.getId(), sara.getPassw());
            System.out.println("Utente "+sara.getId()+" creato!");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Utente già esistente!");
        }

        System.out.println("\n - "+mario.getId()+" inserisce il numero 10 e "+sara.getId()+" inserisce il numero 342");
        //Inserisco i dati
        try {
            if (container.put(mario.getId(), mario.getPassw(), 10))
                System.out.println(mario.getId()+" ha inserito con successo il numero 10!");
            else
                System.out.println("Numero 10 non inserito!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato! Dato non inserito.");
        }

        try {
            if (container.put(sara.getId(), sara.getPassw(), 342))
                System.out.println(sara.getId()+" ha inserito con successo il numero 342!");
            else
                System.out.println("Numero 342 non inserito!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato! Dato non inserito.");
        }

        //Inserisco un dato ma sbaglio credenziale
        try {
            System.out.println("\n - "+ sara.getId()+" inserisce il numero 720 ma sbaglia l'ID.");
            if (container.put("Sarasbagliato", sara.getPassw(), 720))
                System.out.println(sara.getId()+" ha inserito con successo il numero 720!");
            else
                System.out.println("Numero 342 non inserito!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato! Dato non inserito.");
        }

        try {
            System.out.println("\n - "+ sara.getId()+" inserisce il numero 60");
            if (container.put(sara.getId(), sara.getPassw(), 60))
                System.out.println(sara.getId()+" ha inserito con successo il numero 60!");
            else
                System.out.println("Numero 60 non inserito!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato! Dato non inserito.");
        }

        //Stampo i dati di Mario
        try {
            System.out.println("\n - Mario ottiene e stampa i suoi dati");
            Iterator<Integer> marioDataIt = container.getIterator(mario.getId(), mario.getPassw());
            System.out.print("[");
            while (marioDataIt.hasNext()) {
                System.out.print(" ");
                System.out.print(marioDataIt.next());
                System.out.print(" ");
            }
            System.out.println("] per un totale di " +  container.getSize(mario.getId(), mario.getPassw())+ " dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }

        //Stampo i dati di Sara
        try {
            System.out.println("\n - Sara ottiene e stampa i suoi dati");
            Iterator<Integer> saraDataIt = container.getIterator(sara.getId(), sara.getPassw());
            System.out.print("[");
            while (saraDataIt.hasNext()) {
                System.out.print(" ");
                System.out.print(saraDataIt.next());
                System.out.print(" ");
            }
            System.out.println("] per un totale di " +  container.getSize(sara.getId(), sara.getPassw())+ " dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }

        //Sara condivide con Mario
        try {
            System.out.println("\n - Sara condivide il numero 342 con Mario");
            container.share(sara.getId(), sara.getPassw(), mario.getId(), 342);
            System.out.println("Dato condiviso con successo.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (IllegalArgumentException e) {
            System.out.println("Sara non ha accesso a questo dato oppure si vuole condividere il dato con se stessi! Dato non condiviso.");
        } catch (UserNotExistsException e) {
            System.out.println("Si vuole condividere il dato con un utente che non esiste! Dato non condiviso.");
        }

        //Sara condivide con Mario la stessa cosa
        try {
            System.out.println("\n - Sara ricondivide il numero 342 con Mario");
            container.share(sara.getId(), sara.getPassw(), mario.getId(), 342);
            System.out.println("Dato condiviso con successo.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (IllegalArgumentException e) {
            System.out.println("Mario ha già accesso a questo dato! Dato non condiviso.");
        } catch (UserNotExistsException e) {
            System.out.println("Si vuole condividere il dato con un utente che non esiste! Dato non condiviso.");
        }

        //Mario duplica
        try {
            System.out.println("\n - Mario duplica il suo 10");
            container.copy(mario.getId(), mario.getPassw(), 10);
            System.out.println("Duplicazione avvenuta con successo.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (IllegalArgumentException e) {
            System.out.println("Mario non ha accesso a questo dato! Duplicazione non avvenuta.");
        }

        //Stampo i dati di Mario
        try {
            System.out.println("\n - Mario ottiene e stampa i suoi dati");
            Iterator<Integer> marioDataIt = container.getIterator(mario.getId(), mario.getPassw());
            System.out.print("[");
            while (marioDataIt.hasNext()) {
                System.out.print(" ");
                System.out.print(marioDataIt.next());
                System.out.print(" ");
            }
            System.out.println("] per un totale di " +  container.getSize(mario.getId(), mario.getPassw())+ " dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }

        //Stampo i dati di Sara
        try {
            System.out.println("\n - Sara ottiene e stampa i suoi dati");
            Iterator<Integer> saraDataIt = container.getIterator(sara.getId(), sara.getPassw());
            System.out.print("[");
            while (saraDataIt.hasNext()) {
                System.out.print(" ");
                System.out.print(saraDataIt.next());
                System.out.print(" ");
            }
            System.out.println("] per un totale di " +  container.getSize(sara.getId(), sara.getPassw())+ " dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }

        //Cancello il 342
        try {
            System.out.println("\n - Sara cancella dalla collezione il numero 342");
            if (container.remove(sara.getId(), sara.getPassw(), 342) != null)
                System.out.println("Dato rimosso!");
            else
                System.out.println("Dato non rimosso!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }

        //Stampo i dati di Mario
        try {
            System.out.println("\n - Mario ottiene e stampa i suoi dati");
            Iterator<Integer> marioDataIt = container.getIterator(mario.getId(), mario.getPassw());
            System.out.print("[");
            while (marioDataIt.hasNext()) {
                System.out.print(" ");
                System.out.print(marioDataIt.next());
                System.out.print(" ");
            }
            System.out.println("] per un totale di " +  container.getSize(mario.getId(), mario.getPassw())+ " dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }

        //Stampo i dati di Sara
        try {
            System.out.println("\n - Sara ottiene e stampa i suoi dati");
            Iterator<Integer> saraDataIt = container.getIterator(sara.getId(), sara.getPassw());
            System.out.print("[");
            while (saraDataIt.hasNext()) {
                System.out.print(" ");
                System.out.print(saraDataIt.next());
                System.out.print(" ");
            }
            System.out.println("] per un totale di " +  container.getSize(sara.getId(), sara.getPassw())+ " dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }
    }

    /*public static void main(String[] args) {
        //container = new SharedDataContainer<>();
        container = new HashingDataContainer<>();
        reader = new Scanner(System.in);
        boolean run = true;

        while (run) {
            StampaMenu();
            int op = ChiediOperazione();
            switch (op)
            {
                case 0: //Esci dal programma
                    run = false;
                    break;
                case 1: //Crea utente
                    CreaUtente();
                    break;
                case 2: //Inserisci dato
                    InserisciDato();
                    break;
                case 3: //Rimuovi dato
                    RimuoviDato();
                    break;
                case 4: //Leggi dato
                    OttieniDato();
                    break;
                case 5: //Copia il dato
                    CopiaDato();
                    break;
                case 6: //Condividi il dato
                    CondividiDato();
                    break;
                case 7: //Ottieni dimensione
                    GetSize();
                    break;
                case 8: //Stampa dati
                    StampaDati();
                    break;
            }
            System.out.println();
        }
        System.out.println("Ciao!");
    }*/

    private static void StampaMenu() {
        System.out.println("0 - Esci dal programma");
        System.out.println("1 - Aggiungi Utente");
        System.out.println("2 - Inserisci Dato");
        System.out.println("3 - Rimuovi Dato");
        System.out.println("4 - Leggi Dato");
        System.out.println("5 - Copia Dato");
        System.out.println("6 - Condividi Dato");
        System.out.println("7 - Ottieni Dimensione");
        System.out.println("8 - Stampa Dati");
    }

    private static int ChiediOperazione() {
        return ChiediInteger("Inserisci operazione");
    }

    private static String ChiediStringa(String message) {
        System.out.print(message+": ");
        return reader.next();
    }

    private static Integer ChiediInteger(String message) {
        System.out.print(message+": ");
        return reader.nextInt();
    }

    private static void CreaUtente() {
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");

        try {
            container.createUser(username, passw);
            System.out.println("Utente creato!");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Utente già esistente!");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        }
    }

    private static void InserisciDato(){
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");
        Integer dato = ChiediInteger("Dato");

        try {
            if (container.put(username, passw, dato))
                System.out.println("Dato inserito!");
            else
                System.out.println("Dato non inserito!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato! Dato non inserito.");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        }
    }

    private static void RimuoviDato() {
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");
        Integer dato = ChiediInteger("Dato");

        try {
            if (container.remove(username, passw, dato) != null)
                System.out.println("Dato rimosso!");
            else
                System.out.println("Dato non rimosso!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato! Dato non rimosso.");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        }
    }

    private static void GetSize() {
        String username = ChiediStringa("Username");
        String password = ChiediStringa("Password");
        try {
            int size = container.getSize(username, password);
            System.out.println("L'utente "+username+" ha "+size+" dati.");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        }
    }

    private static void OttieniDato() {
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");
        Integer datoInput = ChiediInteger("Dato");

        try {
            Integer dato = container.get(username, passw, datoInput);
            if (dato != null)
                System.out.println("Il dato letto è "+dato);
            else
                System.out.println("Il dato letto non è presente");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        }
    }

    private static void CopiaDato(){
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");
        Integer datoInput = ChiediInteger("Dato");

        try {
            container.copy(username, passw, datoInput);
            System.out.println("Dato copiato con successo");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        } catch (IllegalArgumentException e) {
            System.out.println("L'utente "+username+" non ha accesso a questo dato!");
        }
    }

    private static void CondividiDato() {
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");
        String other = ChiediStringa("Altro Utente");
        Integer datoInput = ChiediInteger("Dato");

        try {
            container.share(username, passw, other, datoInput);
            System.out.println("Dato condiviso con successo.");
        } catch (UserNotExistsException e) {
            System.out.println("L'utente "+other+" non esiste!!!");
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        }  catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        } catch (IllegalArgumentException e) {
            System.out.println("L'utente "+username+" non ha accesso a questo dato oppure si vuole condividere il dato con se stessi!");
        }
    }

    private static void StampaDati(){
        String username = ChiediStringa("Username");
        String passw = ChiediStringa("Password");

        try {
            Iterator it = container.getIterator(username, passw);
            printCollection(it);
        } catch (UserAccessDeniedException e) {
            System.out.println("Accesso negato!");
        } catch (NullPointerException e) {
            System.out.println("Qualche valore è nullo!");
        }
    }

    private static void printCollection(Iterator it){
        System.out.print("La collezione contiene: [");
        while (it.hasNext())
            System.out.print(it.next()+" ");
        System.out.println("]");
    }
}
