package mailProject.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new ClientGUI("epinefridio@gmail.com").start(new Stage());
        new ClientGUI("picciotto666@gmail.com").start(new Stage());
        new ClientGUI("brunodinotte@libero.it").start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
