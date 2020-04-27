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
        loadMailFromFile();
        logList.add("Email loaded.");
    }

    // METHOD //

    private void loadMailFromFile() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILEPATH));
            Email email = new Email("picciotto666@gmail.com", "oguaglioneinnero@outlook.com",
                    "Minaccia", "Vedi di vendere tutti quei decodere velocemente, il tuo fratellino sta bene. " +
                    "E' qui con me.\nDomani ti mando il suo dito indice.", 70);
            out.writeObject(email);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILEPATH));
            Email imp = (Email) in.readObject();
            System.out.println("Email: "+ imp + "\n");
        } catch (IOException | ClassNotFoundException e) {System.out.println(e.getMessage());}
    }

    public void saveMailOnFile() {
        try {
            FileWriter writer = new FileWriter(FILEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
