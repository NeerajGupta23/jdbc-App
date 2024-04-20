package in.pwskills.neeraj.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import in.pwskills.neeraj.utility.Utility;

public class PrepStatement {
	private final static String SQL_SELECT_QUERY = "select pname, pcost from product where pid = ?";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			statement = connection.prepareStatement(SQL_SELECT_QUERY);

			statement.setString(1, "21");
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
				System.out.println(resultSet.getString(2));
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, resultSet);
		}
	}
}
