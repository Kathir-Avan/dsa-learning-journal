import java.util.*;

/**
 * ============================================================================
 *  LeetCode 726 — Number of Atoms
 *  STUDY GUIDE: Brute Force -> Intermediate -> Optimal
 * ============================================================================
 *
 * PROBLEM
 * -------
 * Given a string `formula` representing a chemical formula, return the count
 * of each atom.
 *
 * Formula rules:
 *   - An atomic element always starts with an uppercase letter, followed by
 *     zero or more lowercase letters, representing its name (e.g. "H", "Mg").
 *   - One or more digits following an element name give its count. If the
 *     count is 1, no digit follows (e.g. "H2O" means H count 2, O count 1).
 *   - Two formulas concatenated represent another formula
 *     (e.g. "H2O2He3Mg4").
 *   - A formula surrounded by parentheses, optionally followed by a count,
 *     is also a formula, and that count multiplies everything inside the
 *     parentheses (e.g. "(H2O2)3" means everything inside is tripled).
 *
 * Return the count of all atoms in `formula`, formatted as: element name
 * (capitalized correctly) followed by its count if greater than 1, with all
 * elements sorted alphabetically and concatenated with no separators.
 *
 * EXAMPLES
 * --------
 *   Input:  "H2O"              Output: "H2O"
 *   Input:  "Mg(OH)2"          Output: "H2MgO2"
 *   Input:  "K4(ON(SO3)2)2"    Output: "K4N2O14S4"
 *
 * CONSTRAINTS
 * -----------
 *   1 <= formula.length <= 1000
 *   formula consists of English letters, digits, '(' and ')'
 *   formula is always valid (no need to validate malformed input)
 *
 * EDGE CASES WORTH TESTING
 * -------------------------
 *   - A single atom with no explicit count, e.g. "H"
 *   - Multiple nested parentheses with chained multipliers, e.g. "(((H2)3)4)5"
 *     (stresses recursion depth and multiplier-chaining arithmetic)
 *   - The same element appearing in multiple, separately-parsed pieces of the
 *     formula that must be summed together, e.g. "H2O(H2O)2"
 *   - Two-letter element names mixed with single-letter ones, e.g. "Fe2O3"
 *
 * PATTERN
 * -------
 * This is a PARSING problem: a recursive-descent / stack-based parser for a
 * mini grammar with nested groups (parentheses), similar in spirit to
 * "Basic Calculator" (LeetCode 224 / 227 / 772) and "Decode String"
 * (LeetCode 394). Whenever you see nested parentheses that need a multiplier
 * applied to everything inside, think: stack of partial results, or
 * recursion using a shared/returned position pointer.
 *
 * COMPLEXITY COMPARISON
 * ----------------------------------------------------------------------------------
 *  Approach                                | Time      | Space         | Note
 *  -----------------------------------------|-----------|---------------|-------------------------------
 *  1. Brute Force (recursive + rescans      | O(n^2)    | O(n)          | Re-scans for the matching ')'
 *     for matching paren + substring copy)    worst case   worst case    and copies a new substring at
 *                                                                         every nesting level — wasted,
 *                                                                         repeated work
 *  2. Intermediate (recursive descent       | O(n)      | O(d)          | Single pass, no re-scanning or
 *     with shared index pointer)                          d = max         copying, but recursion depth
 *                                                          nesting depth   grows with nesting depth
 *  3. Optimal (iterative, explicit stack    | O(n)      | O(n)          | Single pass, no recursion at
 *     of element-count maps)                                              all — safe from stack overflow
 *                                                                         no matter how deeply the
 *                                                                         formula nests
 * ----------------------------------------------------------------------------------
 *  (n = formula.length(); k = number of distinct element names — TreeMap
 *  operations add a small O(log k) factor per insert in all three, which is
 *  negligible next to the dominant O(n) / O(n^2) terms above.)
 * ============================================================================
 */
public class NumberOfAtomsLC726 {

