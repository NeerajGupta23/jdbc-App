package BatchExecution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import in.pwskills.neeraj.utility.Utility;

public class BatchTransaction {
	private static final String SQL_UPDATE_QUERY = "update product set pcost = pcost + ? where pname = ?";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statement = null;
		Scanner scanner = new Scanner(System.in);
		int[] executeBatch = { 0 };

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			statement = connection.prepareStatement(SQL_UPDATE_QUERY);
			System.out.println("\n\nEnter Details for Transaction......");

			System.out.print("\nSender Name : ");
			statement.setString(2, scanner.next());
			System.out.print("Cost to Send : ");
			Float cost = scanner.nextFloat();
			statement.setFloat(1, -cost);
			statement.addBatch();

			System.out.print("\nReceiver Name : ");
			statement.setString(2, scanner.next());
			statement.setFloat(1, cost);
			statement.addBatch();

			System.out.print("\nConfirm [yes/no] ? ");
			String option = scanner.next();

			if (option.equalsIgnoreCase("yes")) {
				executeBatch = statement.executeBatch();
			}

			System.out.println("\nTotal Updation :: " + Utility.getSumOfArray(executeBatch));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, null);
			scanner.close();
			executeBatch = null;
		}
	}
}