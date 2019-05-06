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
<<<<<<< HEAD
        /*
        String jdbcUrl = "jdbc:oracle:thin:@cswinserv.eku.edu:1521:cscdb";  // URL for the database including the protocol (jdbc), the vendor (oracle), the driver (thin), the server (csshrpt.eku.edu), the port number (1521), database instance name (cscdb)

        String username = "vaughan5452019";
        String password = "2108";
        */
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";  // URL for the database including the protocol (jdbc), the vendor (oracle), the driver (thin), the server (csshrpt.eku.edu), the port number (1521), database instance name (cscdb)
=======
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:db";  // URL for the database including the protocol (jdbc), the vendor (oracle), the driver (thin), the server (csshrpt.eku.edu), the port number (1521), database instance name (cscdb)

        String username = "username";
        String password = "password";
>>>>>>> c38ebde221b3ea5bfe66f6154af2d9e024392e54

        String username = "ethan545";
        String password = "1234";
        
        
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