    // ========================================================================
    // APPROACH 1: BRUTE FORCE  —  O(n^2) worst case time, O(n) space
    // Pattern: Naive Recursive Parsing + Repeated Substring Slicing
    // ========================================================================
    // Idea: whenever we hit '(', find its matching ')' with a dedicated
    // left-to-right scan (tracking parenthesis depth), cut out the substring
    // in between, and recursively re-parse that substring from scratch.
    //
    // Why it's correct: the matching-parenthesis scan correctly identifies
    // the boundaries of each nested group, and recursing on the inner
    // substring correctly handles arbitrarily deep nesting.
    //
    // Why it's slow: two sources of repeated, avoidable work pile up at every
    // level of nesting —
    //   (a) findMatchingParen() re-scans characters that the caller has
    //       already looked at once while searching for ITS OWN matching
    //       parenthesis, and
    //   (b) formula.substring(...) allocates and copies a brand-new String
    //       at every nesting level, so the same characters get copied again
    //       and again as we recurse deeper.
    // For a formula with deep nesting (e.g. many groups like
    // "((((...))))"), both costs stack up across O(n) nesting levels,
    // pushing total time toward O(n^2) in the worst case.
    public String countOfAtomsBruteForce(String formula) {
        Map<String, Integer> counts = parseBruteForce(formula);
        return buildFormulaString(counts);
    }

    private Map<String, Integer> parseBruteForce(String formula) {
        Map<String, Integer> counts = new TreeMap<>(); // TreeMap keeps element names sorted automatically
        int n = formula.length();
        int i = 0;

        while (i < n) {
            char c = formula.charAt(i);

            if (c == '(') {
                // Dedicated O(n) scan just to locate the matching ')' —
                // wasted work, since a single pass could track this with
                // one shared index instead (see the Intermediate approach).
                int closeIndex = findMatchingParen(formula, i);

                // O(n) copy of the inner substring — repeated at every
                // nesting level as we recurse, which is the other big cost.
                String inner = formula.substring(i + 1, closeIndex);
                Map<String, Integer> innerCounts = parseBruteForce(inner); // re-parses the copy from scratch

                i = closeIndex + 1;

                // Parse an optional multiplier digit string right after ')'
                int start = i;
                while (i < n && Character.isDigit(formula.charAt(i))) i++;
                int multiplier = (start == i) ? 1 : Integer.parseInt(formula.substring(start, i));

                for (Map.Entry<String, Integer> entry : innerCounts.entrySet()) {
                    counts.merge(entry.getKey(), entry.getValue() * multiplier, Integer::sum);
                }
            } else {
                // Parse one element token: uppercase letter + lowercase letters*
                int start = i;
                i++; // the leading uppercase letter
                while (i < n && Character.isLowerCase(formula.charAt(i))) i++;
                String name = formula.substring(start, i);

                // Parse an optional count digit string right after the name
                start = i;
                while (i < n && Character.isDigit(formula.charAt(i))) i++;
                int count = (start == i) ? 1 : Integer.parseInt(formula.substring(start, i));

                counts.merge(name, count, Integer::sum);
            }
        }
        return counts;
    }

