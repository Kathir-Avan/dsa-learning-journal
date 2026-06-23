import java.util.Arrays;

/**
 * LEETCODE 344: REVERSE STRING
 * ==============================================================================
 * PROBLEM (plain English):
 *   You're given a string, but it's handed to you as a char array (char[]),
 *   not a String object. You must reverse the array IN PLACE — meaning you
 *   modify the array directly instead of building and returning a new one.
 *   No extra array of O(n) size is allowed if you want to hit the optimal
 *   space complexity.
 *
 * EXAMPLES:
 *   Input:  ['h','e','l','l','o']
 *   Output: ['o','l','l','e','h']
 *
 *   Input:  ['H','a','n','n','a','h']
 *   Output: ['h','a','n','n','a','H']
 *
 * CONSTRAINTS (typical for this problem on LeetCode):
 *   - 1 <= s.length <= 10^5
 *   - s[i] is a printable ASCII character.
 *
 * EDGE CASES WORTH TESTING:
 *   - Single character array (e.g., ['a']) — reversing should be a no-op.
 *   - Even-length array (e.g., 4 chars) — pointers meet with no middle element.
 *   - Odd-length array (e.g., 5 chars) — pointers meet AT a middle element,
 *     which should be left untouched (swapping an element with itself).
 *   - Array with duplicate/repeated characters — reversal logic doesn't care
 *     about character values at all, so duplicates should "just work," but
 *     it's a good sanity check that we're not accidentally comparing values.
 *   - Large input near the constraint max (10^5) — good for confirming our
 *     optimal solution doesn't blow the stack or run too slowly, unlike a
 *     naive recursive approach might.
 *   - Note: an empty array (length 0) is technically outside the stated
 *     constraints (length >= 1), but it's worth a defensive test anyway —
 *     a well-written solution should handle it gracefully rather than crash.
 *
 * PATTERN:
 *   This is the textbook "Two Pointers" pattern (specifically, the
 *   "opposite-direction / converging pointers" variant), where one pointer
 *   starts at the front, one at the back, and they walk toward each other
 *   swapping elements until they meet or cross.
 *
 * COMPLEXITY COMPARISON:
 * ------------------------------------------------------------------------------
 * Approach                  | Time   | Space  | Note
 * ------------------------------------------------------------------------------
 * Brute Force (extra array) | O(n)   | O(n)   | Correct, but violates the
 *                           |        |        | "in-place" requirement by
 *                           |        |        | allocating a second array.
 * Optimal (two pointers)    | O(n)   | O(1)   | Swaps in place; only a couple
 *                           |        |        | of extra variables used.
 * ------------------------------------------------------------------------------
 * Note: there isn't a meaningful "intermediate" approach for this problem —
 * the jump from "use extra space" to "use no extra space via two pointers"
 * is really the whole problem, so that section is intentionally skipped.
 * ==============================================================================
 */
public class ReverseStringLC344 {

    /**
     * BRUTE FORCE APPROACH
     * --------------------
     * THE NAIVE IDEA:
     *   Walk the input from the end to the beginning, copying each character
     *   into a brand-new array from front to back. Once the copy is done,
     *   write the new array's contents back over the original array (since
     *   the problem technically wants the original array mutated, even
     *   though this approach "cheats" by using a helper array internally).
     *
     * WHY IT'S CORRECT:
     *   If you read the source from last-to-first and write to the
     *   destination from first-to-last, the destination ends up holding
     *   the characters in reverse order by construction — this is just the
     *   definition of "reverse."
     *
     * WHAT MAKES IT SLOW / SUBOPTIMAL:
     *   It's not slow in terms of TIME (it's still O(n)), but it's wasteful
     *   in SPACE: we allocate a second array of size n just to shuffle data
     *   around, then copy that data back into the original array — meaning
     *   we touch every element TWICE (once to fill the temp array, once to
     *   copy it back) instead of once. For a problem whose whole point is
     *   "can you do this in place," this defeats the purpose.
     */
    public static void reverseBruteForce(char[] s) {
        int n = s.length;
        char[] temp = new char[n]; // extra O(n) space — this is the "cheat"

        // Read from the back of s, write to the front of temp.
        for (int i = 0; i < n; i++) {
            temp[i] = s[n - 1 - i];
        }

        // Copy the fully-reversed temp array back over the original array,
        // since the method must mutate s in place to match the problem spec.
        for (int i = 0; i < n; i++) {
            s[i] = temp[i];
        }
    }

    /**
     * OPTIMAL APPROACH — TWO POINTERS
     * --------------------------------
     * THE KEY INSIGHT:
     *   You don't need a second array at all. Reversing is really just
     *   "swap the 1st and last elements, then the 2nd and 2nd-to-last,
     *   and so on, until you reach the middle." That's naturally expressed
     *   with two pointers — one starting at index 0 (left), one starting
     *   at index n-1 (right) — that swap their elements and step toward
     *   each other until they meet (or cross, for even-length arrays).
     *
     * TRADE-OFF VERSUS BRUTE FORCE:
     *   Time complexity stays O(n) in both cases, but space drops from
     *   O(n) to O(1) — we only ever use a constant number of extra
     *   variables (left, right, a temp char for swapping), regardless of
     *   how large the input is. This also means each element is visited
     *   only once (well, touched twice if you count being read AND
     *   written during its swap), rather than copied twice like before.
     */
    public static void reverseOptimal(char[] s) {
        int left = 0;
        int right = s.length - 1;

        // Pointers converge toward the middle. When left == right, we're
        // sitting on the single middle element of an odd-length array, and
        // there's nothing left to swap, so the loop correctly stops there.
        // When left > right (only possible mid-loop for even-length arrays
        // after the last valid swap), we've also correctly finished.
        while (left < right) {
            // Classic three-step swap using a temp variable. We avoid
            // "clever" XOR-swap tricks here on purpose — they're not faster
            // for chars, and they hurt readability for zero real benefit.
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;

            left++;
            right--;
        }
    }

