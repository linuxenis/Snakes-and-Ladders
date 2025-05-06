import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class laukelis
{
    public int nr, move; // move = snake arba kopecios
    public boolean mina;

    public laukelis(){}
    public laukelis(int x, int y, boolean z)
    {
        nr = x;
        move = y;
        mina = z;
    }
}

class zaidejas
{
    public int pozicija = 0;
    public String vardas;
}

public class Main {
    public static final String color_reset = "\u001B[0m";
    public static final String red = "\u001B[31m";
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String cyan = "\u001B[36m";
    static void tikrinimas(zaidejas zaid, LinkedList<laukelis> lenta, LinkedList<zaidejas> eile)
    {
        while(true) //ejimas pagal minas ir gyvates/kopecias
        {
            int pos = zaid.pozicija;

            for(int i=0; i<eile.size(); i++)
            {
                if(eile.get(i) != zaid && zaid.pozicija == eile.get(i).pozicija)
                {
                    zaidejas temp = eile.get(i);
                    temp.pozicija--;
                    eile.set(i, temp);
                    tikrinimas(eile.get(i), lenta, eile); //rekursija
                    break;
                }
            }

            if (lenta.get(pos - 1).mina) //minu tikrinimas
            {
                laukelis temp = lenta.get(pos-1);
                temp.mina = false;
                for (int i = 0; i < 6; i++)
                {
                    if(zaid.pozicija > 1)
                        zaid.pozicija--;
                }
                System.out.println(blue + "Uzlipta ant minos! Paeita atgal iki" + zaid.pozicija + color_reset);
            }
            else if (lenta.get(pos - 1).move != 0) //gyvaciu ir kopeciu tikrinimas
            {
                if (lenta.get(pos - 1).move > pos)
                {
                    for (int i = pos; i < lenta.get(pos - 1).move; i++)
                        zaid.pozicija++;
                    System.out.println(blue + "Uzlipta ant kopeciu :) Nueita i prieki iki " + zaid.pozicija + color_reset);
                }
                else
                {
                    for (int i = pos; i > lenta.get(pos - 1).move; i--)
                        zaid.pozicija--;
                    System.out.println(blue + "Uzlipta ant gyvates! Nueita atgal iki " + zaid.pozicija + color_reset);
                }
            }
            else
                break;
        }
    }
    public static void main(String[] args) throws IOException {

        Path pathlenta = Path.of("C://Users//Linas//IdeaProjects//snakes and ladders//jsonlenta.json"); //json path
        String stringlenta = Files.readString(pathlenta); //nuskaitomas json
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<laukelis> lenta = mapper.readValue(stringlenta, new TypeReference<LinkedList<laukelis>>() {}); //sukuriamas linkedlistas
        Scanner input = new Scanner(System.in);

        outer: while(true)
        {
            System.out.println("1 - redaguoti lenta.");
            System.out.println("2 - zaisti.");
            System.out.println("3 - baigti programa.");
            System.out.printf(green + "Iveskite pasirinkima: " + color_reset);
            int nr = input.nextInt();

            inner: switch(nr)
            {
                case 1:
                {
                    redagavimas: while(true) {
                        System.out.println("1 - papildyti zaidimo lenta zaidimo laukeliu (i pozicija)");
                        System.out.println("2 - istrinti norima laukeli");
                        System.out.println("3 - papildyti arba istrinti gyvateles ir kopeteles");
                        System.out.println("4 - ideti/panaikinti mina");
                        System.out.println("5 - is naujo uzkrauti zaidimo lenta is dokumento");
                        System.out.println("6 - perziureti lenta");
                        System.out.println("7 - atgal i meniu");
                        System.out.printf(green + "Iveskite pasirinkima: " + color_reset);
                        int pasirinkimas = input.nextInt();
                        switch (pasirinkimas) {
                            case 1: //langelio pridejimas
                            {
                                System.out.printf(green + "Iveskite pozicija: " + color_reset);
                                int pos = input.nextInt();
                                if (pos > lenta.size() + 1 || pos < 1)
                                    System.out.println(red + "Netinkama pozicija." + color_reset);
                                else {
                                    laukelis a = new laukelis(pos, 0, false);

                                    lenta.add(pos - 1, a);
                                    for (int i = 0; i < lenta.size(); i++) {
                                        laukelis temp = lenta.get(i);
                                        if (i >= pos)
                                            temp.nr++;
                                        if (temp.move >= pos)
                                            temp.move++;
                                        lenta.set(i, temp);
                                    }
                                }
                                break;
                            }
                            case 2: //langelio salinimas
                            {
                                System.out.printf(green + "Iveskite pozicija: " + color_reset);
                                int pos = input.nextInt();
                                if (pos > lenta.size() || pos < 1)
                                    System.out.println(red + "Netinkama pozicija." + color_reset);
                                else {
                                    lenta.remove(pos - 1);

                                    for (int i = 0; i < lenta.size(); i++)
                                    {
                                        laukelis temp = lenta.get(i);
                                        if (temp.nr > pos)
                                            temp.nr--;
                                        if (temp.move == pos)
                                            temp.move = 0;
                                        if (temp.move > pos)
                                            temp.move--;
                                        lenta.set(i, temp);
                                    }
                                }
                                break;
                            }
                            case 3: //gyvateles/kopeteles pridejimas
                            {
                                System.out.printf(green + "Iveskite gyvateles/kopeteles pradzia: " + color_reset);
                                int pr = input.nextInt();
                                if (pr > lenta.size() || pr < 1) {
                                    System.out.println(red + "Netinkama pozicija." + color_reset);
                                    break;
                                }

                                System.out.printf(green + "Iveskite gyvateles/kopeteles pabaiga(gyvateliu/kopeteliu trinimui rasykite 0): " + color_reset);
                                int pb = input.nextInt();
                                if (pb > lenta.size() || pr == pb) {
                                    System.out.println(red + "Netinkama pozicija." + color_reset);
                                    break;
                                }

                                laukelis temp = lenta.get(pr - 1);
                                temp.move = pb;
                                lenta.set(pr - 1, temp);

                                break;
                            }
                            case 4: //minos pridejimas
                            {
                                System.out.printf(green + "Iveskite pozicija: " + color_reset);
                                int pos = input.nextInt();
                                if (pos > lenta.size() || pos < 1)
                                    System.out.println(red + "Netinkama pozicija." + color_reset);
                                else {
                                    laukelis temp = lenta.get(pos - 1);
                                    temp.mina = !temp.mina;
                                    lenta.set(pos - 1, temp);
                                }
                                break;
                            }
                            case 5: //lentos nuskaitymas is failo
                            {
                                stringlenta = Files.readString(pathlenta);
                                lenta = mapper.readValue(stringlenta, new TypeReference<LinkedList<laukelis>>() {});
                                break;
                            }
                            case 6: //lentos isvedimas
                            {
                                for (int i = 0; i < lenta.size(); i++) {
                                    System.out.println(blue + "nr:" + lenta.get(i).nr + " move:" + lenta.get(i).move + " mina:" + lenta.get(i).mina + color_reset);
                                }
                                break;
                            }
                            case 7: //gryzimas i meniu
                            {
                                break redagavimas;
                            }
                            default: //klaida
                            {
                                System.out.println(red + "Netinkamas Pasirinkimas" + color_reset);
                                break;
                            }
                        }
                    }
                    break;
                }
                case 2: //zaidzia
                {
                    lenta.getLast().mina = false; //nuemu mina ir gyvate nuo paskutinio laukelio
                    lenta.getLast().move = 0;
                    int kiekzaideju;
                    while(true)  //zaideju skaiciaus ivedimas
                    {
                        System.out.printf(green + "Iveskite zaideju skaiciu: " + color_reset);
                        kiekzaideju = input.nextInt();
                        if(kiekzaideju < 0)
                            System.out.println(red + "Netinkamas zaideju skaicius" + color_reset);
                        else
                            break;
                    }
                    LinkedList<zaidejas> eile = new LinkedList<>();
                    for(int i=1; i<=kiekzaideju; i++) //zaideju vardu ivedimas
                    {
                        System.out.printf(green + i + "-o zaidejo vardas: " + color_reset);
                        zaidejas temp = new zaidejas();
                        temp.vardas = input.next();
                        eile.add(temp);
                    }
                    Collections.shuffle(eile);
                    System.out.println("Zaideju eiliskumas: ");
                    for(int i=0; i<kiekzaideju; i++) //zaideju eiliskumas
                    {
                        System.out.println(blue + (i+1) + " eina " + eile.get(i).vardas + '.' + color_reset);
                    }
                    System.out.println(red + "enter mygtuko paspaudimas meta kauliuka, exit ivedimas leidzia iseit atgal i meniu(zaidimo busena nesaugoma)" + color_reset);
                    String failsafe = input.nextLine();
                    while(true) //zaidimo eiga
                    {
                        for(int i=0; i<eile.size(); i++) //kur kas yra
                        {
                            System.out.println(cyan + eile.get(i).vardas + " yra " + eile.get(i).pozicija + " langelyje" + color_reset);
                        }
                        System.out.printf(green + eile.getFirst().vardas + " turi mest kauliuka." + color_reset);
                        String ivestis = input.nextLine();
                        Random rand = new Random();
                        switch(ivestis)
                        {
                            case "exit":
                            {
                                break inner;
                            }
                            default:
                            {
                                int kauliukas = rand.nextInt(6)+1;
                                System.out.println(blue + "Buvo isridenta " + kauliukas + color_reset);
                                if(kauliukas == 6) // minos dejimas
                                {
                                    while(true)
                                    {
                                        System.out.printf(green + "Iveskite kur norit padeti(arba panaikinti) mina: " + color_reset);
                                        int minospos = input.nextInt();
                                        if(minospos > 0 && minospos < lenta.size())
                                        {
                                            laukelis temp = lenta.get(minospos-1);
                                            temp.mina = !temp.mina;
                                            lenta.set(minospos-1, temp);
                                            break;
                                        }
                                        else
                                        {
                                            System.out.println(red + "Mina negali buti uz lentos ribu arba paskutinis langelis" + color_reset);
                                        }
                                    }
                                }
                                boolean atgal = false;
                                for(int i=0; i<kauliukas; i++) //ejimas pagal kauliuka
                                {
                                    if(atgal)
                                        eile.getFirst().pozicija--;
                                    else
                                        eile.getFirst().pozicija++;

                                    if(eile.getFirst().pozicija == lenta.size())
                                        atgal = true;
                                }
                                System.out.println(blue + "Paeita iki " + eile.getFirst().pozicija + color_reset);
                                tikrinimas(eile.getFirst(), lenta, eile);
                                if(eile.getFirst().pozicija == lenta.size()) //laimejimas
                                {
                                    System.out.println(blue + "______________________________" + color_reset);
                                    System.out.println(blue + "laimejo " + eile.getFirst().vardas + '!' + color_reset);
                                    System.out.println(blue + "______________________________" + color_reset);
                                    ivestis = input.nextLine();
                                    break inner;
                                }
                                eile.add(eile.getFirst());
                                eile.removeFirst();
                            }
                        }
                    }
                }
                case 3:
                {
                    System.out.println(blue + "Programa nutraukta." + color_reset);
                    break outer;
                }
                default:
                {
                    System.out.println(red + "Netinkamas Pasirinkimas" + color_reset);
                }
            }
        }
    }
}