    // Scans forward from an opening '(' at openIndex, tracking nesting depth,
    // until it finds the ')' that closes THAT SPECIFIC parenthesis.
    // This is the redundant O(n) scan referenced above.
    private int findMatchingParen(String formula, int openIndex) {
        int depth = 0;
        for (int i = openIndex; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1; // formula is guaranteed valid, so this should never happen
    }

    // ========================================================================
    // APPROACH 2 (INTERMEDIATE): RECURSIVE DESCENT WITH SHARED INDEX
    //                              O(n) time, O(d) recursion stack space
    // Pattern: Recursive Descent Parsing (Shared Pointer / Cursor-Based Recursion)
    // ========================================================================
    // Key insight that fixes both brute-force inefficiencies at once: instead
    // of pre-locating the matching ')' and copying a substring, keep ONE
    // shared position pointer (an instance field) that every recursive call
    // reads from and advances. A recursive call simply consumes characters
    // until it sees ')' (or runs out of input), then returns — the caller
    // resumes from wherever the callee left off. No re-scanning, no copying.
    //
    // Why it still isn't optimal: it's tied with the iterative approach on
    // time complexity, O(n), but it pays for nested groups with actual Java
    // call-stack frames. A formula that nests very deeply (close to the
    // formula.length() limit, e.g. many consecutive "(" before any content)
    // recurses just as deeply, risking a StackOverflowError that the
    // iterative, explicit-stack version below can never hit.
    private int pos; // shared parsing cursor used only by this approach

    public String countOfAtomsIntermediate(String formula) {
        pos = 0; // reset cursor for every fresh call
        Map<String, Integer> counts = parseGroupIndexed(formula);
        return buildFormulaString(counts);
    }

    // Parses one "group" — a run of element tokens and nested parenthesized
    // groups — stopping as soon as it hits ')' or the end of the string.
    private Map<String, Integer> parseGroupIndexed(String formula) {
        Map<String, Integer> counts = new TreeMap<>();
        int n = formula.length();

        while (pos < n && formula.charAt(pos) != ')') {
            char c = formula.charAt(pos);

            if (c == '(') {
                pos++; // consume '('
                Map<String, Integer> inner = parseGroupIndexed(formula); // recurse — shares `pos`, no copying
                pos++; // consume the ')' that the recursive call stopped at

                int start = pos;
                while (pos < n && Character.isDigit(formula.charAt(pos))) pos++;
                int multiplier = (start == pos) ? 1 : Integer.parseInt(formula.substring(start, pos));

                for (Map.Entry<String, Integer> entry : inner.entrySet()) {
                    counts.merge(entry.getKey(), entry.getValue() * multiplier, Integer::sum);
                }
            } else {
                int start = pos;
                pos++; // leading uppercase letter
                while (pos < n && Character.isLowerCase(formula.charAt(pos))) pos++;
                String name = formula.substring(start, pos);

                start = pos;
                while (pos < n && Character.isDigit(formula.charAt(pos))) pos++;
                int count = (start == pos) ? 1 : Integer.parseInt(formula.substring(start, pos));

                counts.merge(name, count, Integer::sum);
            }
        }
        return counts;
    }

    // ========================================================================
    // APPROACH 3 (OPTIMAL): ITERATIVE, SINGLE PASS, EXPLICIT STACK
    //                         O(n) time, O(n) space
    // Pattern: Stack-Based Iterative Parsing (Explicit Stack of Partial Results)
    // ========================================================================
    // Key insight: replace the JVM call stack with our own explicit
    // Deque<Map<String,Integer>>. Push a fresh map when we see '(', pop and
    // fold it into the new top-of-stack (scaled by any trailing multiplier)
    // when we see ')'. Everything else — parsing an element token — just
    // updates whatever map is currently on top.
    //
    // Trade-off versus Intermediate: identical O(n) time complexity, but
    // because the "recursion" is now just loop iterations manipulating our
    // own heap-allocated stack, there is no JVM call-stack depth limit to
    // worry about — this version is safe no matter how deeply the formula
    // nests, which is what makes it the production-grade, optimal choice.
    public String countOfAtomsOptimal(String formula) {
        int n = formula.length();
        Deque<Map<String, Integer>> stack = new ArrayDeque<>();
        stack.push(new TreeMap<>()); // top-level group

        int i = 0;
        while (i < n) {
            char c = formula.charAt(i);

            if (c == '(') {
                stack.push(new TreeMap<>()); // start a fresh group on the stack
                i++;
            } else if (c == ')') {
                Map<String, Integer> finishedGroup = stack.pop(); // this group is done
                i++;

                int start = i;
                while (i < n && Character.isDigit(formula.charAt(i))) i++;
                int multiplier = (start == i) ? 1 : Integer.parseInt(formula.substring(start, i));

                Map<String, Integer> parent = stack.peek(); // fold scaled counts into the enclosing group
                for (Map.Entry<String, Integer> entry : finishedGroup.entrySet()) {
                    parent.merge(entry.getKey(), entry.getValue() * multiplier, Integer::sum);
                }
            } else {
                int start = i;
                i++; // leading uppercase letter
                while (i < n && Character.isLowerCase(formula.charAt(i))) i++;
                String name = formula.substring(start, i);

                start = i;
                while (i < n && Character.isDigit(formula.charAt(i))) i++;
                int count = (start == i) ? 1 : Integer.parseInt(formula.substring(start, i));

                stack.peek().merge(name, count, Integer::sum);
            }
        }

        return buildFormulaString(stack.pop());
    }

    // ========================================================================
    // SHARED HELPER — formats a TreeMap of element -> count into the
    // required output string: alphabetical order, count omitted when 1.
    // ========================================================================
    private String buildFormulaString(Map<String, Integer> counts) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) { // TreeMap already iterates alphabetically
            sb.append(entry.getKey());
            if (entry.getValue() > 1) {
                sb.append(entry.getValue());
            }
        }
        return sb.toString();
    }

    // ========================================================================
    // MAIN — runs every approach against the problem's own examples plus a
    // handful of edge cases, with clearly labeled, side-by-side output.
    // ========================================================================
    public static void main(String[] args) {
        NumberOfAtomsLC726 solution = new NumberOfAtomsLC726();

        String[] formulas = {
            "H2O",                  // basic example -> "H2O"
            "Mg(OH)2",               // single nested group -> "H2MgO2"
            "K4(ON(SO3)2)2",         // double-nested groups -> "K4N2O14S4"
            "H",                     // edge case: single atom, no digits at all -> "H"
            "(((H2)3)4)5",            // edge case: chained multipliers across 3 nesting levels -> "H120"
            "H2O(H2O)2",             // edge case: same element split across concatenated pieces -> "H6O3"
            "Fe2O3"                  // edge case: two-letter element name -> "Fe2O3"
        };

        for (String formula : formulas) {
            System.out.println("Formula: " + formula);
            System.out.println("  Brute Force:   " + solution.countOfAtomsBruteForce(formula));
            System.out.println("  Intermediate:  " + solution.countOfAtomsIntermediate(formula));
            System.out.println("  Optimal:       " + solution.countOfAtomsOptimal(formula));
            System.out.println();
        }

        // Note: every test above stays well within formula.length() <= 1000,
        // so none of the three approaches actually fails here. The Brute
        // Force approach's O(n^2) cost and the Intermediate approach's
        // recursion-depth risk are real concerns only at the upper end of
        // the constraint range (e.g. a formula built almost entirely of
        // nested parentheses) — called out above in comments rather than
        // forced into a live failing test, since LeetCode guarantees valid,
        // bounded-length input.
    }
}

