package JavaFXMLApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Random;

public class Controller {

    private Bill myBill = null;
    private final Random random = new Random();

    @FXML
    private Pane pane0;

    @FXML
    private Pane pane1;

    @FXML
    private Pane pane2;

    @FXML
    private Pane pane3;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        //int i = random.nextInt(10000);
        //myBill.setAmount(String.valueOf(i));
        new ColorThread(1, pane0);
        new ColorThread(2, pane1);
        new ColorThread(2, pane2);
        new ColorThread(1, pane3);
    }

    private class ColorThread extends Thread {

        private int delay;
        private Pane pane;

        public ColorThread(int delay, Pane pane) {
            this.delay = delay;
            this.pane = pane;
            start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(delay*1000);
                } catch (InterruptedException e) {e.getMessage();}
                pane.setBackground(new Background(
                        new BackgroundFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()),
                                CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {
        myBill = new Bill();
        textField.textProperty().bind(myBill.amountProperty());
    }*/
}
