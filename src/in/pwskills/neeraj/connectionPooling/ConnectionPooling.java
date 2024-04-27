package in.pwskills.neeraj.connectionPooling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionPooling {

	private static final String SQL_SELECT_STATEMENT = "select pname, pcost from product";

	public static void main(String[] args) {
		String choose = null;
		String prevChoose = null;
		Scanner scanner = new Scanner(System.in);
		ArrayList<Connection> connections = new ArrayList<>();

		while (true) {
			prevChoose = choose;

			System.out.print("Connnecion [Add/Close/Display/Query/Quit/Prev] : ");
			choose = scanner.next();
			if (choose.equalsIgnoreCase("quit")) {
				closeAllConnections(connections);
				break;
			}
			if (choose.equalsIgnoreCase("prev") && prevChoose != null) {
				choose = prevChoose;
			}

			try {
				if (!performOperation(choose, connections, scanner)) {
					System.out.println("Invalid Option !");
					continue;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.out.println();
		}

		System.out.println();
		System.out.println("Application is Terminated....");
	}

	private static void closeAllConnections(ArrayList<Connection> connections) {
		for (Connection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println("All Connection Object are Closed....");
	}

	private static boolean performOperation(String choose, ArrayList<Connection> connections, Scanner scanner)
			throws SQLException {
		// perform add, close and display operation on connection object based on user
		// input "choose"
		boolean inputValid = true;

		switch (choose.toLowerCase()) {

		case "add":
			Connection connection = GetAndCloseConnection.getConnection();
			System.out.println("New Connection Object : " + connection.hashCode());
			connections.add(connection);
			break;

		case "close":
			if (connections.isEmpty()) {
				System.out.println("Operation is Invalid !");
				break;
			}

			System.out.print(String.format("Choose Element [0 : %d] : ", connections.size() - 1));
			int index = scanner.nextInt();

			Connection connection2 = connections.remove(index);
			int hashCode = connection2.hashCode();
			connection2.close();

			System.out.println(String.format("Connection '%d' is Closed ", hashCode));
			break;

		case "query":
			executeQuery(connections, scanner);
			break;

		case "display":
			GetAndCloseConnection.displayConnection(connections);
			break;

		case "prev":
			// dummy case
			break;

		default:
			inputValid = false;
		}

		return inputValid;
	}

	private static void executeQuery(ArrayList<Connection> connections, Scanner scanner) throws SQLException {
		int sizeOfConnections = connections.size();

		if (sizeOfConnections == 0) {
			System.out.println("No Connection Object is present !");
			return;
		}

		System.out.print(String.format("Select Connection Object [0, %d] : ", sizeOfConnections - 1));
		int connectionsIndex = scanner.nextInt();

		if (connectionsIndex >= sizeOfConnections || connectionsIndex < 0) {
			System.out.println("Invalid Index !");
			return;
		}

		Connection connection = connections.get(connectionsIndex);
		PreparedStatement prepareStatement = connection.prepareStatement(SQL_SELECT_STATEMENT);
		ResultSet resultSet = prepareStatement.executeQuery();

		System.out.println();
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + "  :  " + resultSet.getShort(2));
		}
		System.out.println();
	}
}
