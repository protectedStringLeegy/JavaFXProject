package exGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultiPanelClose {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.add(new MultiPanel());
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}

class MultiPanel extends JPanel {

    public MultiPanel() {

        JButton newButton = new JButton("New");
        JButton closeAllButton = new JButton("Close All");

        add(newButton);
        add(closeAllButton);

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BlankFrame newFrame = new BlankFrame(closeAllButton);
            }
        });
    }

    private static class BlankFrame extends JFrame {

        private ActionListener closeListener;

        public BlankFrame(JButton closeButton) {

            add(new JLabel("Sono un frame vuoto"));

            setSize(400, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);

            closeListener = ev -> {
                closeButton.removeActionListener(closeListener);
                dispose();
            };
            closeButton.addActionListener(closeListener);
        }
    }
}
