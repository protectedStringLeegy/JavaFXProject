package exInputOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class IndirizzarioMain {
    public static void main(String[] args) {
        Indirizzario adressFromFile = new Indirizzario();
        adressFromFile.importAdress("src/exInputOutput/indirizziPerRubrica.txt");
        adressFromFile.add(new Indirizzario.Indirizzo("Bruschi", "Christian",
                "Cazzi", 5, "Cuorgnè"));
        adressFromFile.exportAdress("src/exInputOutput/indirizziPerRubricaMod.txt");
        System.out.println(adressFromFile);
    }
}

class Indirizzario {

    private ArrayList<Indirizzo> adressList;

    public Indirizzario() {
        adressList = new ArrayList<>();
    }

    public boolean add(Indirizzo indirizzo) {
        return adressList.add(indirizzo);
    }

    public boolean remove(Indirizzo indirizzo) {
        return adressList.remove(indirizzo);
    }

    public void importAdress(String fileName) {

        Scanner adressScan = null;
        Scanner lineScanner = null;

        try {
            adressScan = new Scanner(new File(fileName));

            while (adressScan.hasNextLine()) {
                String line = adressScan.nextLine();
                lineScanner = new Scanner(line);
                lineScanner.useDelimiter("\\s*;\\s* | \\r\\n");
                adressList.add(new Indirizzo(lineScanner.next(), lineScanner.next(), lineScanner.next(),
                        lineScanner.nextInt(), lineScanner.next()));
            }
        } catch (InputMismatchException mismatchException) {
            System.out.println("Il file è formattato male.");
        } catch (FileNotFoundException notFoundException) {
            System.out.println("File inesistente o path errato.");
        } finally {
            if (lineScanner != null)
                lineScanner.close();
            if (adressScan != null)
                adressScan.close();
        }
    }

    public void exportAdress(String fileName) {

        try {
            PrintWriter printWriter = new PrintWriter(new File(fileName));

            for (Indirizzo i : adressList) {
                printWriter.println(i.cognome + "; " + i.nome + "; " + i.via + "; " + i.numCivico + "; " + i.citta);
            }

            printWriter.flush();
        } catch (FileNotFoundException notFoundException) {
            System.out.println("File inesistente o path errato.");
        }
    }

    @Override
    public String toString() {
        return "Indirizzario { " + adressList +
                " }";
    }

    protected static class Indirizzo {

        private String nome;
        private String cognome;
        private String via;
        private int numCivico;
        private String citta;

        public Indirizzo(String cognome, String nome, String via, int numCivico, String citta) {
            this.nome = nome;
            this.cognome = cognome;
            this.via = via;
            this.numCivico = numCivico;
            this.citta = citta;
        }

        public String getNome() {
            return nome;
        }

        public String getCognome() {
            return cognome;
        }

        public String getVia() {
            return via;
        }

        public int getNumCivico() {
            return numCivico;
        }

        public String getCitta() {
            return citta;
        }

        @Override
        public String toString() {
            return "Indirizzo{" +
                    "nome='" + nome + '\'' +
                    ", cognome='" + cognome + '\'' +
                    ", via='" + via + '\'' +
                    ", numCivico=" + numCivico +
                    ", citta='" + citta + '\'' +
                    '}';
        }
    }
}
