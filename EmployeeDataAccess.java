package javaapplication3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.fluttercode.datafactory.impl.DataFactory;
import java.util.ArrayList;
import java.util.List;



public class EmployeeDataAccess {
    


    public static List<Employee> getEmployees() {
        List<Employee> list = new ArrayList<>();
        String[] depts = {"IT", "Account", "Admin", "Sales"};
        DataFactory df = new DataFactory();
        try {

            
            Connection connection=ConnectionManager.getConnection();
            String query="select PRODUCT_NAME,concat(DISCTIPTION,'  | Unit Price =',cast(UNIT_PRICE as decimal(19,2)), concat( ' | DISC = ',discount) ) as discription,UID from PRODUCT";
            Statement st=connection.createStatement();
            ResultSet set=st.executeQuery(query);
            while(set.next())
            {
                Employee e = new Employee();
                e.setName(set.getString(1));
                e.setDept(set.getString(2));
                e.setAddress("");
                e.setUid(set.getString(3));
                list.add(e);
            }
        
        } catch (Exception e) {
        }
        
//        for (int i = 1; i <= 100; i++) {
//            Employee e = new Employee();
//            e.setName(df.getName());
//            e.setAddress(df.getAddress() + ", " + df.getCity());
//            e.setDept(df.getItem(depts));
//            e.setPhone(df.getNumberText(3) + "-" + df.getNumberText(3) +
//                    "-" + df.getNumberText(4));
//            list.add(e);
//            
//            
//        }
        return list;
    }
}