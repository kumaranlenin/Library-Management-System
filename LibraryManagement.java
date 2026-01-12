import java.util.*;

/*
 ============================================================
 Interface: Repository
 ------------------------------------------------------------
 Purpose:
 - Abstracts storage of books and members
 - Allows easy replacement with database/JDBC later
 ============================================================
*/
interface Repository {
    void addBook(Book book);
    Book getBookById(String bookId);

    void addMember(Member member);
    Member getMemberById(String memberId);
}

/*
 ============================================================
 Class: InMemoryRepository
 ------------------------------------------------------------
 Purpose:
 - Concrete implementation of Repository
 - Uses HashMaps to store books and members in memory
 ============================================================
*/
class InMemoryRepository implements Repository {

    // Stores books using BookID as key
    private Map<String, Book> books = new HashMap<>();

    // Stores members using MemberID as key
    private Map<String, Member> members = new HashMap<>();

    @Override
    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }

    @Override
    public Book getBookById(String bookId) {
        return books.get(bookId);
    }

    @Override
    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    @Override
    public Member getMemberById(String memberId) {
        return members.get(memberId);
    }
}

/*
 ============================================================
 Class: Book
 ------------------------------------------------------------
 Purpose:
 - Encapsulates book information
 - Tracks availability and borrowing status
 ============================================================
*/
class Book {
    private String bookId;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    // Constructor
    public Book(String bookId, String title, String author, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    // Attempts to borrow the book
    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    // Returns the book
    public void returnBook() {
        availableCopies++;
    }

    // Displays book details
    public void displayBookInfo() {
        System.out.println(
                "BookID: " + bookId +
                        ", Title: " + title +
                        ", Author: " + author +
                        ", Available: " + availableCopies + "/" + totalCopies
        );
    }

    // Getters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }
}

/*
 ============================================================
 Abstract Class: Member
 ------------------------------------------------------------
 Purpose:
 - Base class for all members
 - Defines common behavior
 ============================================================
*/
abstract class Member {
    protected String memberId;
    protected String name;
    protected List<Book> borrowedBooks;

    // Constructor
    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    // Abstract methods (Polymorphism)
    public abstract int getMaxBorrowLimit();
    public abstract double calculateFine(int overdueDays);

    // Borrow a book
    public boolean borrowBook(Book book) {
        if (borrowedBooks.size() >= getMaxBorrowLimit()) {
            System.out.println("Borrowing failed: Borrow limit reached.");
            return false;
        }

        if (book.borrowBook()) {
            borrowedBooks.add(book);
            System.out.println("Borrowing Successful");
            return true;
        } else {
            System.out.println("Borrowing failed: Book unavailable.");
            return false;
        }
    }

    // Return a book and calculate fine
    public boolean returnBook(Book book, int overdueDays) {
        if (borrowedBooks.remove(book)) {
            book.returnBook();
            double fine = calculateFine(overdueDays);

            System.out.println("Returned Book: " + book.getTitle());
            System.out.println("Overdue Days: " + overdueDays);
            System.out.println("Fine: $" + fine);
            return true;
        }
        System.out.println("Return failed: Book not found.");
        return false;
    }

    // Getters
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}

/*
 ============================================================
 Class: StudentMember
 ------------------------------------------------------------
 Purpose:
 - Represents student members
 - Lower borrow limit, higher fine
 ============================================================
*/
class StudentMember extends Member {

    public StudentMember(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaxBorrowLimit() {
        return 3;
    }

    @Override
    public double calculateFine(int overdueDays) {
        return overdueDays * 1.0;
    }
}

/*
 ============================================================
 Class: FacultyMember
 ------------------------------------------------------------
 Purpose:
 - Represents faculty members
 - Higher borrow limit, lower fine
 ============================================================
*/
class FacultyMember extends Member {

    public FacultyMember(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaxBorrowLimit() {
        return 5;
    }

    @Override
    public double calculateFine(int overdueDays) {
        return overdueDays * 0.5;
    }
}

/*
 ============================================================
 Class: LibraryService
 ------------------------------------------------------------
 Purpose:
 - Handles all business logic
 ============================================================
*/
class LibraryService {
    private Repository repository;

    public LibraryService(Repository repository) {
        this.repository = repository;
    }

    // Handles borrow requests
    public void borrowBook(String memberId, String bookId) {
        Member member = repository.getMemberById(memberId);
        Book book = repository.getBookById(bookId);

        if (member == null || book == null) {
            System.out.println("Invalid member or book ID.");
            return;
        }

        System.out.println("\nMember: " + member.getName() +
                " (" + member.getClass().getSimpleName().replace("Member", "") + ")");
        System.out.println("Borrowed Book: " + book.getTitle());

        member.borrowBook(book);
    }

    // Handles return requests
    public void returnBook(String memberId, String bookId, int overdueDays) {
        Member member = repository.getMemberById(memberId);
        Book book = repository.getBookById(bookId);

        if (member == null || book == null) {
            System.out.println("Invalid member or book ID.");
            return;
        }

        System.out.println("\nMember: " + member.getName() +
                " (" + member.getClass().getSimpleName().replace("Member", "") + ")");
        member.returnBook(book, overdueDays);
    }

    // Displays summary
    public void displaySummary() {
        System.out.println("\nBOOK BORROWING SUMMARY");
    }
}

/*
 ============================================================
 Main Class: LibraryManagement
 ------------------------------------------------------------
 Purpose:
 - Reads input
 - Creates objects
 - Executes transactions
 ============================================================
*/
public class LibraryManagement {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Repository repository = new InMemoryRepository();
        LibraryService service = new LibraryService(repository);

        // -------------------------
        // Input: Books
        // -------------------------
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("done")) break;

            String[] parts = line.split(" ");
            repository.addBook(new Book(
                    parts[0],
                    parts[1],
                    parts[2],
                    Integer.parseInt(parts[3])
            ));
        }

        // -------------------------
        // Input: Members
        // -------------------------
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("done")) break;

            String[] parts = line.split(" ");
            String memberId = parts[0];
            String name = parts[1];
            String type = parts[2];

            if (type.equalsIgnoreCase("Student")) {
                repository.addMember(new StudentMember(memberId, name));
            } else {
                repository.addMember(new FacultyMember(memberId, name));
            }
        }

        // -------------------------
        // Input: Transactions
        // -------------------------
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("done")) break;

            String[] parts = line.split(" ");
            String memberId = parts[0];
            String bookId = parts[1];
            String operation = parts[2];

            if (operation.equalsIgnoreCase("borrow")) {
                service.borrowBook(memberId, bookId);
            } else {
                int overdueDays = Integer.parseInt(parts[3]);
                service.returnBook(memberId, bookId, overdueDays);
            }
        }

        service.displaySummary();
        scanner.close();
    }
}
