package aed.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


//podem alterar esta classe, se quiserem
class Limits
{
    char minChar;
    char maxChar;
    int maxLength;

    public Limits(char minChar, char maxChar, int maxLength)
    {
        this.minChar = minChar;
        this.maxChar = maxChar;
        this.maxLength = maxLength;

    }
}
@SuppressWarnings("unchecked")
public class RecursiveStringSort extends Sort
{
    private static final Random R = new Random();


    //esta implementação base do quicksort é fornecida para que possam comparar o tempo de execução do quicksort
    //com a vossa implementação do RecursiveStringSort
    public static <T extends Comparable<T>> void quicksort(T[] a)
    {
        qsort(a, 0, a.length-1);
    }

    private static <T extends Comparable<T>> void qsort(T[] a, int low, int high)
    {
        if (high <= low) return;
        int j = partition(a, low, high);
        qsort(a, low, j-1);
        qsort(a, j+1, high);
    }

    private static <T extends Comparable<T>> int partition(T[] a, int low, int high)
    {
        //partition into a[low...j-1],a[j],[aj+1...high] and return j
        //choose a random pivot
        int pivotIndex = low + R.nextInt(high+1-low);
        exchange(a,low,pivotIndex);
        T v = a[low];
        int i = low, j = high +1;

        while(true)
        {
            while(less(a[++i],v)) if(i == high) break;
            while(less(v,a[--j])) if(j == low) break;

            if(i >= j) break;
            exchange(a , i, j);
        }
        exchange(a, low, j);

        return j;
    }

    //método de ordenação insertionSort
    //no entanto este método recebe uma Lista de Strings em vez de um Array de Strings
    public static void insertionSort(List<String> a)
    {
        if (a.size() == 0)
            return;


        int n = a.size();
        String[] array = a.toArray(new String[0]);

        if(isSorted(array))
            return;

        for(int i = 1; i < n; i++)
        {
            for(int j = i; j > 0; j--)
            {
                if(less(array[j], array[j-1]))
                {
                    exchange(array, j, j-1);

                }
                else break;

            }

        }
        for(int i = 0; i < n; i++)
        {
            a.set(i,array[i]);
        }
    }

    public static Limits determineLimits(List<String> a, int characterIndex)
    {
        char minChar = Character.MAX_VALUE;
        char maxChar = 0;
        int  maxLength = 0;

        for(String s: a)
        {
            maxLength = Math.max( maxLength, s.length());
            if(characterIndex < s.length())
            {
                char currentChar = s.charAt(characterIndex);
                minChar = (char) Math.min(minChar,currentChar);
                maxChar = (char) Math.max(maxChar,currentChar);

            }
            else
                minChar = 0;

        }
        if(minChar == Character.MAX_VALUE)
            minChar = 0;

        return new Limits(minChar, maxChar, maxLength);

    }

    //ponto de entrada principal para o vosso algoritmo de ordenação
    public static void sort(String[] a)
    {
        recursive_sort(Arrays.asList(a),0);
    }


    //mas este é que faz o trabalho todo
    public static void recursive_sort(List<String> a, int characterIndex) {
        if (a.size() <= 1)
            return;

        int cutOff = 50;

        if (a.size() <= cutOff) {
            insertionSort(a);
            return;
        }

        boolean allStringsShorter = true;
        for (String s : a) {
            if (s.length() > characterIndex) {
                allStringsShorter = false;
                break;
            }
        }
        if (allStringsShorter)
            return;

        Limits limits = determineLimits(a, characterIndex);

        @SuppressWarnings("unchecked")
        ArrayList<String>[] bucketsArray = new ArrayList[limits.maxChar - limits.minChar + 1];

        for (int i = 0; i < bucketsArray.length; i++) {
            bucketsArray[i] = new ArrayList<>();
        }

        for (String s : a) {
            char currentChar;

            if (characterIndex < s.length()) {
                currentChar = s.charAt(characterIndex);
            } else {
                currentChar = limits.minChar;
            }

            int bucketIndex = currentChar - limits.minChar;
            bucketsArray[bucketIndex].add(s);
        }

        int index = 0;
        for (ArrayList<String> bucket : bucketsArray) {
            if (!bucket.isEmpty()) {
                recursive_sort(bucket, characterIndex + 1);
                for (String s : bucket) {
                    a.set(index++, s);
                }
            }
        }
    }



    public static void fasterSort(String[] a) {
        recursiveFasterSort(Arrays.asList(a), 0);
    }

    private static void recursiveFasterSort(List<String> a, int characterIndex) {
        if (a.size() <= 1)
            return;

        int cutOff = 50;

        if (a.size() <= cutOff) {
            insertionSort(a);
            return;
        }

        boolean allStringsShorter = true;
        for (String s : a) {
            if (s.length() > characterIndex) {
                allStringsShorter = false;
                break;
            }
        }
        if (allStringsShorter)
            return;



        char minChar = 32;
        char maxChar = 1000;

        @SuppressWarnings("unchecked")
        ArrayList<String>[] bucketsArray = new ArrayList[maxChar - minChar + 1];

        for (int i = 0; i < bucketsArray.length; i++) {
            bucketsArray[i] = new ArrayList<>();
        }

        for (String s : a) {
            char currentChar;

            if (characterIndex < s.length()) {
                currentChar = s.charAt(characterIndex);
            } else {
                currentChar = minChar;
            }

            int bucketIndex = currentChar - minChar;
            bucketsArray[bucketIndex].add(s);
        }

        int index = 0;
        for (ArrayList<String> bucket : bucketsArray) {
            if (!bucket.isEmpty()) {
                recursiveFasterSort(bucket, characterIndex + 1);
                for (String s : bucket) {
                    a.set(index++, s);
                }
            }
        }
    }


    public static void main(String[] args)
    {
        // Teste insertion sort
        List<String> list = new ArrayList<>();
        list.add("bom");
        list.add("antes");
        list.add("dia");
        list.add("ceu");
        insertionSort(list);
        System.out.println(list);

        // Teste para o método determineLimits

        List<String> strings = List.of("ola", "bom dia", "ate amanha");
        int index = 2;
        Limits limits = determineLimits(strings, index);

        System.out.println("Min Char: " + limits.minChar);
        System.out.println("Max Char: " + limits.maxChar);
        System.out.println("Max Length: " + limits.maxLength);


        // Teste para o método recursive sort

        List<String> unsortedList = Arrays.asList("date", "banana", "apple", "cherry");
        System.out.println("Unsorted list: " + unsortedList);
        recursive_sort(unsortedList, 0);
        System.out.println("Sorted list: " + unsortedList);

        // Teste para o faster sort

        String[] arrayString = {"d","g","s","a","e","b"};
        System.out.println("Unsorted list: " + Arrays.toString(arrayString));
        fasterSort(arrayString);
        System.out.println("Sorted list: " + Arrays.toString(arrayString));





    }
}

