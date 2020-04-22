package exOOandGUI;

import java.util.*;

public class ObserverObservable {

    public static void main(String[] args) {
        Visualizzatore v = new Visualizzatore();
        Filtro f = new Filtro();
        Contatore c = new Contatore();
        c.addObserver(f);
        f.addObserver(v);
        c.start();
    }
}


class Contatore extends Observable{

    private int c;

    public Contatore() {
        c = 0;
    }

    public int getCurrent() {
        return c;
    }

    public void start() {
        for (int i=0; i<50; i++) {
            c++;
            if (c%5==0) {
                setChanged();
                notifyObservers();
            }
        }
    }
}

class Filtro extends Observable implements Observer{
    private List<Integer> lista;

    public Filtro() {
        lista = new ArrayList<>();
    }

    public void filtra(int c) {
        lista.add(c);
        if (lista.size()%2==0) {
            System.out.println("lista size: " + lista.size());
            setChanged();
            notifyObservers();
        }
    }

    public List<Integer> getList() {
        return lista;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Contatore) {
            filtra(((Contatore)o).getCurrent());
        }
    }
}

class Visualizzatore implements Observer{

    public void visualizza(List<Integer> lista) {
        for (Integer i : lista) {
            System.out.println(i.intValue());
        }
        System.out.println();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Filtro)
            visualizza(((Filtro)o).getList());
    }
}
