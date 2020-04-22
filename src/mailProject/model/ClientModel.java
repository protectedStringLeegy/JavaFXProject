package mailProject.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {

    private final ObservableList<Email> emailList = FXCollections.observableArrayList();

    private final ObjectProperty<Email> currentEmail = new SimpleObjectProperty<>(null);

    public ObjectProperty<Email> currentEmailProperty() {
        return currentEmail ;
    }

    public final Email getCurrentEmail() {
        return currentEmailProperty().get();
    }

    public final void setCurrentEmail(Email email) {
        currentEmailProperty().set(email);
    }

    public ObservableList<Email> getEmailList() {
        return emailList ;
    }

    public void loadData() {
        emailList.add(0, new Email("epinefridio@gmail.com", "grandecazzo11@gmail.com",
                        "My Dick", "Ascolta se vuoi vedere il mio cazzo ok.", 11));
        emailList.add(0, new Email("brunodinotte@libero.it", "epinefridio@gmail.com",
                "Some cod skills", "Bello! Guarda che cazzo ho combinato in 2v2 su Cod", 43));
        emailList.add(0, new Email("oguaglioneinnero@outlook.com", "epinefridio@gmail.com",
                "Decoder illegale", "Bro, ho scoperto come pagare pochissimo e avere tutti gli abbonamenti.", 19));
    }
}
