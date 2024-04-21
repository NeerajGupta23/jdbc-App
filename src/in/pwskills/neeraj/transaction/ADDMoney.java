package in.pwskills.neeraj.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import in.pwskills.neeraj.utility.Utility;

public class ADDMoney {
	private static final String SQL_UPDATE_QUERY = "update bank set amount = amount + ? where name = ?";
	private static final String SQL_SELECT_QUERY = "select amount from bank where name = ?";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statementForUpdate = null;
		PreparedStatement statementForSelect = null;
		ResultSet resultSet = null;
		Scanner scanner = new Scanner(System.in);

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			connection.setAutoCommit(false);

			statementForSelect = connection.prepareStatement(SQL_SELECT_QUERY);
			System.out.print("Enter Name : ");
			String name = scanner.nextLine();
			statementForSelect.setString(1, name);
			resultSet = statementForSelect.executeQuery();

			if (!resultSet.next()) {
				System.out.println("Credentials are not matched...");
				return;
			}
			float currAmount = resultSet.getFloat(1);
			System.out.println("Your Current balance is : " + currAmount);

			statementForUpdate = connection.prepareCall(SQL_UPDATE_QUERY);
			statementForUpdate.setString(2, name);

			System.out.print("\nEnter Amount for Addtion : ");
			float amountForAddtion = scanner.nextFloat();
			
			if (amountForAddtion <= 0) {
				System.out.println("Amount is not Valid...");
				return;
			}
			statementForUpdate.setFloat(1, amountForAddtion);

			System.out.print("Validation [done] : ");
			String validation = scanner.next();
			int count = statementForUpdate.executeUpdate();

			System.out.println();
			if (validation.equalsIgnoreCase("done")) {
				connection.commit();
				if (count > 0) {
					System.out.println("Transaction Completed Successfully...");
					System.out.println("Your Current balance is : " + (currAmount + amountForAddtion));
				} else {
					System.out.println("Transaction not Completed...");
				}
			} else {
				connection.rollback();
				System.out.println("An Error has Occured...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statementForUpdate, resultSet);
			Utility.closeResources(null, statementForSelect, null);
			scanner.close();
		}
	}
}