    public static void main(String[] args) {
        // ---- Problem-statement examples ----
        char[] example1 = {'h', 'e', 'l', 'l', 'o'};
        char[] example2 = {'H', 'a', 'n', 'n', 'a', 'h'};

        // ---- Edge cases ----
        char[] singleChar = {'a'};                     // single element
        char[] evenLength = {'a', 'b', 'c', 'd'};       // pointers meet with no middle
        char[] oddLength = {'a', 'b', 'c', 'd', 'e'};   // pointers meet AT a middle element
        char[] duplicates = {'x', 'x', 'y', 'x', 'x'};  // repeated characters
        char[] empty = {};                              // defensive test, outside stated constraints

        // Large input near the constraint max, to sanity-check performance.
        // We build it programmatically rather than typing 100,000 characters.
        int largeSize = 100_000;
        char[] largeInput = new char[largeSize];
        for (int i = 0; i < largeSize; i++) {
            largeInput[i] = (char) ('a' + (i % 26));
        }

        // Each test case needs its own fresh copies, since both methods
        // mutate their input in place — running one method on an array
        // would corrupt the input for the other method's test.
        runComparison("Example 1 (\"hello\")", example1.clone(), example1.clone());
        runComparison("Example 2 (\"Hannah\")", example2.clone(), example2.clone());
        runComparison("Single character", singleChar.clone(), singleChar.clone());
        runComparison("Even length", evenLength.clone(), evenLength.clone());
        runComparison("Odd length", oddLength.clone(), oddLength.clone());
        runComparison("Duplicates", duplicates.clone(), duplicates.clone());
        runComparison("Empty array", empty.clone(), empty.clone());

        // Both approaches are O(n) time, so neither one "times out" on the
        // large input conceptually. We still run it on the optimal version
        // only with a printed note, mainly to demonstrate that the brute
        // force version is the one paying extra memory cost here, not time.
        System.out.println("\n--- Large input (n = " + largeSize + ") ---");
        char[] largeBruteCopy = largeInput.clone();
        char[] largeOptimalCopy = largeInput.clone();
        reverseBruteForce(largeBruteCopy);
        reverseOptimal(largeOptimalCopy);
        boolean largeMatches = Arrays.equals(largeBruteCopy, largeOptimalCopy);
        System.out.println("Brute Force and Optimal agree on large input: " + largeMatches);
        System.out.println("Note: both run in O(n) time here; the real difference is that " +
                "Brute Force allocated a second " + largeSize + "-element array while " +
                "Optimal used only a few extra variables (O(1) space).");
    }

    /**
     * Helper for main(): runs both approaches on equivalent copies of the
     * same input and prints the results side by side, labeled, so they're
     * easy to compare at a glance.
     */
    private static void runComparison(String label, char[] bruteInput, char[] optimalInput) {
        System.out.println("\n--- " + label + " ---");
        System.out.println("Input:        " + Arrays.toString(bruteInput));

        reverseBruteForce(bruteInput);
        System.out.println("Brute Force:  " + Arrays.toString(bruteInput));

        reverseOptimal(optimalInput);
        System.out.println("Optimal:      " + Arrays.toString(optimalInput));
    }
}

/*
 * ==============================================================================
 * CLOSING NOTES — RECOGNIZING THIS PATTERN
 * ==============================================================================
 * RECOGNIZING IT IN THE FUTURE:
 *   Whenever you see "reverse," "palindrome check," "is sorted," or any
 *   problem where you need to compare/swap elements symmetrically from
 *   both ends of a linear structure (array, string, or even a string
 *   represented as a char array) — especially with an in-place / O(1)
 *   space requirement — think "two pointers, converging from both ends."
 *   The signature is a `while (left < right)` loop with `left++` and
 *   `right--` somewhere inside it.
 *
 * COMMON INTERVIEW FOLLOW-UP VARIANTS:
 *   - "Reverse only the vowels in the string" — same two-pointer skeleton,
 *     but each pointer skips forward/backward until it lands on a vowel
 *     before swapping.
 *   - "Reverse words in a string" (e.g., "the sky is blue" ->
 *     "blue is sky the") — a different problem in disguise; typically
 *     solved by splitting on whitespace and reversing the word order,
 *     or via in-place reversal at the word level after reversing the
 *     whole string once.
 *   - "Reverse a string recursively" — interviewers sometimes ask this
 *     to test recursion fundamentals; the recursive version essentially
 *     re-expresses the two-pointer swap as recursive calls that swap
 *     s[left]/s[right] then recurse on (left+1, right-1), with the base
 *     case being left >= right. Watch out for recursion depth on very
 *     large inputs (this is exactly why the iterative version is
 *     generally preferred in production code).
 *   - "Reverse a linked list" — conceptually related (you're reversing
 *     order), but the technique is different since you can't index from
 *     the "end" of a singly linked list in O(1) — that one typically uses
 *     prev/curr/next pointer rewiring instead.
 * ==============================================================================
 */
