package src.framework.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.json.JSONException;
import org.json.JSONObject;

public final class OracleHelper {
	private String _connection;
	private String _userName;
	private String _password; 

	/**
	 * 构造函数，传入数据库地址和账户信息
	 * 
	 * @param connection 如：jdbc:oracle:thin:@192.168.100.22:1521:orcl
	 * @param userName   如：SCHONLINESER
	 * @param password   如：SCHONLINESER
	 */
	public OracleHelper(final String connection, final String userName, final String password) {
		_connection = connection;
		_userName = userName;
		_password = password;
	}

	/**
	 * 取得数据库连接
	 */
	private Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			final java.sql.Connection conn = DriverManager.getConnection(_connection, _userName, _password);
			return conn;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 关闭数据库连接
	 */
	private void close(final Connection conn, final PreparedStatement pstm, final ResultSet rs) {
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
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行单个数据库操作 Insert,Update,Delete
	 * 
	 * @return 成功执行的记录数
	 */
	public Integer Update(final String sql, final String[] params) {
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

		} catch (final Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			close(conn, pstm, null);
		}
	}

	/**
	 * 执行多个数据库操作，包含事务处理功能
	 * 
	 * @return 如果事务执行成功返回1，如果事务执行不成功返回0
	 */
	public Integer Update(final String[] sqls, final String[][] params) {
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
		} catch (final Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (final Exception e2) {
				e2.printStackTrace();
			}
			return 0;
		} finally {
			close(conn, pstm, null);
		}
	}

	/**
	 * 执行SQL语句，返回JSON结果（不带参数）
	 */
	public String QueryJson(final String sql) {
		final Connection conn = getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			final ResultSet rs = stmt.executeQuery(sql);
			final String json = resultSetToJson(rs);
			rs.close();
			stmt.close();
			conn.close();
			return json;
		} catch (final SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 执行SQL语句，返回JSON结果（带参数）
	 */
	public String QueryJson(final String sql, final String[] params) {
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
			final ResultSet rs = pstm.executeQuery();
			final String json = resultSetToJson(rs);
			return json;
		} catch (final Exception e) {
			e.printStackTrace();
			return e.toString();
		} finally {
			close(conn, pstm, null);
		}
	}

	public Object ExecuteScalar(final String sql) {
		Object value = null;
		try {
			final RowSet rs = Query(sql);
			if (rs != null) {
				rs.next();
				value = rs.getObject(1);
			}
			return value;
		} catch (final SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public Object ExecuteScalar(final String sql, final String[] params) {
		Object value = null;
		try {
			final RowSet rs = Query(sql, params);
			if (rs != null) {
				rs.next();
				value = rs.getObject(1);
				return value;
			}
		} catch (final Exception e) {
			return value;
		}
		return value;
	}

	public RowSet Query(final String sql) {
		final Connection conn = getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();

			final ResultSet rs = stmt.executeQuery(sql);
			final RowSetFactory factory = RowSetProvider.newFactory();
			final CachedRowSet cachedRs = factory.createCachedRowSet();
			cachedRs.populate(rs);
			rs.close();
			stmt.close();
			conn.close();
			return cachedRs;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public RowSet Query(final String sql, final String[] params) {
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
			final ResultSet rs = pstm.executeQuery();
			final RowSetFactory factory = RowSetProvider.newFactory();
			final CachedRowSet cachedRs = factory.createCachedRowSet();
			// 使用ResultSet装填RowSet
			cachedRs.populate(rs);
			rs.close();
			pstm.close();
			conn.close();
			return cachedRs;

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstm, null);
		}
		return null;
	}

	/**
	 * 执行需要分页的数据库查询操作
	 * 
	 * @return 查询结果的离线RowSet
	 */
	public RowSet Query(final String sql, final String[] params, final Integer pageIndex, final Integer pageSize) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		CachedRowSet crs = null;

		try {
			conn = getConnection();
			pstm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstm.setString(i + 1, params[i]);
				}
			}
			rs = pstm.executeQuery();

			// 创建CacheRowSet
			final RowSetFactory factory = RowSetProvider.newFactory();
			crs = factory.createCachedRowSet();
			crs.setPageSize(pageSize);
			crs.populate(rs, (pageIndex - 1) * pageSize + 1);

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstm, rs);
		}

		return crs;
	}

	/**
	 * 执行查询的存储过程"{ call addUser(?,?,?,?) }"
	 * 
	 * @return 返回查询结果的RowSet集合
	 */
	public RowSet executeStoredProcedure(final String dsName, final String sp_name, final String[] params) {
		Connection conn = null;
		CallableStatement cstm = null;
		ResultSet rs = null;
		CachedRowSet crs = null;

		try {
			conn = getConnection();
			cstm = conn.prepareCall(sp_name);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					cstm.setString(i + 1, params[i]);
				}
			}
			rs = cstm.executeQuery();

			// 创建CacheRowSet
			final RowSetFactory factory = RowSetProvider.newFactory();
			crs = factory.createCachedRowSet();
			crs.populate(rs);

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, cstm, rs);
		}

		return crs;
	}

	public void setConnection(final String connection) {
		this._connection = connection;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(final String userName) {
		this._userName = userName;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(final String password) {
		this._password = password;
	}

	/**
	 * 辅助方法-将ResultSet转换为JSON字符串
	 */
	public String resultSetToJson(final ResultSet rs) {
		final org.json.JSONArray array = new org.json.JSONArray();
		ResultSetMetaData metaData;
		try {
			metaData = rs.getMetaData();

			final int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				final JSONObject jsonObj = new JSONObject();

				for (int i = 1; i <= columnCount; i++) {
					final String columnName = metaData.getColumnLabel(i);
					final String value = rs.getString(columnName);
					try {
						jsonObj.put(columnName, value);
					} catch (final JSONException e) {
						e.printStackTrace();
					}
				}
				array.put(jsonObj);
			}
			final String json = array.toString();
			return json;
		} catch (final SQLException e) {
			e.printStackTrace();

		}
		return "";
	}

	// 查询所有表及注释
	public List<Table> GetTables() {
		ArrayList<Table> list = new ArrayList<Table>();
		try {
			final String sql = "select user_tables.TABLE_NAME,user_tab_comments.COMMENTS from user_tab_comments, user_tables where user_tab_comments.TABLE_NAME=user_tables.TABLE_NAME";
			final RowSet rs = Query(sql);
			while (rs.next()) {
				final String key = rs.getString("TABLE_NAME").trim() == null ? "" : rs.getString("TABLE_NAME").trim();
				final String val = rs.getString("COMMENTS") == null ? "" : rs.getString("COMMENTS").trim();
				Table table = new Table(key, val);
				if (!list.contains(table)) {
					list.add(table);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return list;
	}

	// 查询指定表字段名及注释
	public List<Field> GetFields(String tableName) {
		ArrayList<Field> list = new ArrayList<Field>();
		try {
			final String sql = "SELECT  f.COLUMN_NAME,f.DATA_TYPE ,f.DATA_LENGTH ,f.DATA_DEFAULT ,c.COMMENTS FROM    user_tab_columns f ,user_col_comments c WHERE   f.COLUMN_NAME = c.COLUMN_NAME AND f.TABLE_NAME = ? AND c.TABLE_NAME = ?";
			final RowSet rs = Query(sql, new String[] { String.valueOf(tableName), tableName });
			while (rs.next()) {
				final String name = rs.getString("COLUMN_NAME").trim() == null ? ""
						: rs.getString("COLUMN_NAME").trim();
				final String type = rs.getString("DATA_TYPE") == null ? "" : rs.getString("DATA_TYPE").trim();
				final String length = rs.getString("DATA_LENGTH") == null ? "" : rs.getString("DATA_LENGTH").trim();
				final String defaul = rs.getString("DATA_DEFAULT") == null ? "" : rs.getString("DATA_DEFAULT").trim();
				final String comment = rs.getString("COMMENTS") == null ? "" : rs.getString("COMMENTS").trim();

				Field table = new Field(name, type, length, defaul, comment);
				if (!list.contains(table)) {
					list.add(table);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return list;
	}

	// 表实体
	public class Table {
		public Table(String key, String val) {
			Name = key;
			Comment = val;
		}

		public String Name;
		public String Comment;
	}

	// 字段实体
	public class Field {
		public Field(String name, String type, String length, String defaul, String comment) {
			Name = name;
			Type = type;
			Length = length;
			Default = defaul;
			Comment = comment;
		}

		public String Name;
		public String Type;
		public String Length;
		public String Comment;
		public String Default;
	}
}
