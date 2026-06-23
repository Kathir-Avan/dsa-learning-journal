import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================================
 * LeetCode 21: Merge Two Sorted Lists
 * ============================================================================
 *
 * PROBLEM (plain English):
 * You're given the heads of two singly linked lists, "list1" and "list2",
 * and both are already sorted in non-decreasing order. Merge them into ONE
 * single sorted linked list by splicing the existing nodes together (you
 * don't need to create brand new nodes — just relink the ones you're given),
 * and return the head of the merged list.
 *
 * EXAMPLES:
 *   Input:  list1 = [1,2,4], list2 = [1,3,4]
 *   Output: [1,1,2,3,4,4]
 *
 *   Input:  list1 = [],      list2 = []
 *   Output: []
 *
 *   Input:  list1 = [],      list2 = [0]
 *   Output: [0]
 *
 * CONSTRAINTS:
 *   The number of nodes in both lists is in the range [0, 50].
 *   -100 <= Node.val <= 100
 *   Both list1 and list2 are sorted in non-decreasing order.
 *
 * EDGE CASES WORTH TESTING:
 *   - Both lists empty                -> result should be empty (null head)
 *   - One list empty, one non-empty   -> result should just be the non-empty one
 *   - Duplicate values across lists   -> e.g. [1,2,4] and [1,3,4], duplicates
 *                                         must all appear in the merged result
 *   - Negative numbers                -> e.g. [-100,-50] and [-90,0], confirms
 *                                         comparisons aren't accidentally
 *                                         treating values as unsigned/positive
 *   - Single element each             -> e.g. [5] and [2]
 *   - One list fully "smaller" than the other -> e.g. [1,2,3] and [4,5,6],
 *     confirms the leftover-tail handling works correctly
 *   - Max constraint size (50 + 50 nodes) -> sanity check for performance
 *
 * PATTERN(S):
 *   - Two pointers (one pointer walking each list, advancing the smaller one)
 *   - This is also the exact "merge" step from Merge Sort, applied directly
 *     to linked lists instead of arrays.
 *
 * COMPLEXITY COMPARISON TABLE:
 * ------------------------------------------------------------------------------------------
 * Approach                | Time            | Space           | Note
 * ------------------------------------------------------------------------------------------
 * Brute Force             | O((m+n) log(m+n))| O(m+n)         | Ignores that the inputs are
 * (collect + sort)        | due to sorting   | for the temp    | already sorted; throws that
 *                          |                  | array + new     | information away and resorts
 *                          |                  | list nodes      | everything from scratch.
 * ------------------------------------------------------------------------------------------
 * Intermediate            | O(m+n)           | O(m+n)          | Reuses the existing nodes
 * (recursive merge)       |                  | call stack       | (no resorting), but the
 *                          |                  | depth            | recursion depth grows with
 *                          |                  |                  | list length, risking a
 *                          |                  |                  | stack overflow on very long
 *                          |                  |                  | lists.
 * ------------------------------------------------------------------------------------------
 * Optimal                 | O(m+n)           | O(1) extra       | Same linear time as the
 * (iterative two-pointer) |                  | space (just a    | recursive version, but with
 *                          |                  | few pointers)    | no call-stack growth — scales
 *                          |                  |                  | safely to very long lists.
 * ------------------------------------------------------------------------------------------
 * ============================================================================
 */
public class MergeTwoSortedListsLC21 {

    /**
     * Standard singly linked list node, as used by LeetCode for this problem.
     */
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    // ========================================================================
    // BRUTE FORCE
    // ========================================================================
    /**
     * BRUTE FORCE — collect every value, sort them all, rebuild a new list.
     *
     * THE NAIVE IDEA:
     * Walk both linked lists and dump every value into one big array/list,
     * completely ignoring the fact that each input list was already sorted.
     * Then sort that combined collection from scratch, and finally build a
     * brand-new linked list out of the sorted values in order.
     *
     * WHY IT'S CORRECT:
     * A sorted merge of two lists is, by definition, just "all the values
     * from both lists, in sorted order." Sorting the combined values
     * directly trivially produces a correct answer, regardless of whether
     * we exploit the fact that the inputs were pre-sorted.
     *
     * WHY IT'S SLOW:
     * We throw away free information. Both inputs arrive pre-sorted, which
     * means a simple linear merge (like the optimal approach below) could
     * produce the same answer in O(m+n) time. Instead, this approach pays
     * the cost of a full O((m+n) log(m+n)) sort, and also allocates an
     * entirely new array plus an entirely new set of list nodes instead of
     * reusing the nodes we were already given.
     */
    public ListNode solveBruteForce(ListNode list1, ListNode list2) {
        List<Integer> values = new ArrayList<>();

        // Dump every value from both lists into one flat collection,
        // discarding the original ordering information entirely.
        for (ListNode node = list1; node != null; node = node.next) {
            values.add(node.val);
        }
        for (ListNode node = list2; node != null; node = node.next) {
            values.add(node.val);
        }

        // Re-sort from scratch, even though both inputs were already sorted.
        Collections.sort(values);

        // Rebuild a brand new linked list from the sorted values.
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        for (int v : values) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }

