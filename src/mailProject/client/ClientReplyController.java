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

public class ClientReplyController {

    private ClientModel model;

    @FXML
    private TextField receiverField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea textField;

    @FXML
    private Label errorLabel;

    @FXML
    private GridPane replyPane;

    public void initClientModel(ClientModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }

}
