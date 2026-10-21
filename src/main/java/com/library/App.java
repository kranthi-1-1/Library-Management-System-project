package com.library;

public class App {

    public static void main(String[] args) {

        var scanner = ConsoleInput.SCANNER;
        int choice = -1;

        do {
            System.out.println("\n====================================");
            System.out.println("   LIBRARY MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Display All Books");
            System.out.println("2. Search Books");
            System.out.println("3. Register Member");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. View Issue Records");
            System.out.println("7. Calculate Fine");
            System.out.println("8. Record and View Fines");
            System.out.println("9. Mark Fine as Paid");
            System.out.println("10. Check Book Availability");
            System.out.println("11. Add a New Book");
            System.out.println("12. Update Book Details");
            System.out.println("13. Delete a Book");
            System.out.println("14. View All Members");
            System.out.println("15. Search Members");
            System.out.println("16. View Member Borrowing History");
            System.out.println("17. View Currently Issued Books");
            System.out.println("18. View Overdue Books");
            System.out.println("19. View Library Statistics");
            System.out.println("20. Advanced Book Search");
            System.out.println("21. Search Book by ISBN");
            System.out.println("22. View Books by Category");
            System.out.println("0. Exit");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                if (!scanner.hasNextLine()) {
                    break;
                }
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    BookDAO.displayAllBooks();
                    break;

                case 2:
                    System.out.print("Enter book title to search: ");
                    String title = scanner.nextLine();
                    BookDAO.searchBook(title);
                    break;

                case 3:
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter phone number: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter department: ");
                    String department = scanner.nextLine();

                    MemberDAO.registerMember(
                            name, email, phone, department);
                    break;

                case 4:
                    System.out.print("Enter Member ID: ");
                    int memberId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Book ID: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Issue Date (YYYY-MM-DD): ");
                    String issueDate = scanner.nextLine();

                    System.out.print("Enter Due Date (YYYY-MM-DD): ");
                    String dueDate = scanner.nextLine();

                    IssueDAO.issueBook(
                            memberId, bookId, issueDate, dueDate);
                    break;

                case 5:
                    System.out.print("Enter Issue ID: ");
                    int issueId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Return Date (YYYY-MM-DD): ");
                    String returnDate = scanner.nextLine();

                    ReturnDAO.returnBook(issueId, returnDate);
                    break;

                case 6:
                    IssuedBooksDAO.displayIssuedBooks();
                    break;

                case 7:
                    System.out.print("Enter overdue days: ");
                    int lateDays = scanner.nextInt();
                    scanner.nextLine();

                    if (lateDays < 0) {
                        System.out.println(
                                "Overdue days cannot be negative.");
                    } else {
                        FineDAO.calculateFine(lateDays);
                    }
                    break;

                case 8:
                    System.out.println("1. Record a fine");
                    System.out.println("2. View all fines");
                    System.out.print("Enter your choice: ");

                    int fineChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (fineChoice == 1) {
                        System.out.print("Enter Issue ID: ");
                        int fineIssueId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter overdue days: ");
                        int overdueDays = scanner.nextInt();
                        scanner.nextLine();

                        FineManagementDAO.recordFine(
                                fineIssueId, overdueDays);

                    } else if (fineChoice == 2) {
                        FineManagementDAO.displayFines();

                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 9:
                    System.out.print("Enter Fine ID: ");
                    int fineId = scanner.nextInt();
                    scanner.nextLine();

                    PayFineDAO.markFineAsPaid(fineId);
                    break;
                case 10:
                    System.out.print("Enter Book ID: ");
                    int availabilityBookId = scanner.nextInt();
                    scanner.nextLine();

                    BookDAO.checkBookAvailability(availabilityBookId);
                    break;
                case 11:
                    AddBookDAO.addBook();
                    break;
                case 12:
                    UpdateBookDAO.updateBook();
                    break;
                case 13:
                    DeleteBookDAO.deleteBook();
                    break;
                case 14:
                    MemberManagementDAO.viewAllMembers();
                    break;

                case 15:
                    MemberManagementDAO.searchMember();
                    break; 
                case 16:
                    MemberHistoryDAO.viewMemberHistory();
                    break;
                case 17:
                    CurrentIssuesDAO.viewCurrentIssues();
                    break;
                case 18:
                    OverdueBooksDAO.viewOverdueBooks();
                    break;  
                case 19:
                    LibraryStatisticsDAO.viewStatistics();
                    break; 
                case 20:
                    BookDAO.advancedSearchBooks();
                    break; 
                case 21:
                    BookDAO.searchBookByISBN();
                    break; 
                case 22:
                    CategoryReportDAO.viewCategoryReport();
                    break;               
                case 0:
                    System.out.println(
                            "Thank you for using the Library Management System!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}