/*
 * ============================================================================
 * PATTERN RECOGNITION
 * ============================================================================
 * Whenever a problem hands you a string with NESTED, BRACKETED structure
 * where something (a multiplier, an operator, a scope) needs to apply to
 * everything inside a bracket pair, reach for one of two tools:
 *   - an explicit stack of "in-progress results," one frame per open
 *     bracket, pushed on '(' / '[' / '{' and popped + folded into the
 *     parent on ')' / ']' / '}', OR
 *   - recursion with a single shared/returned position pointer, where each
 *     recursive call parses one bracket's contents and returns control
 *     (and its current position) to the caller.
 * Avoid the brute-force trap of re-scanning for matching brackets and/or
 * slicing new substrings at every level — that's the most common way this
 * pattern accidentally becomes O(n^2).
 *
 * COMMON FOLLOW-UP VARIANTS
 * --------------------------
 *   - LeetCode 394, "Decode String" — same nested-bracket-with-multiplier
 *     structure, but decoding repeated substrings instead of summing atom
 *     counts. The stack-of-partial-results technique transfers directly.
 *   - LeetCode 224 / 227 / 772, "Basic Calculator I/II/III" — parsing
 *     arithmetic expressions with parentheses; same shared-index recursive
 *     descent or explicit-stack technique, generalized to +, -, *, /.
 *   - A natural interview follow-up for THIS problem: "what if the formula
 *     could be invalid?" — would require adding validation (unmatched
 *     parentheses, malformed element names, digit-only multipliers) before
 *     or during the same parse, typically by tracking depth and rejecting
 *     early on any inconsistency.
 * ============================================================================
 */
