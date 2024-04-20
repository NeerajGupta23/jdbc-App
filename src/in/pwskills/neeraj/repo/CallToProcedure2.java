package in.pwskills.neeraj.repo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import in.pwskills.neeraj.utility.Utility;

public class CallToProcedure2 {
	private static final String SQL_PROCEDURE = "{call total_cost (?)}";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		CallableStatement prepareCall = null;

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			prepareCall = connection.prepareCall(SQL_PROCEDURE);

			prepareCall.registerOutParameter(1, Types.INTEGER);
			prepareCall.execute();

			System.out.println(prepareCall.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, prepareCall, null);
		}
	}
}