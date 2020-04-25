package mailProject.client;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import java.util.regex.Pattern;

public class ClientFormController {

    private ClientModel model;
    private final FadeTransition fadeOut = new FadeTransition(Duration.millis(5000));
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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

    @FXML
    private Button closeButton;



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
        ArrayList<String> emailReceivers = new ArrayList<>();
        boolean flagValidEmail = true;
        String[] auxReceiver = receiverField.getText().split(", *");
        String emailSubject = subjectField.getText();
        String emailText = textField.getText();

        for (String s : auxReceiver) {
            if (s.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                emailReceivers.add(s);
            } else {
                errorLabel.setText("Insert a valid receiver address.");
                errorLabel.setTextFill(Color.DARKRED);
                flagValidEmail = false;
            }
        }

        if (flagValidEmail) {
            model.getEmailList().add(new Email(model.getClientUsername(), emailReceivers,
                    emailSubject, emailText, model.getUniqueId()));
        }

    }

    @FXML
    private void closeButtonAction(){
        long mTime = System.currentTimeMillis();
        long end = mTime + 2000;
        Stage stage = (Stage) closeButton.getScene().getWindow();
        while (mTime < end)
        {
            mTime = System.currentTimeMillis();
        }
        stage.close();
    }
}
