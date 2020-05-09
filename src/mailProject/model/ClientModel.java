package mailProject.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientModel {

    // CONNECTION PARAMETERS //

    private Socket userSocket;
    private String nomeHost;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientResponseHandler clientResponseHandler;

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
    public final void setIsEmailSelected(boolean bool) {
        emailSelectedBooleanProperty().set(bool);
    }

    // CLIENT CONNECTION NOTIFIER //

    private final SimpleBooleanProperty isClientConnected = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty isClientConnectedProperty(){return isClientConnected; }
    public final void setIsClientConnected(boolean bool){isClientConnectedProperty().set(bool);}

    // USERNAME PROPERTY //

    private final String clientUsername;
    public String getClientUsername() {
        return clientUsername;
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

            attendEmail();

            outputStream.writeObject(clientUsername);
            outputStream.flush();

        } catch (IOException e) {
            System.err.println("Server Offline ...");
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

            if (outputStream != null)
                outputStream.writeObject("sessionClosed");

            if (isClientConnectedProperty().get()) {
                clientResponseHandler.quit();
            }

            if (userSocket != null)
                userSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMailFromServer(Email email) {
        try {

            outputStream.writeObject("deleteMail");
            outputStream.writeObject(email);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attendEmail() {
        try {
            clientResponseHandler = new ClientResponseHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(clientResponseHandler).start();
    }
}


