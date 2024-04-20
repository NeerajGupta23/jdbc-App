package BatchExecution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import in.pwskills.neeraj.utility.Utility;

public class BatchInsert {
	private static final String SQL_INSERT_QUERY = "insert into product(pname, pcost) values(?, ?)";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statement = null;
		Scanner scanner = new Scanner(System.in);

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			statement = connection.prepareStatement(SQL_INSERT_QUERY);
			String option = "yes";
			System.out.println("\n\nInsertion Operation (multiple Insertion in single go)......");
			System.out.println();

			while (option.equalsIgnoreCase("yes")) {
				System.out.print("\nEnter Product Name : ");
				String pName = scanner.next();
				statement.setString(1, pName);

				System.out.print("Enter Product Cost : ");
				Float pCost = scanner.nextFloat();
				statement.setFloat(2, pCost);

				statement.addBatch();

				System.out.print("\nMore [yes/no] ? ");
				option = scanner.next();
			}

			int[] executeBatch = statement.executeBatch();
			System.out.println("\nTotal Insertion :: " + Utility.getSumOfArray(executeBatch));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, null);
			scanner.close();
		}

	}
}
