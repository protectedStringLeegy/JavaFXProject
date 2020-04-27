package mailProject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Email implements Serializable {

    private final long id;
    private final String sender;
    private ArrayList<String> receivers = new ArrayList<>();
    private final String subject;
    private final String text;
    private final Calendar date;

    public Email(String sender, String receiver, String subject, String text, long id) {

        this.sender = sender;
        this.receivers.add(receiver);
        this.subject = subject;
        this.text = text;
        this.date = Calendar.getInstance();
        this.id = id;
    }

    public Email(String sender, ArrayList<String> receivers, String subject, String text, long id) {

        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.text = text;
        this.date = Calendar.getInstance();
        this.id = id;
    }

    public Email(String sender, String receiver, String subject, int id) {

        this.sender = sender;
        this.receivers.add(receiver);
        this.subject = subject;
        this.text = "";
        this.date = Calendar.getInstance();
        this.id = id;
    }

    public long getId() {
        return id;
    }

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
