package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class FineDAO {

    public static void calculateFine(int lateDays) {

        String query = "SELECT CalculateFine(?) AS fine_amount";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query)
        ) {
            statement.setInt(1, lateDays);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    double fine = resultSet.getDouble("fine_amount");

                    System.out.println("\n===== FINE DETAILS =====");
                    System.out.println("Late Days: " + lateDays);
                    System.out.printf("Fine Amount: Rs.%.2f%n", fine);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error calculating fine.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== FINE CALCULATOR =====");
        System.out.print("Enter the number of overdue days: ");

        int lateDays = scanner.nextInt();

        if (lateDays < 0) {
            System.out.println("Overdue days cannot be negative.");
        } else {
            calculateFine(lateDays);
        }

        scanner.close();
    }
}


