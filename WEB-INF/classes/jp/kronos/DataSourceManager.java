package jp.kronos;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

public class DataSourceManager {

	public static Connection getConnection() throws ServletException, NamingException, SQLException {
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/knowledge");
			return dataSource.getConnection();
		} catch (NamingException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
	}
}
