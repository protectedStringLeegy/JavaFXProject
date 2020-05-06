package mailProject.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientModel {

    // CONNECTION PARAMETERS //

    private Socket userSocket;
    private String nomeHost;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ThreadedEmailReceiver threadedEmailReceiver;

    // EMAIL List PROPERTY //

    private final ObservableList<Email> emailList = FXCollections.observableArrayList();
    public ObservableList<Email> getEmailList() {
        return emailList ;
    }

    // Input & Output STREAMS //

    public ObjectInputStream getInputStream() {
        return inputStream;
    }
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    // CURRENT EMAIL PROPERTY //

    private final ObjectProperty<Email> currentEmail = new SimpleObjectProperty<>(null);
    public ObjectProperty<Email> currentEmailProperty() {
        return currentEmail ;
    }
    public final Email getCurrentEmail() {
        return currentEmailProperty().get();
    }
    public final void setCurrentEmail(Email email) {
        currentEmailProperty().set(email);
    }

    // IS EMAIL SELECTED PROPERTY //

    private final SimpleBooleanProperty isEmailSelected = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty emailSelectedBooleanProperty() {
        return isEmailSelected;
    }
    public final boolean getIsEmailSelected() {
        return emailSelectedBooleanProperty().get();
    }
    public final void setIsEmailSelected(boolean bool) {
        emailSelectedBooleanProperty().set(bool);
    }

    // USERNAME PROPERTY //

    private final String clientUsername;
    public String getClientUsername() {
        return clientUsername;
    }

    // EMAIL UNIQUE ID //

    private long uniqueId;
    public long getUniqueId() {
        return uniqueId++;
    }
    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    // CONSTRUCTOR //

    public ClientModel(String username) {
        System.out.println("Creazione Model ...");
        clientUsername = username;
        try {
            nomeHost = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        connectToServerAndLoadData();
        attendEmail();
    }

    // METHODS //

    private void connectToServerAndLoadData() {
        try {
            userSocket = new Socket(nomeHost, 8189);
            System.out.println("Connesso al server.");

            outputStream = new ObjectOutputStream(userSocket.getOutputStream());
            inputStream = new ObjectInputStream(userSocket.getInputStream());

            System.out.println("Carico i dati ...");

            outputStream.writeObject(clientUsername);
            outputStream.flush();

            new Thread(() -> {
                try {
                    ArrayList<Email> aux = (ArrayList<Email>)inputStream.readObject();
                    if (aux != null) {
                        emailList.addAll(aux);
                        System.out.println("Dati caricati.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.err.println("ClassNotFoundException");
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmail(Email email) {
        try {

            outputStream.writeObject("sendRequest");
            outputStream.writeObject(email);

            return inputStream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeUserSession() {
        try {
            outputStream.writeObject("sessionClosed");
            threadedEmailReceiver.quit();
            userSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attendEmail() {
        threadedEmailReceiver = new ThreadedEmailReceiver(userSocket, this);
        new Thread(threadedEmailReceiver).start();
    }
}

class ThreadedEmailReceiver implements Runnable {

    private ClientModel model;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean quit = false;

    public ThreadedEmailReceiver(Socket socket, ClientModel model) {
        this.model = model;
        this.socket = socket;
        inputStream = model.getInputStream();
        outputStream = model.getOutputStream();
    }

    public void quit() {
        quit = true;
    }

    @Override
    public void run() {

        while (!quit) {
            try {

                Email auxEmail = (Email) inputStream.readObject();

                if (auxEmail.getReceiver().contains(model.getClientUsername())) {
                    model.getEmailList().add(auxEmail);
                    outputStream.writeObject("emailReceived");
                }

            } catch (SocketException e) {
                System.out.println("Client chiuso.");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
