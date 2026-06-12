# Time and Space Complexity – Interview Notes

## 1. Introduction
- Computers have limited resources (CPU cycles, memory).
- **Algorithm analysis** provides a mathematical model to evaluate efficiency independent of hardware.
- Two key measures:
  - **Time Complexity** → How runtime grows with input size.
  - **Space Complexity** → How memory usage grows with input size.

---

## 2. Time Complexity

### Big-O Notation
- Describes **upper bound** of runtime growth.
- Ignores constants and lower-order terms.
- Common complexities:
  - **O(1)** → Constant time (e.g., accessing array element).
  - **O(log N)** → Logarithmic time (e.g., binary search).
  - **O(N)** → Linear time (e.g., linear search).
  - **O(N log N)** → Linearithmic (e.g., efficient sorting like Merge Sort, Quick Sort average).
  - **O(N²)** → Quadratic (e.g., nested loops, Bubble Sort).
  - **O(N³)** → Cubic (e.g., triple nested loops).
  - **O(2^N)** → Exponential (e.g., recursive subset generation).
  - **O(N!)** → Factorial (e.g., brute-force traveling salesman).

**Growth Comparison Example (N = 100):**
- O(log N) → ~10 operations
- O(N) → 100 operations
- O(N log N) → ~1000 operations
- O(N²) → 10,000 operations
- O(2^N) → ~10^30 operations

---

### Cases
- **Best Case** → Minimum operations (e.g., searching first element).
- **Average Case** → Expected operations over random inputs.
- **Worst Case** → Maximum operations (e.g., searching last element).

**Interview Tip:** Worst-case analysis is most common in interviews.

---

## 3. Space Complexity
- Measures memory usage of an algorithm.
- Components:
  - **Fixed Part** → Independent of input size (constants, program code).
  - **Variable Part** → Depends on input size (arrays, recursion stack).
- Examples:
  - **O(1)** → Constant space (e.g., iterative algorithms).
  - **O(N)** → Linear space (e.g., recursion depth, dynamic arrays).

---

## 4. Recursion and Complexity
- Recursion adds **extra stack frames** → increases space complexity.
- Example:
  - Factorial recursion → O(N) space (stack depth).
  - Iterative factorial → O(1) space.

---

## 5. Comparison of Complexities
- **O(1) < O(log N) < O(N) < O(N log N) < O(N²) < O(N³) < O(2^N) < O(N!)**
- HashMap lookup → O(1) average.
- Binary Search → O(log N).
- Linear Search → O(N).

---

## 6. Interview Angles

### Common Questions
- Difference between **time complexity** and **space complexity**.
- Why constants are ignored in Big-O.
- Best vs Worst case analysis.
- Compare recursion vs iteration in terms of space.
- Explain **Big-O, Big-Theta, Big-Omega**:
  - **O (Big-O)** → Upper bound.
  - **Ω (Big-Omega)** → Lower bound.
  - **Θ (Big-Theta)** → Tight bound (exact growth rate).

### Practical Examples
- Sorting algorithms:
  - Bubble Sort → O(N²).
  - Merge Sort → O(N log N).
- Searching:
  - Linear Search → O(N).
  - Binary Search → O(log N).
- Hashing:
  - Lookup → O(1) average, O(N) worst case (collisions).

---

## 7. Key Takeaways
- Time complexity measures **speed**, space complexity measures **memory**.
- Big-O notation focuses on **scalability**.
- Worst-case analysis is most important for interviews.
- Recursion often increases space usage due to stack frames.
- Understanding trade-offs between time and space is critical.