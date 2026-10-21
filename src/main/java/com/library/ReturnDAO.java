package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Scanner;

public class ReturnDAO {

    public static void returnBook(int issueId, String returnDate) {

        String query = """
                UPDATE issue
                SET return_date = ?
                WHERE issue_id = ?
                  AND return_date IS NULL
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query)
        ) {
            statement.setDate(1, Date.valueOf(returnDate));
            statement.setInt(2, issueId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Book returned successfully!");
            } else {
                System.out.println(
                    "Return unsuccessful. Issue ID may not exist, " +
                    "or the book may already have been returned."
                );
            }

        } catch (IllegalArgumentException e) {
            System.out.println(
                "Invalid date format. Please use YYYY-MM-DD."
            );
        } catch (SQLException e) {
            System.out.println("Error returning the book.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== RETURN BOOK =====");

        System.out.print("Enter Issue ID: ");
        int issueId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Return Date (YYYY-MM-DD): ");
        String returnDate = scanner.nextLine();

        returnBook(issueId, returnDate);

        scanner.close();
    }
}


