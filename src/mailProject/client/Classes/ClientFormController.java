package mailProject.client.Classes;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import mailProject.model.ClientModel;
import mailProject.model.Email;

import java.util.ArrayList;

public class ClientFormController {

    private ClientModel model;
    private final FadeTransition fadeOut = new FadeTransition(Duration.millis(5000));
    private final FadeTransition sendEmailTransition = new FadeTransition(Duration.millis(1000));

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

        sendEmailTransition.setNode(errorLabel);
        sendEmailTransition.setFromValue(1.0);
        sendEmailTransition.setToValue(0.0);
        sendEmailTransition.setCycleCount(1);
        sendEmailTransition.setAutoReverse(false);
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
        ArrayList<String> emailReceivers = new ArrayList<>();
        boolean flagValidEmail = true;
        String[] auxReceiver = receiverField.getText().split(", *");
        String emailSubject = subjectField.getText();
        String emailText = textField.getText();

        for (String s : auxReceiver) {
            if (s.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                emailReceivers.add(s);
            } else {
                flagValidEmail = false;
            }
        }

        if (flagValidEmail && emailSubject.length() > 0 && emailText.length() > 0) {
            errorLabel.setText("Sending email...");
            errorLabel.setTextFill(Color.DARKGREEN);
            sendEmailTransition.playFromStart();
            model.sendEmail(new Email(model.getClientUsername(), emailReceivers,
                    emailSubject, emailText, model.getUniqueId()));
            while (model.isWaitSendingResponse()) {
                errorLabel.setText(errorLabel.getText().concat("."));
            }
            if (model.isMailSended()) {
                errorLabel.setText("Email sended.");
                sendEmailTransition.setOnFinished(actionEvent -> ((Stage)mainPane.getScene().getWindow()).close());
            } else {
                errorLabel.setTextFill(Color.DARKRED);
                errorLabel.setText("Failed to send.");
            }
            sendEmailTransition.playFromStart();

        } else {
            if (emailSubject.length() == 0) {
                errorLabel.setText("Insert a valid email subject.");
                errorLabel.setTextFill(Color.DARKRED);
            } else if (emailText.length() == 0) {
                errorLabel.setText("Insert a valid email subject.");
                errorLabel.setTextFill(Color.DARKRED);
            } else {
                errorLabel.setText("Insert a valid receiver address.");
                errorLabel.setTextFill(Color.DARKRED);
            }

            fadeOut.playFromStart();
        }
    }
}
