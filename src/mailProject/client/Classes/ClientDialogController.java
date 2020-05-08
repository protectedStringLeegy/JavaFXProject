package mailProject.client.Classes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mailProject.model.Email;

public class ClientDialogController {

    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Button confirmButton;

    public void initDialog(Email email, Stage stage) {
        senderLabel.setText(email.getSender());
        subjectLabel.setText(email.getSubject());
        confirmButton.setOnAction(actionEvent -> {
            stage.close();
        });
    }
}
