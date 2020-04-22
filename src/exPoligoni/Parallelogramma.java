package exPoligoni;

public class Parallelogramma extends Poligono{

    private int base;
    private int altezza;

    public Parallelogramma(int base, int altezza){
        this.base = base;
        this.altezza = altezza;
    }

    public int getBase() {
        return base;
    }

    public int getAltezza() {
        return altezza;
    }

    @Override
    public int getLati() {
        return 4;
    }

    @Override
    public double getArea() {
        return base*altezza;
    }

    @Override
    public int getPerimetro() {
        return (base+altezza)*2;
    }

    @Override
    public String toString() {
        return "Parallelogramma {" + base +
                " x " + altezza +
                "}";
    }
}
