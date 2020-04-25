package mailProject.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mailProject.model.ClientModel;
import mailProject.model.Email;

public class ClientGUI extends Application {

    private String applicationUser;

    public ClientGUI(String applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public void start(Stage clientStage) throws Exception {

        BorderPane mainPane = new BorderPane();
        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("clientFXML/clientListView.fxml"));
        ListView<Email> listView = listLoader.load();
        ClientListController listController = listLoader.getController();
        mainPane.setLeft(listView);

        FXMLLoader toolbarLoader = new FXMLLoader(getClass().getResource("clientFXML/clientToolbarView.fxml"));
        ToolBar toolBar = toolbarLoader.load();
        ClientToolbarController toolbarController = toolbarLoader.getController();
        mainPane.setTop(toolBar);

        FXMLLoader emailViewLoader = new FXMLLoader(getClass().getResource("clientFXML/clientEmailView.fxml"));
        GridPane emailView = emailViewLoader.load();
        ClientEmailController emailController = emailViewLoader.getController();
        mainPane.setCenter(emailView);

        ClientModel model = new ClientModel(applicationUser);
        emailController.initClientModel(model);
        listController.initClientModel(model);
        toolbarController.initClientModel(model);

        clientStage.setTitle(model.getClientUsername());
        clientStage.setScene(new Scene(mainPane));
        clientStage.show();
    }
}
