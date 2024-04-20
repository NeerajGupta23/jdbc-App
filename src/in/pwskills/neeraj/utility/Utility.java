package in.pwskills.neeraj.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Utility {
	public static Properties getPropertise(Properties properties) {
		String location = ".\\src\\in\\pwskills\\neeraj\\properties\\database.properties";
		File file = new File(location);
		System.out.println(file);
		
		try {
			properties.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		System.out.println(properties);
		return properties;
	}

	public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static java.sql.Date getSqlDate(String string) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.sql.Date sqlDate = null;

		try {
			Date utilDate = simpleDateFormat.parse(string);
			long ms = utilDate.getTime();

			sqlDate = new java.sql.Date(ms);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sqlDate;
	}
}
	