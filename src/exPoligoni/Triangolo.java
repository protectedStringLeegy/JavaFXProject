package exPoligoni;

public class Triangolo extends Poligono{

    private int base;
    private int lato1;
    private int lato2;

    public Triangolo(int base, int lato1, int lato2) {
        this.base = base;
        this.lato1 = lato1;
        this.lato2 = lato2;
    }

    public int getLati() {
        return 3;
    }

    public int getBase() {
        return base;
    }

    public int getLato1() {
        return lato1;
    }

    public int getLato2() {
        return lato2;
    }

    @Override
    public double getArea() {
        int semiperimetro = getPerimetro()/2;
        return Math.sqrt((double)semiperimetro*(semiperimetro-base)*(semiperimetro-lato1)*(semiperimetro-lato2));
    }

    @Override
    public int getPerimetro() {
        return base+lato1+lato2;
    }

    @Override
    public String toString() {
        return "Triangolo {" + base +
                " x " + lato1 +
                " x " + lato2 +
                "}";
    }
}
