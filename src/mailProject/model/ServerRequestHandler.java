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
                    Email auxMail = (Email) inputStream.readObject();
                    Platform.runLater(() -> {
                        serverModel.getLogList().add("L'utente " + auxMail.getSender() + " ha inviato una mail.");
                    });
                    outputStream.writeBoolean(true);
                    outputStream.flush();

                    for (ServerRequestHandler trh : serverModel.getUserSessions()) {
                        if (auxMail.getReceiver().contains(trh.user)) {
                            trh.outputStream.writeObject(auxMail);
                            trh.outputStream.flush();
                        }
                    }

                } else {
                    user = aux;
                    Platform.runLater(() -> {
                        serverModel.getLogList().add(user + " si è collegato al server.");
                        serverModel.getUserSessions().add(this);
                    });
                    if (serverModel.getMailMap().containsKey(aux)) {
                        System.out.println(serverModel.getMailMap().get(aux));
                        outputStream.writeObject(serverModel.getMailMap().get(aux));
                    } else {
                        serverModel.getMailMap().put(aux, new ArrayList<>());
                        outputStream.writeObject(null);
                    }
                    outputStream.flush();
                }
            } catch (IOException e) {
                System.out.println("Server chiuso.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}