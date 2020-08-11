package Kieran;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String dbURL = "jdbc:mysql://172.17.0.2:3306/university";
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "ratbox94");

        try {
            // Init connection instance
            Connection connection = DriverManager.getConnection(dbURL, properties);

            // Init scanner instance
            Scanner scanner = new Scanner(System.in);

            // App Life cycle
            boolean endLoop = false;
            while (!endLoop) {
                try {
                    // Printing the application menu
                    System.out.println("-".repeat(20));
                    System.out.println("University Console");
                    System.out.println("-".repeat(20));
                    System.out.println("1) View Students");
                    System.out.println("2) Add Students");
                    System.out.println("3) Enroll Students");
                    System.out.println("4) Remove Students");
                    System.out.println("5) View Courses");
                    System.out.println("6) Add Courses");
                    System.out.println("0) Quit");
                    System.out.println("\nPlease make your selection:");

                    int userMenuSelection = Integer.parseInt(scanner.next());
                    if (userMenuSelection >= 0 && userMenuSelection <= 6) {
                        switch (userMenuSelection) {
                            case 0: // Closing the program
                                System.out.println("Shutting down...");
                                connection.close();
                                endLoop = true;
                                break;

                            case 1: // View all students

                                break;

                            case 2: // Add a student

                                break;

                            case 3: // Enroll a student to a course

                                break;

                            case 4: // Remove a student

                                break;

                            case 5: // View all courses

                                break;

                            case 6: // Add a course subject

                                break;

                        }

                    } else {
                        throw new NumberFormatException();
                    }

                } catch (NumberFormatException e) {
                    System.out.println("\nPlease make a valid selection");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static void addNewStudent(Connection connection, String studentName, String studentAddress) {
        try {
            System.out.println("Inserting " + studentName + " into database...");

            String sql = "INSERT INTO students(name, address) VALUES(?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, studentAddress);

            preparedStatement.executeUpdate();

            System.out.println("Done!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
