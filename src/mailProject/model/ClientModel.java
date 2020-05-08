package mailProject.model;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    // NEW EMAIL BOOL Property //

    private final SimpleBooleanProperty isNewMailReceived = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty getNewMailReceivedBooleanProperty() {
        return isNewMailReceived;
    }
    public final boolean getIsNewMailReceived() {
        return getNewMailReceivedBooleanProperty().get();
    }
    public final void setIsNewMailReceived(boolean bool) {
        getNewMailReceivedBooleanProperty().set(bool);
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
        try {
            threadedEmailReceiver = new ThreadedEmailReceiver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(threadedEmailReceiver).start();
    }
}


