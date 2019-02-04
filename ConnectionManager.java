	package javaapplication3;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;

	public class ConnectionManager {

	private static String url = "jdbc:h2:E://Suryam Workspace//db//ia";    
	private static String driverName = "org.h2.Driver";   
	private static String username = "intact";   
	private static String password = "abode";
	private static Connection connection;
	private static String urlstring;

	public static Connection getConnection() {
	    try {
	        Class.forName(driverName);
	        try {
	            connection = DriverManager.getConnection(url, username, password);
	            System.out.println("Connection Sucessful");
	        } catch (SQLException ex) {
	            // log an exception. fro example:
	            System.out.println("Failed to create the database connection."); 
	            ex.printStackTrace();
	        }
	    } catch (ClassNotFoundException ex) {
	        // log an exception. for example:
	        System.out.println("Driver not found."); 
	    }
	    
	    return connection;  
	}
	public static void main(String[] args) {

	    getConnection();
	}

}
	
