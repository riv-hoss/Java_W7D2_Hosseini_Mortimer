package Amin.A3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddDeptWindow implements ActionListener {

    JFrame frame, frame1;
    JTextField textbox1, textbox2;
    JLabel label1, label2;
    JButton button;
    JPanel panel;
    static JTable table;
    String[] columnNames = {"dept_no", "dept_name"};


    public void createUI()
    {
        frame = new JFrame("Add Department to database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        textbox1 = new JTextField();
        textbox2 = new JTextField();
        textbox1.setBounds(180,30,150,20);
        label1 = new JLabel("Enter department no");
        label1.setBounds(10, 30, 180, 20);

        textbox2.setBounds(180,60,150,20);
        label2 = new JLabel("Enter department name");
        label2.setBounds(10, 60, 180, 20);

        button = new JButton("add new row");
        button.setBounds(200,130,150,20); // x: 120 to 200
        button.addActionListener(this);

        frame.add(textbox1);
        frame.add(label1);
        frame.add(textbox2);
        frame.add(label2);
        frame.add(button);
        frame.setVisible(true);
        frame.setSize(800, 600);
    }

    public void actionPerformed(ActionEvent ae)
    {
        button = (JButton)ae.getSource();
        System.out.println("Showing Table Data.......");
        addRowShowTable();
    }

    public void addRowShowTable() {

        frame1 = new JFrame("Department table");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        String textvalue1 = textbox1.getText();
        String textvalue2 = textbox2.getText();
        String dept_no= "";
        String dept_name= "";

        // add new row
        try {
            DepartmentAccess deptAccess = new DepartmentAccess();
            deptAccess.addDepartment(textvalue1, textvalue2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //showing table

        try
        {

            Connection con = new DepartmentAccess().getConnection();
            String sql = "select * from departments";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i =0;
            while(rs.next())
            {
                dept_no = rs.getString("dept_no");
                dept_name = rs.getString("dept_name");

                model.addRow(new Object[]{dept_no, dept_name});
                i++;
            }
            if(i <1)
            {
                JOptionPane.showMessageDialog(null, "No Record Found","Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            if(i ==1)
            {
                System.out.println(i+" Record Found");
            }
            else
            {
                System.out.println(i+" Records Found");
            }

            ps.close();
            con.close();

        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400,300);


    }




}
