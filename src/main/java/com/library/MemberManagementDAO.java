package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberManagementDAO {

    public static void viewAllMembers() {

        String sql = "SELECT * FROM member ORDER BY member_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== REGISTERED MEMBERS ==========");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Member ID: " + rs.getInt("member_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No members registered yet.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving members: " + e.getMessage());
        }
    }

    public static void searchMember() {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\n========== SEARCH MEMBER ==========");
            System.out.println("1. Search by Member ID");
            System.out.println("2. Search by Name");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            String sql;

            if (choice == 1) {
                System.out.print("Enter Member ID: ");
                int memberId = scanner.nextInt();
                scanner.nextLine();

                sql = "SELECT * FROM member WHERE member_id = ?";

                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setInt(1, memberId);
                    displaySearchResults(ps);

                } catch (SQLException e) {
                    System.out.println("Error searching member: " + e.getMessage());
                }

            } else if (choice == 2) {
                System.out.print("Enter member name: ");
                String name = scanner.nextLine();

                sql = "SELECT * FROM member WHERE name LIKE ? ORDER BY name";

                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setString(1, "%" + name + "%");
                    displaySearchResults(ps);

                } catch (SQLException e) {
                    System.out.println("Error searching member: " + e.getMessage());
                }

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void displaySearchResults(PreparedStatement ps)
            throws SQLException {

        try (ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\nMember ID: " + rs.getInt("member_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No matching member found.");
            }
        }
    }
}


