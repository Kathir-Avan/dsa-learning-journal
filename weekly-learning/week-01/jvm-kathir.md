# Java Virtual Machine (JVM) – Interview Notes

## 1. JDK, JRE, and JVM
- **JDK (Java Development Kit)** → Development tools (compiler, debugger, JVM, libraries).
- **JRE (Java Runtime Environment)** → JVM + core libraries required to run Java applications.
- **JVM (Java Virtual Machine)** → Abstract machine that executes Java bytecode.

**Key Point:**  
JVM is platform-dependent (different implementations per OS), but bytecode is platform-independent → *Write Once, Run Anywhere*.

---

## 2. JVM Architecture

### Class Loader Subsystem
- **Loading** → Reads `.class` files into memory.
- **Linking**
  - Verification → Ensures bytecode correctness.
  - Preparation → Allocates memory for static variables.
  - Resolution → Converts symbolic references to direct references.
- **Initialization** → Assigns values to static variables.

**Types of Class Loaders**
- Bootstrap → Loads core classes (`JAVA_HOME/lib`).
- Extension → Loads classes from `JAVA_HOME/lib/ext`.
- Application → Loads classes from user classpath.

---

### JVM Memory Model
- **Method Area** → Class-level metadata, runtime constant pool.
- **Heap** → Objects; shared across threads.
  - **Young Generation** → Eden + Survivor spaces (short-lived objects).
  - **Old Generation** → Long-lived objects.
  - **Metaspace (Java 8+)** → Replaces PermGen; stores class metadata.
- **Stack** → Per-thread; stores method calls, local variables.
- **PC Register** → Current instruction address per thread.
- **Native Method Stack** → For native (C/C++) methods.

---

### Execution Engine
- **Interpreter** → Executes bytecode line by line.
- **JIT Compiler (Just-In-Time)** → Converts bytecode to native machine code for performance.
- **Garbage Collector (GC)** → Removes unused objects.
  - Algorithms: Serial, Parallel, CMS (Concurrent Mark-Sweep), G1 (Garbage First).

---

## 3. Java Native Interface (JNI)
- Enables Java to call native libraries (C, C++).
- Used for performance optimization and platform-specific features.

---

## 4. Common Interview Angles

### Differences
- **JDK vs JRE vs JVM** → Development vs Runtime vs Execution engine.
- **Compiler vs Interpreter** → Java compiler → bytecode; JVM interpreter/JIT → machine code.

### Garbage Collection
- Generational GC (Young vs Old).
- Stop-the-world events.
- G1 GC → region-based, reduces pause times.

### Performance
- JIT optimizations (inline caching, hotspot detection).
- Memory leaks in Java → caused by unreferenced but reachable objects.

### Security
- Bytecode verification ensures type safety.
- Sandboxing prevents unauthorized access.

---

## 5. Key Takeaways
- JVM abstracts hardware → portability.
- Memory model (Heap, Stack, Metaspace) is crucial for performance.
- GC strategies and JIT are common interview focus areas.
- JNI bridges Java with native code when needed.