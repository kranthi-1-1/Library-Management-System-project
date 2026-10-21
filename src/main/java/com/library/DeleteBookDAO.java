package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteBookDAO {

    public static void deleteBook() {

        int bookId;
        Scanner scanner = ConsoleInput.SCANNER;
        System.out.print("Enter Book ID to delete: ");
        bookId = scanner.nextInt();
        scanner.nextLine();

        String checkSql =
                "SELECT COUNT(*) FROM issue WHERE book_id = ?";

        String deleteSql =
                "DELETE FROM book WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            // Check whether the book has existing issue records
            try (PreparedStatement checkStmt =
                         con.prepareStatement(checkSql)) {

                checkStmt.setInt(1, bookId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println(
                                "Cannot delete this book because issue records exist."
                        );
                        return;
                    }
                }
            }

            // Delete the book if no issue records exist
            try (PreparedStatement deleteStmt =
                         con.prepareStatement(deleteSql)) {

                deleteStmt.setInt(1, bookId);

                int rows = deleteStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("Book deleted successfully!");
                } else {
                    System.out.println("Book ID not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }
}

