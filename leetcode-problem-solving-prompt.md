# LeetCode Problem-Solving Prompt Template

A reusable prompt you can paste into Claude (or any AI assistant) alongside any LeetCode problem statement. It instructs the assistant to generate a single, runnable, well-commented **.java study file** — problem explanation at the top, then brute force, then optimized (and intermediate, if one exists) approaches, all wired up in a `main()` method you can compile and run immediately.

## How to use it

1. Attach this `.md` file to your conversation with your AI assistant.
2. Along with it, just give the LeetCode problem number and the question text (you don't need to retype the template each time — the assistant will follow the structure in this file).
3. The assistant will generate a complete `.java` file you can save, compile, and run.

---

## Prompt Template

```
I'm practicing LeetCode and want a single, well-commented Java file I can save and run to
study this problem — not just a chat explanation. Be beginner-friendly: explain the "why"
in plain English inside comments, but don't dumb down the algorithmic content.

PROBLEM NUMBER: [LeetCode problem number]
PROBLEM:
[paste the full problem statement, constraints, and examples here]

Generate a single Java file (use create_file, name it after the problem, e.g.
ProblemNameLC<number>.java) structured exactly as follows:

1. TOP-OF-FILE JAVADOC COMMENT BLOCK
   - Restate the problem in plain English
   - Include the given examples (input -> output) and constraints
   - Note the edge cases worth testing (empty input, duplicates, negative numbers, single
     element, max constraint size, etc.)
   - Name the underlying pattern(s) if recognizable (e.g., two pointers, sliding window,
     binary search, DP, graph traversal, greedy, backtracking)
   - Include a complexity comparison table (approach name, time, space, one-line note) for
     every approach implemented in the file

2. BRUTE FORCE METHOD
   - A method named with a `BruteForce` suffix (e.g., `solveBruteForce`)
   - A comment block directly above it explaining the naive idea in plain English, why it's
     correct, and specifically what causes it to be slow
   - Inline comments on the non-obvious lines inside the method body

3. INTERMEDIATE METHOD (only if a genuinely distinct approach exists between brute force
   and optimal — skip this section entirely if not, don't force one in)
   - A method named with an appropriate descriptive suffix
   - A comment block above explaining the key insight that improves on brute force, and
     why it still isn't optimal
   - Inline comments on the non-obvious lines

4. OPTIMIZED METHOD
   - A method named with an `Optimal` suffix (e.g., `solveOptimal`)
   - A comment block above explaining the key insight/trick that achieves the best known
     complexity, and the trade-off versus the previous approach(es)
   - Inline comments on the non-obvious lines, especially anything tricky (overflow
     handling, off-by-one risks, bit manipulation, pointer movement, etc.)

5. MAIN METHOD — must actually run and demonstrate every approach above
   - Define the example inputs from the problem statement, plus at least 2-3 edge cases
   - Call every approach (brute force, intermediate if present, optimal) on the same inputs
     and print each result clearly labeled, so outputs can be compared side by side
   - If an approach would fail or be impractical on a particular edge case (e.g., brute
     force timing out conceptually, or recursion stack overflow on extreme input), add a
     guard with a printed note explaining why it's skipped, rather than silently omitting it
   - Keep print statements clean and labeled (e.g., "Brute Force: ", "Optimal: ")

6. CLOSING COMMENT BLOCK (after main, or at the very bottom of the file)
   - One or two sentences on how to recognize this pattern in future problems, so the
     technique transfers rather than being memorized for this question alone
   - Note any common interview follow-up variants of this problem and how the approach
     would need to adapt for them

Requirements:
- The file must compile and run as-is (single public class matching the filename, proper
  imports, no pseudocode).
- Keep code clean and idiomatic for Java. Avoid unnecessary libraries unless they're the
  idiomatic/standard way to solve it.
- Prioritize comments that explain WHY over comments that restate WHAT the code does.
- After generating the file, present it for download rather than pasting the full contents
  into the chat response.
```

---

## Tips for getting the most out of this

- **Compile and run it yourself** before reading the optimized method — try predicting the brute force output for each edge case first.
- **Don't skip the brute force method**, even if you can already see the optimal solution. Understanding *why* it's slow is what makes the optimization stick.
- **Ask "why" follow-ups.** If a comment in the optimized method isn't clear, ask the assistant to expand just that part rather than regenerating the whole file.
- **Keep a running folder** of these generated `.java` files, named by LeetCode number, as a personal study archive you can recompile and re-run before interviews.
- **Revisit problems** after a few days by deleting the optimized method and the call to it, then trying to rewrite it from scratch using only the comment block above it as a hint.
