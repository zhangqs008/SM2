package src.framework.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.json.JSONObject;

public class SqlServerHelper {
	private String _connection;
	private String _userName;
	private String _password;

	/**
	 * 构造函数，传入数据库地址和账户信息
	 *
	 * @param connection 如：jdbc:sqlserver://localhost:1433;DatabaseName=Test
	 * @param userName   如：sa
	 * @param password   如：123456
	 */
	public SqlServerHelper(String connection, String userName, String password) {
		_connection = connection;
		_userName = userName;
		_password = password;
	}

	private Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = (Connection) DriverManager.getConnection(_connection, _userName, _password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void closeCon(Connection conn, PreparedStatement pstm, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (pstm != null) {
				pstm.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public RowSet Query(String sql, String[] params) {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = getConnection();
			pstm = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstm.setString(i + 1, params[i]);
				}
			}
			ResultSet rs = pstm.executeQuery();
			RowSetFactory factory = RowSetProvider.newFactory();
			CachedRowSet cachedRs = factory.createCachedRowSet();
			// 使用ResultSet装填RowSet
			cachedRs.populate(rs);
			rs.close();
			pstm.close();
			conn.close();
			return cachedRs;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCon(conn, pstm, null);
		}
		return null;
	}

	/**
	 * 执行单个数据库操作 Insert,Update,Delete
	 * 
	 * @return 成功执行的记录数
	 */
	public Integer Update(String sql, String[] params) {
		Connection conn = null;
		PreparedStatement pstm = null;

		try {
			conn = getConnection();
			pstm = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstm.setString(i + 1, params[i]);
				}
			}
			return pstm.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			closeCon(conn, pstm, null);
		}
	}

	/**
	 * 执行多个数据库操作，包含事务处理功能
	 * 
	 * @return 如果事务执行成功返回1，如果事务执行不成功返回0
	 */
	public Integer Update(String[] sqls, String[][] params) {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = getConnection();
			// 禁止自动提交事务
			conn.setAutoCommit(false);
			for (int i = 0; i < sqls.length; i++) {
				pstm = conn.prepareStatement(sqls[i]);
				if (params != null) {
					for (int j = 0; j < params[i].length; j++) {
						pstm.setString(j + 1, params[i][j]);
					}
				}
				pstm.executeUpdate();
			}
			conn.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return 0;
		} finally {
			closeCon(conn, pstm, null);
		}
	}

	/**
	 * 执行SQL语句，返回JSON结果（不带参数）
	 */
	public String QueryJson(String sql) {
		Connection conn = getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			String json;
			try {
				json = resultSetToJson(rs);
				return json;
			} catch (Exception e) {
				rs.close();
				stmt.close();
				conn.close();
				e.printStackTrace();
				return e.getMessage();
			}
		} catch (SQLException e) {

			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 执行SQL语句，返回JSON结果（带参数）
	 */
	public String QueryJson(String sql, String[] params) {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = getConnection();
			pstm = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstm.setString(i + 1, params[i]);
				}
			}
			ResultSet rs = pstm.executeQuery();
			String json = resultSetToJson(rs);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		} finally {
			closeCon(conn, pstm, null);
		}
	}

	public Object ExecuteScalar(String sql) {
		Object value = null;
		RowSet rs;
		try {
			rs = Query(sql);

			if (rs != null) {
				rs.next();
				value = rs.getObject(1);
			}
			return value;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object ExecuteScalar(String sql, String[] params) {
		Object value = null;
		try {
			RowSet rs = Query(sql, params);
			if (rs != null) {
				rs.next();
				value = rs.getObject(1);
				return value;
			}
		} catch (Exception e) {
			return value;
		}
		return value;
	}

	public RowSet Query(String sql) {
		Connection conn = getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			RowSetFactory factory = RowSetProvider.newFactory();
			CachedRowSet cachedRs = factory.createCachedRowSet();
			cachedRs.populate(rs);
			rs.close();
			stmt.close();
			conn.close();
			return cachedRs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String resultSetToJson(ResultSet rs) throws SQLException, Exception {
		org.json.JSONArray array = new org.json.JSONArray();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		while (rs.next()) {
			JSONObject jsonObj = new JSONObject();

			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.put(jsonObj);
		}

		String json = array.toString();
		return json;
	}
}
