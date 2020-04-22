package exKernelModule;

public interface SortableList<T extends Comparable> {

    boolean add(T comparable);

    boolean remove(T comparable);

    void sort();

    void print();
}
