package sprint5.mode.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;

import javax.swing.JOptionPane;

public class DBConnection {
	private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
	private static String URL;
	private static String USERNAME;
	private static String PASSWORD;
	private static String DRIVER;
	private static Properties properties = new Properties();
	// static block to load properties
	static {

		loadConfiguration();
		loadDriver();
	}

	public static void loadConfiguration() {
		InputStream input = null;
		try {
			// load from class path
			input = DBConnection.class.getClassLoader().getResourceAsStream("database.properties");

			if (input == null) {
				try {
					input = new FileInputStream("database.properties");
				} catch (IOException e) {
					// continue trying
				}
			}

			if (input == null) {
				String errorMsg = ("Cannot find database.properties file!\n" + "Current working directory: "
						+ System.getProperty("user.dir") + "\n" + "Please ensure database.properties is in:\n"
						+ "1. Project root directory (where you have it now)\n" + "2. OR copy it to src/ folder\n"
						+ "File should contain:\n" + "  db.driver=com.mysql.cj.jdbc.Driver\n"
						+ "  db.url=jdbc:mysql://localhost:3306/sos_game_db\n" + "  db.user=root\n"
						+ "  db.password=your_password");
				LOGGER.severe(errorMsg);
				throw new IOException(errorMsg);
			}

			properties.load(input);

			// Extract configuration values
			URL = properties.getProperty("db.url");
			USERNAME = properties.getProperty("db.user");
			PASSWORD = properties.getProperty("db.password");
			DRIVER = properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
			validateConfiguration();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to load database properties", e);
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Warning: Failed to close properties file stream");
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
			LOGGER.warning("db.password is empty (no password set)");
		}
	}

	private static void loadDriver() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			String errorMsg = " JDBC Driver not found: " + DRIVER + "\nPlease add MySQL Connector/J to your project:\n"
					+ "Download from: https://dev.mysql.com/downloads/connector/j/\n"
					+ "Or add to Build Path in Eclipse/IntelliJ";
			LOGGER.severe(errorMsg);
			throw new RuntimeException("Database driver not found", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			return conn;
		} catch (SQLException e) {
			String errorMsg = String.format(
					" Failed to connect to database\n" + "  URL: %s\n" + "  User:  %s\n" + "Error: \s"
							+ "\nTroubleshooting:\n" + " 1. Is MySQL server running?"
							+ " 2. Does database '%s' exist?\n" + " 3. Is the password correct?\n"
							+ " 4. Can you connect with: mysql -u %s -p",
					URL, USERNAME, e.getMessage(), extractDatabaseName(URL), USERNAME);
			LOGGER.severe(errorMsg);
			;
			throw e;
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
			LOGGER.log(Level.WARNING, "Error extracting database name", e);
		}
		return "unknown";
	}

	// Getters for testing
	public static String getURL() {
		return URL;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	public static String getPASSWORD() {
		return PASSWORD;
	}

}