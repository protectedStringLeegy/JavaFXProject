package mailProject.client.Main;

import javafx.application.Application;
import javafx.stage.Stage;
import mailProject.client.Classes.ClientGUI;

public class ClientMain0 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new ClientGUI("utente0@example.com").start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}