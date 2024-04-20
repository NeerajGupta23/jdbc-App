package in.pwskills.neeraj.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cj.util.Util;

import in.pwskills.neeraj.utility.Utility;

public class SelectQuery {
	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		Properties properties = new Properties();
		Utility.getPropertise(properties);

		try {
//			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
					properties.getProperty("password"));
			statement = connection.createStatement();

			if (statement.execute("select * from product")) {
				resultSet = statement.getResultSet();
				while (resultSet.next()) {
					System.out.println(resultSet.getString(1));
					System.out.println(resultSet.getString(2));
					System.out.println(resultSet.getString(3));
					System.out.println();
				}
			} else {
				System.out.println(statement.getUpdateCount());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, resultSet);
		}
	}
}
