package mailProject.client;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mailProject.model.ClientModel;
import mailProject.model.Email;

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

    private ClientModel model;
    private GridPane formPane;
    private ClientFormController formController;
    private Stage formWindow;
    private Scene formScene;

    public void initClientModel(ClientModel model) throws IOException{
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;

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

        FXMLLoader formLoader = new FXMLLoader(getClass().getResource("clientFXML/clientFormView.fxml"));
        formPane = formLoader.load();
        formController = formLoader.getController();
        formController.initClientModel(model);
        formScene = new Scene(formPane);
    }

    @FXML
    public void newMail() {
        formWindow = new Stage();
        formWindow.setTitle("New E-Mail");
        formWindow.setScene(formScene);
        formWindow.show();
    }

    @FXML
    public void deleteMail() {
        model.getEmailList().remove(model.getCurrentEmail());
        model.setCurrentEmail(null);
        model.setIsEmailSelected(false);
    }
}
