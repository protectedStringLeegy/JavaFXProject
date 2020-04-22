package exOOandGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class ProverbsMVC {

    public static void main(String[] args) {
        ProverbsView pv = new ProverbsView();
        ProverbsContainer pc = new ProverbsContainer();
        PVController c = new PVController(pc);

        pv.setController(c);
        pc.addObserver(pv);

        pv.setSize(500, 500);
        pv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pv.setVisible(true);

    }
}

class ProverbsView extends JFrame implements Observer {

    private JTextField textField;
    private JButton button;

    public ProverbsView() {

        JPanel panel = new JPanel();
        JLabel label = new JLabel("ProverbsAPP");
        textField = new JTextField("Proverbs here ...");
        button = new JButton("NEW PROVERB");
        add(panel);
        panel.setLayout(new GridLayout(2, 2, 20, 20));
        panel.add(label);
        panel.add(new Label(""));
        panel.add(button);
        panel.add(textField);

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ProverbsContainer)
        textField.setText(((ProverbsContainer)o).getProverb());
    }

    public void setController(PVController c) {
        button.addActionListener(c); // aggancia il listener del controller al bottone
    }
}

class ProverbsContainer extends Observable {

    private ArrayList<String> proverbsList;
    private int proverbIndex = 0;

    public ProverbsContainer() {

        proverbsList = new ArrayList<>();
        proverbsList.add("Se sei un guaglione prima o poi finisci in prigione");
        proverbsList.add("Chi la fa l'aspetti");
        proverbsList.add("Chi trova un amico trova un tesoro");
        proverbsList.add("Non dire gatto finchè non ce l'hai nel sacco");
        proverbsList.add("Brutus, brutus");

    }

    public void changeProverb() {
        proverbIndex = new Random().nextInt(5);
        setChanged();
        notifyObservers();
    }

    public String getProverb() {
        return proverbsList.get(proverbIndex);
    }
}

class PVController implements ActionListener {

    ProverbsContainer proverbsContainer;

    public PVController(ProverbsContainer pc) {
        proverbsContainer = pc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        proverbsContainer.changeProverb();
    }
}
