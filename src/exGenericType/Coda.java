package exGenericType;

import java.util.ArrayList;
import java.util.List;

public class Coda<T> {

    List<T> objList;

    public Coda(List<T> objList) {
        this.objList = objList;
    }

    public boolean put(T e) {
        return objList.add(e);
    }

    public T get() {
        if (empty()) {
            throw new IndexOutOfBoundsException("Empty List");
        } else return objList.remove(0);
    }

    public boolean empty() {
        return objList.isEmpty();
    }

    public static void main(String[] args) {
        Coda<Integer> integerCoda = new Coda<>(new ArrayList<>());

        integerCoda.put(5);
        integerCoda.put(10);
        integerCoda.put(23);
        integerCoda.put(43);
        integerCoda.put(7);

        System.out.println(integerCoda.get());
        System.out.println(integerCoda.get());
        System.out.println(integerCoda.empty());
    }
}
