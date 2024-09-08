package com.wg.banking.dao;

import java.util.Map;

import com.wg.banking.dao.DAO;

public class LoginDAO {
	public static Map<String,String> authenticateUser(String username, String password) {
		String sqlQuery = "SELECT * FROM User WHERE username=" + "\"" + username + "\"";
		DAO dao = new DAO();
		return dao.get(sqlQuery);
	}
}