package mailProject.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import mailProject.model.ClientModel;

public class ClientFormController {

    private ClientModel model;
    private int receiverCounter = 0;

    @FXML
    private TextField receiverField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea textField;

    @FXML
    private GridPane mainPane;

    public void initClientModel(ClientModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }

    @FXML
    public void addReceiver() {
        TextField receiverField2 = new TextField();
        receiverField2.setPromptText("Other receiver ...");
        mainPane.addRow(++receiverCounter);
        mainPane.add(receiverField2, 0, receiverCounter, 2, 1);
    }

    @FXML
    public void sendEmail() {

    }
}
