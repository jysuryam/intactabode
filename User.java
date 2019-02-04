/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * User.java
 *
 * Created on 3 Jan, 2019, 11:40:22 AM
 */
package javaapplication3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.utils.FadingUtils;
import net.java.balloontip.utils.TimingUtils;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author J Suryam
 */
public class User extends javax.swing.JDialog {

    Connection connection=null;
    Statement st=null;
    PreparedStatement ps=null;
    ResultSet set=null;
    CommonMethodClass cms=new CommonMethodClass("");
    DefaultTableModel model=new DefaultTableModel();
    String row_id=null;
    String gstNumber="Select Row to view";
    String email="";
    
    
    
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form User */
    public User(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getContentPane().setBackground(Color.WHITE); // Background Color

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
        
        showCustmerData("name","");
        cms.generateUniqueID("USER", "UID");
        customer_id.setText(cms.id);
        cms.tableHeaderSettings(customer_table);
    }

    	
    
    public void showCustmerData(String column, String like)
    {
        try {
            connection=ConnectionManager.getConnection();
        String query="SELECT ID,"
                + "UID ,"
                + "NAME,"
                + "GST_TYPE AS TYPE,"
                + "CONCAT(ADDRESS1,',',ADDRESS2) AS ADDRESS,"
                + "PHONE"
                + " FROM USER where "+column+" like '%"+like+"%' "
                +" order by id desc";
            System.out.println(query);
        st=connection.createStatement();
        set=st.executeQuery(query);
        customer_table.setModel(DbUtils.resultSetToTableModel(set));
        //Table column size
        tableColumnSize();
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
    
    public void clearFields()
    {
            row_id="";
            customer_id.setText("");
            customer_Name.setText("");
            customer_gstIN.setText("");
            customer_type.setSelectedIndex(0);
            customer_address1.setText("");
            customer_address2.setText("");
            customer_phone.setText("");
            customer_email.setText("");
            
            showCustmerData("Name","");
            cms.generateUniqueID("USER", "UID");
            customer_id.setText(cms.id);
    }
    
    
    private void tableColumnSize()
	{
	    
//		for(int j=0; j<=15;j++)
//		{
		
			//customer_table.getColumnModel().getColumn(j).setPreferredWidth(100);

			customer_table.getColumnModel().getColumn(1).setPreferredWidth(60);
			customer_table.getColumnModel().getColumn(2).setPreferredWidth(150);
			customer_table.getColumnModel().getColumn(3).setPreferredWidth(80);
			customer_table.getColumnModel().getColumn(4).setPreferredWidth(100);
//			customer_table.getColumnModel().getColumn(5).setPreferredWidth(150);

                        
                        customer_table.getColumnModel().getColumn(0).setMinWidth(0);
                        customer_table.getColumnModel().getColumn(0).setMaxWidth(0);
                        customer_table.getColumnModel().getColumn(0).setWidth(0);

                        

		
//		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//		for(int i=0; i<=6;i++)
//		{
//			customer_table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
//		}
//		JTableHeader header = customer_table.getTableHeader();
//		header.setPreferredSize(new Dimension(100, 32));
		
		
		
		
		
	}
    
    
    public void fetchCustomerData(String id)
    {
        
        String query="select * from USER where ID='"+id+"'";
        System.out.println(query);
        try {
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
            customer_id.setText(set.getString(2));
            customer_Name.setText(set.getString(3));
            customer_gstIN.setText(set.getString(4));
            //Setting variable for hover
            gstNumber=set.getString(4);
            customer_type.setSelectedItem(set.getString(5));
            customer_address1.setText(set.getString(6));
            customer_address2.setText(set.getString(7));
            customer_phone.setText(set.getString(8));
            customer_email.setText(set.getString(9));
            //Setting variable for hover
            email=set.getString(9);
            
                System.out.println(gstNumber+" "+email);
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
    
    
    public void insertIntoCustomers()
    {   
//        cms.JTextieldValidation(customer_id, "ID must not be empty", this);
//        cms.JTextieldValidation(customer_Name, "Name must not be empty", this);
//        cms.fieldValidation(customer_Name, , "Name must not be Empty");
        
        
        // Check wheather data is existing
        try {
            String query="select * from customer where uid='"+customer_id.getText()+"'";
            connection=ConnectionManager.getConnection();
            st=connection.createStatement();
            set=st.executeQuery(query);
            if(set.next())
            {
               int reply = JOptionPane.showConfirmDialog(this, "Do you want to update "+customer_id.getText()+" data ?", "Confirm?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                     updateCustomerData("UID", customer_id.getText());
                     showCustmerData("NAME","");
                     
                }
               
               
            }else
            {
                String query1="insert into USER  values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            connection=ConnectionManager.getConnection();
            ps=connection.prepareStatement(query1);
            ps.setString(1, cms.csdf.format(new Date()));
            ps.setString(2, customer_id.getText());
            ps.setString(3, customer_Name.getText());
            ps.setString(4, customer_gstIN.getText());
            ps.setString(5, customer_type.getSelectedItem().toString());
            ps.setString(6,customer_address1.getText());
            ps.setString(7,customer_address2.getText());
            ps.setString(8, customer_phone.getText());
            ps.setString(9, customer_email.getText());
            ps.setString(10,user_bankname_bankType.getText());
            ps.setString(11,user_bankAccountNo.getText());
            ps.setString(12, user_ifscCode.getText());
            ps.setString(13, user_accountMessage.getText());
            int i=ps.executeUpdate();
            if(i>0)
            {
            JOptionPane.showMessageDialog(this, "User added successful","successful",JOptionPane.INFORMATION_MESSAGE,cms.icon);
            showCustmerData("NAME","");
            clearFields();
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
            
        } catch (Exception e) {
        }
        
        
        
    }
    
    public void updateCustomerData(String column,String id)
    {
        String query="update USER "
                + "set uid=?,"
                + "NAME =?,"
                + "GSTIN  =?,"
                + "GST_TYPE  =?,"
                + "ADDRESS1  =?,"
                + "ADDRESS2  =?,"
                + "PHONE  =?,"
                + "EMAIL  =? "
                + "where "+column+" =?";
        
        try {
            connection=ConnectionManager.getConnection();
            ps=connection.prepareStatement(query);

            ps.setString(1, customer_id.getText());
            ps.setString(2, customer_Name.getText());
            ps.setString(3, customer_gstIN.getText());
            ps.setString(4, customer_type.getSelectedItem().toString());
            ps.setString(5,customer_address1.getText());
            ps.setString(6,customer_address2.getText());
            ps.setString(7, customer_phone.getText());
            ps.setString(8, customer_email.getText());
            ps.setString(9,user_bankname_bankType.getText());
            ps.setString(10,user_bankAccountNo.getText());
            ps.setString(11, user_ifscCode.getText());
            ps.setString(12, user_accountMessage.getText());
            ps.setString(13, id);
            int i=ps.executeUpdate();
            if(i>0)
            {
            JOptionPane.showMessageDialog(this, "Customer data updated successful","successful",JOptionPane.INFORMATION_MESSAGE,cms.icon);
            showCustmerData("NAME","");
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
    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customer_table = new javax.swing.JTable(){
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Alternate row color

                if (!isRowSelected(row))
                c.setBackground(row % 2 == 0 ? getBackground() : cms.light1);

                return c;
            }
        };
        ;
        jPanel3 = new javax.swing.JPanel();
        customer_id = new javax.swing.JTextField();
        customer_Name = new javax.swing.JTextField();
        customer_gstIN = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        customer_type = new javax.swing.JComboBox();
        customer_address1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        customer_address2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        customer_phone = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        customer_email = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        save_label = new javax.swing.JLabel();
        id_search = new javax.swing.JLabel();
        phone_search = new javax.swing.JLabel();
        name_search = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        user_accountMessage = new javax.swing.JTextField();
        user_ifscCode = new javax.swing.JTextField();
        user_bankAccountNo = new javax.swing.JTextField();
        user_bankname_bankType = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        jButton1.setText("Save");

        setTitle("Add Customer/Client");
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setForeground(new java.awt.Color(-1,true));
        jPanel1.setOpaque(false);

        jLabel1.setBackground(cms.light1);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18));
        jLabel1.setForeground(cms.light3);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("USER");
        jLabel1.setOpaque(true);

        jPanel2.setOpaque(false);

        jScrollPane1.setForeground(new java.awt.Color(-4144960,true));

        customer_table.setFont(new java.awt.Font("SansSerif", 0, 12));
        customer_table.setForeground(new java.awt.Color(-12566464,true));
        customer_table.setModel(new javax.swing.table.DefaultTableModel(
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
        customer_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        customer_table.setGridColor(cms.light2);
        customer_table.setRowHeight(20);
        customer_table.setSelectionBackground(cms.light3);
        customer_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customer_tableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                customer_tableMouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customer_tableMouseReleased(evt);
            }
        });
        customer_table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                customer_tableMouseMoved(evt);
            }
        });
        customer_table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customer_tableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customer_tableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(customer_table);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
        );

        jPanel3.setOpaque(false);

        customer_id.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_idActionPerformed(evt);
            }
        });
        customer_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customer_idKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customer_idKeyTyped(evt);
            }
        });

        customer_Name.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_NameActionPerformed(evt);
            }
        });
        customer_Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customer_NameKeyTyped(evt);
            }
        });

        customer_gstIN.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_gstIN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_gstINActionPerformed(evt);
            }
        });
        customer_gstIN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customer_gstINKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customer_gstINKeyTyped(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("ID :");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Name :");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("GSTIN No.");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("GST Type :");

        customer_type.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CGST/SGST", "IGST" }));
        customer_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_typeActionPerformed(evt);
            }
        });

        customer_address1.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_address1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_address1ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Address Line 1 :");

        customer_address2.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_address2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_address2ActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Address Line 2 :");

        customer_phone.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_phoneActionPerformed(evt);
            }
        });
        customer_phone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customer_phoneKeyTyped(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Phone :");

        customer_email.setFont(new java.awt.Font("Dialog", 0, 12));
        customer_email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customer_emailKeyTyped(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Email :");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel10.setForeground(new java.awt.Color(-8355712,true));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/cancel.png"))); // NOI18N
        jLabel10.setText("Close");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
        });

        save_label.setFont(new java.awt.Font("Dialog", 0, 14));
        save_label.setForeground(new java.awt.Color(-8355712,true));
        save_label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        save_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/save.png"))); // NOI18N
        save_label.setText("Save");
        save_label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        save_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                save_labelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                save_labelMouseEntered(evt);
            }
        });

        id_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/magnifier.png"))); // NOI18N
        id_search.setToolTipText("search ID");
        id_search.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        id_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id_searchMouseClicked(evt);
            }
        });

        phone_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/magnifier.png"))); // NOI18N
        phone_search.setToolTipText("search Phone");
        phone_search.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        phone_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                phone_searchMouseClicked(evt);
            }
        });

        name_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/magnifier.png"))); // NOI18N
        name_search.setToolTipText("search Name");
        name_search.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        name_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                name_searchMouseClicked(evt);
            }
        });

        jLabel11.setBackground(cms.light3);
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Add Bank Details if you want to display on Invoce Template");
        jLabel11.setOpaque(true);

        user_accountMessage.setFont(new java.awt.Font("Dialog", 0, 12));
        user_accountMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                user_accountMessageKeyTyped(evt);
            }
        });

        user_ifscCode.setFont(new java.awt.Font("Dialog", 0, 12));
        user_ifscCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_ifscCodeActionPerformed(evt);
            }
        });
        user_ifscCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                user_ifscCodeKeyTyped(evt);
            }
        });

        user_bankAccountNo.setFont(new java.awt.Font("Dialog", 0, 12));
        user_bankAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_bankAccountNoActionPerformed(evt);
            }
        });

        user_bankname_bankType.setFont(new java.awt.Font("Dialog", 0, 12));
        user_bankname_bankType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_bankname_bankTypeActionPerformed(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Bank,Branch & Type :");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("A/C No. :");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("IFSC Code :");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Message :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customer_address1)
                            .addComponent(customer_gstIN)
                            .addComponent(customer_Name)
                            .addComponent(customer_email)
                            .addComponent(customer_phone)
                            .addComponent(customer_address2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(customer_type, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(customer_id, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(id_search, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(name_search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(phone_search, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(save_label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addGap(50, 50, 50))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(user_bankname_bankType)
                                    .addComponent(user_bankAccountNo)
                                    .addComponent(user_ifscCode)
                                    .addComponent(user_accountMessage))
                                .addGap(41, 41, 41))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customer_id)
                        .addComponent(id_search, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customer_Name)
                        .addComponent(name_search, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel4))
                    .addComponent(customer_gstIN, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customer_type, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customer_address1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customer_address2, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(customer_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(customer_email, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(phone_search, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(user_bankname_bankType, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(user_bankAccountNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(user_ifscCode, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(user_accountMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(save_label, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)))
        );

        jLabel12.setBackground(cms.light1);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/idea.png"))); // NOI18N
        jLabel12.setToolTipText("Tips");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(668, 795));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

private void customer_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseClicked
// TODO add your handling code here:
    row_id=customer_table.getValueAt(customer_table.getSelectedRow(),0).toString();
    fetchCustomerData(row_id);
    
}//GEN-LAST:event_customer_tableMouseClicked

private void customer_tableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseEntered
// TODO add your handling code here:
    
}//GEN-LAST:event_customer_tableMouseEntered

private void customer_tableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseMoved
// TODO add your handling code here:
    
}//GEN-LAST:event_customer_tableMouseMoved

private void customer_tableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_tableMouseReleased
// TODO add your handling code here:
    
}//GEN-LAST:event_customer_tableMouseReleased

private void customer_tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_tableKeyPressed

}//GEN-LAST:event_customer_tableKeyPressed

private void customer_tableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_tableKeyReleased
// TODO add your handling code here:
             row_id=customer_table.getValueAt(customer_table.getSelectedRow(),0).toString();
             fetchCustomerData(row_id);
}//GEN-LAST:event_customer_tableKeyReleased

private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
// TODO add your handling code here:
    cms.baloopTipOnJlabel(id_search, "<html>Enter existing ID<br> to search records</html>", BalloonTip.Orientation.RIGHT_ABOVE);
    cms.baloopTipOnJlabel(name_search, "<html>Enter existing name<br> to search records</html>",BalloonTip.Orientation.RIGHT_ABOVE);
    cms.baloopTipOnJlabel(phone_search, "<html>Enter existing phone number<br> to search records</html>",BalloonTip.Orientation.RIGHT_ABOVE);
    cms.baloopTipOnJlabel(save_label,"<html>Click save to insert and<br>update records</html>",BalloonTip.Orientation.RIGHT_ABOVE );
}//GEN-LAST:event_jLabel12MouseClicked

    private void name_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_name_searchMouseClicked
        // TODO add your handling code here:
        showCustmerData("upper(NAME)",customer_Name.getText().toUpperCase());
    }//GEN-LAST:event_name_searchMouseClicked

    private void phone_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_phone_searchMouseClicked
        // TODO add your handling code here:
        showCustmerData("upper(phone)",customer_phone.getText().toUpperCase());
    }//GEN-LAST:event_phone_searchMouseClicked

    private void id_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id_searchMouseClicked
        // TODO add your handling code here:
        showCustmerData("upper(UID)",customer_id.getText().toUpperCase());
    }//GEN-LAST:event_id_searchMouseClicked

    private void save_labelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_save_labelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_save_labelMouseEntered

    private void save_labelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_save_labelMouseClicked
        // TODO add your handling code here:
        insertIntoCustomers();
    }//GEN-LAST:event_save_labelMouseClicked

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void customer_emailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_emailKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_emailKeyTyped

    private void customer_phoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_phoneKeyTyped
        // TODO add your handling code here:
        showCustmerData("upper(phone)",customer_phone.getText().toUpperCase());
    }//GEN-LAST:event_customer_phoneKeyTyped

    private void customer_phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_phoneActionPerformed

    private void customer_address2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_address2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_address2ActionPerformed

    private void customer_address1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_address1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_address1ActionPerformed

    private void customer_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_typeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_typeActionPerformed

    private void customer_gstINKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_gstINKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_gstINKeyTyped

    private void customer_gstINKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_gstINKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_gstINKeyPressed

    private void customer_gstINActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_gstINActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_gstINActionPerformed

    private void customer_NameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_NameKeyTyped
        // TODO add your handling code here:
        showCustmerData("upper(NAME)",customer_Name.getText().toUpperCase());
    }//GEN-LAST:event_customer_NameKeyTyped

    private void customer_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_NameActionPerformed

    private void customer_idKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_idKeyTyped
        // TODO add your handling code here:
        showCustmerData("upper(UID)",customer_id.getText().toUpperCase());
    }//GEN-LAST:event_customer_idKeyTyped

    private void customer_idKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_idKeyReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_customer_idKeyReleased

    private void customer_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customer_idActionPerformed

    private void user_accountMessageKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_user_accountMessageKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_user_accountMessageKeyTyped

    private void user_ifscCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_ifscCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_ifscCodeActionPerformed

    private void user_ifscCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_user_ifscCodeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_user_ifscCodeKeyTyped

    private void user_bankAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_bankAccountNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_bankAccountNoActionPerformed

    private void user_bankname_bankTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_bankname_bankTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_bankname_bankTypeActionPerformed
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

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
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                User dialog = new User(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
                        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField customer_Name;
    public javax.swing.JTextField customer_address1;
    public javax.swing.JTextField customer_address2;
    public javax.swing.JTextField customer_email;
    public javax.swing.JTextField customer_gstIN;
    public javax.swing.JTextField customer_id;
    public javax.swing.JTextField customer_phone;
    private javax.swing.JTable customer_table;
    public javax.swing.JComboBox customer_type;
    private javax.swing.JLabel id_search;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel name_search;
    public javax.swing.JLabel phone_search;
    public javax.swing.JLabel save_label;
    public javax.swing.JTextField user_accountMessage;
    public javax.swing.JTextField user_bankAccountNo;
    public javax.swing.JTextField user_bankname_bankType;
    public javax.swing.JTextField user_ifscCode;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    
    
}
