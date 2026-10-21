package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class FineManagementDAO {

    // Record a fine for an issue
    public static void recordFine(int issueId, int lateDays) {

        if (lateDays < 0) {
            System.out.println("Overdue days cannot be negative.");
            return;
        }

        String fineQuery = "SELECT CalculateFine(?) AS amount";

        String insertQuery = """
                INSERT INTO fine (issue_id, amount, paid_status)
                VALUES (?, ?, FALSE)
                """;

        try (Connection connection = DBConnection.getConnection()) {

            // Calculate the fine using the existing MySQL function
            double amount;

            try (PreparedStatement fineStatement =
                         connection.prepareStatement(fineQuery)) {

                fineStatement.setInt(1, lateDays);

                try (ResultSet resultSet = fineStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        System.out.println("Could not calculate the fine.");
                        return;
                    }

                    amount = resultSet.getDouble("amount");
                }
            }

            // Store the calculated fine
            try (PreparedStatement insertStatement =
                         connection.prepareStatement(insertQuery)) {

                insertStatement.setInt(1, issueId);
                insertStatement.setDouble(2, amount);

                int rowsInserted = insertStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Fine recorded successfully!");
                    System.out.printf("Fine Amount: Rs. %.2f%n", amount);
                    System.out.println("Payment Status: UNPAID");
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to record the fine.");
            System.out.println(e.getMessage());
        }
    }

    // Display all fines
    public static void displayFines() {

        String query = """
                SELECT f.fine_id, f.issue_id, m.name,
                       b.title, f.amount, f.paid_status
                FROM fine f
                JOIN issue i ON f.issue_id = i.issue_id
                JOIN member m ON i.member_id = m.member_id
                JOIN book b ON i.book_id = b.book_id
                ORDER BY f.fine_id DESC
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {

            System.out.println("\n========== FINE REPORT ==========");

            boolean found = false;

            while (resultSet.next()) {
                found = true;

                System.out.println("\nFine ID: "
                        + resultSet.getInt("fine_id"));

                System.out.println("Issue ID: "
                        + resultSet.getInt("issue_id"));

                System.out.println("Member: "
                        + resultSet.getString("name"));

                System.out.println("Book: "
                        + resultSet.getString("title"));

                System.out.printf("Amount: Rs. %.2f%n",
                        resultSet.getDouble("amount"));

                boolean paid = resultSet.getBoolean("paid_status");

                System.out.println("Status: "
                        + (paid ? "PAID" : "UNPAID"));

                System.out.println("---------------------------------");
            }

            if (!found) {
                System.out.println("No fines recorded.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve fines.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== FINE MANAGEMENT =====");
        System.out.println("1. Record a fine");
        System.out.println("2. View all fines");
        System.out.print("Enter your choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid menu number.");
            scanner.close();
            return;
        }

        int choice = scanner.nextInt();

        switch (choice) {

            case 1:
                System.out.print("Enter Issue ID: ");
                int issueId = scanner.nextInt();

                System.out.print("Enter overdue days: ");
                int lateDays = scanner.nextInt();

                recordFine(issueId, lateDays);
                break;

            case 2:
                displayFines();
                break;

            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}


