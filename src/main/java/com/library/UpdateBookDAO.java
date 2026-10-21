package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateBookDAO {

    public static void updateBook() {

        Scanner scanner = ConsoleInput.SCANNER;
        System.out.print("Enter Book ID to update: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Choose what you want to update:");
        System.out.println("1. Update Title");
        System.out.println("2. Update ISBN");
        System.out.println("3. Update Quantity");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String sql;
        String value;

        switch (choice) {

            case 1:
                System.out.print("Enter new title: ");
                value = scanner.nextLine();
                sql = "UPDATE book SET title = ? WHERE book_id = ?";
                break;

            case 2:
                System.out.print("Enter new ISBN: ");
                value = scanner.nextLine();
                sql = "UPDATE book SET isbn = ? WHERE book_id = ?";
                break;

            case 3:
                System.out.print("Enter new quantity: ");
                int quantity = scanner.nextInt();

                if (quantity < 0) {
                    System.out.println("Quantity cannot be negative.");
                    return;
                }

                updateQuantity(bookId, quantity);
                return;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, value);
            ps.setInt(2, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book details updated successfully!");
            } else {
                System.out.println("Book ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }

    private static void updateQuantity(int bookId, int quantity) {

        String sql = "UPDATE book SET quantity = ? WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book quantity updated successfully!");
            } else {
                System.out.println("Book ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating quantity: " + e.getMessage());
        }
    }
}
