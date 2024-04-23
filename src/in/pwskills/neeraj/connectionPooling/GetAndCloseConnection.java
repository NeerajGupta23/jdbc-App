package in.pwskills.neeraj.connectionPooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class GetAndCloseConnection {
	private static HikariDataSource dataSource = null;

	static {
		HikariConfig config = new HikariConfig("./src/in/pwskills/neeraj/properties/hikaricp.properties");
		dataSource = new HikariDataSource(config);
	}

	public static Connection getConnection() {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public static void displayConnection(ArrayList<Connection> connections) {
		int i = 0;

		for (Connection connection : connections) {
			System.out.println(i++ + "  :  " + connection.hashCode());
		}
		
		System.out.println();
	}
}