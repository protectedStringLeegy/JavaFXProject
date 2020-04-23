package mailProject.client;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import mailProject.model.ClientModel;
import mailProject.model.Email;

public class ClientListController {

    @FXML
    private ListView<Email> mailList;
    private ClientModel model;

    public void initClientModel(ClientModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model;
        model.loadData();
        mailList.setItems(model.getEmailList());
        mailList.setCellFactory(param -> new ClientListCell());

        mailList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Email> obs, Email oldSelection, Email newSelection) -> {
            model.setCurrentEmail(newSelection);
        });

        model.currentEmailProperty().addListener((obs, oldPerson, newEmail) -> {
            if (newEmail == null) {
                mailList.getSelectionModel().clearSelection();
            } else {
                mailList.getSelectionModel().select(newEmail);
            }
        });

        model.setCurrentEmail(model.getEmailList().get(0));
    }
}
