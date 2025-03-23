package aed.sorting;

import aed.utils.TimeAnalysisUtils;

import java.util.Random;
import java.util.function.Function;

public class testsA {

    static int iterations = 13;

    private static final Random R = new Random();
    public static void main(String[] args)
    {

        Function<Integer, String[]> ArrayGenerator = capacity -> {
            String[] newArray = new String[capacity];

            for(int i = 0; i< capacity; i++)
            {
                int lenght = R.nextInt(10) +1;
                char[] randomChars = new char[lenght];

                for(int j = 0; j< lenght;j++)
                {
                    char randomChar = (char) (R.nextInt(26) + 'A');
                    randomChars[j] = randomChar;
                }

                newArray[i] = new String (randomChars);
            }
            return newArray;
        };

        // Testes para o quick sort

        System.out.println("Testes para o metodo qsort \n");

        TimeAnalysisUtils.runDoublingRatioTest(
                ArrayGenerator,
                a -> {
                    RecursiveStringSort.quicksort(a);

                },
                iterations
        );
        System.out.println("-----------------------------------------");

        // Testes para o sort
        System.out.println("Testes para o metodo sort \n");

        TimeAnalysisUtils.runDoublingRatioTest(
                ArrayGenerator,
                a -> {
                    RecursiveStringSort.sort(a);

                },
                iterations
        );




    }


}
