package Amin.A3;

import java.sql.*;

public class DepartmentAccess {

    private Connection con;

    public DepartmentAccess () throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                new Credential().getUrl(),
                new Credential().getUser(),
                new Credential().getPassword());
    }

    public void addDepartment (String dept_no, String dept_name) throws Exception{

        String insert = "INSERT INTO departments (dept_no, dept_name) VALUES (?,?)";
        PreparedStatement pst = con.prepareStatement(insert);
        pst.setString(1, dept_no);
        pst.setString(2, dept_name);
        try {
            int count = pst.executeUpdate();
            System.out.println(count + " row/s successfully added to the departments table!\n");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println(e.getMessage());
        }
        pst.close();
        con.close();
    }

    public void updateDepartment (String dept_no, String dept_name) throws Exception{
        String update = "UPDATE departments SET dept_name=? WHERE dept_no=?";
        PreparedStatement pst = con.prepareStatement(update);
        pst.setString(1, dept_name);
        pst.setString(2,dept_no);

        try {
            int count = pst.executeUpdate();
            System.out.println(count + " row/s successfully updated in the departments table!\n");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println(e.getMessage());
        }
        pst.close();
        con.close();
    }

    public void deleteDepartment (String dept_no) throws Exception{
        String update = "DELETE FROM departments WHERE dept_no=?";
        PreparedStatement pst = con.prepareStatement(update);
        pst.setString(1,dept_no);

        try {
            int count = pst.executeUpdate();
            System.out.println(count + " row/s successfully deleted from the departments table!\n");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println(e.getMessage());
        } catch (SQLSyntaxErrorException e) {
            System.err.println( e.getMessage());
            System.err.println("Please enter a valid key for department number!");
        }
        pst.close();
        con.close();
    }


    public void displayDepartments () throws Exception{
        Statement st = null;

        try {
            st = con.createStatement();
            ResultSet rs = null;
            try {
                String query = "SELECT * FROM departments";
                rs = st.executeQuery(query);
                System.out.printf("-".repeat(31) +"%n%-13s %-16s |%n" + "-".repeat(31),
                        "dept_no", "dept_name");
                while (rs.next()) {
                    String name = String.format("%n%-10s %-20s|",
                            rs.getString("dept_no"),
                            rs.getString("dept_name"));

                    System.out.println(name);
                }
                System.out.println("-".repeat(31));
            } finally {
                if (rs != null) rs.close();
            }
        } finally {
            if (st != null) st.close();
        }
    }
}
