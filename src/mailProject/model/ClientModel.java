package mailProject.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ClientModel {

    // EMAIL List PROPERTY //

    private final ObservableList<Email> emailList = FXCollections.observableArrayList();
    public ObservableList<Email> getEmailList() {
        return emailList ;
    }

    // CURRENT EMAIL PROPERTY //

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

    // IS EMAIL SELECTED PROPERTY //

    private final SimpleBooleanProperty isEmailSelected = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty emailSelectedBooleanProperty() {
        return isEmailSelected;
    }
    public final boolean getIsEmailSelected() {
        return emailSelectedBooleanProperty().get();
    }
    public final void setIsEmailSelected(boolean bool) {
        emailSelectedBooleanProperty().set(bool);
    }

    // USERNAME PROPERTY //

    private final String clientUsername;
    public String getClientUsername() {
        return clientUsername;
    }

    // EMAIL UNIQUE ID //

    private long uniqueId;
    public long getUniqueId() {
        return uniqueId++;
    }
    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    // CONSTRUCTOR //

    public ClientModel(String username) {
        clientUsername = username;
        loadData();
    }



    private void loadData() {
        ArrayList<String> auxArrList = new ArrayList<>();
        auxArrList.add("epinefridio@gmail.com");
        auxArrList.add("oguaglioneinnero@outlook.com");
        auxArrList.add("grandecazzo11@gmail.com");

        emailList.add(0, new Email("picciotto666@gmail.com", "oguaglioneinnero@outlook.com",
                "Minaccia", "Vedi di vendere tutti quei decodere velocemente, il tuo fratellino sta bene. " +
                "E' qui con me.\nDomani ti mando il suo dito indice.", 70));
        emailList.add(0, new Email("epinefridio@gmail.com", "grandecazzo11@gmail.com",
                        "My Dick", "Ascolta se vuoi vedere il mio cazzo ok.", 11));
        emailList.add(0, new Email("brunodinotte@libero.it", auxArrList,
                "Some cod skills", "Bello! Guarda che cazzo ho combinato in 2v2 su Cod", 43));
        emailList.add(0, new Email("oguaglioneinnero@outlook.com", "epinefridio@gmail.com",
                "Decoder illegale", "Bro, ho scoperto come pagare pochissimo e avere tutti gli abbonamenti.", 19));

        setUniqueId(100);
    }
}
