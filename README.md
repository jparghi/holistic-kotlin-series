
# Holistic Kotlin Series

## 1. Introduction
The **Holistic Kotlin Series** is part of the *Holistic Learning Series* (Java • Python • Kotlin).  
This repository provides a step-by-step path to learn Kotlin — starting from core language fundamentals and progressing to advanced concepts such as data structures, concurrency, and project-based development.

Each section is designed for hands-on learning with real examples, tests, and best practices for writing clean, idiomatic Kotlin code.

---

## 2. Objectives
- Build a strong foundation in Kotlin syntax and semantics.
- Learn to implement practical data structures and algorithms.
- Understand concurrency and memory management.
- Apply test-driven and modular design principles.
- Develop production-quality Kotlin utilities and small projects.

---

## 3. Repository Structure

```

holistic-kotlin-series/
│
├── basics/
│   ├── Variables.kt
│   ├── Loops.kt
│   ├── Strings.kt
│   └── Collections.kt
│
├── advanced/
│   ├── Hierarchy.kt
│   ├── HierarchyTest.kt
│   ├── SimpleCache.kt
│   └── CodeReview.md
│
├── projects/
│   └── mini-tasks/
│       ├── Task1.kt
│       ├── Task2.kt
│       └── Task3.kt
│
├── build.gradle.kts
└── settings.gradle.kts

````

### Folder Description
| Folder | Description |
|---------|-------------|
| **basics/** | Core Kotlin topics such as variables, loops, and collections. |
| **advanced/** | Data structure and concurrency problems with test cases. |
| **projects/** | Small project implementations combining multiple concepts. |

---

## 4. Topics Covered
### 4.1 Kotlin Fundamentals
- Variables, constants, and types  
- Control flow (if, when, loops)  
- Functions and lambdas  
- Null safety and type inference  

### 4.2 Object-Oriented Programming
- Classes, interfaces, and inheritance  
- Data classes and sealed classes  
- Companion objects and extensions  

### 4.3 Collections and Functional Style
- Lists, sets, and maps  
- Higher-order functions (`map`, `filter`, `reduce`)  
- Immutability and scope functions (`let`, `apply`, `run`)  

### 4.4 Concurrency and Multithreading
- Coroutines and structured concurrency  
- Thread-safe data structures  
- Performance tuning  

### 4.5 Testing and Clean Code
- JUnit 5 test setup  
- Writing maintainable, idiomatic Kotlin  
- Clean architecture and code readability  

---

## 5. Build and Run

### 5.1 Prerequisites
- **Kotlin:** 1.9 or higher  
- **Java:** 17 or higher  
- **Gradle:** 8.x  
- **JUnit:** 5.x  

### 5.2 Commands
Build and run all tests using Gradle:

```bash
./gradlew build
./gradlew test
````

Or open directly in **IntelliJ IDEA** and run test configurations.

---

## 6. Example Modules

### 6.1 Hierarchy Module

Implements a forest-like data structure (`Hierarchy`) that can be filtered using a predicate.
Demonstrates algorithmic reasoning, depth traversal, and test-driven development.

### 6.2 SimpleCache Module

Analyzes and improves a concurrent cache implemented with `ConcurrentHashMap`.
Focuses on TTL logic, thread-safety, and memory management.

---

## 7. Related Repositories

* [Holistic Java Series](https://github.com/jparghi/holistic-java-series)
* [Holistic Python Basics](https://github.com/jparghi/holistic-python-basics)

---

## 8. Author

**Jigish Parghi**
Software Architect and Educator  
LinkedIn: [linkedin.com/in/jigishparghi](https://linkedin.com/in/jigishparghi)  
Email: [jigish.parghi@yahoo.ca](mailto:jigish.parghi@yahoo.ca) 
 
```
