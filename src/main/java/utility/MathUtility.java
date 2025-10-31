package utility;

public class MathUtility {


    public static long sumOddNumbers(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Input must be greater than or equal to 1.");
        }
        long sum = 0;
        int k = (n + 1) / 2;
        return (long) k * k;
    }
}