        return dummyHead.next;
    }

    // ========================================================================
    // INTERMEDIATE
    // ========================================================================
    /**
     * INTERMEDIATE — recursive merge, reusing the original nodes.
     *
     * THE KEY INSIGHT THAT IMPROVES ON BRUTE FORCE:
     * Since both lists are already sorted, the smallest remaining value
     * overall must be the smaller of the two current list heads. So we can
     * just compare list1's head against list2's head, take the smaller one
     * as the next node of our result, and recursively merge "the rest."
     * This reuses existing nodes (no resorting, no new node allocation for
     * values) and runs in linear time.
     *
     * WHY IT STILL ISN'T OPTIMAL:
     * Each recursive call adds a new frame to the call stack, and the
     * recursion only bottoms out once one list is fully consumed — so the
     * stack depth grows proportionally to the combined list length (up to
     * m+n frames deep). For very long lists this risks a StackOverflowError,
     * which an iterative version avoids entirely while doing the exact same
     * amount of comparison work.
     */
    public ListNode solveIntermediateRecursive(ListNode list1, ListNode list2) {
        // Base cases: if one list has run out, the entire rest of the
        // result is simply whatever remains of the other list — no more
        // comparisons are needed since it's already sorted.
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }

        // Whichever head is smaller becomes the next node in the merged
        // result; we then recursively merge "everything after it" with the
        // other list's untouched head.
        if (list1.val <= list2.val) {
            list1.next = solveIntermediateRecursive(list1.next, list2);
            return list1;
        } else {
            list2.next = solveIntermediateRecursive(list1, list2.next);
            return list2;
        }
    }

    // ========================================================================
    // OPTIMIZED
    // ========================================================================
    /**
     * OPTIMAL — iterative two-pointer merge.
     *
     * THE KEY INSIGHT/TRICK:
     * This is exactly the same comparison logic as the recursive version
     * above, but driven by a "while" loop with two pointers instead of
     * recursive calls. We keep a "dummy" placeholder node in front of the
     * result so we never have to special-case "what is the very first node
     * of the merged list" — we just always attach to dummy.next at the end
     * and skip past the dummy itself.
     *
     * TRADE-OFF VS THE PREVIOUS APPROACH:
     * Same O(m+n) time as the recursive version, since we still make exactly
     * one comparison per node. The win is space: this version uses only a
     * fixed, small number of pointer variables (O(1) extra space) instead of
     * growing the call stack with list length, so it safely handles lists
     * far longer than recursion could handle without overflowing the stack.
     */
    public ListNode solveOptimal(ListNode list1, ListNode list2) {
        // The dummy node is never part of the final answer — it just gives
        // "tail" a safe, non-null starting point to attach nodes to, so the
        // very first real node doesn't need special-case handling.
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;

        // Walk both lists simultaneously, always attaching the smaller
        // current node and advancing only that list's pointer.
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                tail.next = list1;
                list1 = list1.next;
            } else {
                tail.next = list2;
                list2 = list2.next;
            }
            tail = tail.next;
        }

        // At most one of list1/list2 still has remaining nodes at this
        // point (the loop only stops when one is null). Since that
        // remainder is already sorted, we can attach it directly in O(1)
        // instead of walking it node-by-node.
        tail.next = (list1 != null) ? list1 : list2;

        return dummyHead.next;
    }

    // ========================================================================
    // HELPER METHODS (not an "approach" — just utilities for the demo below)
    // ========================================================================

    /** Builds a linked list from an int array, for easy test setup. */
    private static ListNode buildList(int[] values) {
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        for (int v : values) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }
        return dummyHead.next;
    }

    /** Renders a linked list as "[1, 2, 3]" style text for printing. */
    private static String describeList(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (ListNode node = head; node != null; node = node.next) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(node.val);
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    // ========================================================================
    // MAIN — runs and compares every approach on the same inputs
    // ========================================================================
    public static void main(String[] args) {
        MergeTwoSortedListsLC21 solver = new MergeTwoSortedListsLC21();

        // ---- Given example: [1,2,4] + [1,3,4] -> [1,1,2,3,4,4] ----
        System.out.println("=== Example: [1,2,4] + [1,3,4] (includes duplicates) ===");
        runAllApproaches(solver, new int[]{1, 2, 4}, new int[]{1, 3, 4});
        System.out.println();

        // ---- Edge case: both lists empty ----
        System.out.println("=== Edge case: both lists empty ===");
        runAllApproaches(solver, new int[]{}, new int[]{});
        System.out.println();

        // ---- Edge case: one list empty, one non-empty ----
        System.out.println("=== Edge case: [] + [0] ===");
        runAllApproaches(solver, new int[]{}, new int[]{0});
        System.out.println();

        // ---- Edge case: negative numbers ----
        System.out.println("=== Edge case: negative numbers [-100,-50] + [-90,0] ===");
        runAllApproaches(solver, new int[]{-100, -50}, new int[]{-90, 0});
        System.out.println();

        // ---- Edge case: one list entirely smaller than the other ----
        System.out.println("=== Edge case: [1,2,3] + [4,5,6] (no interleaving needed) ===");
        runAllApproaches(solver, new int[]{1, 2, 3}, new int[]{4, 5, 6});
        System.out.println();

        // ---- Stress case: max constraint size (50 + 50 nodes) ----
        // All three approaches handle this fine, since 50 is tiny — this is
        // just a sanity check that nothing throws or misbehaves at the
        // upper bound of the stated constraints.
        System.out.println("=== Stress check: 50 + 50 nodes (max constraint size) ===");
        int[] big1 = new int[50];
        int[] big2 = new int[50];
        for (int i = 0; i < 50; i++) {
            big1[i] = i * 2;       // 0, 2, 4, ..., 98
            big2[i] = i * 2 + 1;   // 1, 3, 5, ..., 99
        }
        ListNode bruteResult = solver.solveBruteForce(buildList(big1), buildList(big2));
        ListNode recursiveResult = solver.solveIntermediateRecursive(buildList(big1), buildList(big2));
        ListNode optimalResult = solver.solveOptimal(buildList(big1), buildList(big2));
        System.out.println("Brute Force      length: " + countLength(bruteResult));
        System.out.println("Intermediate     length: " + countLength(recursiveResult));
        System.out.println("Optimal          length: " + countLength(optimalResult));
        System.out.println("(Lists are long, so we print lengths instead of full contents here.)");
    }

    /**
     * Runs all three approaches on freshly-built copies of the same input
     * arrays (each approach gets its OWN list nodes, since merging mutates
     * "next" pointers in place and would otherwise corrupt a shared list
     * before the next approach gets to use it) and prints each result.
     */
    private static void runAllApproaches(MergeTwoSortedListsLC21 solver, int[] arr1, int[] arr2) {
        ListNode bruteResult = solver.solveBruteForce(buildList(arr1), buildList(arr2));
        System.out.println("Brute Force : " + describeList(bruteResult));

        ListNode recursiveResult = solver.solveIntermediateRecursive(buildList(arr1), buildList(arr2));
        System.out.println("Intermediate: " + describeList(recursiveResult));

        ListNode optimalResult = solver.solveOptimal(buildList(arr1), buildList(arr2));
        System.out.println("Optimal     : " + describeList(optimalResult));
    }

    private static int countLength(ListNode head) {
        int count = 0;
        for (ListNode node = head; node != null; node = node.next) {
            count++;
        }
        return count;
    }
}

