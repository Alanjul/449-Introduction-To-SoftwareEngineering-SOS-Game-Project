package sprint5.mode.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class DBConnection {
	private static String URL;
	private static String USERNAME;
	private static String PASSWORD;
	private static String DRIVER;
	private static Properties properties = new Properties();
	private static Connection connection;

	// static block to load properties
	static {

		loadConfiguration();
		loadDriver();
	}

	public static void loadConfiguration() {
		InputStream input = null;
		boolean loaded = false;
		try {
			// load from class path
			input = DBConnection.class.getClassLoader().getResourceAsStream("database.properties");

			if (input != null) {
				loaded = true;
			}
			if (input == null) {
				try {
					input = new FileInputStream("database.properties");
					loaded = true;
				} catch (IOException e) {
					// File not in root, continue trying
				}
			}

			if (input == null) {
				throw new IOException("Cannot find database.properties file!\n" + "Current working directory: "
						+ System.getProperty("user.dir") + "\n" + "Please ensure database.properties is in:\n"
						+ "1. Project root directory (where you have it now)\n" + "2. OR copy it to src/ folder\n"
						+ "File should contain:\n" + "  db.driver=com.mysql.cj.jdbc.Driver\n"
						+ "  db.url=jdbc:mysql://localhost:3306/sos_game_db\n" + "  db.user=root\n"
						+ "  db.password=your_password");
			}

			properties.load(input);

			// Extract configuration values
			URL = properties.getProperty("db.url");
			USERNAME = properties.getProperty("db.user");
			PASSWORD = properties.getProperty("db.password");
			DRIVER = properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
			validateConfiguration();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to load db.properties file");
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,"Warning: Failed to close properties file stream");
				}
			}
		}

	}

	private static void validateConfiguration() {
		if (URL == null || URL.trim().isEmpty()) {
			throw new IllegalStateException("db.url is not configured in database.properties\n"
					+ "Add: db.url=jdbc:mysql://localhost:3306/sos_game_db");
		}
		if (USERNAME == null || USERNAME.trim().isEmpty()) {
			throw new IllegalStateException("db.user is not configured in database.properties\n" + "Add: db.user=root");
		}
		if (PASSWORD == null) {
			JOptionPane.showMessageDialog(null," Warning: db.password is empty (no password set)");
		}
	}

	private static void loadDriver() {
		try {
			Class.forName(DRIVER);
			System.out.println(" JDBC Driver loaded: " + DRIVER);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null," JDBC Driver not found: " + DRIVER);
			JOptionPane.showMessageDialog(null,"  Please add MySQL Connector/J to your project:");
			JOptionPane.showMessageDialog(null,"  Download from: https://dev.mysql.com/downloads/connector/j/");
			JOptionPane.showMessageDialog(null,"  Or add to Build Path in Eclipse/IntelliJ");
			throw new RuntimeException("Database driver not found", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			}
			return connection;
		} catch (SQLException e) {
			System.err.println(" Failed to connect to database");
			System.err.println("  URL: " + URL);
			System.err.println("  User: " + USERNAME);
			System.err.println("  Error: " + e.getMessage());
			System.err.println("\nTroubleshooting:");
			System.err.println("  1. Is MySQL server running?");
			System.err.println("  2. Does database '" + extractDatabaseName(URL) + "' exist?");
			System.err.println("  3. Is the password correct?");
			System.err.println("  4. Can you connect with: mysql -u " + USERNAME + " -p");
			throw e;
		}
	}

	// close connection
	public static void closeConnection() {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
					JOptionPane.showMessageDialog(null,"Database connection closed");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Error closing connection: " + e.getMessage());
			}
		}
	}

	private static String extractDatabaseName(String url) {
		if (url == null) {
			return "unknown";
		}
		try {

			String[] parts = url.split("/");
			if (parts.length >= 4) {
				String dbPart = parts[3];

				int questionMark = dbPart.indexOf('?');
				if (questionMark > 0) {
					return dbPart.substring(0, questionMark);
				}
				return dbPart;
			}
		} catch (Exception e) {

		}
		return "unknown";
	}

}