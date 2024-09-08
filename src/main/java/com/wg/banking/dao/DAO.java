package com.wg.banking.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wg.banking.config.DatabaseConnection;

public class DAO {
	
	
	public Map<String,String> get(String sqlQuery) {
		Map<String,String> mp = new HashMap<>();
		try(Connection connection = DatabaseConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			
			
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            if (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    resultSetMetaData.getColumnTypeName(columnCount);
                    String columnValue = resultSet.getObject(i).toString();
                    
                    mp.put(columnName, columnValue);
                }
            }

		} catch(Exception e) {
			e.printStackTrace();
		}
		return mp;
	}
}