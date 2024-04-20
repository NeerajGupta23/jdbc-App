package in.pwskills.neeraj.naukriPortal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import in.pwskills.neeraj.utility.Utility;

public class Insert {
	public static final String SQL_INSERT_QUERY = "insert into NaukriPortal(name, address, photo, resume) values(?, ?, ?, ?)";

	public static void main(String[] args) {
		Properties properties = new Properties();
		Utility.getPropertise(properties);

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection(properties.getProperty("url"), properties);
			statement = connection.prepareStatement(SQL_INSERT_QUERY);

			statement.setString(1, "Venu");
			statement.setString(2, "Sector - 06");

			InputStream profilePhoto = getProfilePhoto(".\\src\\multimedia\\input\\img3.jpeg");
			statement.setBinaryStream(3, profilePhoto);

			Reader resume = getResume(".\\src\\multimedia\\input\\file3.txt");
			statement.setCharacterStream(4, resume);

			int count = statement.executeUpdate();
			System.out.println(count + " row is Inserted....");

			profilePhoto.close();
			resume.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			Utility.closeResources(connection, statement, null);
		}
	}

	private static Reader getResume(String string) {
		File file = new File(string);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		return br;
	}

	private static InputStream getProfilePhoto(String string) {
		File file = new File(string);
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return fis;
	}
}
