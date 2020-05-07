package mailProject.model;

import javafx.application.Platform;
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

    // Email SENDED Property //

    private boolean mailSended = false;
    public boolean isMailSended() {
        return mailSended;
    }
    public void setMailSended(boolean mailSended) {
        this.mailSended = mailSended;
    }

    // Waiting for Mail BOOL //

    private volatile boolean waitSendingResponse = true;
    public boolean isWaitSendingResponse() {
        return waitSendingResponse;
    }
    public void setWaitSendingResponse(boolean waitSendingResponse) {
        this.waitSendingResponse = waitSendingResponse;
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

    // CLIENT CONNECTION NOTIFIER //

    private final SimpleBooleanProperty isClientConnected = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty isClientConnectedProperty(){return isClientConnected; }
    public final boolean getIsClientConnected(){return isClientConnectedProperty().get();}
    public final void setIsClientConnected(boolean bool){isClientConnectedProperty().set(bool);}

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

    public void  refreshClientConnection(){
        connectToServerAndLoadData();
    }

    // METHODS //

    private void connectToServerAndLoadData() {
        try {
            userSocket = new Socket(nomeHost, 8189);
            System.out.println("Connesso al server.");

            outputStream = new ObjectOutputStream(userSocket.getOutputStream());
            inputStream = new ObjectInputStream(userSocket.getInputStream());

            outputStream.writeObject(clientUsername);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmail(Email email) {
        try {
            waitSendingResponse = true;
            System.out.println("Invio la MAIL ...");

            outputStream.writeObject("sendRequest");
            outputStream.writeObject(email);

            while (waitSendingResponse) {
                System.out.println("Attendo risposta...");
                Thread.sleep(100);
            }
            return isMailSended();

        } catch (IOException | InterruptedException e) {
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
        threadedEmailReceiver = new ThreadedEmailReceiver(this);
        new Thread(threadedEmailReceiver).start();
    }
}

class ThreadedEmailReceiver implements Runnable {

    private final ClientModel model;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private boolean quit = false;

    public ThreadedEmailReceiver(ClientModel model) {
        this.model = model;
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
                String aux = (String) inputStream.readObject();
                System.out.println(aux);
                if (aux.equalsIgnoreCase("mailList")) {
                    ArrayList<Email> emailArrayList = (ArrayList<Email>) inputStream.readObject();
                    Platform.runLater(() -> {
                        model.getEmailList().addAll(emailArrayList);
                        model.setIsClientConnected(true);
                    });
                } else if (aux.equalsIgnoreCase("emptyMailList")) {
                    Platform.runLater(() -> {
                        model.setIsClientConnected(true);
                    });
                } else if (aux.equalsIgnoreCase("newMail")) {
                    Email auxEmail = (Email) inputStream.readObject();
                    model.getEmailList().add(auxEmail);
                    outputStream.writeObject("emailReceived");
                } else if (aux.equalsIgnoreCase("mailSended")) {
                    System.out.println("Mail INVIATA!");
                    setServerResponse(true);
                } else if (aux.equalsIgnoreCase("mailFailed")) {
                    System.out.println("Mail FALLITA!");
                    setServerResponse(false);
                }

            } catch (SocketException e) {
                System.out.println("Client chiuso.");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                quit();
            }
        }
    }

    private void setServerResponse(boolean bool) {
        model.setMailSended(bool);
        model.setWaitSendingResponse(false);
    }
}
