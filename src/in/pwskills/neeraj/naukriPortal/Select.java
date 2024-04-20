package in.pwskills.neeraj.naukriPortal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import in.pwskills.neeraj.utility.Utility;

public class Select {
	private static final String SQL_SELECT_QUERY = "select name, address, photo, resume from NaukriPortal where regNo = ?";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			statement = connection.prepareStatement(SQL_SELECT_QUERY);

			statement.setInt(1, 3);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
				System.out.println(resultSet.getString(2));

				InputStream stream = resultSet.getBinaryStream(3);
				byte[] data = new byte[stream.available()];
				stream.read(data);
				FileOutputStream fos = new FileOutputStream(".\\src\\multimedia\\output\\img3.jpg");
				fos.write(data);
				data = null;
				stream.close();
				fos.close();

				Reader characterStream = resultSet.getCharacterStream(4);
				PrintWriter out = new PrintWriter(new File(".\\src\\multimedia\\output\\file3.txt"));
				characterStream.transferTo(out);
				out.close();
				characterStream.close();
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, resultSet);
		}
	}
}