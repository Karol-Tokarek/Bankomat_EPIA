import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.jar.JarInputStream;

public class Main {

    int PIN;
    int ID;
    int[] uzytkownicyID = new int[5];
    int[] hasla = new int[5];
    int[] kwoty = new int[5];

    public Main(int PIN, int ID, int z) throws FileNotFoundException {
        this.PIN = PIN;
        this.ID = ID;
        Zpliku();

    }

    public void pobieraniePinu() throws FileNotFoundException{
        System.out.println("Wpisz swój PIN:");
        Scanner scan = new Scanner(System.in);
        int PIN2 = scan.nextInt();
        sprawdz(PIN2);
    }

    boolean niepasuje = true;

    private void sprawdz(int PIN) throws FileNotFoundException{
        //System.out.println(PIN);
        for (int p = 0; p < 5; p++) {

            if (PIN == hasla[p]) {
                ID = uzytkownicyID[p];
                niepasuje = false;
                istnieje(niepasuje);
                wybieranieOperacji(kwoty[p], PIN, p);
            }


        }
        if (niepasuje == false) {
        } else {
            System.out.println("Nie znaleziono");
            istnieje(niepasuje);
        }

    }

    private boolean istnieje(boolean niepas) {
        if (niepas == true) {
            return false;
        } else {
            return true;
        }

    }

    private void wybieranieOperacji(int kwota_z_bazy, int PINek, int nr) throws FileNotFoundException{
        //int wybor;
        System.out.println("-------- WPLACANIE = 1, WYPLACANIE = 2, WYJSCIE = 3, ZMIANA_PINU = 4, TRANSAKCJE = 5 , PRZELEW = 6 ----------");
        Scanner scan = new Scanner(System.in);
        int wybor = scan.nextInt();
        switch (wybor) {
            case 1:
                wplacanie(kwota_z_bazy, nr);
                break;
            case 2:
                wyplacanie(kwota_z_bazy, nr);
                break;
            case 3:
                System.exit(1);

                break;
            case 4:
                  zmiana_pinu(PINek, nr);
                break;

            case 5:
                  transakcje(kwota_z_bazy, nr);
                break;

            case 6:
                przelew(kwota_z_bazy, nr);
                break;

            default:
                // cout<<"ERROR";
        }


    }

