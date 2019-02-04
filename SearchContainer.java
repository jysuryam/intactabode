/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SearchContainer.java
 *
 * Created on 3 Jan, 2019, 6:41:29 PM
 */
package javaapplication3;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import net.java.balloontip.BalloonTip;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author J Suryam
 */
public class SearchContainer extends javax.swing.JDialog {

    /**
     * @return the searchID
     */
    public String getSearchID() {
        return searchID;
    }

    /**
     * @param searchID the searchID to set
     */
    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }

    /**
     * @return the part1Query
     */
    public String getPart1Query() {
        return part1Query;
    }

    /**
     * @param part1Query the part1Query to set
     */
    public void setPart1Query(String part1Query) {
        this.part1Query = part1Query;
    }

    /**
     * @return the part2Query
     */
//    public String getPart2Query() {
//        return part2Query;
//    }
//
//    /**
//     * @param part2Query the part2Query to set
//     */
//    public void setPart2Query(String part2Query) {
//        this.part2Query = part2Query;
//    }

    
    CommonMethodClass cms=new CommonMethodClass(null);
    Connection connection=null;
    Statement st=null;
    PreparedStatement ps=null;
    ResultSet set=null;
   
    
    
    private String searchID;
    private String part1Query;
//    private String part2Query;
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form SearchContainer */
    
    
    
             private void tableColumnSize()
	{
	    
//		for(int j=0; j<=15;j++)
//		{
		
			//table.getColumnModel().getColumn(j).setPreferredWidth(100);

			search_table.getColumnModel().getColumn(1).setPreferredWidth(80);
			search_table.getColumnModel().getColumn(2).setPreferredWidth(200);
			search_table.getColumnModel().getColumn(3).setPreferredWidth(200);


                        
                        search_table.getColumnModel().getColumn(0).setMinWidth(0);
                        search_table.getColumnModel().getColumn(0).setMaxWidth(0);
                        search_table.getColumnModel().getColumn(0).setWidth(0);

                        

		
//		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//		for(int i=5; i<=11;i++)
//		{
//			search_table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
//		}
//		JTableHeader header = search_table.getTableHeader();
//		header.setPreferredSize(new Dimension(100, 32));
//		
//		
//                
//                TableColumn sportColumn = search_table.getColumnModel().getColumn(1);
//
//                JComboBox comboBox = new JComboBox();
//                comboBox.setFont(new java.awt.Font("Dialog", 0, 14));
//                
//                //comboBox.setEditable(true);
//                comboBox.addItem("Snowboarding");
//                comboBox.addItem("Rowing");
//                comboBox.addItem("Chasing toddlers");
//                comboBox.addItem("Speed reading");
//                comboBox.addItem("Teaching high school");
//                comboBox.addItem("None");
//                sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		
		
		
	}
    
    

    private void createKeybindings(JTable table) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        table.getActionMap().put("Enter", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent ae) {
           
            dispose();
           setSearchID(search_table.getValueAt(search_table.getSelectedRow(), 0).toString());
            
        
        }
    });
}         
             
    public SearchContainer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

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
        
              cms.tableHeaderSettings(search_table);
    }
    
        public void searchInContainer(JTable table,JTextField searchField)
        {
            try {
                String qualifiedQuery=getPart1Query()+"like '%"+searchField.getText().toLowerCase()+"%'";
                System.out.println(qualifiedQuery);
                connection=ConnectionManager.getConnection();
                st=connection.createStatement();
                set=st.executeQuery(qualifiedQuery);
                table.setModel(DbUtils.resultSetToTableModel(set));
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

        jPanel1 = new javax.swing.JPanel();
        container_title = new javax.swing.JLabel();
        search_item = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        container_tooltip = new javax.swing.JLabel();
        contailer_specfic_toolTip = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        search_table = new javax.swing.JTable();
        ;
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        container_title.setBackground(cms.light1);
        container_title.setFont(new java.awt.Font("Dialog", 0, 18));
        container_title.setForeground(cms.light3);
        container_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        container_title.setText("Search for Customers");
        container_title.setOpaque(true);

        search_item.setBackground(cms.light3);
        search_item.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        search_item.setForeground(new java.awt.Color(255, 255, 255));
        search_item.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                search_itemComponentRemoved(evt);
            }
        });
        search_item.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                search_itemFocusGained(evt);
            }
        });
        search_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_itemActionPerformed(evt);
            }
        });
        search_item.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_itemKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search_itemKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14));
        jLabel3.setText("Search Product");

        container_tooltip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/idea.png"))); // NOI18N
        container_tooltip.setToolTipText("");
        container_tooltip.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        container_tooltip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                container_tooltipMouseClicked(evt);
            }
        });

        contailer_specfic_toolTip.setForeground(cms.light3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(container_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(search_item)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(container_tooltip)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contailer_specfic_toolTip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(container_title, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(container_tooltip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contailer_specfic_toolTip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search_item, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        search_table.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        search_table.setModel(new javax.swing.table.DefaultTableModel(
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
        search_table.setGridColor(cms.light2);
        search_table.setRowHeight(20);
        search_table.setSelectionBackground(cms.light3);
        search_table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                search_tableFocusGained(evt);
            }
        });
        search_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search_tableMouseClicked(evt);
            }
        });
        search_table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search_tableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search_tableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(search_table);

        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1030, 534));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
    dispose();
}//GEN-LAST:event_jButton2ActionPerformed

