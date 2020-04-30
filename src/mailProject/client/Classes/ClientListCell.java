package mailProject.client.Classes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import mailProject.model.Email;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientListCell extends ListCell<Email> {

    @FXML
    private Label senderLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label previewLabel;

    @FXML
    private GridPane itemGridPane;

    private FXMLLoader fxmlLoader;
    private final SimpleDateFormat hoursFormat = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("dd:M");


    @Override
    protected void updateItem(Email email, boolean empty) {
        super.updateItem(email, empty);

        if(empty || email == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mailProject/client/ClientFXML/clientListItemView.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            setInfo(email);

            setText(null);
            setGraphic(itemGridPane);
        }

    }

    public void setInfo(Email email) {
        if (email != null) {
            itemGridPane.setId(Long.toString(email.getId()));
            senderLabel.setText(email.getSender());
            dateLabel.setText(getDateFormat(email.getDate()));
            subjectLabel.setText(email.getSubject());

            String auxStringPreview = email.getText();
            if (auxStringPreview.length() == 0)
                previewLabel.setText("<No text>");
            else if (auxStringPreview.length() > 29)
                previewLabel.setText(auxStringPreview.substring(0, 30) + "...");
            else
                previewLabel.setText(auxStringPreview);
        }
    }

    public String getDateFormat(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
            return hoursFormat.format(calendar.getTime());
        else return dayFormat.format(calendar.getTime());
    }
}