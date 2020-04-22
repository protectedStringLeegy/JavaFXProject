package exPoligoni;

import java.util.ArrayList;

public class Geometrie {

    private ArrayList<Poligono> poligoni;

    public Geometrie() {
        poligoni = new ArrayList<>();
    }

    public ArrayList<Poligono> getPoligoni() {
        return poligoni;
    }

    public boolean add(Poligono poligono){
        return poligoni.add(poligono);
    }

    public Poligono remove(int index) {
        return poligoni.remove(index);
    }

    public Poligono get(int index) {
        return poligoni.get(index);
    }

    @Override
    public String toString() {
        return poligoni.toString();
    }
}
