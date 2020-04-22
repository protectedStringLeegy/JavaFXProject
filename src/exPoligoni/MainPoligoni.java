package exPoligoni;

public class MainPoligoni {

    public static void main(String[] args) {

        Triangolo t1 = new Triangolo(5, 6, 8);
        Triangolo t2 = new Triangolo(5, 4, 2);
        Rettangolo r1 = new Rettangolo(4, 6);
        Rettangolo r2 = new Rettangolo(6, 4);
        Parallelogramma p1 = new Parallelogramma(4, 6);
        Parallelogramma p2 = new Parallelogramma(7, 5);

        Geometrie geometrie = new Geometrie();
        geometrie.add(t1);
        geometrie.add(t2);
        geometrie.add(r1);
        geometrie.add(r2);
        geometrie.add(p1);
        geometrie.add(p2);

        Poligono poligono1 = geometrie.get(2);
        Poligono poligono2 = geometrie.get(3);

        System.out.println(geometrie);
        System.out.println(poligono1.equals(poligono2));

    }
}
