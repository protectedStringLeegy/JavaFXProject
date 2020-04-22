package exGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeGUIBackground {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new ButtonPanel());
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class ButtonPanel extends JPanel implements ActionListener{

    public ButtonPanel() {

        JButton redButton = new JButton("Red");
        JButton yellowButton = new JButton("Yellow");
        JButton blueButton = new JButton("Blue");

        add(redButton);
        add(yellowButton);
        add(blueButton);

        redButton.addActionListener(this);
        yellowButton.addActionListener(this);
        blueButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String colorCommand = e.getActionCommand();
        switch (colorCommand) {
            case "Red":
                setBackground(Color.RED);
                break;
            case "Yellow":
                setBackground(Color.YELLOW);
                break;
            case "Blue":
                setBackground(Color.BLUE);
                break;
        }
    }
}
