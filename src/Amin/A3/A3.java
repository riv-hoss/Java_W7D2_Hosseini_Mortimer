package Amin.A3;

public class A3  {


    public static void main(String[] args) throws Exception {


        // create and test Department class
        Department dpt = new Department();
        dpt.displayDepartments();

        // create JTable calling ShowTable class
        ShowTable st = new ShowTable();
        st.createUI();



    }


}
