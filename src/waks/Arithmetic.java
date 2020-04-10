package waks;

import math.arithmetic.GCDComputer;
import math.arithmetic.PrimeTester;


public class Arithmetic {


    public static int sum(int ... ints){
        int sum = 0;
        for (int i : ints) {
            sum += i;
        }

        return sum;
    }

    public static int product(int ... ints){
        if (ints.length == 0) {
            return 0;
        }

        int product = 1;
        for (int i : ints) {
            product *= i;
        }

        return product;
    }

    public static boolean isDivisible(int number, int divisor){
        return number % divisor == 0;
    }

    public static boolean isEven(int n){
        return isDivisible(n, 2);
    }

    public static boolean isOdd(int n){
        return !isEven(n);
    }

    public static boolean isComposite(int n){
        return !PrimeTester.isPrime(n);
    }

    public static void assertPositivity(int n, String alt){
        if (n <= 0){
            throw new IllegalArgumentException(alt);
        }
    }

    public static int sumOfTerms(int from, int to, int step){
        if (from == to) {
            return to;
        }
        assertPositivity(to - from, "Illegal summation interval: from "+from+" to "+to);
        int sum = 0;
        for (int i = from; i <= to; i += step) {
            sum += i;
        }

        return sum;
    }

    //step-sizing is 1, by default
    public static int sumOfTerms(int from, int to){
        return (from == 0 || from == 1) ? firstNTerms(to) : sumOfTerms(from, to, 1);
    }

    public static int firstNTerms(int n){
        return n * (n + 1) / 2;
    }

    public static boolean areCoPrimes(int p1, int p2){
        return GCDComputer.gcdOf(p1, p2) == 1;
    }

    /**
     * The linear combination of 'n' with respect to 'd' as the divisor.
     * This is based on the Euclid'd Division Theorem: n = d(q) + r
     */
    public static String linearCombinationOf(int n, int d){
        int q = n / d;
        int r = n - (d * q);

        return n+" = "+d+"("+q+") + "+r;
    }

    /**
     * Finds the gcd using Euclid's Algorithm
     */
    public static void launchEuclidAlgorithmForGCD(int a, int b) {
        int number = Math.max(a, b);
        int divisor = Math.min(a, b);
        int quot = number / divisor;
        int rem = number - (divisor * quot);
        while (rem != 0) {
            System.out.println(number+" = "+divisor+"("+quot+") + "+rem);
            number = divisor;
            divisor = rem;
            quot = number / divisor;
            rem = number - (divisor * quot);
        }
        System.out.println(number+" = "+divisor+"("+quot+") + "+rem);
        System.out.println(" \u2234 gcd("+a+", "+b+") = "+divisor);
    }

    public static int maxOf(int[] array){
        int dMax = array[0];
        for (int t : array) {
            if (t > dMax) {
                dMax = t;
            }
        }

        return dMax;
    }

    public static int minOf(int[] array){
        int dMin = array[0];
        for (int t : array) {
            if (t < dMin) {
                dMin = t;
            }
        }

        return dMin;
    }

}
