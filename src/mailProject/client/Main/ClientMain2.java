package mailProject.client.Main;

import javafx.application.Application;
import javafx.stage.Stage;
import mailProject.client.Classes.ClientGUI;

public class ClientMain2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new ClientGUI("brunodinotte@libero.it").start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}