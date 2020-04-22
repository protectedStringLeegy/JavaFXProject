package mailProject.client;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mailProject.model.ClientModel;

public class ClientFormController {

    private ClientModel model;
    private int receiverCounter = 0;
    private FadeTransition fadeOut = new FadeTransition(Duration.millis(5000));

    @FXML
    private TextField receiverField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea textField;

    @FXML
    private Label errorLabel;

    @FXML
    private GridPane mainPane;

    public void initClientModel(ClientModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;

        initSceneElements();
    }

    private void initSceneElements() {

        fadeOut.setNode(errorLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(1);
        fadeOut.setAutoReverse(false);
    }

    @FXML
    public void addReceiver() {
        if (receiverField.getText().length() == 0) {
            errorLabel.setText("You need to add the receiver first.");
            errorLabel.setTextFill(Color.DARKRED);
        } else {
            receiverField.setText(receiverField.getText() + ", ");
            errorLabel.setText("Digit the next receiver.");
            errorLabel.setTextFill(Color.DARKGREEN);
        }

        fadeOut.playFromStart();
    }

    @FXML
    public void sendEmail() {

    }
}
