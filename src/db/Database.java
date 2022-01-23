package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    static String jdbcURL = "jdbc:postgresql://db.fe.up.pt/";
    static String username="up201806281";
    static String password="YKYG5u266";
    
    public Connection connectDB() throws SQLException{
        Connection c = DriverManager.getConnection(jdbcURL, username, password);
        //System.out.println("Connected to PostgreSQL server");
        
        return c;
    }

    public int disconnectDB(Connection c) throws SQLException{
        c.close();
        return 0;
    }

}