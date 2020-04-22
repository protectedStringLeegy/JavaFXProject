package exOOandGUI;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.EventListener;

public class Testing {
    public static void main(String[] args) {
        EventSourceFrame daieh = new EventSourceFrame();
        daieh.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        daieh.setVisible(true);
    }
}

class PaintCountPanel extends JPanel {

    private int paintCount;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerList.add(PropertyChangeListener.class, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listenerList.remove(PropertyChangeListener.class, listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int oldPaintCount = paintCount;
        paintCount++;
        firePropertyChangeEvent(new PropertyChangeEvent(this, "paintCount", oldPaintCount, paintCount));
        super.paintComponent(g);
    }

    public void firePropertyChangeEvent(PropertyChangeEvent event) {
        for (EventListener listener : listenerList.getListeners(PropertyChangeListener.class))
            ((PropertyChangeListener) listener).propertyChange(event);
    }
}

class EventSourceFrame extends JFrame {
    public EventSourceFrame() {
        setTitle("EventSourceTest");
        setSize(500, 500);
        final PaintCountPanel panel = new PaintCountPanel();
        add(panel);
        panel.addPropertyChangeListener(ev -> setTitle("EventSourceTest - " + ev.getNewValue()));
    }
}