    private void wplacanie(int kwota_z_bazy, int nr) throws FileNotFoundException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Ile wplacamy? ");
        int kwota = scan.nextInt();
        kwota_z_bazy = kwota_z_bazy + kwota;
        System.out.println("Kwota po wplacie: " + kwota_z_bazy);
        kwoty[nr] = kwota_z_bazy;
        aktualizacja();
        System.exit(1);

    }

    private void wyplacanie(int kwota_z_bazy, int nr) throws FileNotFoundException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Ile wyplacamy? ");
        int kwota = scan.nextInt();
        int tak = 1;
        if (kwota_z_bazy < kwota) {
            System.out.println("Czy na pewno chcesz sie zadluzyc? Masz za malo hajsu 1= tak, 0 = nie");
            tak = scan.nextInt();
        }
        if (tak == 1) {
            kwota_z_bazy = kwota_z_bazy - kwota;
            System.out.println("Kwota po wyplacie: " + kwota_z_bazy);
            kwoty[nr] = kwota_z_bazy;
            aktualizacja();
            System.exit(1);

        }

    }

    private void zmiana_pinu(int pin, int nr) throws FileNotFoundException {
        int nowypin;
        Scanner scan = new Scanner(System.in);
        System.out.println("Zmieniamy PIN , podaj nowy PIN !: ");
        nowypin = scan.nextInt();
        hasla[nr] = nowypin;
        aktualizacja();
        //wypisz_baze();



    }

    private void transakcje(int kwota_z_bazy, int nr) throws FileNotFoundException{
        Scanner scan = new Scanner(System.in);
        int ile_operacji;
        int kwota_doladowania;
        System.out.println("Transakcja! Podaj ilosc operacji, ktorych bedziesz dokonywal:");
        ile_operacji = scan.nextInt();
        int[][] tablica_operacji = new int[ile_operacji][3];
            for(int i=0;  i<ile_operacji; i++) {
                for(int j=0; j<3; j++) {

                    tablica_operacji[i][j]=0;

                }


            }

            for(int i=0; i<ile_operacji; i++){
                System.out.println("Podaj kwote doladowania (np. telefonu): ");
                kwota_doladowania = scan.nextInt();
                if(kwota_doladowania <= kwota_z_bazy)
                {
                    kwoty[nr] = kwoty[nr] - kwota_doladowania;
                    kwota_z_bazy = kwota_z_bazy - kwota_doladowania;
                    aktualizacja();
                    tablica_operacji[i][0] = i+1;
                    tablica_operacji[i][1] = nr+1;
                    tablica_operacji[i][2] = kwota_doladowania;
                }
                else
                {
                    System.out.println("Masz za malo pieniedzy! :( ");
                    tablica_operacji[i][0] = i+1;
                    tablica_operacji[i][1] = nr+1;
                    tablica_operacji[i][2] = 0;  ///poniewaz za malo hajsu :(


                }
            }
            System.out.println("");
            System.out.println(" ---Logi z transakcji:--- (po kolei: ID transakcji, UID uzytkownika dokonujacego operacji, Kwota doladowania ");

            for(int i=0;  i<ile_operacji; i++) {
                for(int j=0; j<3; j++) {

                System.out.print(tablica_operacji[i][j] + " ");

            }
                System.out.println("");

        }







    }
    private void przelew(int kwota_z_bazy, int nr)  throws FileNotFoundException{

        Scanner scan = new Scanner(System.in);
        System.out.println("Do kogo wysyłamy przelew? Podaj id uzytkownika: ");
        int odbiorca = scan.nextInt();
        System.out.println("Ile przelewamy do uzytkownika o id: " + odbiorca + "?: ");
        int ile_przelewamy = scan.nextInt();
        if(ile_przelewamy <= kwota_z_bazy)
        {
            kwota_z_bazy = kwota_z_bazy - ile_przelewamy;
            System.out.println("Kwota po przelewie: " + kwota_z_bazy);
            kwoty[nr] = kwota_z_bazy;
            kwoty[odbiorca-1] = kwoty[odbiorca-1] + ile_przelewamy;
            aktualizacja();
            System.out.println("Przelałeś "+ile_przelewamy+"PLN do uzytkownika o ID:"+odbiorca);
            System.exit(1);

        }
        else
        {
            System.out.println("ZA MAŁO PIENIĘDZY !");
        }

    }

    private void aktualizacja() throws FileNotFoundException{

       // File file = new File("src\\dane.txt");
        PrintWriter zapis = new PrintWriter("src\\dane.txt");
        int x, y, z;

        for(int i=0; i<5; i++)
        {


            x = uzytkownicyID[i];
            y = hasla[i];
            z = kwoty[i];
            zapis.println(x + " " +  y + " " + z);

        }
        zapis.close();

    }


    private void Zpliku() throws FileNotFoundException, NoSuchElementException
    {

        File file = new File("src\\dane.txt");
        Scanner in = new Scanner(file);
        System.out.println("BAZA DANYCH: (ID, PIN, STAN KONTA)");

        boolean exists = file.exists();
       // System.out.println(exists);
        int a, b, c;
        int i=0;
        try {
            while (in.hasNextLine()) {
                a = in.nextInt();
                b = in.nextInt();
                c = in.nextInt();


                uzytkownicyID[i] = a;
                hasla[i] = b;
                kwoty[i] = c;
                System.out.println(uzytkownicyID[i] + " " + hasla[i] + " " + kwoty[i]);
                //int zdanie = in.nextInt();
                //  System.out.println(zdanie);
                i++;

            }
            in.close();
            }
        catch(Exception e){
            return;
    }


    }





    public static void main(String[] args) throws FileNotFoundException
    {


        Main M1 = new Main(0, 0, 0);
        M1.pobieraniePinu();

    }



}
