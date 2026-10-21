package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OverdueBooksDAO {


public static void viewOverdueBooks() {
    String sql = """
            SELECT i.issue_id, m.member_id, m.name,
                   b.title, i.issue_date, i.due_date,
                   DATEDIFF(CURDATE(), i.due_date) AS overdue_days
            FROM issue i
            JOIN member m ON i.member_id = m.member_id
            JOIN book b ON i.book_id = b.book_id
            WHERE i.return_date IS NULL
              AND i.due_date < CURDATE()
            ORDER BY i.due_date
            """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        boolean found = false;

        System.out.println("\n========== OVERDUE BOOKS ==========");

        while (rs.next()) {
            found = true;

            System.out.println("Issue ID: " + rs.getInt("issue_id"));
            System.out.println("Member ID: " + rs.getInt("member_id"));
            System.out.println("Member Name: " + rs.getString("name"));
            System.out.println("Book Title: " + rs.getString("title"));
            System.out.println("Issue Date: " + rs.getDate("issue_date"));
            System.out.println("Due Date: " + rs.getDate("due_date"));
            System.out.println("Overdue Days: "
                    + rs.getInt("overdue_days"));
            System.out.println("-----------------------------------");
        }

        if (!found) {
            System.out.println("There are no overdue books.");
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving overdue books: "
                + e.getMessage());
    }
}

}

