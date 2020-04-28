package mailProject.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerModel {

    // I/O Parameters //

    private static final String FILEPATH = "src/mailProject/EmailStorage.txt";

    // LOG List PROPERTY //

    private final ObservableList<String> logList = FXCollections.observableArrayList();
    public ObservableList<String> getLogList() {
        return logList;
    }

    // USER List PROPERTY //

    private final ObservableList<String> userList = FXCollections.observableArrayList();
    public ObservableList<String> getUserList() {
        return userList;
    }

    // CURRENT USER PROPERTY //

    private final StringProperty currentUser = new SimpleStringProperty();
    public StringProperty currentUserProperty() {
        return currentUser;
    }
    public String getCurrentUser() {
        return currentUserProperty().get();
    }
    public void setCurrentUser(String currentUser) {
        currentUserProperty().set(currentUser);
    }

    // Mail HASH MAP //

    private final HashMap<String, ArrayList<Email>> mailMap = new HashMap<>();
    public HashMap<String, ArrayList<Email>> getMailMap() {
        return mailMap;
    }

    // CONSTRUCTOR //

    public ServerModel() {
        logList.add("Server started ...");

        ArrayList<String> auxArrList = new ArrayList<>();
        auxArrList.add("epinefridio@gmail.com");
        auxArrList.add("oguaglioneinnero@outlook.com");
        auxArrList.add("grandecazzo11@gmail.com");
        auxArrList.add("sonobelloebasta@alice.it");


        ArrayList<Email> emailList = new ArrayList<>();
        emailList.add(0, new Email("picciotto666@gmail.com", "oguaglioneinnero@outlook.com",
                "Minaccia", "Vedi di vendere tutti quei decodere velocemente, il tuo fratellino sta bene. " +
                "E' qui con me.\nDomani ti mando il suo dito indice.", 70));
        emailList.add(0, new Email("epinefridio@gmail.com", "grandecazzo11@gmail.com",
                "My Dick", "Ascolta se vuoi vedere il mio cazzo ok.", 11));
        emailList.add(0, new Email("brunodinotte@libero.it", auxArrList,
                "Some cod skills", "Bello! Guarda che cazzo ho combinato in 2v2 su Cod", 43));
        emailList.add(0, new Email("oguaglioneinnero@outlook.com", "epinefridio@gmail.com",
                "Decoder illegale", "Bro, ho scoperto come pagare pochissimo e avere tutti gli abbonamenti.", 19));

        insertMailsOnFile(emailList);
        logList.add("Loading email ...");
        loadMailFromFile();
        insertUserInView();
    }

    // METHOD //

    private void loadMailFromFile() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILEPATH));

            while (true) {
                Email auxEmail = (Email)in.readObject();

                for (String s : auxEmail.getReceiver()) {
                    if (!mailMap.containsKey(s))
                        mailMap.put(s, new ArrayList<>());
                    mailMap.get(s).add(auxEmail);
                }
            }
        } catch (ClassNotFoundException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (EOFException eofException) {
            logList.add("Email loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMailOnFile() {
        clearFile();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILEPATH));

            for (ArrayList<Email> emails : mailMap.values()) {
                for (Email mail : emails) {
                    out.writeObject(mail);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFile() {
        try (PrintWriter writer = new PrintWriter(FILEPATH)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertMailsOnFile(ArrayList<Email> mails) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILEPATH));

            for (Email mail : mails)
                out.writeObject(mail);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertUserInView() {
        userList.addAll(mailMap.keySet());
    }
}
