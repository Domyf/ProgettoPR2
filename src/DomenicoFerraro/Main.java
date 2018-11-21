package DomenicoFerraro;

import java.util.Iterator;
import java.util.Scanner;

public class Main {

    private static Scanner reader;
    private static SharedDataContainer<Integer> container;

    public static void main(String[] args) {
        //new Window(800,600,"TestProgettoPR2");
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
                case 1:
                    CreaUtente();
                    break;
                case 2:
                    InserisciDato();
                    break;
                case 3:
                    RimuoviDato();
                    break;
            }
            System.out.println();
        }
    }

    private static void StampaMenu() {
        System.out.println("0 - Esci dal programma");
        System.out.println("1 - Aggiungi Utente");
        System.out.println("2 - Inserisci Dato");
        System.out.println("3 - Rimuovi Dato");
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
        }
    }

    private static void printCollection(Iterator it){
        System.out.print("La collezione contiene: [");
        while (it.hasNext())
            System.out.print(it.next()+" ");
        System.out.println("]");
    }
}
