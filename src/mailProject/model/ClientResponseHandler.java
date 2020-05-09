package mailProject.model;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mailProject.client.Classes.ClientDialogController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientResponseHandler implements Runnable {

    private final ClientModel model;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private boolean quit = false;

    FXMLLoader dialogLoader = new FXMLLoader(getClass().getClassLoader().getResource("mailProject/client/clientFXML/clientDialogView.fxml"));
    DialogPane dialogPane = dialogLoader.load();
    ClientDialogController dialogController = dialogLoader.getController();
    Stage stage = new Stage();



    private void initDialog() {
        stage.setScene(new Scene(dialogPane));
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    private void showAlert(Email email) {
        dialogController.initDialog(email, stage);
        stage.showAndWait();
    }

    public ClientResponseHandler(ClientModel model) throws IOException {
        this.model = model;
        inputStream = model.getInputStream();
        outputStream = model.getOutputStream();
        initDialog();
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
                } else if (aux.equalsIgnoreCase("serverOffline")) {
                    Platform.runLater(() -> {
                        model.setIsClientConnected(false);
                        model.getEmailList().clear();
                        model.emailSelectedBooleanProperty().set(false);
                    });
                    quit();
                } else if (aux.equalsIgnoreCase("emptyMailList")) {
                    Platform.runLater(() -> model.setIsClientConnected(true));
                } else if (aux.equalsIgnoreCase("newMail")) {
                    Email auxEmail = (Email) inputStream.readObject();
                    Platform.runLater(() -> model.getEmailList().add(auxEmail));
                    outputStream.writeObject("emailReceived");
                    Platform.runLater(() -> showAlert(auxEmail));
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