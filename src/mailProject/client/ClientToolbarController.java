package mailProject.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mailProject.model.ClientModel;

import java.io.IOException;

public class ClientToolbarController {

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

        FXMLLoader formLoader = new FXMLLoader(getClass().getResource("clientFXML/clientFormView.fxml"));
        formPane = formLoader.load();
        formController = formLoader.getController();
        formScene = new Scene(formPane);
    }

    @FXML
    public void newMail() {
        formWindow = new Stage();
        formWindow.setTitle("New E-Mail");
        formWindow.setScene(formScene);
        formWindow.show();
    }
}
