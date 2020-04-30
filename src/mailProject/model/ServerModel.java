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
    public void stop() {
        runningServer = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // I/O Parameters //

    private static final String FILEPATH = "src/mailProject/server/storage.dat";

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
        logList.add("Loading email ...");
        loadMailFromFile();
        startServer();
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
                        e.printStackTrace();
                    }
                    Runnable runnable = new ThreadedRequestHandler(incoming, mailMap, this);
                    new Thread(runnable).start();
                }
            }).start();
        }
        catch (IOException e) {e.printStackTrace();}
    }
}

class ThreadedRequestHandler implements Runnable {

    Socket incoming;
    HashMap<String, ArrayList<Email>> mailMap;
    ServerModel serverModel;

    public ThreadedRequestHandler (Socket incoming, HashMap<String, ArrayList<Email>> map, ServerModel model) {
        this.incoming = incoming;
        this.mailMap = map;
        serverModel = model;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(incoming.getOutputStream());
            inputStream = new ObjectInputStream(incoming.getInputStream());


            String aux = (String)inputStream.readObject();
            if (aux.equalsIgnoreCase("sessionClosed")) {
                String disconnectedUser = (String)inputStream.readObject();
                Platform.runLater(() -> {
                    serverModel.getLogList().add(disconnectedUser + " si è disconnesso.");
                    serverModel.getUserList().remove(disconnectedUser);
                });
            } else if (aux.equalsIgnoreCase("emailRequest")) {
                Email auxMail = (Email)inputStream.readObject();
                Platform.runLater(() -> {
                    serverModel.getLogList().add("L'utente " + auxMail.getSender() + " ha inviato una mail.");
                });
                outputStream.writeObject(true);

            } else {
                Platform.runLater(() -> {
                    serverModel.getLogList().add(aux + " si è collegato al server.");
                    serverModel.getUserList().add(aux);
                });
                if (mailMap.containsKey(aux)) {
                    System.out.println(mailMap.get(aux));
                    outputStream.writeObject(mailMap.get(aux));
                } else {
                    mailMap.put(aux, new ArrayList<>());
                    outputStream.writeObject(null);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                assert outputStream != null;
                outputStream.flush();
                incoming.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
