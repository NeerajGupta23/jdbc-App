package in.pwskills.neeraj.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import in.pwskills.neeraj.utility.Utility;

public class InsertDate {
	public static final String SQL_INSERT_QUERY = "insert into friends(name, Birthdate) values(?, ?)";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);
		
		System.out.println(properties);

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			statement = connection.prepareStatement(SQL_INSERT_QUERY);

			statement.setString(1, "Ujjwal");
			statement.setDate(2, Utility.getSqlDate("07/09/2002"));

			int count = statement.executeUpdate();
			System.out.println(count + " Rows Inserted....");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, null);
		}
	}
}
