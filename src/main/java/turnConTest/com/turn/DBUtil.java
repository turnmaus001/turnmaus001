package turnConTest.com.turn;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	private static Connection connection;
	/*public static Connection getConnection() throws URISyntaxException, SQLException {
		String dbUrl = System.getenv("JDBC_DATABASE_URL");
		return DriverManager.getConnection(dbUrl);
	}*/

	public static Connection getConnection() throws URISyntaxException, SQLException {
		   try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   String url = "jdbc:postgresql://localhost:5432/turn";
		   try {

		    connection = DriverManager.getConnection(url, "postgres", "123456a-");
		//    System.out.print("Kết nối thành công");
		   } catch (SQLException e) {
		    e.printStackTrace();
		   }
	    /*URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";*/
		return connection;

	//    return DriverManager.getConnection(dbUrl, username, password);
	}
}
