package in.pwskills.neeraj.navigation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import in.pwskills.neeraj.utility.Utility;

public class NavigateAndUpdate {

	private static final String SQL_SELECT_QUERY = "select pname, pcost, pid from product";
	private static final String SQL_COUNT_QUERY = "select count(*) from product";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Scanner scanner = new Scanner(System.in);

		System.out.println();
		System.out.println("_____Navigation and Updation_____");
		System.out.println();

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);

			// to avoid Sql-Injection prepared statement is used
			statement = connection.prepareStatement(SQL_SELECT_QUERY, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			resultSet = statement.executeQuery();
			int rowCount = findRowCount(connection);

			displayAllRows(resultSet);
			Integer[] info = getNavigationInfo(scanner, rowCount);

			if (info == null) {
				System.out.println("Entered location is Invalid !");
				return;
			}
			displaySelectedRow(resultSet, info);

			System.out.println();
			System.out.print("Do you want to update [yes/no] : ");
			String confirmation = scanner.next();
			if (!confirmation.equalsIgnoreCase("yes")) {
				System.out.println("Program is Terminated...");
				return;
			}

			updateSeletedRow(resultSet, scanner, info);
			System.out.println("Program is Terminated...");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, resultSet);

			scanner.close();
		}

	}

	private static void displayAllRows(ResultSet resultSet) throws SQLException {
		// Displays All the Rows to user

		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + " : " + resultSet.getString(2));
		}

		System.out.println();
		System.out.println();
	}

	private static void updateSeletedRow(ResultSet resultSet, Scanner scanner, Integer[] info) throws SQLException {
		// updates the name and/or cost of selected row

		Boolean flag = false;
		System.out.println();

		System.out.print("Name [New Value / No] : ");
		scanner.nextLine();
		String name = scanner.nextLine();

		System.out.print("Cost [New Value / No] : ");
		String cost = scanner.nextLine();

		if (!name.equalsIgnoreCase("no")) {
			resultSet.updateString(1, name);
			resultSet.updateRow();
			flag = true;
		}
		if (!cost.equalsIgnoreCase("no")) {
			resultSet.updateString(2, cost);
			resultSet.updateRow();
			flag = true;
		}

		if (flag) {
			System.out.println("Row updated successfully....");
			System.out.println();
			displaySelectedRow(resultSet, info);
		}
	}

	private static void displaySelectedRow(ResultSet resultSet, Integer[] info) throws SQLException {
		// displays the selected row to user

		resultSet.absolute(info[0]);
		resultSet.relative(info[1]);

		System.out.println(resultSet.getString(1) + " : " + resultSet.getString(2));
	}

	private static Integer[] getNavigationInfo(Scanner scanner, Integer totalRows) {
		// return input(ie [absolute, relative] index) taken by user
		Integer[] info = new Integer[2];

		System.out.println();
		System.out.print("Enter location for navigation [absolute(1 - " + totalRows + "), relative] : ");

		info[0] = scanner.nextInt();
		info[1] = scanner.nextInt();

		if (isValid(info, totalRows)) {
			return info;
		}

		return null;
	}

	private static boolean isValid(Integer[] info, Integer totalRows) {
		// return true is given info is valid according to total Rows otherwise return
		// false
		// here absolute is always positive

		int absolute = info[0];
		int relative = info[1];
		int eval = absolute + relative;
		boolean isValid = true;

		if (absolute < 1 || absolute > totalRows) {
			isValid = false;
		}

		if (eval < 1 || eval > totalRows) {
			isValid = false;
		}

		return isValid;
	}

	private static int findRowCount(Connection connection) throws SQLException {
		// this method return total number of rows in product table

		PreparedStatement prepareStatement = connection.prepareStatement(SQL_COUNT_QUERY);
		ResultSet resultSet = prepareStatement.executeQuery();
		int rowCount = -1;

		if (resultSet.next()) {
			rowCount = resultSet.getInt(1);
		}

		// closing all resources
		prepareStatement.close();
		resultSet.close();

		// unable to find count
		return rowCount;
	}
}
