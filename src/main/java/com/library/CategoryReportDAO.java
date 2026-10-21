package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryReportDAO {

public static void viewCategoryReport() {

    String sql = """
            SELECT c.category_name,
                   COUNT(b.book_id) AS total_titles,
                   COALESCE(SUM(b.quantity), 0) AS total_copies
            FROM category c
            LEFT JOIN book b ON c.category_id = b.category_id
            GROUP BY c.category_id, c.category_name
            ORDER BY c.category_name
            """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        boolean found = false;

        System.out.println("\n========== BOOKS BY CATEGORY ==========");

        while (rs.next()) {
            found = true;

            System.out.println("Category: "
                    + rs.getString("category_name"));
            System.out.println("Total Book Titles: "
                    + rs.getInt("total_titles"));
            System.out.println("Total Copies: "
                    + rs.getInt("total_copies"));
            System.out.println("---------------------------------------");
        }

        if (!found) {
            System.out.println("No categories found.");
        }

    } catch (SQLException e) {
        System.out.println("Error generating category report: "
                + e.getMessage());
    }
}


}

