/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Dashboard.java
 *
 * Created on 2 Jan, 2019, 4:18:37 PM
 */
package javaapplication3;



import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import net.java.balloontip.BalloonTip;
import net.proteanit.sql.DbUtils;




/**
 *
 * @author J Suryam
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * @return the bill_type
     */


    Connection connection=null;
    Statement st=null;
    PreparedStatement ps=null;
    ResultSet set=null;
    CommonMethodClass cms=new CommonMethodClass("");
    String uid=null;
    List<Employee> employees = EmployeeDataAccess.getEmployees();

    /** Creates new form Dashboard */
    public Dashboard() {
        initComponents();
        getContentPane().setBackground(Color.WHITE);
        showInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
        cms.tableHeaderSettings(invoice_table);
        cms.generateUniqueID("INVOICE", "INVOICE_NO");
        invoice_InvoiceNo.setText(cms.id);

        // Adding items in product,UOM,CUSTOMER,TAX
//        cms.comboBoxAddItems(invoice_Product, "select distinct PRODUCT_NAME from PRODUCT");
    }

    

    
    
    public void showInvoiceData(String column,String like)
    {   
        String query="select UID, "+
                        "PRODUCT ," +
                        "DISCRIPTION ," +
                        "UOM ," +
                        "UNITPRICE ," +
                        "QTY, " +
                        "DISCOUNT," +
                        "cast(((UNITPRICE*QTY)/100)*DISCOUNT as decimal(19,2)) as discountVal," +
                        "TAX ," +
    /* Asume as B2C*/   "case when BILL_TYPE='B2C' then cast(((cast(cast((UNITPRICE*QTY) as decimal(19,2))-(UNITPRICE*QTY)/100*DISCOUNT as decimal(19,2)))*TAX)/(100+TAX) as decimal(19,2)) "
                       + "else cast(((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT)/100*TAX as decimal(19,2)) end as taxVal," +
    /* Asume as B2C*/   "case when BILL_TYPE='B2C' then cast((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT as decimal(19,2)) "
                       + "else cast(((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT)+cast(((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT)/100*TAX as decimal(19,2)) as decimal(19,2)) end as NetAmt " +
                        "from INVOICE where "+column+"='"+like+"' order by UID";
        System.out.println("Table : "+query);
        
        try {
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            invoice_table.setModel(DbUtils.resultSetToTableModel(set));
            tableColumnSize();
            showTotals("INVOICE_NO", invoice_InvoiceNo.getText());
	      
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                st.close();
                set.close();
            } catch (Exception e) {
            }
            
        }
        
        
    }
    


    
    public void showTotals(String column,String like)
    {   
        
        
        try {
            String query="select sum(QTY), " // 1
                + "sum(cast(UNITPRICE*QTY as decimal(19,2))) as gross1," // 2
                + "sum(cast(((UNITPRICE*QTY)/100)*DISCOUNT as decimal(19,2))) as discountVal," // 3
                + "case when BILL_TYPE='B2C' then "
                + "cast(sum(cast(((cast(cast((UNITPRICE*QTY) as decimal(19,2))-(UNITPRICE*QTY)/100*DISCOUNT as decimal(19,2)))*TAX)/(100+TAX) as decimal(19,2))) as decimal(19,2)) "
                + "else "
                + "sum(cast(((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT)/100*TAX as decimal(19,2))) end as taxVal," // 4
                + "sum(cast((UNITPRICE*QTY)-((UNITPRICE*QTY)/100)*DISCOUNT as decimal(19,2))) as gross," // 5
                + "case "
                + "when BILL_TYPE='B2C' then sum(cast((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT as decimal(19,2)))else "
                + "sum(cast(((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT)+cast(((UNITPRICE*QTY)-(UNITPRICE*QTY)/100*DISCOUNT)/100*TAX as decimal(19,2)) as decimal(19,2))) end as NetAmt,"//6
                + "GST_TYPE " // 7
                + "from INVOICE where "+column+"='"+like+"' group by BILL_TYPE";
        System.out.println(query);
            
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {       
                   System.out.println("next");
                    total_gross_value.setText(set.getString(2));
                    total_discount_value.setText(set.getString(3));
                    total_taxable_value.setText(set.getString(5));
                    total_amount_value.setText(set.getString(6));
                    
                 if(set.getString(7).equals("CGST/SGST"))
                 {
                     total_gst_label.setText("<html>CGST :<br> SGST :<html>");
                     try {
                         
                         DecimalFormat df=new DecimalFormat("0.00");
                     total_gst_val.setText("<html>"+df.format(Double.parseDouble(set.getString(4))/2)+"<br>"+df.format(Double.parseDouble(set.getString(4))/2)+"</html>");
                     } catch (Exception e) {
                     }
                 }
                 else if(set.getString(7).equals("IGST"))
                 {
                     total_gst_label.setText("IGST :");
                     total_gst_val.setText(set.getString(4));
                 }
                 set.getString(1);
                 if(set.wasNull())
                 {
                     System.out.println("not next");
                     total_gross_value.setText("0.00");
                     total_discount_value.setText("0.00");
                     total_taxable_value.setText("0.00");
                     total_amount_value.setText("0.00");
                     total_gst_label.setText("GST :");
                     total_gst_val.setText("0.00");
                 }
//                    show_totals.setText("<html>\n\n               "
//    + "<font color=rgb("+cms.rgb3+") size=+1>Total Gross :"+total_gross+"</font><br>\n               "
//    + "<font color=rgb("+cms.rgb3+") size=+1>Total Tax : "+total_taxVal+"</font><br>\n              "
//    + "<font color=rgb("+cms.rgb3+") size=+2>Net Value : "+total_netaAmt+"</font><br>\n               "
//    + "</html>");
            }
	      
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                st.close();
                set.close();
            } catch (Exception e) {
            }
            
        }
    }    
    

    private void clearFields()
    {
    invoice_Discription.setText("");
    invoice_InvoiceNo.setText("");
    invoice_Invoice_date.setDate(new Date());
    invoice_PO_No.setText("");
    invoice_PO_date.setDate(new Date());
    invoice_Phone.setText("");
    invoice_Product.setText("");
    invoice_UnitPrice.setText("0");
    invoice_Uom.setText("");
    invoice_address.setText("");
    invoice_customerName.setText("");
    invoice_discount.setText("0");
    invoice_gstType.setText("CGST/SGST");
    invoice_paymentType.setSelectedIndex(0);
    invoice_qty.setText("0");
    invoice_taxGST.setText("0");	
    }
    
    public void afterInsertUpdateClearFields()
    {
                invoice_Product.setText("");
                invoice_Discription.setText("");
                invoice_Uom.setText("");
                invoice_qty.setText("0");
                invoice_taxGST.setText("0");
                invoice_discount.setText("0");
                invoice_UnitPrice.setText("0");
                uid="";
                invoice_id_barcode.setText("");
                invoice_hsn_code.setText("");
                invoice_id_barcode.grabFocus();
    }
    
    private void insertIntoInvoice()
    {
        //Inserting data into invoice table
        connection=ConnectionManager.getConnection();
        try {
            String query="insert into invoice values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps=connection.prepareStatement(query);
            ps.setString(1, cms.csdf.format(new Date()));
            ps.setString(2, invoice_customerName.getText());
            ps.setString(3, invoice_gstType.getText());
            ps.setString(4, invoice_address.getText());
            ps.setString(5,invoice_paymentType.getSelectedItem().toString());
            ps.setString(6,invoice_PO_No.getText());
            ps.setString(7, cms.sdf.format(invoice_PO_date.getDate()));
            ps.setString(8, invoice_Phone.getText());
            ps.setString(9, invoice_InvoiceNo.getText());
            ps.setString(10, cms.sdf.format(invoice_Invoice_date.getDate()));
            ps.setString(11, invoice_Product.getText());
            ps.setString(12,invoice_Discription.getText());
            ps.setString(13, invoice_Uom.getText());
            ps.setString(14, invoice_taxGST.getText());
            ps.setString(15, invoice_UnitPrice.getText());
            ps.setString(16, invoice_discount.getText());
            ps.setString(17, invoice_qty.getText());
            ps.setString(18, invoice_hsn_code.getText());
            ps.setString(19, bill_type.getText());
            
            int i=ps.executeUpdate();
            if(i>0)
            {
//                JOptionPane.showMessageDialog(this, "Product added successful","Done",JOptionPane.INFORMATION_MESSAGE,cms.icon);
                showInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
//                clearFields();
                afterInsertUpdateClearFields();
                
            }
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                ps.close();
            } catch (Exception e) {
            }
        }
        
    }
    
        private void updateIntoInvoice(String uid)
    {
        //Inserting data into invoice table
        connection=ConnectionManager.getConnection();
        try {
            String query="update invoice set "
                        +"CUSTOMER=?,	"
                        +"GST_TYPE=?,	"
                        +"ADDRESS=?,"
                        +"PAYMENT_TYPE=? ,"	
                        +"PO_NO=?	,"
                        +"PO_DATE=?,	"
                        +"PHONE_NO=?,"	
                        +"INVOICE_NO=?,"	
                        +"INVOICE_DATE=?,"	
                        +"PRODUCT=?,"
                        +"DISCRIPTION=?,"	
                        +"UOM=?,"
                        +"TAX=?,"
                        +"UNITPRICE=?,	"
                        +"DISCOUNT=?,	"
                        +"QTY=?,HSNCODE=?,BILL_TYPE=?	where UID=?";
            ps=connection.prepareStatement(query);
            ps.setString(1, invoice_customerName.getText());
            ps.setString(2, invoice_gstType.getText());
            ps.setString(3, invoice_address.getText());
            ps.setString(4,invoice_paymentType.getSelectedItem().toString());
            ps.setString(5,invoice_PO_No.getText());
            ps.setString(6, cms.sdf.format(invoice_PO_date.getDate()));
            ps.setString(7, invoice_Phone.getText());
            ps.setString(8, invoice_InvoiceNo.getText());
            ps.setString(9, cms.sdf.format(invoice_Invoice_date.getDate()));
            ps.setString(10, invoice_Product.getText());
            ps.setString(11,invoice_Discription.getText());
            ps.setString(12, invoice_Uom.getText());
            ps.setString(13, invoice_taxGST.getText());
            ps.setString(14, invoice_UnitPrice.getText());
            ps.setString(15, invoice_discount.getText());
            ps.setString(16, invoice_qty.getText());
            ps.setString(17, invoice_hsn_code.getText());
            ps.setString(18, bill_type.getText());
            ps.setString(19, uid);
            
            
            int i=ps.executeUpdate();
            if(i>0)
            {
                JOptionPane.showMessageDialog(this, "Product updated successful","Done",JOptionPane.INFORMATION_MESSAGE,cms.icon);
                showInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
//                clearFields();

                afterInsertUpdateClearFields();
            }
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                ps.close();
            } catch (Exception e) {
            }
        }
        
    }
        
        
      public void fetchProductData()
    {
        
        String query="select * from PRODUCT  where uid='"+invoice_id_barcode.getText()+"'";
        System.out.println(query);
        try {
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
//            product_id.setText(set.getString(2));
            invoice_Product.setText(set.getString(3));
            invoice_Discription.setText(set.getString(4));
            invoice_Uom.setText(set.getString(5));
            invoice_hsn_code.setText(set.getString(6));
            invoice_UnitPrice.setText(set.getString(7));
            invoice_taxGST.setText(set.getString(8).toString());
            invoice_discount.setText(set.getString(9));
            invoice_qty.grabFocus();
            
            }else
            {
                JOptionPane.showMessageDialog(null,"<html>Data for the entered id/Barcode<br>'"+invoice_id_barcode.getText()+"' is not available</html>","",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                st.close();
                set.close();
            } catch (Exception e) {
            }
        }

    }
        
    
        private void fetchInvoiceData(String column,String where)
    {
        //Inserting data into invoice table
        connection=ConnectionManager.getConnection();
        try {
            String query="select * from invoice where "+column+"='"+where+"'";
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
            invoice_customerName.setText(set.getString(2));
            invoice_gstType.setText(set.getString(3));
            invoice_address.setText(set.getString(4));
            invoice_paymentType.getModel().setSelectedItem(set.getString(5));
            invoice_PO_No.setText(set.getString(6));
            invoice_PO_date.setDate(cms.sdf.parse(set.getString(7)));
            invoice_Phone.setText(set.getString(8));
            invoice_InvoiceNo.setText(set.getString(9));
            invoice_Invoice_date.setDate(cms.sdf.parse(set.getString(10)));
            invoice_Product.setText(set.getString(11));
            invoice_Discription.setText(set.getString(12));
            invoice_Uom.setText(set.getString(13));
            invoice_taxGST.setText(set.getString(14));
            invoice_UnitPrice.setText(set.getString(15));
            invoice_discount.setText(set.getString(16));
            invoice_qty.setText(set.getString(17));
            invoice_hsn_code.setText(set.getString(18));
            bill_type.setText(set.getString(19));
            }
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                ps.close();
            } catch (Exception e) {
            }
        }
        
    }
        
        private void getInvoiceData(String column,String where)
    {
        //Inserting data into invoice table
        connection=ConnectionManager.getConnection();
        try {
            String query="select * from invoice where "+column+"='"+where+"'";
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
            invoice_customerName.setText(set.getString(2));
            invoice_gstType.setText(set.getString(3));
            invoice_address.setText(set.getString(4));
            invoice_paymentType.getModel().setSelectedItem(set.getString(5));
            invoice_PO_No.setText(set.getString(6));
            invoice_PO_date.setDate(cms.sdf.parse(set.getString(7)));
            invoice_Phone.setText(set.getString(8));
            invoice_InvoiceNo.setText(set.getString(9));
            invoice_Invoice_date.setDate(cms.sdf.parse(set.getString(10)));
            bill_type.setText(set.getString(19));

            }else
            {
            invoice_customerName.setText("");
            invoice_gstType.setText(invoice_gstType.getText());
            invoice_address.setText("");
            invoice_paymentType.setSelectedIndex(0);
            invoice_PO_No.setText("");
            invoice_PO_date.setDate(new Date());
            invoice_Phone.setText("");
//            invoice_InvoiceNo.setText(set.getString(9));
            invoice_Invoice_date.setDate(new Date());
//            bill_type.setText(set.getString(19));
            invoice_customerName.grabFocus();
            }
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                ps.close();
            } catch (Exception e) {
            }
        }
        
    }
        
         private void tableColumnSize()
	{
	    
//		for(int j=0; j<=15;j++)
//		{
		
			//table.getColumnModel().getColumn(j).setPreferredWidth(100);

			invoice_table.getColumnModel().getColumn(1).setPreferredWidth(200);
			invoice_table.getColumnModel().getColumn(2).setPreferredWidth(200);
			invoice_table.getColumnModel().getColumn(3).setPreferredWidth(50);
			invoice_table.getColumnModel().getColumn(4).setPreferredWidth(80);
			invoice_table.getColumnModel().getColumn(5).setPreferredWidth(80);
                        invoice_table.getColumnModel().getColumn(6).setPreferredWidth(100);
                        invoice_table.getColumnModel().getColumn(7).setPreferredWidth(100);
                        invoice_table.getColumnModel().getColumn(8).setPreferredWidth(50);
                        invoice_table.getColumnModel().getColumn(9).setPreferredWidth(100);
                        invoice_table.getColumnModel().getColumn(10).setPreferredWidth(100);
//                        invoice_table.getColumnModel().getColumn(11).setPreferredWidth(100);

                        
                        invoice_table.getColumnModel().getColumn(0).setMinWidth(0);
                        invoice_table.getColumnModel().getColumn(0).setMaxWidth(0);
                        invoice_table.getColumnModel().getColumn(0).setWidth(0);

                        

		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		for(int i=4; i<=10;i++)
		{
			invoice_table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
		}
		JTableHeader header = invoice_table.getTableHeader();
		header.setPreferredSize(new Dimension(100, 32));
		
		
//                JTable mainTable = new JTable();
//                JScrollPane scrollPane = new JScrollPane(mainTable);

                JTable rowTable = new RowNumberTable(invoice_table);
                invoiceTable_scrollPane.setRowHeaderView(rowTable);
                invoiceTable_scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                rowTable.getTableHeader());
                




                TableColumn sportColumn = invoice_table.getColumnModel().getColumn(1);

                JComboBox comboBox = new JComboBox();
                comboBox.setFont(new java.awt.Font("Dialog", 0, 14));
                

		
		
	}
    

         
         public void insertAction()
         {
             
             if(invoice_InvoiceNo.getText().trim().equals(""))
             {
                 JOptionPane.showMessageDialog(null, "Invoice No. must not be blank","",JOptionPane.WARNING_MESSAGE);
                 invoice_InvoiceNo.grabFocus();
                 return;
             }
             if(invoice_customerName.getText().trim().equals(""))
             {
                 JOptionPane.showMessageDialog(null, "Customer/Client name must not be blank","",JOptionPane.WARNING_MESSAGE);
                 invoice_customerName.grabFocus();
                 return;
             }
             if(invoice_Product.getText().trim().equals(""))
             {
                 JOptionPane.showMessageDialog(null, "Product name must not be blank","",JOptionPane.WARNING_MESSAGE);
                 invoice_Product.grabFocus();
                 return;
             }
             if(invoice_UnitPrice.getText().trim().equals("0") || invoice_UnitPrice.getText().trim().equals("0.00"))
             {
                 JOptionPane.showMessageDialog(null, "Unit price must be greater than 0","",JOptionPane.WARNING_MESSAGE);
                 invoice_UnitPrice.grabFocus();
                 return;
             }
             if(invoice_qty.getText().trim().equals("0") || invoice_qty.getText().trim().equals("0.00"))
             {
                 JOptionPane.showMessageDialog(null, "Quantity must be greater than 0","",JOptionPane.WARNING_MESSAGE);
                 invoice_qty.grabFocus();
                 return;
             }

              try {
            String query="select * from invoice where uid='"+uid+"'";
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
               int reply = JOptionPane.showConfirmDialog(this, "Do you want to save the changes ?", "Confirm?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                     updateIntoInvoice(uid);
                     showInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
  
                }
               
               
            }else
            {
                insertIntoInvoice();
            }
    } catch (Exception e) {
        
        
    }          

         

         
         }
         
         
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        invoice_address = new javax.swing.JTextArea();
        add_customer = new javax.swing.JLabel();
        invoice_gstType = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        invoice_customerName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        bill_type = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        invoice_InvoiceNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        invoice_Invoice_date = new com.toedter.calendar.JDateChooser(new Date());
        phone_search2 = new javax.swing.JLabel();
        invoice_Phone = new javax.swing.JTextField();
        search_invoice = new javax.swing.JLabel();
        new_invoice_icon = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        invoice_PO_No = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        invoice_paymentType = new javax.swing.JComboBox();
        invoice_PO_date = new com.toedter.calendar.JDateChooser(new Date());
        jLabel14 = new javax.swing.JLabel();
        invoiceTable_scrollPane = new javax.swing.JScrollPane(invoice_table);
        invoice_table = new javax.swing.JTable(){
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Alternate row color

                if (!isRowSelected(row))
                c.setBackground(row % 2 == 0 ? getBackground() : cms.light1);

                return c;
            }
        };
        jPanel4 = new javax.swing.JPanel();
        total_netAmt_label = new javax.swing.JLabel();
        total_amount_value = new javax.swing.JLabel();
        total_taxable_value = new javax.swing.JLabel();
        total_taxable_label = new javax.swing.JLabel();
        total_gst_val = new javax.swing.JLabel();
        total_gst_label = new javax.swing.JLabel();
        total_discount_value = new javax.swing.JLabel();
        total_discount_label = new javax.swing.JLabel();
        total_gross_label = new javax.swing.JLabel();
        total_gross_value = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        help_label = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        invoice_Discription = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        invoice_UnitPrice = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        invoice_discount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        invoice_qty = new javax.swing.JTextField();
        add_product = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        invoice_id_barcode = new javax.swing.JTextField();
        invoice_hsn_code = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        invoice_Product = new javax.swing.JTextField();
        invoice_Uom = new javax.swing.JTextField();
        invoice_taxGST = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("IA-Invoice");

        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel1.setText("Customer/Clinet :");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel2.setText("Address");

        invoice_address.setEditable(false);
        invoice_address.setColumns(20);
        invoice_address.setFont(new java.awt.Font("Dialog", 0, 14));
        invoice_address.setForeground(cms.light3);
        invoice_address.setRows(5);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, invoice_address, org.jdesktop.beansbinding.ELProperty.create("true"), invoice_address, org.jdesktop.beansbinding.BeanProperty.create("lineWrap"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(invoice_address);

        add_customer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        add_customer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/add.png"))); // NOI18N
        add_customer.setToolTipText("+ Add New");
        add_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add_customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_customerMouseClicked(evt);
            }
        });

        invoice_gstType.setFont(new java.awt.Font("Dialog", 0, 14));
        invoice_gstType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        invoice_gstType.setText("CGST/SGST");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/idea.png"))); // NOI18N
        jLabel21.setToolTipText("Tips");
        jLabel21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
        });

        invoice_customerName.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        invoice_customerName.setForeground(cms.light3);
        invoice_customerName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoice_customerNameFocusGained(evt);
            }
        });
        invoice_customerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoice_customerNameActionPerformed(evt);
            }
        });
        invoice_customerName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_customerNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_customerNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                invoice_customerNameKeyTyped(evt);
            }
        });

        bill_type.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        bill_type.setForeground(cms.light3);
        bill_type.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bill_type.setText("B2B");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(invoice_customerName, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add_customer))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(invoice_gstType, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bill_type, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoice_gstType, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add_customer, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(invoice_customerName))
                .addGap(21, 21, 21)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bill_type, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setOpaque(false);

        invoice_InvoiceNo.setFont(new java.awt.Font("Dialog", 1, 14));
        invoice_InvoiceNo.setForeground(new java.awt.Color(255, 255, 255));
        invoice_InvoiceNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoice_InvoiceNoActionPerformed(evt);
            }
        });
        invoice_InvoiceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_InvoiceNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_InvoiceNoKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel3.setText("Invoice Number :");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel6.setText("Invoice Date :");

        invoice_Invoice_date.setForeground(cms.light3);
        invoice_Invoice_date.setDateFormatString("dd-MM-yyyy");
        invoice_Invoice_date.setFont(new java.awt.Font("Dialog", 0, 12));

        phone_search2.setFont(new java.awt.Font("Dialog", 0, 14));
        phone_search2.setText("Phone No:");

        invoice_Phone.setFont(new java.awt.Font("Dialog", 0, 14));
        invoice_Phone.setForeground(cms.light3);
        invoice_Phone.setText("8317501567");

        search_invoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/magnifier.png"))); // NOI18N

        new_invoice_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/add.png"))); // NOI18N
        new_invoice_icon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        new_invoice_icon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new_invoice_iconMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(invoice_Invoice_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(phone_search2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                            .addComponent(invoice_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invoice_InvoiceNo, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(new_invoice_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(search_invoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(30, 30, 30))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(phone_search2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoice_Phone, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(search_invoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(invoice_InvoiceNo)
                    .addComponent(new_invoice_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(invoice_Invoice_date, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        invoice_InvoiceNo.setBackground(cms.light3);

        jPanel3.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel4.setText("Purchase Order No. ");

        invoice_PO_No.setFont(new java.awt.Font("Dialog", 0, 14));
        invoice_PO_No.setForeground(cms.light3);
        invoice_PO_No.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_PO_NoKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel5.setText("Payment Type");

        invoice_paymentType.setFont(new java.awt.Font("Dialog", 0, 14));
        invoice_paymentType.setForeground(cms.light3);
        invoice_paymentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CASH", "CREDIT", "NEFT/IMPS/RTGS", " " }));

        invoice_PO_date.setForeground(cms.light3);
        invoice_PO_date.setDateFormatString("dd-MM-yyyy");
        invoice_PO_date.setFont(new java.awt.Font("Dialog", 0, 12));

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel14.setText("Purchase Order Date ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(invoice_PO_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoice_PO_No, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoice_paymentType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(invoice_paymentType, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoice_PO_No, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoice_PO_date, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        invoiceTable_scrollPane.setBorder(null);
        invoiceTable_scrollPane.setAlignmentX(1.0F);
        invoiceTable_scrollPane.setAlignmentY(1.0F);
        invoiceTable_scrollPane.setFont(new java.awt.Font("Dialog", 0, 14));
        invoiceTable_scrollPane.setOpaque(false);

        invoice_table.setFont(new java.awt.Font("Dialog", 0, 14));
        invoice_table.setForeground(new java.awt.Color(-12566464,true));
        invoice_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        invoice_table.setGridColor(cms.light2);
        invoice_table.setOpaque(false);
        invoice_table.setRowHeight(20);
        invoice_table.setSelectionBackground(cms.light3);
        invoice_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoice_tableMouseClicked(evt);
            }
        });
        invoice_table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_tableKeyPressed(evt);
            }
        });
        invoiceTable_scrollPane.setViewportView(invoice_table);

        jPanel4.setOpaque(false);

        total_netAmt_label.setBackground(new java.awt.Color(255, 255, 255));
        total_netAmt_label.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        total_netAmt_label.setForeground(cms.light3);
        total_netAmt_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_netAmt_label.setText("Net Amt. :");

        total_amount_value.setBackground(cms.light1);
        total_amount_value.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        total_amount_value.setForeground(cms.light3);
        total_amount_value.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_amount_value.setText("0.00");
        total_amount_value.setOpaque(true);

        total_taxable_value.setBackground(cms.light1);
        total_taxable_value.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_taxable_value.setForeground(cms.light3);
        total_taxable_value.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_taxable_value.setText("0.00");
        total_taxable_value.setOpaque(true);

        total_taxable_label.setBackground(new java.awt.Color(255, 255, 255));
        total_taxable_label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_taxable_label.setForeground(cms.light3);
        total_taxable_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_taxable_label.setText("Taxable Amt. :");

        total_gst_val.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_gst_val.setForeground(cms.light3);
        total_gst_val.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_gst_val.setText("0.00");

        total_gst_label.setBackground(new java.awt.Color(255, 255, 255));
        total_gst_label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_gst_label.setForeground(cms.light3);
        total_gst_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_gst_label.setText("GST :");
        total_gst_label.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        total_discount_value.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_discount_value.setForeground(cms.light3);
        total_discount_value.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_discount_value.setText("0.00");

        total_discount_label.setBackground(new java.awt.Color(255, 255, 255));
        total_discount_label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_discount_label.setForeground(cms.light3);
        total_discount_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_discount_label.setText("Discount :");

        total_gross_label.setBackground(new java.awt.Color(255, 255, 255));
        total_gross_label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_gross_label.setForeground(cms.light3);
        total_gross_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_gross_label.setText("Gross Amt. :");

        total_gross_value.setBackground(cms.light1);
        total_gross_value.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total_gross_value.setForeground(cms.light3);
        total_gross_value.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_gross_value.setText("0.00");
        total_gross_value.setOpaque(true);

        jLabel17.setText("Help");

        help_label.setForeground(cms.light3);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 349, Short.MAX_VALUE))
                    .addComponent(help_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(help_label, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(total_gst_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_taxable_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_discount_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_gross_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_netAmt_label, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(total_gst_val, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total_taxable_value, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total_discount_value, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total_amount_value, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .addComponent(total_gross_value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(total_gross_value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(total_gross_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total_discount_label, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(total_discount_value, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(total_taxable_value, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(total_taxable_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(total_gst_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(total_gst_val, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(total_netAmt_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(total_amount_value, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Product");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel8.setText("Discription");

        invoice_Discription.setFont(new java.awt.Font("Dialog", 1, 14));
        invoice_Discription.setForeground(cms.light3);
        invoice_Discription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_DiscriptionKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel9.setText("UOM");

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel11.setText("Unit Price");

        invoice_UnitPrice.setFont(new java.awt.Font("Dialog", 1, 14));
        invoice_UnitPrice.setForeground(cms.light3);
        invoice_UnitPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        invoice_UnitPrice.setText("0");
        invoice_UnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoice_UnitPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                invoice_UnitPriceFocusLost(evt);
            }
        });
        invoice_UnitPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_UnitPriceKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel12.setText("Discount(%)");

        invoice_discount.setFont(new java.awt.Font("Dialog", 1, 14));
        invoice_discount.setForeground(cms.light3);
        invoice_discount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        invoice_discount.setText("0");
        invoice_discount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoice_discountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                invoice_discountFocusLost(evt);
            }
        });
        invoice_discount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_discountKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel13.setText("Tax (GST %)");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel10.setText("Qty");

        invoice_qty.setFont(new java.awt.Font("Dialog", 1, 14));
        invoice_qty.setForeground(cms.light3);
        invoice_qty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        invoice_qty.setText("0");
        invoice_qty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoice_qtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                invoice_qtyFocusLost(evt);
            }
        });
        invoice_qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_qtyKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_qtyKeyReleased(evt);
            }
        });

        add_product.setFont(new java.awt.Font("Dialog", 0, 14));
        add_product.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        add_product.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/add.png"))); // NOI18N
        add_product.setToolTipText("+ Add New Product");
        add_product.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add_product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_productMouseClicked(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel18.setForeground(cms.light3);
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("ID / Barcode :");

        invoice_id_barcode.setFont(new java.awt.Font("Dialog", 1, 10));
        invoice_id_barcode.setForeground(new java.awt.Color(-1,true));
        invoice_id_barcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_id_barcodeKeyPressed(evt);
            }
        });

        invoice_hsn_code.setForeground(cms.light3);
        invoice_hsn_code.setEnabled(false);
        invoice_hsn_code.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoice_hsn_codeActionPerformed(evt);
            }
        });

        jLabel20.setForeground(cms.light3);
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("HSN");

        invoice_Product.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        invoice_Product.setForeground(cms.light3);
        invoice_Product.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoice_ProductFocusGained(evt);
            }
        });
        invoice_Product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoice_ProductActionPerformed(evt);
            }
        });
        invoice_Product.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoice_ProductKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_ProductKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                invoice_ProductKeyTyped(evt);
            }
        });

        invoice_Uom.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        invoice_Uom.setForeground(cms.light3);

        invoice_taxGST.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        invoice_taxGST.setForeground(cms.light3);
        invoice_taxGST.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        invoice_taxGST.setText("0");
        invoice_taxGST.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeBorderColor")));
        invoice_taxGST.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                invoice_taxGSTFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                invoice_taxGSTFocusLost(evt);
            }
        });
        invoice_taxGST.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                invoice_taxGSTKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(invoice_Product))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add_product)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invoice_id_barcode)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invoice_hsn_code))
                    .addComponent(invoice_Discription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(invoice_Uom, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(invoice_taxGST, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoice_UnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoice_discount, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(invoice_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel10)
                                        .addComponent(invoice_hsn_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel20))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(add_product, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(invoice_id_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invoice_Discription, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invoice_Product)
                            .addComponent(invoice_Uom)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invoice_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(invoice_discount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                                .addComponent(invoice_UnitPrice, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(invoice_taxGST, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        invoice_id_barcode.setBackground(cms.light3);

        jMenu1.setText("File");

        jMenuItem4.setText("Exit             Esc");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem4MouseClicked(evt);
            }
        });
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenuItem4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jMenuItem4KeyPressed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(invoiceTable_scrollPane)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, 0, 195, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoiceTable_scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bindingGroup.bind();

        setSize(new java.awt.Dimension(1224, 707));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void invoice_PO_NoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_PO_NoKeyPressed
// TODO add your handling code here:
    if(evt.getKeyCode()==KeyEvent.VK_ENTER)
    {
//        showEmpData();
    }
}//GEN-LAST:event_invoice_PO_NoKeyPressed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    insertAction();
    
}//GEN-LAST:event_jButton1ActionPerformed

private void add_customerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_customerMouseClicked
// TODO add your handling code here:
    AddCustomer ad=new AddCustomer(this, rootPaneCheckingEnabled);
    ad.setVisible(true);
}//GEN-LAST:event_add_customerMouseClicked

