package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberHistoryDAO {

public static void viewMemberHistory() {
    Scanner scanner = ConsoleInput.SCANNER;

    System.out.print("Enter Member ID: ");
    int memberId = scanner.nextInt();
    scanner.nextLine();

    String sql = """
            SELECT m.name, b.title, i.issue_date,
                   i.due_date, i.return_date
            FROM issue i
            JOIN member m ON i.member_id = m.member_id
            JOIN book b ON i.book_id = b.book_id
            WHERE m.member_id = ?
            ORDER BY i.issue_date DESC
            """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, memberId);

        try (ResultSet rs = ps.executeQuery()) {
            boolean found = false;

            System.out.println("\n========== MEMBER BORROWING HISTORY ==========");

            while (rs.next()) {
                found = true;

                System.out.println("Member Name: " + rs.getString("name"));
                System.out.println("Book Title: " + rs.getString("title"));
                System.out.println("Issue Date: " + rs.getDate("issue_date"));
                System.out.println("Due Date: " + rs.getDate("due_date"));

                java.sql.Date returnDate = rs.getDate("return_date");

                if (returnDate == null) {
                    System.out.println("Return Date: Not returned yet");
                    System.out.println("Status: Issued");
                } else {
                    System.out.println("Return Date: " + returnDate);
                    System.out.println("Status: Returned");
                }

                System.out.println("----------------------------------------------");
            }

            if (!found) {
                System.out.println(
                        "No borrowing history found for this member.");
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving member history: "
                + e.getMessage());
    }
}


}

