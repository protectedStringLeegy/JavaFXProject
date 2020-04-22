package exGenericType;

import java.util.ArrayList;
import java.util.List;

public class Calcolatrice {

    public static void printNumbers(List<? extends Number> listNumbers) {
        for (Number number : listNumbers) {
            System.out.println(number);
        }
    }

    public static double getSum(List<? extends Number> listNumbers) {

        double sum = 0;

        for (Number number : listNumbers) {
            sum += number.doubleValue();
        }

        return sum;
    }

    public static double getMax(List<? extends Number> listNumbers) {

        double max = 0;

        for (Number number : listNumbers) {
            if (number.doubleValue() > max)
                max = number.doubleValue();
        }

        return max;
    }

    public static void main(String[] args) {

        ArrayList<Integer> integerArrayList = new ArrayList<>();
        integerArrayList.add(5);
        integerArrayList.add(8);
        integerArrayList.add(12);
        integerArrayList.add(3);
        integerArrayList.add(7);

        ArrayList<Double> doubleArrayList = new ArrayList<>();
        doubleArrayList.add(32.0);
        doubleArrayList.add(1.12);
        doubleArrayList.add(23.4);
        doubleArrayList.add(45.52);
        doubleArrayList.add(0.01);

        Calcolatrice.printNumbers(integerArrayList);
        System.out.println(Calcolatrice.getSum(integerArrayList));
        System.out.println(Calcolatrice.getMax(integerArrayList));

        Calcolatrice.printNumbers(doubleArrayList);
        System.out.println(Calcolatrice.getSum(doubleArrayList));
        System.out.println(Calcolatrice.getMax(doubleArrayList));
    }
}