private void invoice_InvoiceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_InvoiceNoKeyPressed
// TODO add your handling code here:
    if(evt.getKeyCode()==KeyEvent.VK_ENTER)
    {
        getInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
        showInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
    }
}//GEN-LAST:event_invoice_InvoiceNoKeyPressed

private void invoice_InvoiceNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoice_InvoiceNoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_invoice_InvoiceNoActionPerformed

private void invoice_UnitPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_UnitPriceFocusGained
// TODO add your handling code here:
    cms.textFieldFocusGained(invoice_UnitPrice);
}//GEN-LAST:event_invoice_UnitPriceFocusGained

private void invoice_UnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_UnitPriceFocusLost
// TODO add your handling code here:
    cms.textFieldFocusLost(invoice_UnitPrice);
}//GEN-LAST:event_invoice_UnitPriceFocusLost

private void invoice_discountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_discountFocusGained
// TODO add your handling code here:
    cms.textFieldFocusGained(invoice_discount);
}//GEN-LAST:event_invoice_discountFocusGained

private void invoice_discountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_discountFocusLost
// TODO add your handling code here:
    cms.textFieldFocusLost(invoice_discount);
}//GEN-LAST:event_invoice_discountFocusLost

private void invoice_qtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_qtyFocusGained
// TODO add your handling code here:
     cms.textFieldFocusGained(invoice_qty);
}//GEN-LAST:event_invoice_qtyFocusGained

