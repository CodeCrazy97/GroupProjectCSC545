package groupproject545;

/*
This class manages the connection to the Oracle database.
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import javax.swing.JOptionPane;

public class ConnectDb {

    public static Connection setupConnection() {
        // Connection information.
        String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
        
        // *** EKU connection ***
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:db";  // URL for the database including the protocol (jdbc), the vendor (oracle), the driver (thin), the server (csshrpt.eku.edu), the port number (1521), database instance name (cscdb)

        String username = "username";
        String password = "password";

        
        // Try to setup the connection.
        try {
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
        }
        return null;  // Return null if an exception was thrown.
    }

    // Close connections
    static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Throwable whatever) {
                System.out.println("Problem closing connection. Message: " + whatever);
            }
        }
    }
    
    // Close statement
    static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Throwable whatever) {
                System.out.println("Problem closing statement. Message: " + whatever);
            }
        }
    }

    // Close prepared statements
    static void close(OraclePreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (Throwable whatever) {
                System.out.println("Problem closing prepared statement. Message: " + whatever);
            }
        }
    }

    // Close result sets
    static void close(OracleResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Throwable whatever) {
                System.out.println("Problem closing result set. Message: " + whatever);
            }
        }
    }

}
