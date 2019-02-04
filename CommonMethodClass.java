/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Font;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.utils.FadingUtils;
import net.java.balloontip.utils.TimingUtils;
import net.java.balloontip.utils.ToolTipUtils;
import net.proteanit.sql.DbUtils;
import org.h2.command.dml.Replace;

/**
 *
 * @author J Suryam
 */
public class CommonMethodClass {
    
    
              String rgb1=null;
              String rgb2=null;
              String rgb3=null;
              
              
              //--------------------->COMMON CALCULATION FORMULAS FOR EXCLUDING TAX IN ENTIRE APPLICATION<------------------------------//
	
	//TOTAL VALUE WITH TAX SALES
//	public String plusTaxSales="sum(cast(round((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC,2) as decimal(19,2))+cast(round(((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2)))";
//	public String plusTaxIndividual="cast(round((sell_PRICE*QTY)-(sell_PRICE*QTY)/100*DISC,2) as decimal(19,2))+cast(round(((sell_PRICE*QTY)-(sell_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2))";
//	
//	
//	public String taxValueSales="sum(cast(round(((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2)))";
//	public String taxValueSalesHalf="sum(cast(round(((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)/100*tax/2,2) as decimal(19,2)))";
//	
//	
//	public String taxValueIndividual="cast(round(((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2))";
//	public String taxValueSalesHalfIndividual="cast(round(((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)/100*tax/2,2) as decimal(19,2))";
//	public String saleValueofTax="sum(cast(round((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC,2) as decimal(19,2)))";
//	
//	
//	public String taxableValueSales="cast(round((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC,2) as decimal(19,2))";
//	
//	
//	//TOTAL VALUE WITH TAX PURCHASES
//	public String plusTaxPurchases="sum(cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2))+cast(round(((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2)))";
//	public String plusTaxPurchasesIndividual="cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2))+cast(round(((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2))";
//	
//	
//	public String taxValuePurchases="sum(cast(round(((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2)))";
//	public String taxValuePurchasesHalf="sum(cast(round(((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)/100*tax/2,2) as decimal(19,2)))";
//	
//	public String taxValuePurchasesIndividual="cast(round(((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2))";
//	
//	public String purchasesValueofTax="sum(cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2)))";
//	
//	public String taxableValuePurchases="cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2))";
	
	
	//------------------------------------------------END---------------------------------------------------------------------//
	
	
	
	
	//--------------------->COMMON CALCULATION FORMULAS FOR INCLUDING TAX IN ENTIRE APPLICATION<------------------------------//
	
	//TOTAL VALUE WITH TAX SALES
//	public String plusTaxSales="sum(cast(round((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC,2) as decimal(19,2)))"; //cast(round(((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)/100*tax,2) as decimal(19,2))
//	public String plusTaxIndividual="cast(round((sell_PRICE*QTY)-(sell_PRICE*QTY)/100*DISC,2) as decimal(19,2))";
//	
//	
//	public String taxValueSales="sum(cast(round((((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)*tax)/(100+tax),2) as decimal(19,2)))";
//	
//	public String taxValueSalesHalf="sum(cast(round((((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)*tax)/(100+tax)/2,2) as decimal(19,2)))";
//	
//	
//	public String taxValueIndividual="cast(round((((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)*tax)/(100+tax),2) as decimal(19,2))";
//	public String taxValueSalesHalfIndividual="cast(round((((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)*tax)/(100+tax)/2,2) as decimal(19,2))";
//	
//	public String saleValueofTax="sum(cast(round((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC,2) as decimal(19,2)))";
//	
//	public String taxableValueSales="cast(round((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC,2) as decimal(19,2))-cast(round((((SELL_PRICE*QTY)-(SELL_PRICE*QTY)/100*DISC)*tax)/(100+tax),2) as decimal(19,2))";
//	
//	
//	//TOTAL VALUE WITH TAX PURCHASES
//	public String plusTaxPurchases="sum(cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2)))";
//	public String plusTaxPurchasesIndividual="cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2))";
//	
//	
//	public String taxValuePurchases="sum(cast(round((((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)*tax)/(100+tax),2) as decimal(19,2)))";
//	public String taxValuePurchasesHalf="sum(cast(round((((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)*tax)/(100+tax)/2,2) as decimal(19,2)))";
//	
//	public String taxValuePurchasesIndividual="cast(round((((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC)*tax)/(100+tax),2) as decimal(19,2))";
//	
//	public String purchasesValueofTax="sum(cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2)))";
//	
//	public String taxableValuePurchases="cast(round((PUR_PRICE*QTY)-(PUR_PRICE*QTY)/100*DISC,2) as decimal(19,2))";
              
