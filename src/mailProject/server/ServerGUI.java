package mailProject.server;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mailProject.model.ServerModel;

public class ServerGUI extends Application {

    @FXML
    private ListView<String> userView;

    @FXML
    private ListView<String> logView;

    @Override
    public void start(Stage serverStage) throws Exception {

        ServerModel serverModel = new ServerModel();

        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("serverMainView.fxml"));
        listLoader.setController(this);
        BorderPane serverPane = listLoader.load();

        logView.setItems(serverModel.getLogList());
        logView.setDisable(true);
        userView.setItems(serverModel.getUserList());

        serverStage.setTitle("SERVER");
        serverStage.setScene(new Scene(serverPane));
        serverStage.show();
    }
}
