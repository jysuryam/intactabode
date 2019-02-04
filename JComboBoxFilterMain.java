package javaapplication3;

import javax.swing.*;
import java.awt.*;
import java.util.List;



public class JComboBoxFilterMain {

    public JComboBoxFilterMain() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JComboBoxFilterMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JComboBoxFilterMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JComboBoxFilterMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JComboBoxFilterMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static void main(String[] args) {
        new JComboBoxFilterMain();
        List<Employee> employees = EmployeeDataAccess.getEmployees();
        JComboBox<Employee> comboBox = new JComboBox<>(
                employees.toArray(new Employee[employees.size()]));

        ComboBoxFilterDecorator<Employee> decorate = ComboBoxFilterDecorator.decorate(comboBox,
                CustomComboRenderer::getEmployeeDisplayText,
                JComboBoxFilterMain::employeeFilter);

        comboBox.setRenderer(new CustomComboRenderer(decorate.getFilterTextSupplier()));

        JPanel panel = new JPanel();
        comboBox.setFont(new java.awt.Font("Dialog", 0, 14));

        panel.add(comboBox);

        JFrame frame = createFrame();
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println(employees);
    }

    private static boolean employeeFilter(Employee emp, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getEmployeeDisplayText(emp).toLowerCase()
                                  .contains(textToFilter.toLowerCase());
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JComboBox Filter Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }
    
    
}