        //
        public void comboBoxAddItems(JComboBox box, String customQuery)
        {   box.removeAllItems();
            try {

                connection=ConnectionManager.getConnection();
                st=connection.createStatement();
                set=st.executeQuery(customQuery);
                while(set.next())
                {
                    box.addItem(set.getString(1));
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
        

              
              
    public CommonMethodClass(String color) {
        
         getThemeColors("Pale Blue");
         light1=new Color(clr1[0],clr1[1],clr1[2]);
         light2=new Color(clr2[0],clr2[1],clr2[2]);
         light3=new Color(clr3[0],clr3[1],clr3[2]);
         
    }
    
    
    
    
    
    SimpleDateFormat csdf=new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df=new DecimalFormat("00000");
    int clr1[];
    int clr2[];
    int clr3[];

// Pale Blue
//    Color light1=new Color(228,245,245);
//    Color light2=new Color(153,208,208);
//    Color light3=new Color(0,153,153);
    
    // icon color BDC3C7
    
// Sky Blue  

    
//    Color light1=new Color(clr1[0],clr1[1],clr1[2]);
//    Color light2=new Color(clr2[0],clr2[1],clr2[2]);
//    Color light3=new Color(clr3[0],clr3[1],clr3[2]);

// Orange
    Color light1;
    Color light2;
    Color light3;
    
//    Grey
//    Color light1=new Color(235,233,233);
//    Color light2=new Color(208,207,207);
//    Color light3=new Color(140,139,139);   
 
//    Green
//    Color light1=new Color(227,241,232);
//    Color light2=new Color(129,204,154);
//    Color light3=new Color(55,150,87); 
    
    Connection connection=null;
    Statement st=null;
    PreparedStatement ps=null;
    ResultSet set=null;
   
            
 
    Icon icon=new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/tick.png"));
    Icon magnifier=new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/images/magnifier.png"));
 
    public static void main(String[] args) {

        
        
       
    }
    
   public void textFieldFocusGained(JTextField field)
   {
       if(field.getText().trim().equals("0") || field.getText().trim().equals("0.00"))
       {
            field.setText("");
       }
       validateForNumber(field);
   }
   
   public void textFieldFocusLost(JTextField field)
   {
       if(field.getText().trim().equals(""))
       {
            field.setText("0");
       }
       
       validateForNumber(field);
   }
    
       public void validateForNumber(JTextField field)
    {
            try {
            double d=Double.parseDouble(field.getText());

            field.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeBorderColor")));
        } catch (NumberFormatException e) {
            field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255,153,153),2));

        }
    
    }
       
    public void validateForTextInput(JTextField field)
    {
            if(field.getText().trim().isEmpty())
            {
               field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255,153,153),2)); 

            }else
            {
                field.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeBorderColor"))); 
            }
    }
   
   public String id; // Auto Generated id;
   public void generateUniqueID(String tableName, String countColumn)
   {
       // Creating common method to generate unique id
       String query="select count(distinct("+countColumn+"))+1 from "+tableName+" ";
       try {
           connection=ConnectionManager.getConnection();
           st=connection.createStatement();
           set=st.executeQuery(query);
           if(set.next())
           {    
               id=tableName.substring(0,3)+df.format(Integer.parseInt(set.getString(1)));
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
   
   public void JTextieldValidation(JTextField field, String message, Component parentComponent)
   {
       if(field.getText().trim().equals(""))
       {
           JOptionPane.showMessageDialog(parentComponent, message,"Please Check",JOptionPane.WARNING_MESSAGE);
           return ;
       }
       
   }
   
   public void tableHeaderSettings(JTable table)
   {
                JTableHeader header = table.getTableHeader();	
		header.setFont( header.getFont().deriveFont(Font.BOLD));
		header.setBackground(light2);
                header.setForeground(light3);
   }
   
   public void getThemeColors(String color)
   {
       connection=ConnectionManager.getConnection();
       try {
           String query="SELECT * FROM THEME  where action='TRUE'";
           st=connection.createStatement();
           set=st.executeQuery(query);
           if(set.next())
           {
//               System.out.println(set.getString(1).replace(color+"=", "").split(","));

              String color1[]=set.getString(2).split(",");
              String color2[]=set.getString(3).split(",");
              String color3[]=set.getString(4).split(",");
                
              // Getting rgb colors directly
              rgb1=set.getString(2);
              rgb2=set.getString(3);
              rgb3=set.getString(4);
              
              
              clr1=new int[]{Integer.parseInt(color1[0].replace("(", "")),Integer.parseInt(color1[1].replace("(", "")),Integer.parseInt(color1[2].replace(")", ""))};
              clr2=new int[]{Integer.parseInt(color2[0].replace("(", "")),Integer.parseInt(color2[1].replace("(", "")),Integer.parseInt(color2[2].replace(")", ""))};        
              clr3=new int[]{Integer.parseInt(color3[0].replace("(", "")),Integer.parseInt(color3[1].replace("(", "")),Integer.parseInt(color3[2].replace(")", ""))};
               
               
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
   
   public void baloopTipOnJlabel(JLabel label, String message, BalloonTip.Orientation position)
    {
        BalloonTip tip = new BalloonTip(
                label, 
                new JLabel(message),
                new RoundedBalloonStyle(5,5,Color.WHITE, light3),
                
                position, 
                BalloonTip.AttachLocation.ALIGNED, 
                15, 
                15, 
                true);
                label.setForeground(light3);
              TimingUtils.showTimedBalloon(tip, 5000);

    }
   
  public void changeTheme(String color)
  {
      try {
          connection=ConnectionManager.getConnection();
          String setFalse="update THEME SET action='FALSE'";
          String query="update THEME SET action='TRUE' where COLOR='"+color+"'";
          
          st=connection.createStatement();
          int i1=st.executeUpdate(setFalse);
          int i=st.executeUpdate(query);
           
          System.out.println(i1);
          System.out.println(i);
      } catch (Exception e) {
      }finally
       {
           try {
               connection.close();
               st.close();
              
           } catch (Exception e) {
           }
       }
  }
   
   
            
}
