package in.pwskills.neeraj.connectionPooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionPooling {
	public static void main(String[] args) {
		String choose = null;
		Scanner scanner = new Scanner(System.in);
		ArrayList<Connection> connections = new ArrayList<>();

		while (true) {
			System.out.print("Connnecion [Add/Close/Display/Quit] : ");
			choose = scanner.next();
			if (choose.equalsIgnoreCase("quit")) {
				break;
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

		case "display":
			GetAndCloseConnection.displayConnection(connections);
			break;

		default:
			inputValid = false;
		}

		return inputValid;
	}
}
