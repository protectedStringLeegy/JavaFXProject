package exPoligoni;

public abstract class Poligono {

    public abstract double getArea();

    public abstract int getLati();

    public abstract int getPerimetro();

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            Poligono poligono = (Poligono)obj;
            return this.getArea() == poligono.getArea() &&
                    this.getPerimetro() == poligono.getPerimetro() &&
                    this.getLati() == poligono.getLati();
        } else return false;
    }
}
