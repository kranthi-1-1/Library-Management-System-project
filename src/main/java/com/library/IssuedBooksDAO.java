package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssuedBooksDAO {

    public static void displayIssuedBooks() {

        String query = """
                SELECT
                    i.issue_id,
                    m.name AS member_name,
                    b.title AS book_title,
                    i.issue_date,
                    i.due_date,
                    i.return_date,
                    CASE
                        WHEN i.return_date IS NULL THEN 'ISSUED'
                        ELSE 'RETURNED'
                    END AS book_status
                FROM issue i
                JOIN member m ON i.member_id = m.member_id
                JOIN book b ON i.book_id = b.book_id
                ORDER BY i.issue_id DESC
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {

            System.out.println("\n========== BOOK ISSUE REPORT ==========");

            boolean found = false;

            while (resultSet.next()) {
                found = true;

                System.out.println("\nIssue ID: "
                        + resultSet.getInt("issue_id"));

                System.out.println("Member Name: "
                        + resultSet.getString("member_name"));

                System.out.println("Book Title: "
                        + resultSet.getString("book_title"));

                System.out.println("Issue Date: "
                        + resultSet.getDate("issue_date"));

                System.out.println("Due Date: "
                        + resultSet.getDate("due_date"));

                System.out.println("Return Date: "
                        + resultSet.getDate("return_date"));

                System.out.println("Status: "
                        + resultSet.getString("book_status"));

                System.out.println("---------------------------------------");
            }

            if (!found) {
                System.out.println("No issue records found.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving issue records.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        displayIssuedBooks();
    }
}


