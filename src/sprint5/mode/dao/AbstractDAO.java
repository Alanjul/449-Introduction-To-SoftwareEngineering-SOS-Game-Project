package sprint5.mode.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public abstract class AbstractDAO<T> {

	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

	protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

	protected abstract String getTableName();

	protected List<T> executeQuery(String sql, Object... params) {
		List<T> results = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			setParameters(stmt, params);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					results.add(mapResultSetToEntity(rs));
				}
			}

		} catch (SQLException e) {
			String msg = String.format("Error executing query on %s: %s", getTableName(), sql);
			LOGGER.log(Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}

		return results;
	}

	protected List<T> executeQuery(PreparedStatement stmt) throws SQLException {
		List<T> results = new ArrayList<>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				results.add(mapResultSetToEntity(rs));
			}
		}
		return results;
	}

	protected int executeUpdate(String sql, Object... params) {
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			setParameters(stmt, params);
			return stmt.executeUpdate();

		} catch (SQLException e) {
			String msg = String.format("Error executing update on %s: %s", getTableName(), sql);
			LOGGER.log(Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	protected Integer executeInsert(String sql, Object... params) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			setParameters(stmt, params);

			int rows = stmt.executeUpdate();
			if (rows == 0) {
				throw new SQLException("Insert failed, no rows affected");
			}

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
				throw new SQLException("Insert failed, no ID returned");
			}

		} catch (SQLException e) {
			String msg = String.format("Error inserting into %s", getTableName());
			LOGGER.log(Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	protected <R> R executeInTransaction(TransactionCallback<R> callback) {
		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			R result = callback.execute(conn);

			conn.commit();
			return result;

		} catch (Exception e) {

			if (conn != null) {
				try {
					conn.rollback();
					LOGGER.info("Transaction rolled back");
				} catch (SQLException rollbackEx) {
					LOGGER.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
				}
			}

			throw new RuntimeException("Transaction failed", e);

		} finally {

			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					LOGGER.log(Level.WARNING, "Error closing connection", e);
				}
			}
		}
	}

	private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			stmt.setObject(i + 1, params[i]);
		}
	}

	@FunctionalInterface
	protected interface TransactionCallback<R> {
		R execute(Connection conn) throws SQLException;
	}
}
