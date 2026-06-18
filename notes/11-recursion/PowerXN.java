```java
/**
 * LeetCode 50 - Pow(x, n)
 *
 * Problem:
 * Implement pow(x, n), which calculates x raised to the power n.
 *
 * Examples:
 *
 * Input: x = 2.00000, n = 10
 * Output: 1024.00000
 *
 * Input: x = 2.10000, n = 3
 * Output: 9.26100
 *
 * Input: x = 2.00000, n = -2
 * Output: 0.25000
 *
 * ============================================================
 * Approach 1: Brute Force
 * ============================================================
 *
 * Idea:
 * Multiply x by itself n times.
 *
 * Example:
 * 2^5
 * = 2 × 2 × 2 × 2 × 2
 *
 * Time Complexity:
 * O(N)
 *
 * Space Complexity:
 * O(1)
 *
 * Drawback:
 * Too slow for very large values of n.
 *
 * ============================================================
 * Approach 2: Divide and Conquer (Recursive Fast Power)
 * ============================================================
 *
 * Key Observation:
 *
 * x^10
 * = (x^5)^2
 *
 * x^11
 * = (x^5)^2 * x
 *
 * Instead of reducing by 1 every time,
 * reduce the exponent by half.
 *
 * Time Complexity:
 * O(log N)
 *
 * Space Complexity:
 * O(log N)
 * (recursive call stack)
 *
 * ============================================================
 * Approach 3: Binary Exponentiation (Optimal)
 * ============================================================
 *
 * Key Observation:
 *
 * Every number can be represented in binary.
 *
 * Example:
 *
 * 13 = 1101₂
 *
 * Therefore:
 *
 * x^13
 * = x^8 × x^4 × x^1
 *
 * We repeatedly:
 * 1. Check current bit
 * 2. Multiply result if bit is 1
 * 3. Square x
 * 4. Move to next bit
 *
 * Time Complexity:
 * O(log N)
 *
 * Space Complexity:
 * O(1)
 */

public class PowXN {

    // ============================================================
    // Approach 1: Brute Force
    // ============================================================
    public double myPowBruteForce(double x, int n) {

        // Convert int to long to safely handle Integer.MIN_VALUE
        long power = n;

        // Handle negative exponent
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }

        double result = 1.0;

        // Multiply x exactly power times
        for (long i = 0; i < power; i++) {
            result *= x;
        }

        return result;
    }

    // ============================================================
    // Approach 2: Recursive Fast Power
    // ============================================================
    public double myPowRecursive(double x, int n) {

        long power = n;

        // Negative exponent:
        // x^-n = 1 / x^n
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }

        return fastPower(x, power);
    }

    /**
     * Recursive helper method.
     */
    private double fastPower(double x, long n) {

        // Base case
        if (n == 0) {
            return 1.0;
        }

        // Solve smaller problem
        double half = fastPower(x, n / 2);

        // Even exponent
        if (n % 2 == 0) {
            return half * half;
        }

        // Odd exponent
        return half * half * x;
    }

    // ============================================================
    // Approach 3: Iterative Binary Exponentiation (Optimal)
    // ============================================================
    public double myPowOptimal(double x, int n) {

        long power = n;

        // Handle negative exponent
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }

        double result = 1.0;

        while (power > 0) {

            // If current bit is 1
            if ((power & 1) == 1) {
                result *= x;
            }

            // Square current value
            x *= x;

            // Move to next bit
            power >>= 1;
        }

        return result;
    }

    // ============================================================
    // Driver Method
    // ============================================================
    public static void main(String[] args) {

        PowXN solution = new PowXN();

        double x = 2.0;
        int n = 10;

        System.out.println("Input:");
        System.out.println("x = " + x);
        System.out.println("n = " + n);

        System.out.println("\nBrute Force:");
        System.out.println(solution.myPowBruteForce(x, n));

        System.out.println("\nRecursive Fast Power:");
        System.out.println(solution.myPowRecursive(x, n));

        System.out.println("\nOptimal Binary Exponentiation:");
        System.out.println(solution.myPowOptimal(x, n));
    }
}
```
