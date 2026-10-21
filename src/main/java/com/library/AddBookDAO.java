package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddBookDAO {

    public static void addBook() {

        Scanner scanner = ConsoleInput.SCANNER;
        System.out.println("\n========== ADD NEW BOOK ==========");

        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter Author ID: ");
        int authorId = scanner.nextInt();

        System.out.print("Enter Publisher ID: ");
        int publisherId = scanner.nextInt();

        System.out.print("Enter Category ID: ");
        int categoryId = scanner.nextInt();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        String sql = "INSERT INTO book " +
                "(title, isbn, author_id, publisher_id, category_id, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, isbn);
            ps.setInt(3, authorId);
            ps.setInt(4, publisherId);
            ps.setInt(5, categoryId);
            ps.setInt(6, quantity);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Unable to add book: " + e.getMessage());
        }
    }
}