private void search_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_itemActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_search_itemActionPerformed

    private void search_itemComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_search_itemComponentRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_search_itemComponentRemoved

    private void search_itemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_itemKeyPressed
        // TODO add your handling code here:
//        cms.searchInContainer(search_table, " like '%"+search_item.getText().toLowerCase()+"%'");
        
        if(evt.getKeyCode()==KeyEvent.VK_DOWN)
        {
            search_table.grabFocus();
            search_table.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_search_itemKeyPressed

    private void search_itemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_itemKeyReleased
        // TODO add your handling code here:
               searchInContainer(search_table, search_item);
                

    }//GEN-LAST:event_search_itemKeyReleased

    private void search_tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_tableKeyPressed

    }//GEN-LAST:event_search_tableKeyPressed

    private void container_tooltipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_container_tooltipMouseClicked
        // TODO add your handling code here:
        cms.baloopTipOnJlabel(container_tooltip,container_tooltip.getToolTipText(), BalloonTip.Orientation.RIGHT_BELOW);
    }//GEN-LAST:event_container_tooltipMouseClicked

    private void search_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search_tableMouseClicked
        // TODO add your handling code here:
        dispose();
        setSearchID(search_table.getValueAt(search_table.getSelectedRow(), 0).toString());

    }//GEN-LAST:event_search_tableMouseClicked

    private void search_itemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_search_itemFocusGained
        // TODO add your handling code here:
        searchInContainer(search_table, search_item);
    }//GEN-LAST:event_search_itemFocusGained

    private void search_tableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_search_tableFocusGained
        // TODO add your handling code here:
            if(search_table.getSelectedRow()==0)
            {
                        String selectedColumn1=search_table.getValueAt(search_table.getSelectedRow(), 2).toString();
                        String selectedColumn2=search_table.getValueAt(search_table.getSelectedRow(), 3).toString();

                        search_item.setText(selectedColumn1+" "+selectedColumn2);
            }
    }//GEN-LAST:event_search_tableFocusGained

    private void search_tableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_tableKeyReleased
        // TODO add your handling code here:
                // TODO add your handling code here:
        String selectedColumn1=search_table.getValueAt(search_table.getSelectedRow(), 2).toString();
        String selectedColumn2=search_table.getValueAt(search_table.getSelectedRow(), 3).toString();

        search_item.setText(selectedColumn1+" "+selectedColumn2);
        
//        if(search_table.getSelectedRow()==0 && search_table.isCellSelected(0, 0) && evt.getKeyCode()==KeyEvent.VK_UP)
//        {
//            search_item.grabFocus();
//            search_item.setText("");
//            searchInContainer(search_table, search_item);
//        }
        
        createKeybindings(search_table);

 
        
        
    }//GEN-LAST:event_search_tableKeyReleased
    
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
            java.util.logging.Logger.getLogger(SearchContainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearchContainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearchContainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchContainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SearchContainer dialog = new SearchContainer(new javax.swing.JFrame(), true);
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
    public javax.swing.JLabel contailer_specfic_toolTip;
    public javax.swing.JLabel container_title;
    public javax.swing.JLabel container_tooltip;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField search_item;
    public javax.swing.JTable search_table;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
