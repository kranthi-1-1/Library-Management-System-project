package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryStatisticsDAO {

public static void viewStatistics() {

    String[] queries = {
        "SELECT COUNT(*) FROM book",
        "SELECT COALESCE(SUM(quantity), 0) FROM book",
        "SELECT COUNT(*) FROM member",
        "SELECT COUNT(*) FROM issue",
        "SELECT COUNT(*) FROM issue WHERE return_date IS NULL",
        "SELECT COUNT(*) FROM issue WHERE return_date IS NULL AND due_date < CURDATE()",
        "SELECT COALESCE(SUM(amount), 0) FROM fine"
    };

    String[] labels = {
        "Total Book Titles",
        "Total Copies in Stock",
        "Registered Members",
        "Total Borrowing Records",
        "Currently Issued Books",
        "Overdue Books",
        "Total Fines Recorded (Rs.)"
    };

    try (Connection con = DBConnection.getConnection()) {

        System.out.println("\n========== LIBRARY STATISTICS ==========");

        for (int i = 0; i < queries.length; i++) {

            try (PreparedStatement ps = con.prepareStatement(queries[i]);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.printf("%-32s: %.2f%n",
                            labels[i], rs.getDouble(1));
                }
            }
        }

        System.out.println("=========================================");

    } catch (SQLException e) {
        System.out.println("Error retrieving statistics: "
                + e.getMessage());
    }
}


}

