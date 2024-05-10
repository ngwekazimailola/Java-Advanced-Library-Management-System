import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryManagementSystem {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private static final String DATA_FILE_PATH = "LibraryData.txt";

    public LibraryManagementSystem() {
        // Books in library
        books.add(new Book(1, "Encyclopedia Of Life", "Miles Kelly", 2017, true));
        books.add(new Book(2, "Matilda", "Roald Dahl", 1988, true));
        books.add(new Book(3, "Diary of a Wimpy Kid", "Jeff Kinney", 2007, true));
        books.add(new Book(4, "Secrets of a Champion", "Jannie Putter", 2004, true));
        books.add(new Book(5, "Fallen", "Lauren Kate", 2009, true));
        books.add(new Book(6, "One Hundred Years of Solitude", "Gabriel García Márquez", 1967, true));
        books.add(new Book(7, "The Great Gatsby", "F. Scott Fitzgerald", 1925, true));
        books.add(new Book(8, "To Kill a Mockingbird", "Harper Lee", 1960, true));
        books.add(new Book(9, "The Birth of Tragedy", "Friedrich Nietzsche", 1872, true));

        // Library members
        members.add(new Member("Thabo Maputla", "thabo.maputla@gmail.com", ""));
        members.add(new Member("Elsa Montana", "elsa.montana@outlook.com", ""));
        members.add(new Member("Jake Le Roux", "jake.ler@outlook.com", ""));
        members.add(new Member("Mbali Smith", "smith.mbali@gmail.com", ""));
        members.add(new Member("Sipho Mbeki", "sipho.be@gmail.com", ""));
        members.add(new Member("Jan Smit", "jan.smit@gmail.com", ""));
    }

    // Main Method
    public static void main(String[] args) {
        LibraryManagementSystem librarySystem = new LibraryManagementSystem();
        librarySystem.loadLibraryData();
        librarySystem.startBackgroundTasks();
        librarySystem.run();
        librarySystem.saveLibraryData();
    }

    public void run() {
        System.out.println("Library Management System");
        while (true) {
            // Library Menu
            System.out.println("1. Manage books ");
            System.out.println("2. Manage members ");
            System.out.println("3. Add new book ");
            System.out.println("4. Search for book by title or author");
            System.out.println("5. Register new member");
            System.out.println("6. Check out book ");
            System.out.println("7. View due dates and fines");
            System.out.println("8. Exit ");
            // Read user input
            int choice = getUserChoice();
            // Actions based on user choice
            switch (choice) {
                case 1:
                    manageBooks();
                    break;
                case 2:
                    manageMembers();
                    break;
                case 3:
                    addNewBook();
                    break;
                case 4:
                    searchBook();
                    break;
                case 5:
                    registerMember();
                    break;
                case 6:
                    checkoutBook();
                    break;
                case 7:
                    viewDueDatesAndFines();
                    break;
                case 8:
                    System.out.println("Exiting Program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    break;
            }
        }
    }

    private int getUserChoice() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }


    // Manage Books Method
    private void manageBooks() {
        System.out.println("Manage books ");
        System.out.println("1. Update book ");
        System.out.println("2. Delete book ");
        System.out.println("3. Display all books ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }
        switch (choice) {
            case 1:
                updateBook();
                break;
            case 2:
                deleteBook();
                break;
            case 3:
                displayAllBooks();
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                break;
        }
    }

    // Update Books Method
    private void updateBook() {
        System.out.println("Enter the ISBN of the book to update: ");
        int bookISBNToUpdate;
        try {
            bookISBNToUpdate = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid book ISBN.");
            return;
        }
        // Finding the book in the list
        Book bookToUpdate = null;
        for (Book book : books) {
            if (book.getBookISBN() == bookISBNToUpdate) {
                bookToUpdate = book;
                break;
            }
        }
        if (bookToUpdate == null) {
            System.out.println("Book searched not found.");
            return;
        }
        // Prompt for new details
        System.out.println("Enter new details for the book:");
        System.out.print("Title: ");
        String newTitle = scanner.nextLine();
        System.out.print("Author: ");
        String newAuthor = scanner.nextLine();
        System.out.print("Year: ");
        int newYear;
        try {
            newYear = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for Year. Please enter a valid number.");
            return;
        }
        // Updating the book properties
        bookToUpdate.setTitle(newTitle);
        bookToUpdate.setAuthor(newAuthor);
        bookToUpdate.setYear(newYear);
        System.out.println("Book updated successfully.");
    }

    // Delete Books Method
    private void deleteBook() {
        System.out.println("Enter the ISBN of the book to delete: ");
        int bookISBNToDelete;
        try {
            bookISBNToDelete = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid book ISBN.");
            return;
        }
        // Finding the book in the list
        Book bookToDelete = null;
        for (Book book : books) {
            if (book.getBookISBN() == bookISBNToDelete) {
                bookToDelete = book;
                break;
            }
        }
        if (bookToDelete == null) {
            System.out.println("Book not found.");
            return;
        }
        // Remove the book
        books.remove(bookToDelete);
        System.out.println("Book deleted successfully.");
    }

    // Display All Books Method
    private void displayAllBooks() {
        System.out.println("All Books:");
        for (Book book : books) {
            System.out.println("ISBN: " + book.getBookISBN());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Year: " + book.getYear());
            System.out.println("Available: " + (book.isAvailable() ? "Yes" : "No"));
            System.out.println();
        }
    }

    // Manage Members Method
    private void manageMembers() {
        System.out.println("Manage members:");
        System.out.println("1. Display all members");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }
        switch (choice) {
            case 1:
                listAllMembers();
                break;
            default:
                System.out.println("Invalid choice. Please enter 1");
                break;
        }
    }

    // List All Members Method
    private void listAllMembers() {
        System.out.println("All Members:");
        for (Member member : members) {
            System.out.println(member);
            System.out.println();
        }
    }

    // Add New Book Method
    private void addNewBook() {
        System.out.println("\nEnter book details:");
        System.out.print("Book ISBN: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for Book ISBN. Please enter a valid number.");
            return;
        }
        System.out.print("Title: ");
        String newTitle = scanner.nextLine();
        System.out.print("Author: ");
        String newAuthor = scanner.nextLine();
        System.out.print("Year: ");
        int newYear;
        try {
            newYear = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for Year. Please enter a valid number.");
            return;
        }
        Book newBook = new Book(bookId, newTitle, newAuthor, newYear, true);
        books.add(newBook);
        System.out.println("Book added successfully.");
    }

    // Search Book Method
    private void searchBook() {
        System.out.println("Search for book by title or author:");
        String searchQuery = scanner.nextLine().toLowerCase(); // Converting user input to lowercase for case-insensitive search
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchQuery) || book.getAuthor().toLowerCase().contains(searchQuery)) {
                System.out.println("Book found:");
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Year: " + book.getYear());
                System.out.println("Available: " + (book.isAvailable() ? "Yes" : "No"));
                found = true;
            }
        }
        if (!found) {
            System.out.println("Book not found.");
        }
    }

    // Register Member Method
    private void registerMember() {
        System.out.println("Register new member:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        String email;
        do {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        } while (!isValidEmail(email));
        // Creating a new member
        Member newMember = new Member(name, email, "");
        members.add(newMember);
        System.out.println("Member registered successfully.");
    }

    // Checkout Book Method
    private void checkoutBook() {
        System.out.println("Check out book:");
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();
        // Find the member
        Member member = null;
        for (Member m : members) {
            if (m.getEmail().equals(email)) {
                member = m;
                break;
            }
        }
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        // Prompt for book ISBN
        System.out.print("Enter book ISBN to check out: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for Book ISBN. Please enter a valid number.");
            return;
        }
        // Find the book
        Book bookToCheckOut = null;
        for (Book book : books) {
            if (book.getBookISBN() == bookId && book.isAvailable()) {
                bookToCheckOut = book;
                break;
            }
        }
        if (bookToCheckOut == null) {
            System.out.println("Book not found or not available for checkout!");
            return;
        }
        // Updating member's borrowed book and book availability
        member.setBorrowedBook(bookToCheckOut.getTitle());
        bookToCheckOut.setAvailable(false);
        System.out.println("Book checked out successfully :)");
    }


    // View Due Dates And Fines Method
    private void viewDueDatesAndFines() {
        System.out.println("View due dates and fines:");

        // Process overdue fines
        processOverdueFines();

        // Print due dates and fines
        boolean anyDueDates = false;
        for (Member member : members) {
            if (!member.getBorrowedBook().isEmpty()) {
                System.out.println("Member: " + member.getName());
                System.out.println("Borrowed Book: " + member.getBorrowedBook());
                // Calculate Due Date
                LocalDate dueDate = LocalDate.now().plusDays(14);
                System.out.println("Due Date: " + dueDate);
                // Calculate Overdue Fines
                LocalDate currentDate = LocalDate.now();
                if (currentDate.isAfter(dueDate)) {
                    long daysOverdue = currentDate.toEpochDay() - dueDate.toEpochDay();
                    double fineAmount = calculateFine(daysOverdue); // Calculate fine amount
                    System.out.println("Overdue Days: " + daysOverdue);
                    System.out.println("Fine Amount: R" + fineAmount);
                    anyDueDates = true;
                } else {
                    System.out.println("Status: Not Overdue");
                }
                System.out.println();
            }
        }

        if (!anyDueDates) {
            System.out.println("No overdue fines and dates to display.");
        }

        // Add an option to return to the main menu
        System.out.println("Press 0 to return to the main menu.");
        int choice = getUserChoice();
        if (choice == 0) {
            return;
        }
    }


    private double calculateFine(long daysOverdue) {
        return daysOverdue * 5.0;
    }

    private void startBackgroundTasks() {
        executorService.scheduleAtFixedRate(this::sendNotifications, 0, 1, TimeUnit.DAYS);
    }

    private void processOverdueFines() {
        System.out.println("Processing Overdue Fines");
        LocalDate currentDate = LocalDate.now();

        boolean anyDueDates = false;

        for (Member member : members) {
            if (!member.getBorrowedBook().isEmpty()) {
                LocalDate dueDate = LocalDate.now().plusDays(14);
                long daysOverdue = daysBetween(dueDate, currentDate);
                if (daysOverdue > 0) {
                    anyDueDates = true;
                    double fineAmount = calculateFine(daysOverdue);
                    System.out.println("Fine for: " + member.getName() + ": R" + fineAmount);
                    member.updateFine(fineAmount);
                }
            }
        }

        if (!anyDueDates) {
            System.out.println("No overdue fines and dates to process.");
        }
    }


    private long daysBetween(LocalDate startDate, LocalDate endDate) {
        long days = 0;
        while (!startDate.isEqual(endDate)) {
            startDate = startDate.plusDays(1);
            days++;
        }
        return days;
    }

    private void sendNotifications() {
        for (Member member : members) {
            if (!member.getBorrowedBook().isEmpty()) {
                LocalDate dueDate = LocalDate.now().plusDays(14);
                LocalDate currentDate = LocalDate.now();
                if (currentDate.isAfter(dueDate)) {
                    System.out.println("Notification being sent to: " + member.getName() + " for overdue book: " + member.getBorrowedBook());
                }
            }
        }
    }
    private void loadLibraryData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
            String line;
            boolean readingBooksData = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("")) {
                    continue;
                }
                if (line.equals("Books Data:")) {
                    reader.readLine();
                    readingBooksData = true;
                    continue;
                }
                if (line.equals("Members Data:")) {
                    break;
                }
                if (readingBooksData) {
                    String[] bookData = line.split(",");
                    int bookISBN = Integer.parseInt(bookData[0]);
                    String title = bookData[1];
                    String author = bookData[2];
                    int year = Integer.parseInt(bookData[3]);
                    boolean available = Boolean.parseBoolean(bookData[4]);
                    books.add(new Book(bookISBN, title, author, year, available));
                }
            }
            // Read members data
            System.out.println("Data was successfully loaded!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void saveLibraryData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE_PATH))) {
            // Write books data
            writer.println("Books Data:");
            writer.println("BookISBN,Title,Author,Year,Available");
            for (Book book : books) {
                writer.println(book.getBookISBN() + "," +
                        book.getTitle() + "," +
                        book.getAuthor() + "," +
                        book.getYear() + "," +
                        book.isAvailable());
            }
            // Write members data
            writer.println("\nMembers Data:");
            writer.println("Name,Email,BorrowedBook,FineAmount");
            for (Member member : members) {
                writer.println(member.getName() + "," +
                        member.getEmail() + "," +
                        member.getBorrowedBook() + "," + "R" +
                        member.getFineAmount());
            }
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    static class Book implements Serializable {
        private int bookISBN;
        private String title;
        private String author;
        private int year;
        private boolean available;

        public Book(int bookISBN, String title, String author, int year, boolean available) {
            this.bookISBN = bookISBN;
            this.title = title;
            this.author = author;
            this.year = year;
            this.available = available;
        }

        public int getBookISBN() {
            return bookISBN;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }

    static class Member implements Serializable {
        private String name;
        private String email;
        private String borrowedBook;
        private double fineAmount;

        public Member(String name, String email, String borrowedBook) {
            this.name = name;
            this.email = email;
            this.borrowedBook = borrowedBook;
            this.fineAmount = 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBorrowedBook() {
            return borrowedBook;
        }

        public void setBorrowedBook(String borrowedBook) {
            this.borrowedBook = borrowedBook;
        }

        public String toString() {
            return "Name: " + name + "\nEmail: " + email + "\nBorrowed Book: " + borrowedBook;
        }

        public double getFineAmount() {
            return fineAmount;
        }

        public void updateFine(double fine) {
            fineAmount += fine;
        }
    }
}