private void invoice_qtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_qtyFocusLost
// TODO add your handling code here:
    cms.textFieldFocusLost(invoice_qty);
}//GEN-LAST:event_invoice_qtyFocusLost

private void add_productMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_productMouseClicked

    AddProduct ap=new AddProduct(this, rootPaneCheckingEnabled);
    ap.setVisible(true);
    
}//GEN-LAST:event_add_productMouseClicked

private void invoice_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoice_tableMouseClicked
// TODO add your handling code here:
         int reply = JOptionPane.showConfirmDialog(this, "Do you want to update product "+invoice_table.getValueAt(invoice_table.getSelectedRow(), 1)+"  ?", "Confirm?",  JOptionPane.YES_NO_OPTION);
         if (reply == JOptionPane.YES_OPTION)
         {
                 uid=invoice_table.getValueAt(invoice_table.getSelectedRow(), 0).toString();
                 fetchInvoiceData("UID", uid);
                     
         }

}//GEN-LAST:event_invoice_tableMouseClicked

private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
// TODO add your handling code here:
    cms.baloopTipOnJlabel(add_product, "<html>Click to add <br>new Customer</html>",BalloonTip.Orientation.LEFT_ABOVE);
    cms.baloopTipOnJlabel(add_customer, "<html>Click to add <br>new Product</html>",BalloonTip.Orientation.LEFT_ABOVE);
    cms.baloopTipOnJlabel(new_invoice_icon, "<html>Click for new invoice<br></html>",BalloonTip.Orientation.LEFT_ABOVE);
    cms.baloopTipOnJlabel(search_invoice, "<html>Click to search<br> all invoices</html>",BalloonTip.Orientation.LEFT_BELOW);

}//GEN-LAST:event_jLabel21MouseClicked

