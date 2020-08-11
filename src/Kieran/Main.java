package Kieran;

import Kieran.exceptions.DuplicateEntryException;
import Kieran.exceptions.InvalidSelectionException;

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
                                // Listing all students for selection
                                viewAllStudents(connection);

                                // Gathering Student ID
                                System.out.println("Please select student ID to enroll:");
                                int studentIDSelection = Integer.parseInt(scanner.next());

                                // Listing all the courses
                                viewAllCourses(connection);

                                // Gathering course ID
                                System.out.println("Please select course ID to enroll:");
                                int courseIDSelection = Integer.parseInt(scanner.next());

                                // Enrolling student in specific course
                                enrollStudentInCourse(connection, studentIDSelection, courseIDSelection);

                                break;

                            case 4: // Remove a student
                                // Listing all student to select from
                                viewAllStudents(connection);

                                // Gathering the student ID from the user
                                System.out.println("Please select the student ID you wish to remove");
                                int studentUserSelection = Integer.parseInt(scanner.next());

                                // Passing the selection to the database
                                removeStudent(connection, studentUserSelection);

                                break;

                            case 5: // View all courses
                                viewAllCourses(connection);

                                break;

                            case 6: // Add a course subject
                                // Awaiting user input
                                System.out.println("Please enter the new course's name:");
                                String newCourseName = scanner.next();

                                // Passing the info to the DB
                                addNewCourse(connection, newCourseName);

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
            System.out.println("All Students:");
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

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeStudent(Connection connection, int studentID) {
        try {
            System.out.println("Removing student");

            // Checking if the selection is valid
            String studentIDCheckQuery = "SELECT * FROM students";
            Statement studentIDCheckStatement = connection.createStatement();
            ResultSet studentIDCheckResultSet = studentIDCheckStatement.executeQuery(studentIDCheckQuery);

            // Iterating through the result set to find the entry that will be deleted, if not present throw exception
            boolean studentFound = false;
            while (studentIDCheckResultSet.next()) {
                if (studentIDCheckResultSet.getInt(1) == studentID) {
                    studentFound = true;
                }
            }

            if (studentFound) {
                String deleteStudentQuery = "DELETE FROM students WHERE id=?";
                PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteStudentQuery);
                deletePreparedStatement.setInt(1, studentID);
                deletePreparedStatement.executeUpdate();

                deletePreparedStatement.close();

                System.out.println("Done!");

            } else {
                throw new InvalidSelectionException("Invalid ID Selected");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (InvalidSelectionException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewAllCourses(Connection connection) {
        final String sql = "SELECT * FROM courses";

        try {
            // Gathering the info from the DB
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Displaying the table header
            System.out.println("ALL Courses:");
            String tableHeader = String.format("%3s %20s", "ID", "Course Name");
            System.out.println("-".repeat(tableHeader.length()));
            System.out.println(tableHeader);
            System.out.println("-".repeat(tableHeader.length()));

            // Displaying the result set in the table
            while (resultSet.next()) {
                int courseID = resultSet.getInt(1);
                String courseName = resultSet.getString(2);

                System.out.println(String.format("%3s %20s", courseID, courseName));
            }

            System.out.println("\n");

            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addNewCourse(Connection connection, String courseName) {
        try {
            System.out.println("Adding " + courseName + " to the database...");

            String addNewCourseQuery = "INSERT INTO courses(name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(addNewCourseQuery);
            preparedStatement.setString(1, courseName);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            System.out.println("Done!");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void enrollStudentInCourse(Connection connection, int studentID, int courseID) {
        try {
            System.out.println("Enrolling student...");

            // Student selection validation check
            String studentCountQuery = "SELECT COUNT(*) FROM students";

            Statement studentStatement = connection.createStatement();
            ResultSet studentResultSet = studentStatement.executeQuery(studentCountQuery);
            studentResultSet.next();

            int totalStudents = studentResultSet.getInt(1);
            if (!(studentID >= 1 && studentID <= totalStudents)) {
                throw new InvalidSelectionException("Invalid Selection");
            }

            studentStatement.close();

            // Course selection validation check
            String courseCountQuery = "SELECT COUNT(*) FROM courses";

            Statement courseStatement = connection.createStatement();
            ResultSet courseResultSet = courseStatement.executeQuery(courseCountQuery);
            courseResultSet.next();

            int totalCourses = courseResultSet.getInt(1);
            if(!(courseID >= 1 && courseID <= totalCourses)) {
                throw new InvalidSelectionException("Invalid Selection");
            }

            courseStatement.close();

            // Duplicate selection checker
            String duplicateCheckQuery = "SELECT * FROM enrollments";
            Statement duplicateCheckStatement = connection.createStatement();
            ResultSet duplicateCheckResultSet = duplicateCheckStatement.executeQuery(duplicateCheckQuery);

            // Iterating through all the entries to determine if this enrollment will cause a duplicate entry to be submitted
            while (duplicateCheckResultSet.next()) {
                int fk_students_id = duplicateCheckResultSet.getInt(1);
                int fk_courses = duplicateCheckResultSet.getInt(2);

                if (fk_students_id == studentID && fk_courses == courseID) {
                    throw new DuplicateEntryException("Entry already exists in database");
                }
            }

            duplicateCheckStatement.close();

            // If all checks clear, begin to insert the request into the database
            String sql = "INSERT INTO enrollments(fk_students_id, fk_courses) VALUES(?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, courseID);

            preparedStatement.executeUpdate();

            System.out.println("Done!");

            preparedStatement.close();

        }catch (SQLException e) {
            e.printStackTrace();

        }catch (InvalidSelectionException | DuplicateEntryException e) {
            System.out.println(e.getMessage());
        }
    }
}
