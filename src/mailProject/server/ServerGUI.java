package mailProject.server;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mailProject.model.ServerModel;
import mailProject.model.ServerRequestHandler;

public class ServerGUI extends Application {

    @FXML
    private ListView<ServerRequestHandler> userView;

    @FXML
    private ListView<String> logView;

    @Override
    public void start(Stage serverStage) throws Exception {
        System.out.println("Creo il Model ...");
        ServerModel serverModel = new ServerModel();

        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("serverMainView.fxml"));
        listLoader.setController(this);
        BorderPane serverPane = listLoader.load();

        logView.setItems(serverModel.getLogList());
        logView.setDisable(true);
        userView.setItems(serverModel.getUserSessions());
        userView.setCellFactory(threadedRequestHandlerListView -> new UserCell());

        serverStage.setTitle("SERVER");
        serverStage.setScene(new Scene(serverPane));
        serverStage.setOnCloseRequest(windowEvent -> {
            serverModel.saveMailOnFile();
            serverModel.stop();
        });
        serverStage.show();
    }

    static class UserCell extends ListCell<ServerRequestHandler> {

        @Override
        protected void updateItem(ServerRequestHandler serverRequestHandler, boolean b) {
            super.updateItem(serverRequestHandler, b);

            if (!b) {
                if (serverRequestHandler != null) {
                    Label userLabel = new Label();
                    userLabel.setText(serverRequestHandler.getUser());
                    setGraphic(userLabel);
                }
            } else {
                setGraphic(null);
            }
        }
    }
}
