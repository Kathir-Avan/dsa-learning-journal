# LeetCode Problem-Solving Prompt Template

A reusable prompt you can paste into Claude (or any AI assistant) alongside any LeetCode problem statement. It forces a structured walkthrough — brute force first, then progressively better solutions — so you build real intuition instead of just copying an optimal answer.

## How to use it

1. Attach this `.md` file to your conversation with your AI assistant.
2. Along with it, just give the LeetCode problem number and the question text (you don't need to retype the template each time — the assistant will follow the structure in this file).
3. The assistant will walk through the problem in Java using the structure below.

---

## Prompt Template

```
I'm practicing LeetCode and want to deeply understand this problem, not just see the final answer.
Walk me through it in Java using the structure below. Be beginner-friendly: explain the
"why" in plain English before showing code, but don't dumb down the algorithmic content.

PROBLEM NUMBER: [LeetCode problem number]
PROBLEM:
[paste the full problem statement, constraints, and examples here]

Please structure your response as follows:

1. PROBLEM BREAKDOWN
   - Restate the problem in your own words
   - Identify input/output, constraints, and edge cases (empty input, duplicates, negative
     numbers, single element, max constraint size, etc.)
   - Name the underlying pattern(s) if recognizable (e.g., two pointers, sliding window,
     binary search, DP, graph traversal, greedy, backtracking)

2. BRUTE FORCE APPROACH
   - Plain-English explanation of the naive idea
   - Time and space complexity, with reasoning (not just the final Big-O)
   - Working code with comments explaining each step
   - Why this approach is inefficient (what specifically causes the slowdown)

3. INTERMEDIATE / BETTER APPROACH (if one exists between brute force and optimal)
   - What insight unlocks this improvement over brute force
   - Time and space complexity
   - Working code
   - Why this still isn't optimal

4. OPTIMIZED APPROACH
   - The key insight or trick that gets to the best known complexity
   - Time and space complexity, with reasoning
   - Working code with comments
   - Trade-offs versus the previous approaches (e.g., extra space for less time)

5. COMPARISON TABLE
   - A table summarizing all approaches discussed: name, time complexity, space complexity,
     one-line trade-off note

6. ADVANCED / HIGHER-LEVEL CONSIDERATIONS
   - Are there multiple equally-optimal approaches using different techniques (e.g., DP vs
     greedy vs two pointers)? Briefly contrast them.
   - How would the solution change under different constraints (e.g., if input is a stream,
     doesn't fit in memory, needs to be thread-safe, or is queried repeatedly — precompute/
     caching strategies)?
   - Is there a known follow-up variant of this problem (common interview follow-ups)? How
     would the approach adapt?
   - Any relevant real-world system design parallel (e.g., LRU cache problems relating to
     actual caching systems)?

7. PATTERN RECOGNITION NOTE
   - One or two sentences on how to recognize this pattern in future problems, so the
     technique transfers rather than being memorized for this question alone.

Keep code clean and idiomatic for Java. Avoid unnecessary libraries unless they're
the idiomatic/standard way to solve it.
```

---

## Tips for getting the most out of this

- **Don't skip the brute force step**, even if you can already see the optimal solution. Understanding *why* it's slow is what makes the optimization stick.
- **Ask "why" follow-ups.** If a step in the optimized approach isn't clear, ask the assistant to expand just that step rather than re-running the whole prompt.
- **Try coding it yourself first** after reading the brute force and intermediate sections, before looking at the optimized code — this builds problem-solving muscle instead of pattern memorization.
- **Revisit problems** after a few days using just the "Pattern Recognition Note" as a hint, to test retention.
