# Library-Management-System
This program simulates a library system that manages books, members, and borrowing transactions using object-oriented design, handling availability, member rules, and fine calculations.


# Library Management System

## Overview
This Java program simulates a **Library Management System** that handles:

- Book storage and management
- Member registration and management
- Borrowing and returning of books
- Fine calculation for overdue books

It demonstrates **Object-Oriented Programming (OOP)** concepts such as **encapsulation, inheritance, polymorphism, and abstraction**.

---

## Features

### 1. Book Management
- Add new books with unique IDs, title, author, and total copies.
- Track available copies for borrowing.
- Display book information.

### 2. Member Management
Supports two types of members:
- **StudentMember**: Borrow limit 3, fine \$1 per overdue day.
- **FacultyMember**: Borrow limit 5, fine \$0.5 per overdue day.
- Track borrowed books per member.

### 3. Borrowing & Returning
- Borrow a book if copies are available and member has not exceeded the limit.
- Return a book and calculate fine based on overdue days.
- Display transaction results in the console.

### 4. Repository Abstraction
- `Repository` interface abstracts data storage for books and members.
- `InMemoryRepository` uses `HashMap` for in-memory storage.
- Easily replaceable with database or JDBC later.

### 5. Library Service
- Handles all business logic.
- Processes borrow and return requests.
- Displays borrowing summary.

---

## Class Structure

| Class / Interface      | Purpose |
|------------------------|---------|
| `Repository`           | Interface for storing books and members. |
| `InMemoryRepository`   | Implements `Repository` using `HashMap`. |
| `Book`                 | Encapsulates book information and borrowing logic. |
| `Member` (abstract)    | Base class for members with common behavior. |
| `StudentMember`        | Student member with lower borrow limit, higher fine. |
| `FacultyMember`        | Faculty member with higher borrow limit, lower fine. |
| `LibraryService`       | Handles borrowing, returning, and summary display. |
| `LibraryManagement`    | Main class to read input, create objects, and execute transactions. |
