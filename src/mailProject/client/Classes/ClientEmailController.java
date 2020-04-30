package mailProject.client.Classes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mailProject.model.ClientModel;

import java.text.SimpleDateFormat;

public class ClientEmailController {

    private ClientModel model;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMMM y  HH:mm");

    @FXML
    private Label senderLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label textLabel;

    public void initClientModel(ClientModel model) {

        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model;

        model.currentEmailProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                senderLabel.setText("");
                dateLabel.setText("");
                subjectLabel.setText("");
                textLabel.setText("");
            } else {
                senderLabel.setText(newValue.getSender());
                dateLabel.setText(timeFormat.format(newValue.getDate().getTime()));
                subjectLabel.setText(newValue.getSubject());
                textLabel.setText(newValue.getText());
            }

        });
    }
}
