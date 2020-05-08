package mailProject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Email implements Serializable {

    private long id;
    private final String sender;
    private ArrayList<String> receivers = new ArrayList<>();
    private final String subject;
    private final String text;
    private final Calendar date;

    public Email(String sender, String receiver, String subject, String text) {

        this.sender = sender;
        this.receivers.add(receiver);
        this.subject = subject;
        this.text = text;
        this.date = Calendar.getInstance();
        this.id = 0;
    }

    public Email(String sender, ArrayList<String> receivers, String subject, String text) {

        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.text = text;
        this.date = Calendar.getInstance();
        this.id = 0;
    }

    public Email(String sender, String receiver, String subject) {

        this.sender = sender;
        this.receivers.add(receiver);
        this.subject = subject;
        this.text = "";
        this.date = Calendar.getInstance();
        this.id = 0;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id; }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getReceiver() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public Calendar getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return getId() == email.getId();
    }

    @Override
    public String toString() {
        return "Email {" +
                "Id = " + id +
                ", Sender = '" + sender + '\'' +
                ", Receiver = '" + receivers + '\'' +
                ", Subject = '" + subject + '\'' +
                ", Text = '" + text + '\'' +
                ", Date = " + date +
                " }";
    }
}
