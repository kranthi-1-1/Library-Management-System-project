package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookDAO {

    // Display all books
    public static void displayAllBooks() {

        String query =
                "SELECT book_id, title, isbn, quantity FROM book";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {

            System.out.println("\n========== LIBRARY BOOKS ==========");

            while (resultSet.next()) {
                displayBook(resultSet);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving books.");
            System.out.println(e.getMessage());
        }
    }

    // Search books by title
    public static void searchBook(String searchTitle) {

        String query =
                "SELECT book_id, title, isbn, quantity "
                + "FROM book WHERE title LIKE ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query)
        ) {

            statement.setString(1, "%" + searchTitle + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                boolean found = false;

                System.out.println("\n========== SEARCH RESULTS ==========");

                while (resultSet.next()) {
                    displayBook(resultSet);
                    found = true;
                }

                if (!found) {
                    System.out.println("No matching books found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching for books.");
            System.out.println(e.getMessage());
        }
    }

    // Display one book
    private static void displayBook(ResultSet resultSet)
            throws SQLException {

        System.out.println("Book ID: "
                + resultSet.getInt("book_id"));

        System.out.println("Title: "
                + resultSet.getString("title"));

        System.out.println("ISBN: "
                + resultSet.getString("isbn"));

        System.out.println("Available Quantity: "
                + resultSet.getInt("quantity"));

        System.out.println("-----------------------------------");
    }

    public static void checkBookAvailability(int bookId) {

        String query = """
            SELECT title, quantity
            FROM book
            WHERE book_id = ?
            """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, bookId);

        try (ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                String title = resultSet.getString("title");
                int quantity = resultSet.getInt("quantity");

                System.out.println("\n===== BOOK AVAILABILITY =====");
                System.out.println("Book Title: " + title);
                System.out.println("Available Copies: " + quantity);

                if (quantity > 0) {
                    System.out.println("Status: AVAILABLE");
                } else {
                    System.out.println("Status: NOT AVAILABLE");
                }

            } else {
                System.out.println("Book ID not found.");
            }
        }

        } catch (SQLException e) {
            System.out.println("Error checking book availability.");
            System.out.println(e.getMessage());
        }
   }
   public static void advancedSearchBooks() {

        Scanner scanner = ConsoleInput.SCANNER;

        System.out.println("\n========== ADVANCED BOOK SEARCH ==========");
        System.out.println("1. Search by Author");
        System.out.println("2. Search by Publisher");
        System.out.println("3. Search by Category");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String column;

        switch (choice) {
           case 1:
              column = "a.author_name";
              break;
           case 2:
              column = "p.publisher_name";
              break;
           case 3:
              column = "c.category_name";
              break;
           default:
              System.out.println("Invalid choice.");
              return;
      }

      System.out.print("Enter search keyword: ");
      String keyword = scanner.nextLine();

      String sql = """
            SELECT b.book_id, b.title, b.isbn,
                   a.author_name, p.publisher_name,
                   c.category_name, b.quantity
            FROM book b
            LEFT JOIN author a ON b.author_id = a.author_id
            LEFT JOIN publisher p ON b.publisher_id = p.publisher_id
            LEFT JOIN category c ON b.category_id = c.category_id
            WHERE """ + " " + column + " LIKE ? ORDER BY b.title";

      try (java.sql.Connection con = DBConnection.getConnection();
          java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

          ps.setString(1, "%" + keyword + "%");

          try (java.sql.ResultSet rs = ps.executeQuery()) {

             boolean found = false;

             System.out.println("\n========== SEARCH RESULTS ==========");

             while (rs.next()) {
                 found = true;

                 System.out.println("Book ID: " + rs.getInt("book_id"));
                 System.out.println("Title: " + rs.getString("title"));
                 System.out.println("ISBN: " + rs.getString("isbn"));
                 System.out.println("Author: " + rs.getString("author_name"));
                 System.out.println("Publisher: "
                        + rs.getString("publisher_name"));
                 System.out.println("Category: "
                        + rs.getString("category_name"));
                 System.out.println("Quantity: " + rs.getInt("quantity"));
                 System.out.println("------------------------------------");
             }

             if (!found) {
                System.out.println("No matching books found.");
             }
          }

      } catch (java.sql.SQLException e) {
         System.out.println("Error searching books: " + e.getMessage());
      }
    }
    public static void searchBookByISBN() {
          Scanner scanner = ConsoleInput.SCANNER;

          System.out.print("Enter ISBN: ");
          String isbn = scanner.nextLine().trim();

          String sql = """
            SELECT book_id, title, isbn, quantity
            FROM book
            WHERE isbn = ?
           """;

          try (java.sql.Connection con = DBConnection.getConnection();
          java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

          ps.setString(1, isbn);

          try (java.sql.ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            System.out.println("\n========== BOOK FOUND ==========");
            System.out.println("Book ID: " + rs.getInt("book_id"));
            System.out.println("Title: " + rs.getString("title"));
            System.out.println("ISBN: " + rs.getString("isbn"));
            System.out.println("Quantity: " + rs.getInt("quantity"));
          } else {
            System.out.println("No book found with ISBN: " + isbn);
          }
          }

          } catch (java.sql.SQLException e) {
            System.out.println("Error searching by ISBN: " + e.getMessage());
          }


    }


    // Main method
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Display all books");
        System.out.println("2. Search book by title");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        switch (choice) {
            case 1:
                displayAllBooks();
                break;

            case 2:
                System.out.print("Enter book title to search: ");
                String title = scanner.nextLine();
                searchBook(title);
                break;

            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}