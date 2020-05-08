package mailProject.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerModel {

    // CONNECTION PARAMETERS //

    private ServerSocket serverSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean runningServer = true;

    // I/O Parameters //

    private static final String FILEPATH = "src/mailProject/server/storage.dat";

    // LOG List PROPERTY //

    private final ObservableList<String> logList = FXCollections.observableArrayList();
    public ObservableList<String> getLogList() {
        return logList;
    }

    // USER Session PROPERTY //
    private final ObservableList<ServerRequestHandler> userSessions = FXCollections.observableArrayList();
    public ObservableList<ServerRequestHandler> getUserSessions() {
        return userSessions;
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

    // Unique INDEX //

    private long uniqueId;
    public synchronized long getUniqueId() {
        return uniqueId++;
    }

    // CONSTRUCTOR //

    public ServerModel() {
        logList.add("Server started ...");
        logList.add("Loading email ...");
        loadMailFromFile();
        startServer();
    }

    // METHOD //

    private void loadMailFromFile() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILEPATH));

            uniqueId = in.readLong();
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

            out.writeLong(uniqueId);
            for (ArrayList<Email> emails : mailMap.values()) {
                for (Email mail : emails) {
                    out.writeObject(mail);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        runningServer = false;
        try {
            for (ServerRequestHandler srh : getUserSessions()) {
                srh.outputStream.writeObject("serverOffline");
            }
            serverSocket.close();
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

    private void startServer() {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server creato.");

            new Thread(() -> {
                while (runningServer) {
                    Socket incoming = null;
                    try {
                        incoming = serverSocket.accept();
                    } catch (IOException e) {
                        System.out.println("Server chiuso.");
                    }
                    if (incoming != null) {
                        Runnable runnable = new ServerRequestHandler(incoming, this);
                        new Thread(runnable).start();
                    }
                }
            }).start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
