package Kieran;

import java.sql.*;
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
            connection.setAutoCommit(true);

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

                    // Switching to relevant controls
                    int userMenuSelection = Integer.parseInt(scanner.next());

                    // Sanitising menu selection
                    if (userMenuSelection >= 0 && userMenuSelection <= 6) {
                        switch (userMenuSelection) {
                            case 0: // Closing the program
                                System.out.println("Shutting down...");
                                connection.close();
                                endLoop = true;
                                break;

                            case 1: // View all students
                                viewAllStudents(connection);
                                break;

                            case 2: // Add a student

                                // Gathering student name
                                System.out.println("Please enter the new student's name:");
                                String nameInput = scanner.next();

                                // Gathering student address
                                System.out.println("Please enter the new student's address:");
                                String addressInput = scanner.next();

                                // Passing information to be sent to the mySQL server
                                addNewStudent(connection, nameInput, addressInput);

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

    public static void viewAllStudents(Connection connection) {
        final String sql = "SELECT * FROM students";

        try {
            // Creating the statement instance
            Statement statement = connection.createStatement();

            // Gathering the dataset
            ResultSet resultSet = statement.executeQuery(sql);

            // Table header
            String tableHeader = String.format("%3s %20s %20s", "ID", "Name", "Address");
            System.out.println("-".repeat(tableHeader.length()));
            System.out.println(tableHeader);
            System.out.println("-".repeat(tableHeader.length()));

            while (resultSet.next()) {
                int studentID = resultSet.getInt(1);
                String studentName = resultSet.getString(2);
                String studentAddress = resultSet.getString(3);

                System.out.println(String.format("%3s %20s %20s", studentID, studentName, studentAddress));
            }

            System.out.println("\n");

            // Ending the statement
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
