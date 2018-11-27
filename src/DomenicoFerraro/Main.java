package DomenicoFerraro;

import java.util.Iterator;
import java.util.Scanner;

public class Main {

    private static Scanner reader;
    private static SharedDataContainer<Integer> container;

    public static void main(String[] args) {
        container = new SharedDataContainer<>();
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
    }

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
        System.out.print("Inserisci operazione: ");
        return reader.nextInt();
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
            container.copy(username, passw,datoInput);
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
