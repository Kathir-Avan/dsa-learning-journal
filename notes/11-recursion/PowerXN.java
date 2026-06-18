/**
 * ============================================================================
 *  LeetCode 50 — Pow(x, n)
 *  STUDY GUIDE: Brute Force -> Intermediate -> Optimal (+ two bonus detours)
 * ============================================================================
 *
 * PROBLEM
 * -------
 * Implement pow(x, n), i.e. compute x raised to the integer power n.
 *
 *   Input:  x = 2.00000, n = 10   ->  Output: 1024.00000
 *   Input:  x = 2.10000, n = 3    ->  Output: 9.26100
 *   Input:  x = 2.00000, n = -2   ->  Output: 0.25000
 *
 * Constraints to keep in mind:
 *   - n can be as small as Integer.MIN_VALUE (-2147483648).
 *     Naively doing "-n" on an int overflows back to the same negative
 *     number, since int range is [-2147483648, 2147483647]. ALWAYS widen
 *     n to a long before negating it.
 *   - Negative exponent:  x^-n = 1 / x^n
 *
 *
 * THE THREE APPROACHES THAT MATTER (in order of how you should learn them)
 * --------------------------------------------------------------------------
 * 1. BRUTE FORCE            — multiply x by itself n times.            O(n) time, O(1) space
 * 2. DIVIDE & CONQUER        — halve the exponent every recursive call. O(log n) time, O(log n) space
 * 3. BINARY EXPONENTIATION   — same idea as #2, done iteratively using   O(log n) time, O(1) space
 *    (the OPTIMAL solution)    the binary digits of n. No recursion.
 *
 * Why binary exponentiation works (the key insight):
 *   Every integer can be written in binary. Example: 13 = 1101 (base 2)
 *   so   x^13 = x^8 * x^4 * x^1   (one term per "1" bit)
 *   We walk through the bits of n from least to most significant:
 *     - if the current bit is 1, fold the current power of x into result
 *     - square x (so it represents the next power of two)
 *     - shift n right by 1
 *   This is exactly what approach #2 does recursively — approach #3 just
 *   does it bottom-up with a loop instead of top-down with the call stack,
 *   which removes the O(log n) stack space cost.
 *
 * Two extra methods are included below purely because they're useful for
 * building intuition (not because you'd use them in an interview):
 *   - naiveRecursive():  the textbook x^n = x * x^(n-1) recursion. Shows
 *     WHY plain recursion is no better than brute force (still O(n)).
 *   - memoizedRecursive(): a common beginner instinct — "just cache it!"
 *     Shown here specifically to demonstrate that memoization does NOT
 *     help this problem, because there are no overlapping subproblems
 *     (each call to pow(x, k) is only ever made once). Time stays O(n)
 *     and you've now spent O(n) extra space for nothing. Recognizing when
 *     memoization helps vs. when it's dead weight is itself a useful skill.
 *
 *
 * COMPLEXITY COMPARISON
 * --------------------------------------------------------------------------
 *  Approach                          | Time      | Space        | Verdict
 *  -----------------------------------|-----------|---------------|------------------
 *  Naive recursion (x * x^(n-1))      | O(n)      | O(n) stack    | No better than brute force
 *  Brute force (iterative loop)       | O(n)      | O(1)          | Simple, but too slow for large n
 *  Memoized recursion                 | O(n)      | O(n)          | Wasted space — no repeated subproblems
 *  Divide & conquer (recursive)       | O(log n)  | O(log n)stack | Good intermediate step
 *  Binary exponentiation (iterative)  | O(log n)  | O(1)          | OPTIMAL — best time AND space
 * ============================================================================
 */

import java.util.HashMap;
import java.util.Map;

public class PowXNStudyGuide {

    // ========================================================================
    // APPROACH 1: BRUTE FORCE  —  O(n) time, O(1) space
    // ========================================================================
    // Idea: 2^5 = 2 * 2 * 2 * 2 * 2. Just multiply x by itself, n times.
    // Drawback: with n up to ~2^31, this loop is far too slow.
    public double myPowBruteForce(double x, int n) {

        long power = n; // widen to long BEFORE negating, to safely handle Integer.MIN_VALUE

        if (power < 0) {
            x = 1 / x;
            power = -power;
        }

        double result = 1.0;
        for (long i = 0; i < power; i++) {
            result *= x;
        }
        return result;
    }