private void invoice_tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_tableKeyPressed
// TODO add your handling code here:
    if(evt.getKeyCode()==KeyEvent.VK_ENTER && invoice_table.isEditing())
    {
        
            JOptionPane.showMessageDialog(null, invoice_table.getValueAt(invoice_table.getSelectedRow(), invoice_table.getSelectedColumn()));
        
    }    
}//GEN-LAST:event_invoice_tableKeyPressed

    private void invoice_DiscriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_DiscriptionKeyPressed
                // TODO add your handling code here:
                if(evt.getKeyCode()==KeyEvent.VK_ENTER)
                {
                        System.out.println(invoice_Product.getText());
                }
    }//GEN-LAST:event_invoice_DiscriptionKeyPressed

    private void invoice_hsn_codeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoice_hsn_codeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoice_hsn_codeActionPerformed

    private void invoice_ProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoice_ProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoice_ProductActionPerformed

    private void invoice_ProductKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_ProductKeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_invoice_ProductKeyPressed

    private void invoice_ProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_ProductKeyReleased
   
        cms.validateForTextInput(invoice_Product);
        
        SearchContainer sc=new SearchContainer(this, rootPaneCheckingEnabled);
        sc.search_item.setText(invoice_Product.getText());
        sc.setPart1Query("select ID,UID,PRODUCT_NAME ,DISCTIPTION ,UOM ,UNIT_PRICE as PRICE,GST_TAX,DISCOUNT as DISC from PRODUCT where lower(concat(UID,' ',PRODUCT_NAME,' ',DISCTIPTION,' UOM=', UOM,' PRICE=',UNIT_PRICE,' TAX=',GST_TAX,' DISC=',DISCOUNT)) ");
        sc.container_tooltip.setToolTipText("<html>Search Anything like Product Name, Discription, UOM,Unit Price,<br> TAX(GST %) or Discount(%) in the below field and Press DOWN ARROW<br> to select the product</html>");
        sc.contailer_specfic_toolTip.setText("You can search product specifically with PRICE, TAX,DISC,UOM (eg: PRICE=20.19)");
        sc.container_title.setText("Select Product and Press ENTER");
        invoice_Product.setText("");
        sc.setVisible(true);
        
                

        try {
        String query="select * from PRODUCT  where ID='"+sc.getSearchID()+"'";
        System.out.println(query);
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
            invoice_id_barcode.setText(set.getString(2));
            invoice_Product.setText(set.getString(3));
            invoice_Discription.setText(set.getString(4));
            invoice_Uom.setText(set.getString(5));
            invoice_hsn_code.setText(set.getString(6));
            invoice_UnitPrice.setText(set.getString(7));
            invoice_taxGST.setText(set.getString(8));
            invoice_discount.setText(set.getString(9));
            invoice_qty.grabFocus();
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                st.close();
                set.close();
            } catch (Exception e) {
            }
        }
        
        
    }//GEN-LAST:event_invoice_ProductKeyReleased

    private void invoice_customerNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_customerNameKeyReleased
        cms.validateForTextInput(invoice_customerName);
        
        SearchContainer sc=new SearchContainer(this, rootPaneCheckingEnabled);
        sc.search_item.setText(invoice_customerName.getText());
        sc.setPart1Query("SELECT ID, "
                + "UID,"
                + "NAME,"
                + "GSTIN,"
                + "GST_TYPE,"
                + "concat(ADDRESS1,', ',ADDRESS2) as Address,"
                + "PHONE, "
                + "EMAIL FROM CUSTOMER where lower(concat(UID,' ',NAME,GSTIN,' ',GST_TYPE,' ',concat(ADDRESS1,', ',ADDRESS2),' ',PHONE,' ', EMAIL)) ");
        sc.container_title.setText("Select Customer and Press ENTER");
        sc.container_tooltip.setToolTipText("<html>Search Anything like UID, Name, GSTIN, Address,Phone and EMAIL<br> in the below field and Press DOWN ARROW<br> to select the Customer/Client</html>");
        sc.contailer_specfic_toolTip.setText("You can search customer specifically with UID, Name, GSTIN, Address,Phone and EMAIL");
        invoice_customerName.setText("");
        sc.setVisible(true);
        
        try {
            String query="select * from CUSTOMER where ID='"+sc.getSearchID()+"'";
            System.out.println(query);
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {

            invoice_customerName.setText(set.getString(3));
            invoice_address.setText(set.getString(4)+", \n"+set.getString(6)+", "+set.getString(7));

            invoice_gstType.setText(set.getString(5));
            invoice_Phone.setText(set.getString(8));
            invoice_id_barcode.grabFocus();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            try {
                connection.close();
                st.close();
                set.close();
            } catch (Exception e) {
            }
        }
        
          
        
    }//GEN-LAST:event_invoice_customerNameKeyReleased

    private void invoice_customerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_customerNameKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE)
        {
            dispose();
        }
        

        
    }//GEN-LAST:event_invoice_customerNameKeyPressed

    private void invoice_ProductKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_ProductKeyTyped
       

    }//GEN-LAST:event_invoice_ProductKeyTyped

    private void invoice_customerNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_customerNameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_invoice_customerNameKeyTyped

    private void invoice_qtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_qtyKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            insertAction();
        }
    }//GEN-LAST:event_invoice_qtyKeyPressed

    private void invoice_customerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoice_customerNameActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_invoice_customerNameActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMenuItem4KeyPressed
        // TODO add your handling code here:
        
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE)
        {
            
        }
    }//GEN-LAST:event_jMenuItem4KeyPressed

    private void jMenuItem4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jMenuItem4MouseClicked

    private void new_invoice_iconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_new_invoice_iconMouseClicked
        // TODO add your handling code here:
        cms.generateUniqueID("INVOICE", "INVOICE_NO");
        invoice_InvoiceNo.setText(cms.id);
        getInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
        showInvoiceData("INVOICE_NO",invoice_InvoiceNo.getText());
    }//GEN-LAST:event_new_invoice_iconMouseClicked

    private void invoice_taxGSTFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_taxGSTFocusLost
        // TODO add your handling code here:
        cms.textFieldFocusLost(invoice_taxGST);
    }//GEN-LAST:event_invoice_taxGSTFocusLost

    private void invoice_taxGSTFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_taxGSTFocusGained
        // TODO add your handling code here:
        cms.textFieldFocusGained(invoice_taxGST);
    }//GEN-LAST:event_invoice_taxGSTFocusGained

    private void invoice_qtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_qtyKeyReleased
            // TODO add your handling code here:
            cms.validateForNumber(invoice_qty);
    }//GEN-LAST:event_invoice_qtyKeyReleased

    private void invoice_taxGSTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_taxGSTKeyReleased
        // TODO add your handling code here:
            cms.validateForNumber(invoice_taxGST);
    }//GEN-LAST:event_invoice_taxGSTKeyReleased

    private void invoice_UnitPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_UnitPriceKeyReleased
        // TODO add your handling code here:
            cms.validateForNumber(invoice_UnitPrice);
    }//GEN-LAST:event_invoice_UnitPriceKeyReleased

    private void invoice_discountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_discountKeyReleased
        // TODO add your handling code here:
            cms.validateForNumber(invoice_discount);
    }//GEN-LAST:event_invoice_discountKeyReleased

    private void invoice_InvoiceNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_InvoiceNoKeyReleased
        // TODO add your handling code here:
        cms.validateForTextInput(invoice_InvoiceNo);
        invoice_InvoiceNo.setText(invoice_InvoiceNo.getText().toUpperCase());
    }//GEN-LAST:event_invoice_InvoiceNoKeyReleased

    private void invoice_id_barcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoice_id_barcodeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            fetchProductData();
        }
    }//GEN-LAST:event_invoice_id_barcodeKeyPressed

    private void invoice_customerNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_customerNameFocusGained
        // TODO add your handling code here:
        cms.validateForTextInput(invoice_customerName);

    }//GEN-LAST:event_invoice_customerNameFocusGained

    private void invoice_ProductFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_invoice_ProductFocusGained
        // TODO add your handling code here:
        cms.validateForTextInput(invoice_Product);
    }//GEN-LAST:event_invoice_ProductFocusGained

 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        
            
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                
                new Dashboard().setVisible(true);
            }
        });
        

       
    }
    

    
        private static boolean employeeFilter(Employee emp, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getEmployeeDisplayText(emp).toLowerCase()
                                  .contains(textToFilter.toLowerCase());
        
       
        }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel add_customer;
    public javax.swing.JLabel add_product;
    public javax.swing.JLabel bill_type;
    private javax.swing.JLabel help_label;
    public javax.swing.JScrollPane invoiceTable_scrollPane;
    private javax.swing.JTextField invoice_Discription;
    private javax.swing.JTextField invoice_InvoiceNo;
    private com.toedter.calendar.JDateChooser invoice_Invoice_date;
    private javax.swing.JTextField invoice_PO_No;
    private com.toedter.calendar.JDateChooser invoice_PO_date;
    private javax.swing.JTextField invoice_Phone;
    private javax.swing.JTextField invoice_Product;
    private javax.swing.JTextField invoice_UnitPrice;
    private javax.swing.JTextField invoice_Uom;
    private javax.swing.JTextArea invoice_address;
    private javax.swing.JTextField invoice_customerName;
    private javax.swing.JTextField invoice_discount;
    public javax.swing.JLabel invoice_gstType;
    public javax.swing.JTextField invoice_hsn_code;
    public javax.swing.JTextField invoice_id_barcode;
    private javax.swing.JComboBox invoice_paymentType;
    private javax.swing.JTextField invoice_qty;
    public javax.swing.JTable invoice_table;
    private javax.swing.JTextField invoice_taxGST;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel new_invoice_icon;
    public javax.swing.JLabel phone_search2;
    public javax.swing.JLabel search_invoice;
    public javax.swing.JLabel total_amount_value;
    private javax.swing.JLabel total_discount_label;
    private javax.swing.JLabel total_discount_value;
    public javax.swing.JLabel total_gross_label;
    public javax.swing.JLabel total_gross_value;
    private javax.swing.JLabel total_gst_label;
    private javax.swing.JLabel total_gst_val;
    public javax.swing.JLabel total_netAmt_label;
    public javax.swing.JLabel total_taxable_label;
    public javax.swing.JLabel total_taxable_value;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    


}
