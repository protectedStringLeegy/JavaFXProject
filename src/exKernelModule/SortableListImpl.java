package exKernelModule;

import java.util.ArrayList;
import java.util.Collections;

public class SortableListImpl<T extends Comparable> implements SortableList<T> {

    ArrayList<T> arrayList;

    public SortableListImpl() {
        arrayList = new ArrayList<>();
    }

    @Override
    public boolean add(T comparable) {
        return arrayList.add(comparable);
    }

    @Override
    public boolean remove(T comparable) {
        return arrayList.remove(comparable);
    }

    @Override
    public void sort() {
        Collections.sort(arrayList);
    }

    @Override
    public void print() {
        System.out.println(arrayList.toString());
    }
}
