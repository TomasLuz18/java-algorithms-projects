package aed.collections;

import aed.utils.TimeAnalysisUtils;

import java.util.Random;
import java.util.function.Function;

public class testsB {

    private static final Random R = new Random();
    static int iterations = 15;

    private static String concatenateStrings(String s1, String s2) {
        return s1 + s2;
    }

    private static String insertStringAt(String original, int index, String insert) {
        if (index < 0 || index > original.length()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return original.substring(0, index) + insert + original.substring(index);
    }


    public static void main(String[] args) {
        testMethods();
    }

    private static void testMethods() {

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

        System.out.println(" append para cordeis \n");

        TimeAnalysisUtils.runDoublingRatioTest(
                getRandomCordelGenerator(),
                cordel -> {
                    Cordel c = cordel.append("world!");

                },
                iterations
        );
        System.out.println("-----------------------------------------");

        System.out.println(" Insert at para cordeis \n");

        TimeAnalysisUtils.runDoublingRatioTest(
                getRandomCordelGenerator(),
                cordel -> {

                    Cordel c = cordel.insertAt( 0, "world!");

                },
                iterations
        );

        System.out.println("-----------------------------------------");

        System.out.println(" append para strings \n");

        TimeAnalysisUtils.runDoublingRatioTest(
                exampleStringGenerator(),
                str -> concatenateStrings(str, "Hi"),


                iterations
        );

        System.out.println("-----------------------------------------");

        System.out.println(" insertAt para strings \n");

        TimeAnalysisUtils.runDoublingRatioTest(
                exampleStringGenerator(),
                str -> insertStringAt(str, 0, "world!"),


                iterations
        );
    }

    private static Function<Integer, Cordel> getRandomCordelGenerator() {
        return capacity -> {
            int length = R.nextInt(10) + 1;
            char[] randomChars = new char[length];

            for (int j = 0; j < length; j++) {
                char randomChar = (char) (R.nextInt(26) + 'A');
                randomChars[j] = randomChar;
            }

            return new Cordel(new String(randomChars));
        };
    }

    private static Function<Integer, String> exampleStringGenerator() {return capacity -> {
        char[] resultChars = new char[capacity];

        for (int i = 0; i < capacity; i++) {
            resultChars[i] = (char) (R.nextInt(26) + 'A');
        }

        return new String(resultChars);
    };
    }

}
