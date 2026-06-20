import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * LeetCode 95: Unique Binary Search Trees II
 * ============================================================================
 *
 * PROBLEM (plain English):
 * Given an integer n, you have the numbers 1, 2, ..., n. Build EVERY possible
 * Binary Search Tree (BST) that uses each of those numbers exactly once as a
 * node value, and return a list containing the ROOT node of each distinct
 * tree shape.
 *
 * A Binary Search Tree means: for every node, everything in its left subtree
 * is smaller than the node, and everything in its right subtree is larger.
 *
 * EXAMPLES:
 *   Input:  n = 3
 *   Output: [[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
 *           (5 distinct BST shapes total — this matches the 3rd Catalan number)
 *
 *   Input:  n = 1
 *   Output: [[1]]   (only one possible tree: a single node)
 *
 * CONSTRAINTS:
 *   1 <= n <= 8
 *   (n is kept small on purpose — the NUMBER of valid trees grows extremely
 *    fast, following the Catalan number sequence: 1, 1, 2, 5, 14, 42, 132...)
 *
 * EDGE CASES WORTH TESTING:
 *   - n = 1            -> exactly one tree (a single leaf node)
 *   - n = 2             -> exactly 2 trees (root=1 with right child 2, or
 *                          root=2 with left child 1)
 *   - n = 3 (given example) -> exactly 5 trees, good for visually checking shapes
 *   - n = 0 is technically outside constraints (n >= 1), but our code is
 *     written defensively to return an empty list rather than crashing if
 *     it's ever called that way internally (start > end case).
 *   - "Duplicates" and "negative numbers" don't apply here since the problem
 *     guarantees the values are always exactly 1..n, all positive, all unique.
 *   - Larger n (e.g., n = 8, the max constraint) -> stress-tests how badly
 *     redundant recomputation hurts the brute-force version, since the
 *     number of trees explodes to 1430 for n=8.
 *
 * PATTERN(S):
 *   - Backtracking / recursive tree construction over a range [start, end]
 *   - Divide and conquer: pick each value in [start, end] as the root, then
 *     recursively solve the left part [start, root-1] and right part
 *     [root+1, end], and combine every left option with every right option.
 *   - The optimized version layers in MEMOIZATION (top-down DP), since the
 *     same [start, end] sub-ranges get asked for again and again across
 *     different branches of the recursion.
 *
 * COMPLEXITY COMPARISON TABLE:
 * ----------------------------------------------------------------------------------
 * Approach                  | Time                          | Space            | Note
 * ----------------------------------------------------------------------------------
 * Brute Force (no memo)     | Exponential; strictly worse    | O(n) call stack  | Recomputes
 *                            | than optimal because identical | + output size    | the SAME
 *                            | [start,end] ranges get rebuilt |                  | [start,end]
 *                            | from scratch many times across |                  | range many
 *                            | different recursive branches.  |                  | times.
 * ----------------------------------------------------------------------------------
 * Optimal (memoized)        | Bounded by the total output    | O(n^2) memo      | Each unique
 *                            | size, which follows the        | table entries    | [start,end]
 *                            | Catalan number C(n) ~ 4^n /    | + O(n) stack     | range is
 *                            | n^1.5. Memoization removes the | + output size    | solved once
 *                            | repeated recomputation seen    |                  | and reused.
 *                            | in brute force.                |                  |
 * ----------------------------------------------------------------------------------
 *
 * NOTE: Because the final answer itself must contain every distinct tree
 * (Catalan-many trees, each potentially with O(n) nodes), NO approach to
 * this exact problem can beat the size of the output. The "optimization"
 * here is about eliminating WASTED, REDUNDANT work on the way to building
 * that output — not about beating an output-size lower bound.
 * ============================================================================
 */
public class UniqueBinarySearchTreesIILC95 {

    /**
     * Simple binary tree node, the standard LeetCode definition.
     */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // ========================================================================
    // BRUTE FORCE
    // ========================================================================
    /**
     * BRUTE FORCE — plain recursion, NO memoization.
     *
     * THE NAIVE IDEA:
     * To build every BST using the values [start..end], try EVERY value in
     * that range as the root. For a chosen root "i":
     *   - every valid left subtree comes from recursively solving [start, i-1]
     *   - every valid right subtree comes from recursively solving [i+1, end]
     * Then we combine each possible left subtree with each possible right
     * subtree (a full cross-product) and attach them under a new root node
     * with value i. Doing this for every possible root, and recursively for
     * every sub-range, naturally enumerates every distinct BST shape.
     *
     * WHY IT'S CORRECT:
     * Every BST over a contiguous set of integers has SOME root, and once
     * you fix that root, the left subtree must contain exactly the smaller
     * values and the right subtree exactly the larger values (that's the
     * BST property). So trying every possible root, and recursively trying
     * every possible left/right subtree shape, covers every possibility
     * with no overlap and no omissions.
     *
     * WHY IT'S SLOW:
     * This method calls itself on the SAME [start, end] sub-range over and
     * over again from different parts of the recursion tree, but never
     * remembers the answer. For example, generateTreesBruteForce(2, 3) might
     * get computed fresh many separate times if it's reachable through many
     * different higher-level root choices. Since the recursion branches
     * combinatorially (cross-product of left options x right options at
     * every level), this redundant recomputation compounds badly as n grows
     * — by n=8 it is noticeably slower than the memoized version even
     * though both ultimately produce the same 1430 trees.
     */
    public List<TreeNode> solveBruteForce(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        return generateTreesBruteForce(1, n);
    }

    private List<TreeNode> generateTreesBruteForce(int start, int end) {
        List<TreeNode> result = new ArrayList<>();

        // Base case: an empty range still counts as ONE valid "shape" — the
        // absence of a subtree (represented as null). Without adding this
        // null placeholder, the cross-product loop below would produce zero
        // combinations instead of "no left child" / "no right child".
        if (start > end) {
            result.add(null);
            return result;
        }

        // Try every value in [start, end] as the root of this sub-tree.
        for (int rootVal = start; rootVal <= end; rootVal++) {
            // Recompute the left and right options from scratch every time,
            // even if this exact [start, end] (or a sub-range of it) was
            // already solved elsewhere in the recursion tree. This recompute
            // is exactly what makes this version "brute force".
            List<TreeNode> leftSubtrees = generateTreesBruteForce(start, rootVal - 1);
            List<TreeNode> rightSubtrees = generateTreesBruteForce(rootVal + 1, end);

            // Cross-product: pair every possible left shape with every
            // possible right shape under this root value.
            for (TreeNode left : leftSubtrees) {
                for (TreeNode right : rightSubtrees) {
                    TreeNode root = new TreeNode(rootVal);
                    root.left = left;
                    root.right = right;
                    result.add(root);
                }
            }
        }

        return result;
    }

    // ========================================================================
    // OPTIMIZED (no separate "intermediate" approach exists for this problem
    // — the only meaningful lever here is removing redundant recomputation
    // via memoization, so we go straight from brute force to optimal).
    // ========================================================================
    /**
     * OPTIMAL — same recursive idea as brute force, but with MEMOIZATION.
     *
     * THE KEY INSIGHT:
     * The recursive sub-problems are fully described by just two numbers:
     * (start, end). No matter which higher-level root choice led us here,
     * solving [start, end] always produces the exact same list of tree
     * shapes. So instead of recomputing it every time we encounter it, we
     * cache the result the first time and simply reuse it on every later
     * visit. We use a String key "start,end" in a HashMap as the cache.
     *
     * IMPORTANT SUBTLETY:
     * We are caching and REUSING the same TreeNode objects across multiple
     * parent trees in the output. That's safe here because we only ever
     * read these trees (we never mutate a node after it's been built and
     * attached), so sharing identical sub-tree structures across multiple
     * results doesn't cause any incorrect behavior — it just avoids
     * allocating duplicate, identical node objects over and over.
     *
     * TRADE-OFF VS BRUTE FORCE:
     * We spend O(n^2) extra space on the memo table (one entry per distinct
     * [start, end] pair) in exchange for never solving the same sub-range
     * twice. The final output size is identical (still Catalan(n) trees),
     * but the WORK done to build that output shrinks substantially because
     * we eliminate the repeated recomputation that brute force suffers from.
     */
    public List<TreeNode> solveOptimal(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        Map<String, List<TreeNode>> memo = new HashMap<>();
        return generateTreesOptimal(1, n, memo);
    }

    private List<TreeNode> generateTreesOptimal(int start, int end, Map<String, List<TreeNode>> memo) {
        List<TreeNode> result = new ArrayList<>();

        // Same base case reasoning as brute force: an empty range is one
        // valid "shape" represented by null. We don't bother caching this
        // trivial case since it's already O(1) work.
        if (start > end) {
            result.add(null);
            return result;
        }

        // The cache key uniquely identifies this sub-range. If we've solved
        // it before (from any other branch of the recursion), reuse it
        // immediately instead of redoing all the work below.
        String key = start + "," + end;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        for (int rootVal = start; rootVal <= end; rootVal++) {
            // These recursive calls now check the memo first, so identical
            // sub-ranges encountered anywhere else in the recursion tree
            // are computed only once, total.
            List<TreeNode> leftSubtrees = generateTreesOptimal(start, rootVal - 1, memo);
            List<TreeNode> rightSubtrees = generateTreesOptimal(rootVal + 1, end, memo);

            for (TreeNode left : leftSubtrees) {
                for (TreeNode right : rightSubtrees) {
                    TreeNode root = new TreeNode(rootVal);
                    root.left = left;
                    root.right = right;
                    result.add(root);
                }
            }
        }

        // Store the fully-computed list for [start, end] before returning,
        // so any future request for this exact range is instant.
        memo.put(key, result);
        return result;
    }

    // ========================================================================
    // HELPER METHODS (not an "approach" — just utilities for the demo below)
    // ========================================================================

    /**
     * Renders a tree as a single-line preorder-ish string like "1(null)(2)"
     * so we can visually compare different tree shapes in console output
     * without needing a graphical tree printer.
     */
    private static String describeTree(TreeNode node) {
        if (node == null) {
            return "null";
        }
        return node.val + "(" + describeTree(node.left) + ")(" + describeTree(node.right) + ")";
    }

    private static void printAllTrees(String label, List<TreeNode> trees) {
        System.out.println(label + " -> " + trees.size() + " tree(s):");
        for (TreeNode t : trees) {
            System.out.println("    " + describeTree(t));
        }
    }

    // ========================================================================
    // MAIN — runs and compares every approach on the same inputs
    // ========================================================================
    public static void main(String[] args) {
        UniqueBinarySearchTreesIILC95 solver = new UniqueBinarySearchTreesIILC95();

        // ---- n = 1: smallest meaningful case (single node) ----
        System.out.println("=== n = 1 (edge case: single element) ===");
        printAllTrees("Brute Force", solver.solveBruteForce(1));
        printAllTrees("Optimal    ", solver.solveOptimal(1));
        System.out.println();

        // ---- n = 2: smallest case with more than one shape ----
        System.out.println("=== n = 2 (edge case: smallest non-trivial case) ===");
        printAllTrees("Brute Force", solver.solveBruteForce(2));
        printAllTrees("Optimal    ", solver.solveOptimal(2));
        System.out.println();

        // ---- n = 3: the example straight from the problem statement ----
        System.out.println("=== n = 3 (given example, expect 5 trees) ===");
        printAllTrees("Brute Force", solver.solveBruteForce(3));
        printAllTrees("Optimal    ", solver.solveOptimal(3));
        System.out.println();

        // ---- n = 8: maximum constraint size, used as a timing comparison ----
        // We don't print all 1430 trees (too noisy for console output) — we
        // just confirm both approaches agree on the COUNT and measure how
        // much longer brute force takes due to redundant recomputation.
        System.out.println("=== n = 8 (max constraint: timing comparison only) ===");

        long bruteStart = System.nanoTime();
        int bruteCount = solver.solveBruteForce(8).size();
        long bruteEnd = System.nanoTime();

        long optimalStart = System.nanoTime();
        int optimalCount = solver.solveOptimal(8).size();
        long optimalEnd = System.nanoTime();

        System.out.println("Brute Force: " + bruteCount + " trees in "
                + (bruteEnd - bruteStart) / 1_000_000.0 + " ms");
        System.out.println("Optimal    : " + optimalCount + " trees in "
                + (optimalEnd - optimalStart) / 1_000_000.0 + " ms");
        System.out.println("(Note: exact timings vary by machine/JIT warm-up, but Optimal");
        System.out.println(" should consistently avoid the redundant recomputation that");
        System.out.println(" Brute Force performs, especially if run across MULTIPLE calls");
        System.out.println(" in the same program, since each call rebuilds its own memo.)");
    }
}

/*
 * ============================================================================
 * CLOSING NOTES
 * ============================================================================
 * RECOGNIZING THIS PATTERN IN FUTURE PROBLEMS:
 * Whenever a problem asks you to enumerate ALL valid structures built from a
 * contiguous range of values/indices (trees, parenthesizations, partitions,
 * expression splits, etc.), and a structure can be split at a "pivot" point
 * into an independent left part and right part, that's a strong signal to
 * use this same "choose a pivot, recurse on both sides, take the
 * cross-product of results" approach — and to memoize on (start, end) the
 * moment you notice the same sub-range can be reached from multiple paths.
 *
 * COMMON INTERVIEW FOLLOW-UP VARIANTS:
 * - "Just count them, don't build them" (this is literally LeetCode 96,
 *   Unique Binary Search Trees) — drop the TreeNode construction entirely
 *   and just track counts: dp[end-start+1] = sum over roots of
 *   dp[leftSize] * dp[rightSize]. This turns an exponential-output problem
 *   into a clean O(n^2) counting DP, since you no longer pay to build and
 *   store every tree.
 * - "What if values aren't 1..n but an arbitrary sorted array?" — same
 *   exact recursion, just index into the array by position instead of
 *   assuming values 1..n; the BST property still only depends on relative
 *   order, not the literal values.
 * - "Return just one valid BST instead of all of them" — drastically
 *   simpler: you don't need backtracking at all, just pick any root
 *   (commonly the middle element for balance) and recurse once per side.
 * ============================================================================
 */
