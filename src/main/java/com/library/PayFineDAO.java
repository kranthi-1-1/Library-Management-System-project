package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PayFineDAO {

    public static void markFineAsPaid(int fineId) {

        String query = """
                UPDATE fine
                SET paid_status = TRUE
                WHERE fine_id = ?
                  AND paid_status = FALSE
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(query)
        ) {
            statement.setInt(1, fineId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Fine marked as PAID successfully!");
            } else {
                System.out.println(
                    "No unpaid fine found with that ID. " +
                    "It may not exist or may already be paid."
                );
            }

        } catch (SQLException e) {
            System.out.println("Error updating fine payment status.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== FINE PAYMENT =====");
        System.out.print("Enter Fine ID: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid Fine ID.");
            scanner.close();
            return;
        }

        int fineId = scanner.nextInt();

        markFineAsPaid(fineId);

        scanner.close();
    }
}


