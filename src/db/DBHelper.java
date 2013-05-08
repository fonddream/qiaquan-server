package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class DBHelper {
	private String resource = "db.properties";
	private Properties properties = null;

	private Connection getConnection() {

		try {
			properties = new Properties();
			InputStream inputStream = getClass().getResourceAsStream(resource);
			properties.load(inputStream);

			String drivers = properties.getProperty("jdbc.drivers");
			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");

			Class.forName(drivers);
			return DriverManager.getConnection(url, username, password);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("连接失败！" + e.getMessage());
		}
		return null;
	}

	public ArrayList<Object> busineLogin(String username, String password) {
		try {
			Connection connection = this.getConnection();

			String sql = "SELECT uid,uname FROM qiaquan_user WHERE login=? AND password=?;";
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet result = preparedStatement.executeQuery();
			ArrayList<Object> list = new ArrayList<Object>();
			while (result.next()) {
				list.add(result.getString(1));
				list.add(result.getString(2));
			}
			connection.close();
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// public void save(Users users) {
	//
	// try {
	// Connection connection = this.getConnection();
	//
	// String sql = "insert into users(username, password) values(?, ?);";
	// PreparedStatement preparedStatement = connection
	// .prepareStatement(sql);
	// preparedStatement.setString(1, users.getUsername());
	// preparedStatement.setString(2, users.getPassword());
	//
	// preparedStatement.executeUpdate();
	// connection.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println("插入错误！" + e.getMessage());
	// }
	// }
	//
	// public Users Search(String username) {
	// Users bean = null;
	//
	// try {
	// Connection connection = this.getConnection();
	// String sql = "select * from users where username=?";
	// PreparedStatement ps = connection.prepareStatement(sql);
	//
	// ps.setString(1, username);
	// ResultSet rs = ps.executeQuery();
	//
	// if (rs.next()) {
	// bean = new Users();
	//
	// bean.setId(rs.getInt("id"));
	// bean.setUsername(rs.getString("username"));
	// bean.setPassword(rs.getString("password"));
	// }
	// connection.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	//
	// System.out.println("查询异常" + e.getMessage());
	// }
	// return bean;
	// }
}
