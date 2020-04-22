package exKernelModule;

public class ArrayOrdinabile<T extends Comparable> {

    private SortableList<T> obj;

    public ArrayOrdinabile (SortableList<T> m) {
        obj = m;
    }

    public boolean add(T comparable) {
        return obj.add(comparable);
    }

    public boolean remove(T comparable) {
        return obj.remove(comparable);
    }

    public void sort() {
        obj.sort();
    }

    public void print() {
        obj.print();
    }

    public static void main(String args[]) {
        ArrayOrdinabile<String> kernel = new ArrayOrdinabile<>(new SortableListImpl<>());
        kernel.add("farfalla");
        kernel.add("ragno");
        kernel.add("zanzara");
        kernel.add("mosca");
        kernel.sort();
        kernel.print();
        kernel.remove("ragno");
        System.out.println();
        kernel.print();

        System.out.println();
        ArrayOrdinabile<Integer> kernel1 = new ArrayOrdinabile<>(new SortableListImpl<>());
        kernel1.add(20);
        kernel1.add(30);
        kernel1.add(10);
        kernel1.add(5);
        kernel1.sort();
        kernel1.print();
    }
}