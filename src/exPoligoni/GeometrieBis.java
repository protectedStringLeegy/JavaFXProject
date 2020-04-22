package exPoligoni;

import java.util.ArrayList;
import java.util.List;

public class GeometrieBis<T extends Poligono> {

    List<T> poligoniList;

    public GeometrieBis(List<T> poligoniList) {
        this.poligoniList = poligoniList;
    }

    public int getNumElement() {
        return poligoniList.size();
    }

    public boolean add(T e) {
        return poligoniList.add(e);
    }

    public void printAree() {
        for (T e : poligoniList) {
            System.out.println(e.getArea());
        }
    }

    public static void main(String[] args) {
        GeometrieBis<Poligono> poligonoGeometrie = new GeometrieBis<>(new ArrayList<>());
        poligonoGeometrie.add(new Triangolo(4, 3, 3));
        poligonoGeometrie.add(new Rettangolo(5, 7));

        System.out.println(poligonoGeometrie.getNumElement());
        poligonoGeometrie.printAree();
    }
}