    // ========================================================================
    // BONUS (anti-pattern): NAIVE RECURSION  —  O(n) time, O(n) stack space
    // ========================================================================
    // This is the textbook recurrence x^n = x * x^(n-1), written recursively
    // instead of with a loop. It is included only to show that swapping a
    // loop for recursion does NOT change the time complexity — it's still
    // O(n), and now you also pay O(n) call-stack space on top of that.
    public double naiveRecursive(double x, int n) {
        long power = n;
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }
        return naiveRecursiveHelper(x, power);
    }

    private double naiveRecursiveHelper(double x, long n) {
        if (n == 0) return 1.0;
        return x * naiveRecursiveHelper(x, n - 1);
    }

    // ========================================================================
    // BONUS (common mistake): MEMOIZED RECURSION  —  O(n) time, O(n) space
    // ========================================================================
    // A natural instinct once you know "memoization speeds up recursion" is
    // to cache every call to pow(x, k). Try it here and see: it does NOT
    // help. Each value of k from n down to 0 is visited exactly once, so
    // there's nothing to reuse — the cache just adds O(n) space for zero
    // benefit. This is a good check of whether you actually understand WHEN
    // memoization helps (overlapping subproblems) versus when it's cargo-cult.
    public double memoizedRecursive(double x, int n) {
        long power = n;
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }
        Map<Long, Double> memo = new HashMap<>();
        memo.put(0L, 1.0);
        return memoHelper(x, power, memo);
    }

    private double memoHelper(double x, long n, Map<Long, Double> memo) {
        if (memo.containsKey(n)) return memo.get(n);
        double val = x * memoHelper(x, n - 1, memo);
        memo.put(n, val);
        return val;
    }

    // ========================================================================
    // APPROACH 2 (INTERMEDIATE): DIVIDE & CONQUER RECURSION
    //                              O(log n) time, O(log n) stack space
    // ========================================================================
    // Key insight: instead of shrinking the exponent by 1 each call (n-1),
    // shrink it by HALF each call.
    //
    //   x^10 = (x^5)^2          <- even exponent: square the half-result
    //   x^11 = (x^5)^2 * x      <- odd exponent: square the half-result, then
    //                               multiply in one extra x to cover the
    //                               leftover from integer division
    //
    // Halving the exponent every call means only O(log n) calls are made.
    public double myPowDivideAndConquer(double x, int n) {
        long power = n;
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }
        return divideAndConquerHelper(x, power);
    }

    private double divideAndConquerHelper(double x, long n) {
        if (n == 0) return 1.0;

        double half = divideAndConquerHelper(x, n / 2);

        if (n % 2 == 0) {
            return half * half;          // even: x^n = (x^(n/2))^2
        } else {
            return half * half * x;      // odd:  x^n = (x^(n/2))^2 * x
        }
    }

    // ========================================================================
    // APPROACH 3 (OPTIMAL): ITERATIVE BINARY EXPONENTIATION
    //                         O(log n) time, O(1) space
    // ========================================================================
    // Same mathematical idea as Approach 2, but unrolled into a loop using
    // the binary digits of n directly — no recursion, so no stack cost.
    //
    // Example: n = 13 -> binary 1101 -> x^13 = x^8 * x^4 * x^1
    //
    // Walking from the least-significant bit upward:
    //   - if the current bit of n is 1, multiply that power of x into result
    //   - square x so it represents the next bit's power of two
    //   - shift n right by one bit and repeat
    public double myPowOptimal(double x, int n) {
        long power = n;
        if (power < 0) {
            x = 1 / x;
            power = -power;
        }

        double result = 1.0;
        while (power > 0) {
            if ((power & 1) == 1) {   // current bit is 1 -> fold x into result
                result *= x;
            }
            x *= x;                  // x now represents the next power of two
            power >>= 1;              // move to the next bit
        }
        return result;
    }

    // ========================================================================
    // DRIVER — runs every approach against the same inputs, including the
    // classic edge cases that trip people up: zero, negative exponent, and
    // the Integer.MIN_VALUE overflow trap.
    // ========================================================================
    public static void main(String[] args) {
        PowXNStudyGuide solution = new PowXNStudyGuide();

        double[] bases = {2.0, 2.1, 2.0};
        int[] exponents = {10, 3, -2};

        System.out.println("=== Standard examples ===");
        for (int i = 0; i < bases.length; i++) {
            runAll(solution, bases[i], exponents[i]);
        }

        System.out.println("=== Edge cases ===");
        runAll(solution, 2.0, 0);                       // anything^0 = 1
        runAll(solution, 1.0001, Integer.MIN_VALUE + 1); // near-extreme negative exponent
        // Note: naiveRecursive / memoizedRecursive are skipped above for the
        // extreme exponent since O(n) recursion depth would StackOverflow —
        // that limitation is itself part of the lesson.
    }

    private static void runAll(PowXNStudyGuide s, double x, int n) {
        System.out.printf("x = %s, n = %d%n", x, n);
        System.out.println("  Brute force:        " + s.myPowBruteForce(x, n));

        if (Math.abs((long) n) <= 100000) { // guard against StackOverflow for huge n
            System.out.println("  Naive recursion:     " + s.naiveRecursive(x, n));
            System.out.println("  Memoized recursion:  " + s.memoizedRecursive(x, n));
        } else {
            System.out.println("  Naive recursion:     skipped (would StackOverflow — O(n) stack depth)");
            System.out.println("  Memoized recursion:  skipped (would StackOverflow — O(n) stack depth)");
        }

        System.out.println("  Divide & conquer:    " + s.myPowDivideAndConquer(x, n));
        System.out.println("  Optimal (iterative):  " + s.myPowOptimal(x, n));
        System.out.println();
    }
}
