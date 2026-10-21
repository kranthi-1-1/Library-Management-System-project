package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberDAO {

    public static void registerMember(
            String name,
            String email,
            String phone,
            String department) {

        String query = """
                INSERT INTO member (name, email, phone, department)
                VALUES (?, ?, ?, ?)
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query)
        ) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, department);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println(
                        "Member registered successfully!");
            }

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Registration failed: the email may already exist.");
            } else {
                System.out.println("Error registering member.");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== MEMBER REGISTRATION =====");

        System.out.print("Enter member name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter department: ");
        String department = scanner.nextLine();

        registerMember(name, email, phone, department);

        scanner.close();
    }
}
