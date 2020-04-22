package JavaFXMLApplication;

import javafx.beans.property.SimpleStringProperty;

public class Bill {

    private SimpleStringProperty amount = new SimpleStringProperty();

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }
}
