package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Scanner;

public class IssueDAO {

    public static void issueBook(
            int memberId,
            int bookId,
            String issueDate,
            String dueDate) {

        String query = "INSERT INTO issue "
            + "(member_id, book_id, issue_date, due_date) "
            + "VALUES (?, ?, ?, ?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query)
        ) {
            statement.setInt(1, memberId);
            statement.setInt(2, bookId);
            statement.setDate(3, Date.valueOf(issueDate));
            statement.setDate(4, Date.valueOf(dueDate));

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Book issued successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Unable to issue the book.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== ISSUE BOOK =====");

        System.out.print("Enter Member ID: ");
        int memberId = scanner.nextInt();

        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Issue Date (YYYY-MM-DD): ");
        String issueDate = scanner.nextLine();

        System.out.print("Enter Due Date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine();

        issueBook(memberId, bookId, issueDate, dueDate);

        scanner.close();
    }
}


