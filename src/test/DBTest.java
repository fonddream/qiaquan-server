package test;

import java.sql.SQLException;
import java.util.ArrayList;

import db.DBHelper;

public class DBTest {
	public static void main(String[] args) throws SQLException {
		DBHelper dbHelper = new DBHelper();
		ArrayList<Object> list = dbHelper.busineLogin("qiaquancom@qq.com",
				"0f86c6ca50c13d6e2a6a05b3cc0dffcd");

		System.out.println(list.get(0) + ": " + list.get(1));
	}
}