/*
 * ============================================================================
 * CLOSING NOTES
 * ============================================================================
 * RECOGNIZING THIS PATTERN IN FUTURE PROBLEMS:
 * Whenever you're combining two (or more) ALREADY-SORTED sequences into one
 * sorted sequence — arrays, linked lists, streams, file lines, etc. — reach
 * for the two-pointer merge: walk both inputs simultaneously, always take
 * the smaller "current" element, and advance only that pointer. This same
 * merge step is the backbone of Merge Sort, k-way merges (using a heap when
 * there are more than 2 sequences), and external sorting of huge files.
 *
 * COMMON INTERVIEW FOLLOW-UP VARIANTS:
 * - "Merge K sorted lists" (LeetCode 23) — extend this same two-pointer idea
 *   using a min-heap (priority queue) that always holds the current head of
 *   each of the K lists, giving O(N log K) time instead of repeatedly
 *   merging pairs.
 * - "Merge two sorted ARRAYS in place, where one has extra capacity at the
 *   end" (LeetCode 88) — same merge logic, but you typically merge from the
 *   BACK of the array forward to avoid overwriting values you haven't read
 *   yet.
 * - "Return the merged list without mutating the original two lists" —
 *   you'd need to allocate new nodes (like the brute-force approach does)
 *   instead of relinking existing ones, trading some extra space for
 *   leaving the inputs untouched.
 * ============================================================================
 */
