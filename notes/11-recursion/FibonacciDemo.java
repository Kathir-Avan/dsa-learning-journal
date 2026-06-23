/**
 * LeetCode 509 - Fibonacci Number
 *
 * Problem:
 * The Fibonacci numbers are defined as:
 *
 * F(0) = 0
 * F(1) = 1
 * F(n) = F(n-1) + F(n-2), for n > 1
 *
 * Given n, return F(n).
 *
 * ----------------------------------------------------
 * Approach 1: Brute Force Recursion
 * ----------------------------------------------------
 *
 * Idea:
 * Directly implement the mathematical formula.
 *
 * Time Complexity: O(2^N)
 * Space Complexity: O(N)
 *
 * Why O(2^N)?
 * Each function call creates two more calls:
 *
 * fib(5)
 * ├── fib(4)
 * └── fib(3)
 *
 * Many subproblems are solved repeatedly.
 *
 * Example:
 *
 * fib(5)
 * ├── fib(4)
 * │   ├── fib(3)
 * │   └── fib(2)
 * └── fib(3)
 *
 * Notice fib(3) is calculated twice.
 */
class FibonacciBruteForce {

    public int fib(int n) {

        // Base case
        if (n <= 1) {
            return n;
        }

        // Recursive formula
        return fib(n - 1) + fib(n - 2);
    }
}

/**
 * ----------------------------------------------------
 * Approach 2: Dynamic Programming (Bottom-Up)
 * ----------------------------------------------------
 *
 * Idea:
 * Store previously calculated Fibonacci numbers
 * and reuse them.
 *
 * Time Complexity: O(N)
 * Space Complexity: O(N)
 *
 * Example:
 *
 * dp[0] = 0
 * dp[1] = 1
 *
 * dp[2] = dp[1] + dp[0]
 * dp[3] = dp[2] + dp[1]
 * dp[4] = dp[3] + dp[2]
 */
class FibonacciDP {

    public int fib(int n) {

        // Base cases
        if (n <= 1) {
            return n;
        }

        // dp[i] stores Fibonacci value of i
        int[] dp = new int[n + 1];

        dp[0] = 0;
        dp[1] = 1;

        // Build solution from smaller subproblems
        for (int i = 2; i <= n; i++) {

            // Current Fibonacci number
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }
}

/**
 * ----------------------------------------------------
 * Approach 3: Optimal Iterative Solution
 * ----------------------------------------------------
 *
 * Observation:
 *
 * fib(i) = fib(i-1) + fib(i-2)
 *
 * We only need the previous two values.
 *
 * Instead of storing all Fibonacci numbers,
 * store only:
 *
 * prev2 = fib(i-2)
 * prev1 = fib(i-1)
 *
 * Time Complexity: O(N)
 * Space Complexity: O(1)
 */
class FibonacciOptimal {

    public int fib(int n) {

        // Base cases
        if (n <= 1) {
            return n;
        }

        // fib(0)
        int prev2 = 0;

        // fib(1)
        int prev1 = 1;

        for (int i = 2; i <= n; i++) {

            // Current Fibonacci number
            int current = prev1 + prev2;

            // Move window forward
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }
}

/**
 * Driver Class
 */
public class FibonacciDemo {

    public static void main(String[] args) {

        int n = 5;

        FibonacciBruteForce bruteForce = new FibonacciBruteForce();
        FibonacciDP dp = new FibonacciDP();
        FibonacciOptimal optimal = new FibonacciOptimal();

        System.out.println("Brute Force Result : " + bruteForce.fib(n));
        System.out.println("DP Result          : " + dp.fib(n));
        System.out.println("Optimal Result     : " + optimal.fib(n));
    }
}
