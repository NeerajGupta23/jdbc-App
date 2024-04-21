package in.pwskills.neeraj.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Properties;
import java.util.Scanner;

import in.pwskills.neeraj.utility.Utility;

public class TransferMoney {
	private static final String SQL_SELECT_AMOUNT = "select amount from bank where name = ?";

	private static final String SQL_SELECT_NAME = "select name from bank where name = ?";

	private static final String SQL_UPDATE_QUERY = "update bank set amount = amount + ? where name = ?";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement selectAmount = null;
		PreparedStatement selectName = null;
		PreparedStatement updateStatement = null;
		ResultSet resultSet = null;
		Scanner scanner = new Scanner(System.in);

		System.out.println("\n_____Transfer Money_____");
		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			connection.setAutoCommit(false);
			selectAmount = connection.prepareStatement(SQL_SELECT_AMOUNT);

			System.out.print("Enter Your Name : ");
			String yourName = scanner.nextLine();
			selectAmount.setString(1, yourName);

			resultSet = selectAmount.executeQuery();
			if (!resultSet.next()) {
				System.out.println("Invalid Credentials !");
				return;
			}
			
			Float currBalanace = resultSet.getFloat(1);
			System.out.println("Your Current Balanace : " + currBalanace);
			System.out.println();

			selectName = connection.prepareStatement(SQL_SELECT_NAME);
			System.out.print("Enter Receiver Name : ");
			String receiverName = scanner.nextLine();
			
			if(receiverName.equalsIgnoreCase(yourName)) {
				System.out.println("Invalid Name !");
				return;
			}
			
			selectName.setString(1, receiverName);
			resultSet.close();

			resultSet = selectName.executeQuery();
			if (!resultSet.next()) {
				System.out.println("Receiver Not Found !");
				return;
			}

			System.out.print("Enter Amount to Transfer : ");
			Float transferAmount = scanner.nextFloat();
			if (transferAmount < 0 || transferAmount > currBalanace) {
				System.out.println("Entered Amount is Invalid !");
				return;
			}

			updateStatement = connection.prepareStatement(SQL_UPDATE_QUERY);
			updateStatement.setFloat(1, -transferAmount);
			updateStatement.setString(2, yourName);
			
			Savepoint setSavepoint1 = connection.setSavepoint();
			updateStatement.executeUpdate();
			updateStatement.close();

			updateStatement = connection.prepareStatement(SQL_UPDATE_QUERY);
			updateStatement.setFloat(1, transferAmount);
			updateStatement.setString(2, receiverName);
			updateStatement.executeUpdate();

			System.out.print("Confirm [yes/no] : ");
			String confirmation = scanner.next();
			if (!confirmation.equalsIgnoreCase("yes")) {
				connection.rollback(setSavepoint1);
				System.out.println("Transaction is Failed !");
				return;
			}

			connection.commit();
			
			System.out.println();
			System.out.println(transferAmount + " Rs. is transfered to " + receiverName + " Successfully...");
			System.out.println("Your Current balanace is : " + (currBalanace - transferAmount));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(null, updateStatement, null);
			Utility.closeResources(connection, selectName, resultSet);
			Utility.closeResources(null, selectName, null);
			scanner.close();
		}

	}
}
