package mailProject.model;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerRequestHandler implements Runnable {

    Socket incoming;
    ServerModel serverModel;
    private String user;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream = null;
    private boolean quit = false;

    public ServerRequestHandler(Socket incoming, ServerModel model) {
        this.incoming = incoming;
        this.serverModel = model;
        try {
            outputStream = new ObjectOutputStream(incoming.getOutputStream());
            inputStream = new ObjectInputStream(incoming.getInputStream());
        } catch (IOException e) {
            System.err.println("Cannot create Streams.");
        }
    }

    public String getUser() {
        return user;
    }

    public void quit() {
        quit = true;
    }

    @Override
    public void run() {

        while (!quit) {
            try {
                String aux = (String) inputStream.readObject();
                if (aux.equalsIgnoreCase("emailReceived")) {
                    Platform.runLater(() -> {
                        serverModel.getLogList().add(user + " ha ricevuto una Mail.");
                    });
                } else if (aux.equalsIgnoreCase("sessionClosed")) {
                    quit();
                    Platform.runLater(() -> {
                        serverModel.getLogList().add(user + " si è disconnesso.");
                        serverModel.getUserSessions().remove(this);
                    });
                } else if (aux.equalsIgnoreCase("sendRequest")) {
                    boolean validReceiver = false;
                    Email auxMail = (Email) inputStream.readObject();
                    Platform.runLater(() -> {
                        serverModel.getLogList().add("L'utente " + auxMail.getSender() + " ha inviato una mail.");
                    });

                    for (ServerRequestHandler trh : serverModel.getUserSessions()) {
                        if (auxMail.getReceiver().contains(trh.user)) {
                            auxMail.setId(serverModel.getUniqueId());
                            trh.outputStream.writeObject("newMail");
                            trh.outputStream.writeObject(auxMail);
                            validReceiver = true;
                            serverModel.getMailMap().get(trh.user).add(auxMail);
                            trh.outputStream.flush();
                        }
                    }

                    if (!validReceiver) {
                        outputStream.writeObject("mailFailed");
                        Platform.runLater(() -> {
                            serverModel.getLogList().add("Il destinatario della Mail non esiste.");
                        });
                    } else {
                        outputStream.writeObject("mailSended");
                    }

                } else {
                    user = aux;
                    Platform.runLater(() -> {
                        serverModel.getLogList().add(user + " si è collegato al server.");
                        serverModel.getUserSessions().add(this);
                    });
                    if (serverModel.getMailMap().containsKey(aux)) {
                        System.out.println(serverModel.getMailMap().get(aux));
                        outputStream.writeObject("mailList");
                        outputStream.writeObject(serverModel.getMailMap().get(aux));
                    } else {
                        serverModel.getMailMap().put(aux, new ArrayList<>());
                        outputStream.writeObject("emptyMailList");
                    }
                    outputStream.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                quit();
            }
        }
    }
}