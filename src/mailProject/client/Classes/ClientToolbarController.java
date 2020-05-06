package mailProject.client.Classes;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import mailProject.model.ClientModel;

import java.io.IOException;

public class ClientToolbarController {

    @FXML
    private Button replyButton;

    @FXML
    private Button replyAllButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button connection;

    @FXML
    private Button refreshButton;

    private ClientModel model;
    private GridPane formPane;
    private Stage formWindow;
    private Scene formScene;
    Tooltip tl = new Tooltip();

    public void initClientModel(ClientModel model, Stage mainStage) throws IOException{
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;

        replyButton.setDisable(true);
        replyAllButton.setDisable(true);
        deleteButton.setDisable(true);
        forwardButton.setDisable(true);
        model.setIsClientConnected(false);
        connection.setStyle("-fx-background-color: red");
        tl.setText("not connected");
        connection.setTooltip(tl);

        model.emailSelectedBooleanProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                replyButton.setDisable(true);
                replyAllButton.setDisable(true);
                deleteButton.setDisable(true);
                forwardButton.setDisable(true);
            } else {
                replyButton.setDisable(false);
                replyAllButton.setDisable(false);
                deleteButton.setDisable(false);
                forwardButton.setDisable(false);
            }
        });


        FadeTransition ft = new FadeTransition(Duration.millis(1000), connection);
        ft.setFromValue(1.0);
        ft.setToValue(0.2);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        model.isClientConnectedProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue){
              refreshButton.setDisable(true);
              connection.setStyle("-fx-background-color: green");
              tl.setText("connected");
          } else {
              refreshButton.setDisable(false);
              connection.setStyle("-fx-background-color: red");
              tl.setText("not connected");
          }
          connection.setTooltip(tl);
      });



        FXMLLoader formLoader = new FXMLLoader(getClass().getClassLoader().getResource("mailProject/client/ClientFXML/clientFormView.fxml"));
        formPane = formLoader.load();
        ClientFormController formController = formLoader.getController();
        formController.initClientModel(model);
        formScene = new Scene(formPane);
        mainStage.setOnCloseRequest(windowEvent -> {
            if (formWindow != null)
                formWindow.close();
        });
    }


    @FXML
    public void newMail() {
        formWindow = new Stage();
        clearForm();
        formWindow.setTitle("New E-Mail");
        formWindow.setScene(formScene);
        formWindow.show();
    }


    @FXML
    public void replyTo() {
        formWindow = new Stage();
        clearForm();
        formWindow.setTitle("Reply-to");
        formWindow.setScene(formScene);
        TextField auxTextField = ((TextField)formPane.getChildren().get(0));
        auxTextField.setText(model.getCurrentEmail().getSender());
        auxTextField.setDisable(true);
        formWindow.show();
    }

    @FXML
    public void replyAll() {
        formWindow = new Stage();
        clearForm();
        TextField auxTextField = ((TextField)formPane.getChildren().get(0));
        auxTextField.setText(getReplyAllReceivers());
        auxTextField.setDisable(true);
        formWindow.setTitle("Reply-all");
        formWindow.setScene(formScene);
        formWindow.show();
    }

    @FXML
    public void forwardTo() {
        formWindow = new Stage();
        clearForm();
        TextArea auxTextField = ((TextArea)formPane.getChildren().get(2));
        auxTextField.setText(model.getCurrentEmail().getText());
        auxTextField.setDisable(true);
        formWindow.setTitle("Forward-to");
        formWindow.setScene(formScene);
        formWindow.show();
    }

    @FXML
    public void deleteMail() {
        model.getEmailList().remove(model.getCurrentEmail());
        model.setCurrentEmail(null);
        model.setIsEmailSelected(false);
    }


    @FXML
    public void refreshPressed() {
        model.refreshClientConnection();
    }

    private void clearForm() {
        for (Node field : formPane.getChildren()) {
            if (TextInputControl.class.isAssignableFrom(field.getClass())) {
                ((TextInputControl)field).setText("");
                field.setDisable(false);
            }
        }
    }

    private String getReplyAllReceivers() {
        StringBuilder auxBuilder = new StringBuilder();
        auxBuilder.append(model.getCurrentEmail().getSender());
        for (String otherReceiver : model.getCurrentEmail().getReceiver()) {
            if (!otherReceiver.equals(model.getClientUsername()))
                auxBuilder.append(", ").append(otherReceiver);
        }

        return auxBuilder.toString();
    }

}
